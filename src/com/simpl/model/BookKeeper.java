package com.simpl.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import com.simpl.model.User;
import com.simpl.model.credit.Credit;
import com.simpl.model.exception.CustomException;
import com.simpl.model.exception.UserNotFoundException;
import com.simpl.model.transaction.Transaction;
import com.simpl.model.transaction.TransactionStatus;

public class BookKeeper {
	private Hashtable<String,User> users;
	private Hashtable<String, Merchant> merchants;
	private Hashtable<Long,Transaction> transactions;
	private static BookKeeper INSTANCE;
	private static final String systemName = "Simpl";
	
	BookKeeper() {
		users = new Hashtable<>();
		merchants = new Hashtable<>();
		transactions = new Hashtable<>();
	}
	
	public static BookKeeper getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new BookKeeper();
		}
		
		return INSTANCE;
	}
	
	
	public User addUser(User user) throws CustomException {
		if(users.containsKey(user.getName())) {
			throw new CustomException("User Already exists with same username");
		}
		users.put(user.getName(),user);
		return user;
	}
	
	public Merchant addMerchant(Merchant merChant) throws CustomException {
		if(merchants.containsKey(merChant.getName())) {
			throw new CustomException("User Already exists with same username");
		}
		merchants.put(merChant.getName() , merChant);
		return merChant;
	}
	
	public User getUser(String  uName) throws UserNotFoundException {
		if(!users.containsKey(uName)) {
			throw new UserNotFoundException("User with this username does not exists.");
		}
		return users.get(uName);
	}
	
	public Merchant getMerChant(String  mName) throws UserNotFoundException {
		if(!merchants.containsKey(mName)) {
			throw new UserNotFoundException("Merchant with this username does not exists");
		}
		return merchants.get(mName);
	}
	
	public Hashtable getUsers(){
		return users;
	}
	
	public Hashtable getMerchants() {
		return merchants;
	}
	
	
	public Merchant setDiscount(String mname , double discount) throws UserNotFoundException, CustomException {
		if(discount > 100 || discount < 0) {
			throw new CustomException("discount is not valid.");
		}
		if(merchants.containsKey(mname)) {
			merchants.get(mname).setDiscount(discount);
			return merchants.get(mname);
		}else {
			throw new UserNotFoundException("Merchant not available with name "+mname );
		}
				
	}
	
	public ArrayList<String> getUserAtCreditReport() {
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (Map.Entry<String,User> entry : users.entrySet()) {
			Credit credit = entry.getValue().getCredit();
			double total_used = credit.getCreditUsed();
			double total_limit = credit.getTotalCreditLimit();
			
			if(total_limit - total_used == 0) {
				list.add(entry.getValue().getName());
			}
		
		}  
            
		return list;
	}
	
	public ArrayList<String> getTotalDues(){
		ArrayList<String> list = new ArrayList<>();
		double total = 0;
		for (Map.Entry<String,User> entry : users.entrySet()) {
			Credit credit = entry.getValue().getCredit();
			double total_used = credit.getCreditUsed();
			
			if(total_used > 0) {
				list.add(entry.getValue().getName()+": "+ total_used);
				total += total_used;
			}
		}  
		
		list.add("total: "+total);
            
		return list;
		
	}
	
	public double getTotalDiscountGivenByMerchant(String mname) {
		double totalDiscount = 0;
		Long mid = null;
		if(merchants.containsKey(mname)) {
			mid = merchants.get(mname).getId();
			for (Map.Entry<Long,Transaction> entry : transactions.entrySet()) {
				if(entry.getValue().getTo()  == mid && entry.getValue().getStatus().equals(TransactionStatus.SUCCESS)) {
					totalDiscount += entry.getValue().getActualAmount() - entry.getValue().getDiscountedAmount(); 
				}
			}
		}
		return totalDiscount;
	}
	
	
	
	public String doTransaction(String from , String to , double amount) {
		String status = "failed!";
		
		synchronized(this){
			Long userId = null;
			Long merchantId = null;
			if(users.containsKey(systemName) && users.get(systemName).getCredit().getTotalCreditLimit() < amount) {
				status = "System have insufficient fund. please try after some time";
				return status;
			}
			
			if(users.containsKey(from)) {
				userId = users.get(from).getId();
			}
			
			if(merchants.containsKey(to)) {
				merchantId = merchants.get(to).getId();
			}
			
		
			if(userId != null) {
				double credit_remaining = users.get(from).getCredit().getTotalCreditLimit() - users.get(from).getCredit().getCreditUsed();
				if(amount > credit_remaining) {
					status = "rejected! (reason: credit limit)";
					return status;
				}
			}else {
				return status;
			}
			
			if(merchantId != null) {
				
				boolean isAmountAdded = false;
				boolean systemDeduction = false;
				boolean merchantCredit = false;
				Long transactionId = null;
				double totalAmountToPay = users.get(from).getCredit().getCreditUsed() + amount;
				double discountedAmount = amount * ((100 - merchants.get(to).getDiscount()) * .01);
				try {
					
					//userDetails updated
					users.get(from).getCredit().setCreditUsed(totalAmountToPay);
					isAmountAdded = true;
					

					//from
					users.get(systemName).getCredit().setTotalCreditLimit(users.get(systemName).getCredit().getTotalCreditLimit() - discountedAmount);
					systemDeduction = true;
					
					//to
					merchants.get(to).setTotalCollection(merchants.get(to).getTotalCollection() + discountedAmount);
					merchantCredit = true;
					
					
					Long systemId = users.get(systemName).getId();
					
					//logs table
					Transaction transac = new Transaction(systemId , merchantId ,  discountedAmount  , amount );
					transactions.put(transac.getId(),transac);
					transactionId = transac.getId();
					
					status = "success!";
					
				}catch(Exception e) {
				  e.printStackTrace();
				  
				  //rollback
				  if(isAmountAdded == true) {
					  users.get(from).getCredit().setCreditUsed(users.get(from).getCredit().getCreditUsed() -amount);
				  }
				  
				  if(systemDeduction == true) {
					  users.get(systemName).getCredit().setTotalCreditLimit(users.get(systemName).getCredit().getTotalCreditLimit() + discountedAmount);
				  }
				  
				  if(merchantCredit == true) {
						merchants.get(to).setTotalCollection(merchants.get(to).getTotalCollection() - discountedAmount);
				  }
				  
				  if(transactionId != null) {
					  transactions.get(transactionId).setStatus(TransactionStatus.FAILED);
				  }
				}
				
			}else {
				return status;
			}
		}
		return status;
	}
	
	
	
	public String payBack(String from , double amount) {
		String message = "failed";
		synchronized(this) {
			double creditUsed = users.get(from).getCredit().getCreditUsed();

			
			//from
			users.get(from).getCredit().setCreditUsed(creditUsed-amount);
			double remainingAmount = (creditUsed-amount);
			message = users.get(from).getName() + "(dues: "+remainingAmount+")";
			
			
			//to
			users.get(systemName).getCredit().setTotalCreditLimit(users.get(systemName).getCredit().getTotalCreditLimit()+amount);
			
			
			Long userId = users.get(from).getId();
			Long systemId = users.get(systemName).getId();

			
			//logs table
			Transaction transac = new Transaction( userId, systemId ,  amount  , amount );
			transactions.put(transac.getId(),transac);
			
			
		}
		
		
		
		
		return message;
	}

	public Hashtable<Long, Transaction> getTransactions() {
		return transactions;
	}
	
	
	
	
	
	
	
}
