package com.egyptianratscrew.dao;

import java.io.Serializable;

public class UserArrayWrapper implements Serializable{
	private IUser[] users;
	
	public UserArrayWrapper(IUser[] both){
		users = both;
	}
	
	/***
	 * Return user array
	 * @return
	 */
	public IUser[] getUsers(){
		return users;
	}
}
