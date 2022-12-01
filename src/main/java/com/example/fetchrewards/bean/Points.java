package com.example.fetchrewards.bean;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Points {
	
	@NotNull @Min(1)
	Integer points;
}
