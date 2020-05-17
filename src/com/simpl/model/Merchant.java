package com.simpl.model;

import com.simpl.model.exception.CustomException;
import com.simpl.validate.Validator;

public class Merchant extends Person {
	private double discount ; //in percentage
	private double totalCollection ;
	
	
	


	public Merchant(String name ,String email, double discount) throws CustomException{
		super(name,email);
		setDiscount(discount);
		setTotalCollection(0);
	}


	public double getDiscount() {
		return discount;
	}


	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public double getTotalCollection() {
		return totalCollection;
	}


	public void setTotalCollection(double totalCollection) {
		this.totalCollection = totalCollection;
	}


	
	@Override
	public String toString() {
		return "Merchant : {"+ "id:"+ this.getId()+ " name :" + this.getName() + " email : "+ this.getEmail() + " discount :-" + discount +"}";
	}
	
	
	

}
