package com.simpl.model.transaction;


// Transaction between user and simpl system 
// and simpl system to merchant 
public class Transaction {
	private long id;
	private long from ;
	private long to;
	private double discountedAmount;
	private double actualAmount;
	private TransactionStatus status ; 
	
	
	private static long NEW_ID = 0;
	
	public Transaction(long from , long to , double discountedAmount , double actualAmount) {
		this.setId();
		this.setFrom(from);
		this.setTo(to);
		this.setDiscountedAmount(discountedAmount);
		this.setActualAmount(actualAmount);
		this.setStatus(TransactionStatus.SUCCESS);
	}
	
	
	
	public TransactionStatus getStatus() {
		return status;
	}



	public void setStatus(TransactionStatus status) {
		this.status = status;
	}



	public long getId() {
		return id;
	}
	public void setId() {
		synchronized(this){
			this.id = NEW_ID ++;
		}
	}
	public long getFrom() {
		return from;
	}
	public void setFrom(long from) {
		this.from = from;
	}
	public long getTo() {
		return to;
	}
	public void setTo(long to) {
		this.to = to;
	}
	public double getDiscountedAmount() {
		return discountedAmount;
	}
	public void setDiscountedAmount(double discountedAmount) {
		this.discountedAmount = discountedAmount;
	}
	public double getActualAmount() {
		return actualAmount;
	}
	public void setActualAmount(double actualAmount) {
		this.actualAmount = actualAmount;
	}
	
	
	
	
}
