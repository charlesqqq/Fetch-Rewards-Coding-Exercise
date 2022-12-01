package com.example.fetchrewards.bean;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class Transaction {
	
	@NotBlank
	String payer;
	@NotNull
	Integer points;
	@NotNull
	LocalDateTime timestamp;
}
