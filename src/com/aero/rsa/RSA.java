package com.aero.rsa;

import java.math.BigInteger;
import java.util.Random;

import com.aero.rsa.RsaKey.*;

/*
 * RSA.java
 * 
 * Ahmet Ertugrul Ozcan ertugrul.ozcan@bil.omu.edu.tr
 * 
 * Mikail Simsek mikail.simsek@bil.omu.edu.tr
 * 
 * RSA sifreleme algoritmasinin uygulamasina iliskin parametrelerin uretiminden sorumlu sinif.
 */

public class RSA
{
	// GEREKLILIKLER:
	/*
	 * 1) p ve q asallarinin uretimi 2) n = p*q 3) Totient'in bulunmasi : T(n) = (p - 1)*(q - 1) 4) 1 < e < T(n)
	 * araliginda ve EBOB(e, T(n))=1 olan bir e sayisi uretilmesi 5) d.e % T(n) = 1 olacak bicimde bir d sayisi
	 * uretilmesi
	 * 
	 * ORTAK ANAHTAR : (n, e) OZEL ANAHTAR : (n, d)
	 * 
	 * NOT : d, p, q ve T(n) degerleri kesinlikle gizli kalmalidir.
	 */
	
	//
	// Degiskenler, sabitler, sinif uyeleri
	//
	private BigInteger p, q, t;
	private PublicKey publicKey;
	private PrivateKey privateKey;
	
	// Sifreleme icin kullanilan asal parametrelerin bit uzunlugu
	/*
	 * KeySize, sifreleme ve desifreleme icin kullanilacak anahtarlarin kac bitten olusacagini gosterir. Bu ozelligin
	 * degerini degistirerek, kullanilacak bit sayisini ayarlayabilirsiniz. Varsayilan anahtar boyutu 1024 bittir.
	 */
	private int keySize = 1024;
	
	private long progressTime;
	
	//
	// Kurucu metod - Constructor (Argumansiz versiyon)
	//
	public RSA()
	{
		long start = System.nanoTime();
		
		// p ve q asallainin uretilmesi
		this.GeneratePrimes();
		
		// Totient hesabi
		this.t = Totient();
		
		// Ortak anahtar uretimi
		this.GeneratePublicKey();
		
		// Ozel anahtar uretimi
		this.GeneratePrivateKey();
		
		long finish = System.nanoTime();
		
		this.progressTime = finish - start;
		
		this.PrintInfo();
	}
	
	public RSA(int keySize)
	{
		long start = System.nanoTime();
		
		// Yeni anahtar degerinin ayarlanmasi
		this.SetKeySize(keySize);
		
		// p ve q asallainin uretilmesi
		this.GeneratePrimes();
		
		// Totient hesabi
		this.t = Totient();
		
		// Ortak anahtar uretimi
		this.GeneratePublicKey();
		
		// Ozel anahtar uretimi
		this.GeneratePrivateKey();

		long finish = System.nanoTime();
		
		this.progressTime = finish - start;
		
		this.PrintInfo();
	}
	
	public PublicKey getPublicKey()
	{
		return this.publicKey;
	}
	
	public PrivateKey getPrivateKey()
	{
		return this.privateKey;
	}
	
	//
	// Ortak anahtar uretimi
	//
	private void GeneratePublicKey()
	{
		BigInteger n = this.p.multiply(this.q);
		BigInteger e = BigInteger.valueOf(65537);
		
		this.publicKey = new PublicKey(n, e);
	}
	
	//
	// Ozel anahtar uretimi
	//
	private void GeneratePrivateKey()
	{
		BigInteger n = this.p.multiply(this.q);
		
		BigInteger d = this.publicKey.E().modInverse(this.t);
		if (d.compareTo(BigInteger.ZERO) == 0)
			d = d.add(this.t);
		
		this.privateKey = new PrivateKey(n, d);
	}
	
	//
	// Asallarin uretimi
	//
	private void GeneratePrimes()
	{
		Random random = new Random();
		// P asalinin uretilmesi
		this.p = BigInteger.probablePrime(this.keySize, random);
		
		// Q asalinin uretilmesi
		// Not : P ile Q esit olmamalidir.
		do
		{
			random = new Random();
			this.q = BigInteger.probablePrime(this.keySize, random);
		}
		while (p.compareTo(q) == 0);
	}
	
	//
	// Totient hesabi
	//
	private BigInteger Totient()
	{
		BigInteger t1 = p.subtract(BigInteger.ONE);
		BigInteger t2 = q.subtract(BigInteger.ONE);
		return t1.multiply(t2);
	}
	
	//
	// Anahtar uzunlugunu ayarlamak icin kullanilan metod.
	// RSA algoritmasi icin anahtar buyuklugu en az 384 bit en fazla 16384 bittir.
	//
	public void SetKeySize(int newSize)
	{
		this.keySize = newSize;
	}
	
	public int GetKeySize()
	{
		return this.keySize;
	}
	
	public long ProgressTime()
	{
		return this.progressTime;
	}
	
	public void PrintInfo()
	{
		System.out.println("RSA Demonstration :");
		System.out.println("p = " + this.p);
		System.out.println("q = " + this.q);
		System.out.println("n = " + this.publicKey.N());
		System.out.println("t = " + this.t);
		System.out.println("e = " + this.publicKey.E());
		System.out.println("d = " + this.privateKey.D());
	}
}
