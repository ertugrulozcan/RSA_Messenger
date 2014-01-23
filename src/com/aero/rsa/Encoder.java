package com.aero.rsa;

import java.math.BigInteger;

import com.aero.rsa.RsaKey.PublicKey;

/*
 * Encoder.java
 * 
 * Ahmet Ertugrul Ozcan ertugrul.ozcan@bil.omu.edu.tr
 * 
 * Mikail Simsek mikail.simsek@bil.omu.edu.tr
 * 
 * Sifreleyici sinifi.
 */

public class Encoder
{
	private PublicKey publicKey;
	
	//
	// Kurucu metod - Constructor
	//
	public Encoder(PublicKey publicKey)
	{
		this.publicKey = publicKey;
	}
	
	public BigInteger Encrypt(BigInteger message)
	{
		// Sifreli mesaj (crypto)
		BigInteger crypto = message.modPow(publicKey.E(), publicKey.N());
		return crypto;
	}
	
	public String Encrypt(String message)
	{
		BigInteger messageBinInt = MessageToBigInteger(message);
		
		// Sifreleme islemi
		return  Encrypt(messageBinInt).toString();
	}
	
	private BigInteger MessageToBigInteger(String message)
	{
		StringBuilder stringBuilder = new StringBuilder();
		int letter;
		
		for (int i = 0; i < message.length(); i++)
		{
			letter = message.codePointAt(i);
			if (letter < 100)
				stringBuilder.append("0" + letter);
			else
				stringBuilder.append(letter);
		}
		
		return new BigInteger(stringBuilder.toString());
	}
}
