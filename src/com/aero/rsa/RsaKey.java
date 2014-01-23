package com.aero.rsa;

import java.math.BigInteger;

/*
 * RsaKey.java
 * 
 * Ahmet Ertugrul Ozcan ertugrul.ozcan@bil.omu.edu.tr
 */

public abstract class RsaKey
{
	// Parametreler
	private BigInteger n;
	
	//
	// Kurucu metod
	//
	public RsaKey(BigInteger n)
	{
		this.n = n;
	}
	
	// getN()
	public BigInteger N()
	{
		return this.n;
	}
	
	//
	// Ortak anahtar
	//
	public static class PublicKey extends RsaKey
	{
		private BigInteger e;
		
		public PublicKey(BigInteger n, BigInteger e)
		{
			super(n);
			this.e = e;
		}
		
		public BigInteger E()
		{
			return this.e;
		}
	}
	
	//
	// Ozel anahtar
	//
	public static class PrivateKey extends RsaKey
	{
		private BigInteger d;
		
		public PrivateKey(BigInteger n, BigInteger d)
		{
			super(n);
			this.d = d;
		}
		
		public BigInteger D()
		{
			return this.d;
		}
	}
}
