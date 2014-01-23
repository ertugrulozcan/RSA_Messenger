package com.aero.math;

import java.math.BigInteger;

public abstract class BinaryModularExponentiation
{
	public static BigInteger ModPower(BigInteger m, BigInteger e, BigInteger n)
	{
		BigInteger c;
		// Mesaj�n bit dizisine d�n��t�r�lmesi;
		Boolean[] bitArray = ConvertToBinary(e);
		
		if (bitArray[0])
			c = m;
		else
			c = BigInteger.ONE;
		
		for (int i = 1; i < bitArray.length; i++)
		{
			/*
			 * if(bitArray[i]) c = Montgomery.ModularMultiply(c, c.multiply(m), n); else c =
			 * Montgomery.ModularMultiply(c, c, n);
			 */
			
			if (bitArray[i])
				c = c.multiply(c).multiply(m);
			else
				c = c.multiply(c);
			
			c = c.mod(n);
		}
		
		return c;
	}
	
	//
	// �ok �ekirdekli i�lemciler i�in
	//
	
	private static final class BinaryModExpThread extends Thread
	{
		private BigInteger m, e, n;
		
		public BigInteger result;
		
		// Kurucu
		public BinaryModExpThread(BigInteger m, BigInteger e, BigInteger n)
		{
			this.m = m;
			this.e = e;
			this.n = n;
		}
		
		@Override
		public void run()
		{
			result = ModPower(m, e, n);
		}
	}
	
	// Tek �ekirdek i�in;
	public static BigInteger ModPowerForSingleCore(BigInteger m, BigInteger e, BigInteger n)
	{
		return ModPower(m, e, n);
	}
	
	// �ift �ekirdek i�in;
	public static BigInteger ModPowerForDualCore(BigInteger m, BigInteger e, BigInteger n)
	{
		// Yap�lacak i�in 2'ye b�l�nmesi
		BigInteger e1, e2;
		
		// e �ift say� ise;
		if (e.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0)
		{
			e1 = e2 = e.divide(BigInteger.valueOf(2));
		}
		else
		{
			e1 = e.add(BigInteger.ONE).divide(BigInteger.valueOf(2));
			e2 = e1.subtract(BigInteger.ONE);
		}
		
		// Thread'lerin olu�turulmas�;
		BinaryModExpThread thread1 = new BinaryModExpThread(m, e1, n);
		BinaryModExpThread thread2 = new BinaryModExpThread(m, e2, n);
		
		// Thread'lerin �al��t�r�lmas�;
		thread1.start();
		thread2.start();
		
		while (thread1.isAlive() || thread2.isAlive())
		{
			// �ki thread de tamamlanana kadar bekle;
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
		
		// Her iki thread de tamamland�ktan sonra sonucu hesapla ve d�n.
		return Montgomery.ModularMultiply(thread1.result, thread2.result, n);
	}
	
	// D�rt �ekirdek i�in;
	public static BigInteger ModPowerForQuadCore(BigInteger m, BigInteger e, BigInteger n)
	{
		// Yap�lacak i�in 4'e b�l�nmesi
		BigInteger e1, e2, e3, e4;
		
		// e �ift say� ise;
		if (e.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0)
		{
			BigInteger _e = e.divide(BigInteger.valueOf(2));
			// _e �ift say� ise;
			if (_e.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0)
			{
				e1 = e2 = e3 = e4 = _e.divide(BigInteger.valueOf(2));
			}
			else
			{
				e1 = _e.add(BigInteger.ONE).divide(BigInteger.valueOf(2));
				e2 = e1.subtract(BigInteger.ONE);
				
				e3 = e1;
				e4 = e2;
			}
		}
		else
		{
			BigInteger _e1 = e.add(BigInteger.ONE).divide(BigInteger.valueOf(2));
			e1 = _e1.add(BigInteger.ONE).divide(BigInteger.valueOf(2));
			e2 = e1.subtract(BigInteger.ONE);
			
			BigInteger _e2 = _e1.subtract(BigInteger.ONE);
			e3 = e4 = _e2.divide(BigInteger.valueOf(2));
		}
		
		// Thread'lerin olu�turulmas�;
		BinaryModExpThread thread1 = new BinaryModExpThread(m, e1, n);
		BinaryModExpThread thread2 = new BinaryModExpThread(m, e2, n);
		BinaryModExpThread thread3 = new BinaryModExpThread(m, e3, n);
		BinaryModExpThread thread4 = new BinaryModExpThread(m, e4, n);
		
		// Thread'lerin �al��t�r�lmas�;
		thread1.start();
		thread2.start();
		thread3.start();
		thread4.start();
		
		while (thread1.isAlive() || thread2.isAlive() || thread3.isAlive() || thread4.isAlive())
		{
			// D�rt thread de tamamlanana kadar bekle;
			try
			{
				Thread.sleep(100);
			}
			catch (InterruptedException ex)
			{
				ex.printStackTrace();
			}
		}
		
		BigInteger resultPart1 = Montgomery.ModularMultiply(thread1.result, thread2.result, n);
		BigInteger resultPart2 = Montgomery.ModularMultiply(thread3.result, thread4.result, n);
		
		// Her d�rt thread de tamamland�ktan sonra sonucu hesapla ve d�n.
		return Montgomery.ModularMultiply(resultPart1, resultPart2, n);
	}
	
	//
	// BigInteger say�n�n binary formata d�n��t�r�lmesi
	//
	private static Boolean[] ConvertToBinary(BigInteger n)
	{
		// Bit uzunlu�unun tespiti;
		int bitLength = GetBitLength(n);
		
		//
		// Bit dizisinin olu�turulmas�
		//
		Boolean[] result = new Boolean[bitLength];
		result[0] = true;
		BigInteger t = BigInteger.valueOf(2).pow(bitLength - 1);
		for (int i = 1; i < bitLength; i++)
		{
			if (t.add(BigInteger.valueOf(2).pow(bitLength - i - 1)).compareTo(n) == 1)
			{
				result[i] = false;
			}
			else
			{
				result[i] = true;
				t = t.add(BigInteger.valueOf(2).pow(bitLength - i - 1));
			}
		}
		
		return result;
	}
	
	//
	// Bit uzunlu�unun tespiti;
	//
	private static int GetBitLength(BigInteger n)
	{
		int bitLength = 1;
		
		// Tek say� m�? �ift say� m�?
		Boolean isDouble = n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0;
		
		while (n.compareTo(BigInteger.ONE) == 1)
		{
			if (!isDouble)
				n = n.subtract(BigInteger.ONE);
			
			n = n.divide(BigInteger.valueOf(2));
			bitLength++;
			
			isDouble = n.mod(BigInteger.valueOf(2)).compareTo(BigInteger.ZERO) == 0;
		}
		
		return bitLength;
	}
}
