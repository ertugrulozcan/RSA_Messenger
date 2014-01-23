package com.aero.math;

import java.math.BigInteger;

public abstract class Montgomery
{
	//
	// Montgomery modüler çarpým algoritmasý
	//
	public static BigInteger ModularMultiply(BigInteger x, BigInteger y, BigInteger p)
	{
		//
		// Amaç : x*y (mod p) iþleminin sonucunu hesaplamak
		//
		
		// Montgomery domain : R
		BigInteger R = BigInteger.ONE;
		
		// R'nin ters modu (inverse R)
		BigInteger _R;
		
		// Kullanýlacak ara parametreler
		BigInteger _x;
		BigInteger _y;
		BigInteger a;
		
		// Sonuç deðeri için
		BigInteger result;
		
		// R'nin bulunmasý;
		// R sayýsý b tabanýnda ve b^k > p olacak þekilde bir tamsayýdýr.
		int base = 10;
		do
		{
			R = R.multiply(BigInteger.valueOf(base));
		}
		while (R.compareTo(p) != 1);
		
		// Yardýmcý ara deðerler hesaplanýr:
		_x = x.multiply(R).mod(p);
		_y = y.multiply(R).mod(p);
		
		// a deðerinin hesaplanmasý:
		// 1. adým;
		a = _x.multiply(_y);
		
		// 2. adým : bulunan a deðerine R'nin tam bir katý olana kadar p eklenir.
		while (a.mod(R).compareTo(BigInteger.ZERO) != 0)
		{
			a = a.add(p);
		}
		
		// 3. adým;
		a = a.divide(R);
		
		// R'nin tersinin hesaplanmasý
		_R = Euclidean.ExtendedEuclidean(R, p)[1];
		// _R = R.modInverse(p);
		if (_R.compareTo(BigInteger.ZERO) == -1)
			_R.add(p);
		
		// SONUÇ :
		result = a.multiply(_R).mod(p);
		
		return result;
	}
}
