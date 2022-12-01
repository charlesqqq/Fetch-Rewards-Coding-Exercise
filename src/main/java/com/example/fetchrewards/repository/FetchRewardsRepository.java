package com.example.fetchrewards.repository;

import com.example.fetchrewards.bean.Transaction;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

@Repository
@Getter
@Setter
public class FetchRewardsRepository {
	
	// payer -> points
	Map<String, Integer> balances;
	// payer -> transactions
	Map<String, PriorityQueue<Transaction>> transactions;
	
	public FetchRewardsRepository() {
		balances = new HashMap<>();
		transactions = new HashMap<>();
	}
	
	// combine all payers' transactions
	public PriorityQueue<Transaction> getAllTransactions() {
		// transactions order by timestamp
		PriorityQueue<Transaction> allTransactions = new PriorityQueue<>(Comparator.comparing(Transaction::getTimestamp));
		for (PriorityQueue<Transaction> transactions : transactions.values()) {
			allTransactions.addAll(transactions);
		}
		return allTransactions;
	}
	
	public int getTotalPoints() {
		int totalPoints = 0;
		for (Integer points : balances.values()) {
			totalPoints += points;
		}
		return totalPoints;
	}
	
}
