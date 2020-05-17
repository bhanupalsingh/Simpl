package com.simpl.model;

public class Person {
	private long id;
	private String name;
	private String email;
	
	private static long NEW_ID = 0;
	
	
	public Person(String name , String email) {
		setId();
		setName(name);
		setEmail(email);
	}	
	
	public long getId() {
		return id;
	}
	public void setId() {
		synchronized(this){
			this.id = NEW_ID++;
		}
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
