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
 */

public class Decoder
{
	private PrivateKey privateKey;
	
	public static String binaryCode;
	
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
		String message = Decrypt(new BigInteger(cryptoString)).toString();
		
		// Elde edilen BigInteger formadaki mesaj 3'er basamak olacak sekilde ayrilir.
		// Her sayi birer karaktere denk gelmektedir.
		if(message.length() % 3 == 2)
			message = "0" + message;
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < message.length(); i+=3)
		{
			int intLetter = Integer.parseInt(message.substring(i, i + 3));
			char letter = (char) intLetter;
			result.append(letter);
		}
		
		System.out.println(message + " >>> " + result.toString());
		
		return result.toString();
	}
	
	private String DecimalToBinary(int dec, int length) throws Exception
	{
		int bitLength = GetBitLength(dec);
		if(bitLength > length)
			throw new Exception("DecimalToBinary hatasi : Sayinin bit uzunlugu, girilen sinir degerinden daha buyuk.");
		
		String zeros = "";
		
		int diff = length - bitLength;
		for(int i = 0; i < diff; i++)
			zeros += '0';
		
		String bin = DecimalToBinary(dec);
		
		return zeros + bin;
	}
	
	private String DecimalToBinary(int dec)
	{
		int bitLength = GetBitLength(dec);
		
		StringBuilder bin = new StringBuilder();
		int total = 0;
		for(int i = bitLength - 1; i >= 0; i--)
		{
			if(total + Math.pow(2, i) <= dec)
			{
				bin.append('1');
				total += Math.pow(2, i);
			}
			else
				bin.append('0');
		}
		
		return bin.toString();
	}
	
	private int GetBitLength(int n)
	{
		int i = 0;
		while(i < n)
		{
			if(Math.pow(2, i) < n && n < Math.pow(2, i+1))
				return i+1;
			
			i++;
		}
		
		return 0;
	}
}
