package com.aero.math;

import java.math.BigInteger;

public abstract class Montgomery
{
	//
	// Montgomery mod�ler �arp�m algoritmas�
	//
	public static BigInteger ModularMultiply(BigInteger x, BigInteger y, BigInteger p)
	{
		//
		// Ama� : x*y (mod p) i�leminin sonucunu hesaplamak
		//
		
		// Montgomery domain : R
		BigInteger R = BigInteger.ONE;
		
		// R'nin ters modu (inverse R)
		BigInteger _R;
		
		// Kullan�lacak ara parametreler
		BigInteger _x;
		BigInteger _y;
		BigInteger a;
		
		// Sonu� de�eri i�in
		BigInteger result;
		
		// R'nin bulunmas�;
		// R say�s� b taban�nda ve b^k > p olacak �ekilde bir tamsay�d�r.
		int base = 10;
		do
		{
			R = R.multiply(BigInteger.valueOf(base));
		}
		while (R.compareTo(p) != 1);
		
		// Yard�mc� ara de�erler hesaplan�r:
		_x = x.multiply(R).mod(p);
		_y = y.multiply(R).mod(p);
		
		// a de�erinin hesaplanmas�:
		// 1. ad�m;
		a = _x.multiply(_y);
		
		// 2. ad�m : bulunan a de�erine R'nin tam bir kat� olana kadar p eklenir.
		while (a.mod(R).compareTo(BigInteger.ZERO) != 0)
		{
			a = a.add(p);
		}
		
		// 3. ad�m;
		a = a.divide(R);
		
		// R'nin tersinin hesaplanmas�
		_R = Euclidean.ExtendedEuclidean(R, p)[1];
		// _R = R.modInverse(p);
		if (_R.compareTo(BigInteger.ZERO) == -1)
			_R.add(p);
		
		// SONU� :
		result = a.multiply(_R).mod(p);
		
		return result;
	}
}
