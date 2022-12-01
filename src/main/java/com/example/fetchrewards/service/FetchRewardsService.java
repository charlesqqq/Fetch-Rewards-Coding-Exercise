package com.example.fetchrewards.service;

import com.example.fetchrewards.bean.SpendResult;
import com.example.fetchrewards.bean.Transaction;
import com.example.fetchrewards.exception.NotEnoughPointsException;
import com.example.fetchrewards.repository.FetchRewardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FetchRewardsService {
	
	@Autowired
	FetchRewardsRepository fetchRewardsRepository;
	
	public void addTransaction(Transaction transaction) {
		String payer = transaction.getPayer();
		Integer points = transaction.getPoints();
		Map<String, Integer> balance = fetchRewardsRepository.getBalances();
		Map<String, PriorityQueue<Transaction>> transactions = fetchRewardsRepository.getTransactions();
		// points is positive, simply add a transaction and update the balance.
		if (points > 0) {
			balance.put(payer, balance.getOrDefault(payer, 0) + points);
			// transactions order by timestamp
			transactions.putIfAbsent(payer, new PriorityQueue<>(Comparator.comparing(Transaction::getTimestamp)));
			transactions.get(payer).offer(transaction);
		}
		// points is negative, we need to spend it using the oldest transaction.
		if (points < 0) {
			points = -points;
			// if the payer doesn't have enough points to spend, throw exception.
			if (points > balance.getOrDefault(payer, 0)) {
				throw new NotEnoughPointsException();
			}
			// payer has enough points, update balance
			balance.put(payer, balance.get(payer) - points);
			PriorityQueue<Transaction> payerTransactions = transactions.get(payer);
			// spend till points == 0
			while (points > 0 && !payerTransactions.isEmpty()) {
				// use the oldest transaction to spend points
				Transaction txn = payerTransactions.poll();
				Integer transactionPoints = txn.getPoints();
				int spend = Math.min(points, transactionPoints);
				points -= spend;
				transactionPoints -= spend;
				// after spend points in this transaction, we still have points,
				// we need this transaction for future use, so add it back to the queue.
				if (transactionPoints > 0) {
					txn.setPoints(transactionPoints);
					payerTransactions.offer(txn);
				}
			}
		}
	}
	
	public List<SpendResult> spendPoints(Integer points) {
		// check if we have enough points to spend.
		int totalPoints = fetchRewardsRepository.getTotalPoints();
		if (points > totalPoints) {
			throw new NotEnoughPointsException();
		}
		Map<String, Integer> spendDetail = new HashMap<>();
		Map<String, Integer> balance = fetchRewardsRepository.getBalances();
		PriorityQueue<Transaction> allTransactions = fetchRewardsRepository.getAllTransactions();
		// spend till points == 0
		while (points > 0 && !allTransactions.isEmpty()) {
			Transaction transaction = allTransactions.poll();
			String payer = transaction.getPayer();
			Integer transactionPoints = transaction.getPoints();
			int spend = Math.min(points, transactionPoints);
			points -= spend;
			transactionPoints -= spend;
			if (transactionPoints > 0) {
				// this transaction still has points, update it
				transaction.setPoints(transactionPoints);
			} else {
				// points in this transaction are all spent, remove it.
				PriorityQueue<Transaction> payerTransactions = fetchRewardsRepository.getTransactions().get(payer);
				payerTransactions.poll();
				if (payerTransactions.isEmpty()) {
					fetchRewardsRepository.getTransactions().remove(payer);
				}
			}
			balance.put(payer, balance.get(payer) - spend);
			spendDetail.put(payer, spendDetail.getOrDefault(payer, 0) - spend);
		}
		
		List<SpendResult> spendResults = new ArrayList<>();
		for (String payer : spendDetail.keySet()) {
			spendResults.add(new SpendResult(payer, spendDetail.get(payer)));
		}
		return spendResults;
	}
	
	public Map<String, Integer> getBalances() {
		return fetchRewardsRepository.getBalances();
	}
	
}
