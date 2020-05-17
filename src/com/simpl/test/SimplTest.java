package com.simpl.test;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import com.simpl.model.BookKeeper;
import com.simpl.model.Merchant;
import com.simpl.model.User;
import com.simpl.model.exception.CustomException;
import com.simpl.model.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SimplTest extends TestCase {
	BookKeeper bookKeeper = BookKeeper.getInstance();
	
	public void test()  {
		
		
		User simplSystem = new User("Simpl", "system@simpl.com" ,99999);
		try {
			bookKeeper.addUser(simplSystem);
		} catch (CustomException e2) {
			e2.printStackTrace();
		}
		
		
		
		
		
		
		
		User u1 = new User("u1", "u1@gmail.com" ,1000);
		Merchant m1 = null;
		try {
			m1 = new Merchant("m1","m1@gmail.com",12);
		} catch (CustomException e1) {
			e1.printStackTrace();
		}
		
		
		
		//add user
		try {
			assertEquals(bookKeeper.addUser(u1) , u1);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		
		
		//add merchant
		try {
			assertEquals(bookKeeper.addMerchant(m1) , m1);
		} catch (CustomException e) {
			e.printStackTrace();
		}
		
		
		
		//user already there
		CustomException thrown = assertThrows(
				CustomException.class,
		           () -> bookKeeper.addUser(u1),
		           "Expected addUser() to throw, but it didn't"
		);
		assertTrue(thrown.getMessage().contains("Already exists with same"));
		    
		
		
		thrown = assertThrows(
				CustomException.class,
		           () -> bookKeeper.setDiscount("m1",112),
		           "Expected setDiscount() to throw, but it didn't"
		);
		assertTrue(thrown.getMessage().contains("discount is not valid"));
		
		
		
		try {
			assertEquals(bookKeeper.setDiscount("m1", 12), m1);
		} catch (UserNotFoundException | CustomException e) {
			e.printStackTrace();
		}
		
		
		
		assertEquals(bookKeeper.doTransaction("u1", "m1", 2000),"rejected! (reason: credit limit)");
		assertEquals(bookKeeper.doTransaction("u1", "m1", 200),"success!");
		
		
		assertEquals(bookKeeper.getTotalDiscountGivenByMerchant("m1"),24.0);
		
		
		
		
	}

	
	
	
}
