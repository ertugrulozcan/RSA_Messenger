package com.aero.math;

import java.math.BigInteger;

public abstract class Euclidean
{
	//
	// Geniþletilmiþ Öklid Algoritmasý
	//
	public static BigInteger[] ExtendedEuclidean(BigInteger A, BigInteger B)
	{
		BigInteger[] result = new BigInteger[3];
		Boolean reverse = false;
		
		if (A.compareTo(B) == -1)
		{
			BigInteger temp = A;
			A = B;
			B = temp;
			reverse = true;
		}
		
		BigInteger r = B;
		BigInteger q = BigInteger.ZERO;
		BigInteger x0 = BigInteger.ONE;
		BigInteger y0 = BigInteger.ZERO;
		BigInteger x1 = BigInteger.ZERO;
		BigInteger y1 = BigInteger.ONE;
		BigInteger x = BigInteger.ZERO, y = BigInteger.ZERO;
		
		while (A.mod(B).compareTo(BigInteger.ZERO) != 0)
		{
			r = A.mod(B);
			q = A.divide(B);
			x = x0.subtract(q.multiply(x1));
			y = y0.subtract(q.multiply(y1));
			x0 = x1;
			y0 = y1;
			x1 = x;
			y1 = y;
			A = B;
			B = r;
		}
		
		result[0] = r;
		
		if (reverse)
		{
			result[1] = y;
			result[2] = x;
		}
		else
		{
			result[1] = x;
			result[2] = y;
		}
		return result;
	}
}
