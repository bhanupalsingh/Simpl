package com.simpl.model;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

import com.simpl.model.exception.CustomException;
import com.simpl.model.exception.UserNotFoundException;
import com.simpl.model.transaction.Transaction;
import com.simpl.validate.Validator;

public class Driver {
	
	public static void main(String[] args) {
		
		BookKeeper b = BookKeeper.getInstance();
		User u1 = new User("Simpl", "system@simpl.com" ,99999);
		try {
			b.addUser(u1);
		} catch (CustomException e1) {
			e1.printStackTrace();
		}
		
		
		String userInput;
		Scanner sn = new Scanner(System.in);
		
		while(true){
			userInput = sn.nextLine();
			String[] userInputArray = userInput.split(" ");
			try {
				if(userInputArray.length ==  4) {
					if(userInputArray[0].equalsIgnoreCase("set") && 
							userInputArray[1].equalsIgnoreCase("discount")
							) {
						//Long mid = Long.parseLong(userInputArray[2]);
						String mname = userInputArray[2];
						double discount = Double.parseDouble(userInputArray[3].replace("%", ""));
						b.setDiscount(mname,discount);
						System.out.println("Discount set for merchant "+ mname + " "+ userInputArray[3] );
					}
				}else if(userInputArray.length >= 2){
					if(userInputArray[0].equalsIgnoreCase("report")) { 
						if(userInputArray[1].equalsIgnoreCase("users-at-credit-limit")) {
							ArrayList<String> list =  b.getUserAtCreditReport();
							for (int counter = 0; counter < list.size(); counter++) { 		      
						          System.out.println(list.get(counter)); 		
						    } 
						}else if(userInputArray[1].equalsIgnoreCase("total-dues")) {
							ArrayList<String> list =  b.getTotalDues();
							for (int counter = 0; counter < list.size(); counter++) { 		      
						          System.out.println(list.get(counter)); 		
						    }
						}else if(userInputArray.length >= 3 &&  userInputArray[1].equalsIgnoreCase("discount")) {
							String mname  = userInputArray[2] ;
							System.out.println(b.getTotalDiscountGivenByMerchant(mname));
						}
					}else if(userInputArray[0].equalsIgnoreCase("new") && userInputArray.length >= 5) {
						if(userInputArray[1].equalsIgnoreCase("txn")) {
								String userName = userInputArray[2];
								String merchantName = userInputArray[3];
								double amount  = Double.parseDouble(userInputArray[4]);
								System.out.println(b.doTransaction(userName , merchantName , amount));
						}else if(userInputArray[1].equalsIgnoreCase("user") ) {
							User newUser = new User(userInputArray[2], userInputArray[3],Double.parseDouble(userInputArray[4]));
							b.addUser(newUser);
							System.out.println(newUser.toString());
						}else if(userInputArray[1].equalsIgnoreCase("merchant") ) {
							String percent = userInputArray[4].replace("%", "");
							Merchant newUser;
							if(!Validator.isDiscountValid(Double.parseDouble(percent))) {
								throw new CustomException("discount is not valid");
							}
							
							newUser = new Merchant(userInputArray[2], userInputArray[3],Double.parseDouble(percent));
							b.addMerchant(newUser);
							System.out.println(newUser.toString());
						}
					}else if(userInputArray[0].equalsIgnoreCase("get") && userInputArray.length == 3) { 
						if(userInputArray[1].equalsIgnoreCase("user")) {
							User u = b.getUser(userInputArray[2]);
							System.out.println(u.toString());
							
						}else if(userInputArray[1].equalsIgnoreCase("merchant")) {
							Merchant u = b.getMerChant(userInputArray[2]);
							System.out.println(u.toString());
						}
					}else if(userInput.equalsIgnoreCase("get users")) {
							Hashtable<String,User> h = b.getUsers();
							System.out.println(h.toString());
					}else if(userInput.equalsIgnoreCase("get merchants")) {
							Hashtable<String,Merchant> h = b.getMerchants();
							System.out.println(h.toString());
					}else if(userInput.equalsIgnoreCase("get transactions")) {
							Hashtable<Long,Transaction> h =b.getTransactions();
							System.out.println(h.toString());
					}else if(userInputArray[0].equalsIgnoreCase("payback")){
						if(userInputArray.length >= 3) {
							String from = userInputArray[1];
							double amount = Double.parseDouble(userInputArray[2]);
							System.out.println(b.payBack(from, amount));
						}
					}else {
						System.out.println("Cant understand your request.");
					}
				}
					
			}catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (UserNotFoundException e) {
				e.printStackTrace();
			} catch(CustomException e) {
				e.printStackTrace();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
		}
	}
	
}

