package com.simpl.validate;

public class Validator {
	public static boolean isDiscountValid(double discount) {
		if(discount <0 || discount > 100) {
			return false;
		}else {
			return true;
		}
	}
}
