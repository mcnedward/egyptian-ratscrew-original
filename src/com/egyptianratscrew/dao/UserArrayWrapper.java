package com.egyptianratscrew.dao;

import java.io.Serializable;

public class UserArrayWrapper implements Serializable{
	private IUser[] users;
	
	public UserArrayWrapper(IUser[] both){
		users = both;
	}
	
	public IUser[] getUsers(){
		return users;
	}
}
