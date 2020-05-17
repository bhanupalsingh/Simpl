package com.simpl.model.checkout;

import com.simpl.model.Merchant;

public class Checkout {
	private long id;
	private Merchant merchant;
	private double amount;
	
	private static long NEW_ID = 0;
	
	public Checkout(Merchant mid,double amount){
		setId();
		setMid(mid);
		setAmount(amount);
	}
	
	
	public long getId() {
		return id;
	}
	public void setId() {
		synchronized(this) {
			this.id = NEW_ID ++;
		}
	}
	public Merchant getMid() {
		return merchant;
	}
	public void setMid(Merchant merchant) {
		this.merchant = merchant;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}	
	
}
