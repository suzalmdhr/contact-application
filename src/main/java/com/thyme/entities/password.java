package com.thyme.entities;

public class password {
	
	private String oldPass;
	
	private String newPass;

	public String getOldPass() {
		return oldPass;
	}

	public void setOldPass(String oldPass) {
		this.oldPass = oldPass;
	}

	public String getNewPass() {
		return newPass;
	}

	public void setNewPass(String newPass) {
		this.newPass = newPass;
	}

	public password(String oldPass, String newPass) {
		super();
		this.oldPass = oldPass;
		this.newPass = newPass;
	}

	public password() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "password [oldPass=" + oldPass + ", newPass=" + newPass + "]";
	}
	
	

}
