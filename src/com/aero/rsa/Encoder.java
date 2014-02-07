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
 */

public class Encoder
{
	private PublicKey publicKey;
	
	public static String binaryCode;
	
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
	
	//
	// String sifreleme islemi
	//
	public String Encrypt(String message)
	{
		// ADIM 1:
		// String'in tum karakterlerinin ascii degerleri birlestirilerek mesaji ifade eden yeni bir sayi olusturulur.
		// Tum karakterler icin ascii karsiligi 3 basamakli olacak sekilde ayarlanmalidir.
		// Bu nedenle 100'den kucuk olanlarin basina '0' eklenerek yazilir.
		StringBuilder intMessage = new StringBuilder();
		for(int i = 0; i < message.length(); i++)
		{
			int letter = message.codePointAt(i);
			if(letter >= 100)
				intMessage.append(letter);
			else
				intMessage.append("0" + letter);
		}
		
		System.out.println(message + " >>> " + intMessage);
		
		// ADIM 2:
		// Elde edilen sayisal mesaj encrypt metodu ile sifrelenir.
		BigInteger crypto = Encrypt(new BigInteger(intMessage.toString()));
		
		return crypto.toString();
	}
	
	private int BinaryToDecimal(String bin)
	{
		bin = new StringBuilder(bin).reverse().toString();
		
		int dec = 0;
		for(int i = 0; i < bin.length(); i++)
		{
			if(bin.charAt(i) == '1')
				dec += Math.pow(2, i);
		}
		
		return dec;
	}
}
