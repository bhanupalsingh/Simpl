package com.simpl.model;

import java.util.List;

import com.simpl.model.credit.Credit;

public class User extends Person {
	private Credit credit;
	

	
	public User(String name,String email , double creditLimit) {
		super(name,email);
		setCredit(new Credit(creditLimit,0));
	}

	
	
	public Credit getCredit() {
		return credit;
	}

	public void setCredit(Credit credit) {
		this.credit = credit;
	}
	
	@Override
	public String toString() {
	    return "User :- " +
	            "id='" + this.getId() + '\'' +
	            ", name='" + this.getName() + '\'' +
	            ", email='" + this.getEmail() + '\'' +
	            ", {" + credit.toString() + "}";
	}
	
	
	
	
}
