package com.example.fetchrewards.controller;

import com.example.fetchrewards.bean.Points;
import com.example.fetchrewards.bean.SpendResult;
import com.example.fetchrewards.bean.Transaction;
import com.example.fetchrewards.exception.NotEnoughPointsException;
import com.example.fetchrewards.service.FetchRewardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class FetchRewardsController {
	
	@Autowired
	FetchRewardsService fetchRewardsService;
	
	@PostMapping("/transactions")
	public String addTransaction(@Validated @RequestBody Transaction transaction) {
		fetchRewardsService.addTransaction(transaction);
		return "Transaction Added";
	}
	
	@PostMapping("/points")
	public List<SpendResult> spendPoints(@Validated @RequestBody Points points) {
		return fetchRewardsService.spendPoints(points.getPoints());
	}
	
	@GetMapping("/balances")
	public Map<String, Integer> getBalances() {
		return fetchRewardsService.getBalances();
	}
	
	@ExceptionHandler({NotEnoughPointsException.class,})
	public String handleNotEnoughPointsException() {
		return "Not Enough Points To Spend.";
	}
}
