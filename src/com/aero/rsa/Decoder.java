package com.aero.rsa;

import java.math.BigInteger;

import com.aero.rsa.RsaKey.PrivateKey;

/*
 * Decoder.java
 * 
 * Ahmet Ertugrul Ozcan ertugrul.ozcan@bil.omu.edu.tr
 * 
 * Mikail Simsek mikail.simsek@bil.omu.edu.tr
 * 
 * Sifre cozucu sinifi.
 */

public class Decoder
{
	private PrivateKey privateKey;
	
	//
	// Kurucu metod - Constructor
	//
	public Decoder(PrivateKey privateKey)
	{
		this.privateKey = privateKey;
	}
	
	public BigInteger Decrypt(BigInteger crypto)
	{
		BigInteger message = crypto.modPow(privateKey.D(), privateKey.N());
		return message;
	}
	
	public String Decrypt(String cryptoString)
	{
		// Bazi karakter hatalarinin giderilmesi
		cryptoString = this.ClearInvalidChars(cryptoString);
		
		// Sifrenin sayisal forma cevrilmesi
		BigInteger crypto = new BigInteger(cryptoString);
		
		// Sifre cozme islemi
		BigInteger messagePattern = Decrypt(crypto);
		
		return BigIntegerToMessage(messagePattern);
	}
	
	private String BigIntegerToMessage(BigInteger messageBigInt)
	{
		String message = messageBigInt.toString();
		StringBuilder stringBuilder = new StringBuilder();
		
		// Sifreli stringin ilk karakterinin 0 olmasi ihtimaline karsilik kontrol;
		// Her karakter 3 basamakli ASCII karsiliga sahip olmalidir.
		// Eger mesaj uzunlugu 3'un bir tam kati degilse basa bir sifir eklenmelidir.
		if (message.length() % 3 != 0)
			message = "0" + message;
		
		for (int i = 0; i < message.length(); i += 3)
			stringBuilder.append((char)Integer.parseInt(message.substring(i, i + 3)));
		
		return stringBuilder.toString();
	}
	
	private String ClearInvalidChars(String message)
	{
		StringBuilder rightMessage = new StringBuilder();
		
		for(int i = 0; i < message.length(); i++)
		{
			if(Character.isDigit(message.charAt(i)))
				rightMessage.append(message.charAt(i));
		}
		
		return rightMessage.toString();
	}
}
