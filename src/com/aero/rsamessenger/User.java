package com.aero.rsamessenger;

import com.aero.rsa.RsaKey.*;

public class User
{
	public String phoneNumber;
	public String emailAdress;
	public PublicKey publicKey;
	public PrivateKey privateKey;
	
	public User(String phoneNumber, String emailAdress, PublicKey publicKey, PrivateKey privateKey)
	{
		this.phoneNumber = phoneNumber;
		this.emailAdress = emailAdress;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}
	
	public User(String phoneNumber, String emailAdress, PublicKey publicKey)
	{
		this.phoneNumber = phoneNumber;
		this.emailAdress = emailAdress;
		this.publicKey = publicKey;
		this.privateKey = null;
	}
	
}
