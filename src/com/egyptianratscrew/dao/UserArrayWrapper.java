package com.egyptianratscrew.dao;

import java.io.Serializable;

public class UserArrayWrapper implements Serializable{
	private User[] users;
	
	public UserArrayWrapper(User[] both){
		users = both;
	}
	
	public User[] getUsers(){
		return users;
	}
}
