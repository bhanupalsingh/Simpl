package com.simpl.model.credit;

public class Credit {
	private long id;
	private double totalCreditLimit;
	private double creditUsed;
	
	private static long NEW_ID = 0;
	
	public Credit(double totalCreditLimit , double creditUsed) {
		setId();
		setTotalCreditLimit(totalCreditLimit);
		setCreditUsed(creditUsed);
	}

	public long getId() {
		return id;
	}

	public void setId() {
		synchronized(this) {
			this.id = NEW_ID ++;
		}
	}

	public double getTotalCreditLimit() {
		return totalCreditLimit;
	}

	public void setTotalCreditLimit(double totalCreditLimit) {
		this.totalCreditLimit = totalCreditLimit;
	}

	public double getCreditUsed() {
		return creditUsed;
	}

	public void setCreditUsed(double creditUsed) {
		this.creditUsed = creditUsed;
	}

	
	@Override
	public String toString() {
	    return "Credit :- " +
	            "id='" + id + '\'' +
	            ", creditUsed='" + creditUsed + '\'' +
	            ", totalCreditLimit='" + totalCreditLimit;
	}
	
	
}
