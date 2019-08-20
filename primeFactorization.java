// Name: primeFactorization
// Author: Colton Thompson
// Date: April 24th, 2019
// Purpose: find the prime factors of BigInteger numbers
// Notes: runs in 3 cases: 
// 					- exceptional for cases with small primes and one large prime
//					- moderate for cases with some small primes	and some large primes
//					- essentially fails for cases where there are no small factors - takes too long

import java.math.*; // for BigInteger
import java.io.*; 
import java.util.*; // for Random and vector
import java.lang.Math; 
import java.security.SecureRandom; // for better randoms


class primeFactorization { 
	private static BigInteger ZERO = new BigInteger("0"); 
	private static BigInteger ONE = new BigInteger("1");
	private static BigInteger TWO = new BigInteger("2");
	private static BigInteger THREE = new BigInteger("3");
	private static BigInteger FOUR = new BigInteger("4");
	private static BigInteger HUNDO = new BigInteger("100");
	
	public static BigInteger modularExp(BigInteger base, BigInteger exponent, BigInteger modulus) {
		BigInteger result = ONE;
		//exponent.compareTo(ZERO) --> (exponent > 0)
		//will return 1 if greater, 0 if equal, -1 if less than
		while (exponent.compareTo(ZERO) > 0) {
				//exp % 2 == 1
			if ((exponent.mod(TWO)).equals(ONE)) {
				//result = (result * base) % modulus
				result = ((result.multiply(base))).mod(modulus);
			}
			//dividing by 2 is same as 1 bit shift (>>)
			//exponent = exponent.divide(TWO); 
			exponent = exponent.shiftRight(1);
			base = ((base.multiply(base))).mod(modulus); 
		} 
		return result; //System.out.println("result: " +  result);
	} //End modularExp()
	
	public static BigInteger nextPrime(BigInteger s1) {
		BigInteger s2, i, j;
		Boolean flag = false;
	
		s2 = s1.add(HUNDO);
		
		//this loop will generate our primes
		for (i = s1.add(TWO); i.compareTo(s2) != 1; i = i.add(TWO)) {
			//this loop will generate mod values to test against primes
			//can start at 3 because factors of 2 have been removed
			for (j = THREE; i.compareTo(j) == 1; j = j.add(TWO)) {
				if(i.mod(j).equals(ZERO)) {
					flag = false;
					break;
				} else {
					flag = true;
				}
			}
			
			if(flag == true) {
				return i;
			}
		}
		//return -1 if an error occurred
		return ONE.negate();
		//return s1.add(TWO);
	} //End nextPrime()
	
	/*
	public static BigInteger sqrt(BigInteger x) {
		BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
		BigInteger div2 = div;
		BigInteger y;
		// Loop until we hit the same value twice in a row, or wind
		// up alternating.
		do {
			y = div.add(x.divide(div)).shiftRight(1);
			//if (y.equals(div) || y.equals(div2))
			//	return y;
			div2 = div;
			div = y;
		} while(!(y.equals(div) || y.equals(div2))); //do while y != either val
		System.out.println("the sqrt of " + x + " is " + y);
		return y;
	} //End sqrt() */
	
	static boolean millerRabinTest(BigInteger d, BigInteger n) { 
		// Pick a random number in [2..n-2] 	
		//System.out.println("numbits: " + (n.subtract(TWO)).bitLength());
		BigInteger a = new BigInteger(n.subtract(TWO).bitLength(), new SecureRandom());
		a = a.add(TWO); //System.out.println("a: " + a);
		
		// Compute a^d % n  
		BigInteger x = modularExp(a, d, n); //System.out.println("x: " + x);
		if (x.equals(ONE) || ((x.equals(n.subtract(ONE)))))
			return true;
		
		//square x repeatedly while:
		//	d is not n - 1
		//	x^2 mod n != 1
		//	x^2 mod n != n - 1
		while (!(d.equals(n.subtract(ONE)))) {
			x = (x.multiply(x)).mod(n); //x = (x * x) % n; 
			d = d.multiply(TWO); 	//d *= 2; 
			
			if (x.equals(ONE)) //(x == 1) 
				return false; 
				
			if (x.equals(n.subtract(ONE))) //(x == n - 1) 
				return true; 
		} 
		return false; // Return composite 
	} //End millerRabinTest()
	
	// false if n is composite, true if n is probably prime 
	// input k is number of iterations, more iterations more accurate (1/4^k)
	static boolean isPrime(BigInteger num, int numIterations) { 
		// Corner cases 
		//if (n <= 1 || n == 4) 
		if (num.compareTo(ONE) == -1 || num.equals(FOUR)) 
			return false; 
		//if (n <= 3) 
		if (num.compareTo(THREE) == -1 || num.equals(THREE))
			return true; 

		//setup d:
		//	- subtract one to make it even
		//	- while d (mod 2) = 0, divide a two out to set up n - 1 = d * 2^r
		BigInteger d = num.subtract(ONE);	// d = n - 1; 
		while (d.mod(TWO).compareTo(ZERO) == 0) { //(d % 2) == 0
			d = d.divide(TWO);
		}

		// Iterate while i is less than numIterations 
		for (int i = 0; i < numIterations; i++) {
			if (!millerRabinTest(d, num)) {
				//Miller-Rabin Test found factor, therefore composite
				return false; 
			}
		}
		//passed all the tests, it most likely prime (safe to assume)
		return true; 
	} //End isPrime()
	
	// A function to print all prime factors 
	// of a given number n 
	public static Vector<BigInteger> primeFactors(BigInteger n, BigInteger index, Vector<BigInteger> factorVec) 
	{ 
		//Vector<BigInteger> factorVec = new Vector<BigInteger>();
		// Print the number of 2s that divide n 
		//while (n%2==0)
		while (n.mod(TWO).equals(ZERO)) 
		{ 
			//System.out.print(2 + " "); 
			factorVec.add(TWO); //System.out.println("new factor is 2"); 
			n = n.divide(TWO); //n /= 2;
		} 
  
		//System.out.println("n value before logic: " + n);
		if (isPrime(n, 100)) {
			//System.out.println(n + " is prime");
			factorVec.add(n);
		} else {
			//System.out.println(n + " is composite");
			while (!(n.mod(index)).equals(ZERO)) {
				//index = nextPrime(index);
				index = index.add(TWO);
				//System.out.println("index: " + index);
			}
			factorVec.add(index); //System.out.println("new factor is " + index);
			//System.out.println("next prime: " + nextPrime(index));
			n = n.divide(index);
			primeFactors(n, index, factorVec);
		}
 		//return vector of factors
		return factorVec;
	} 
  
	public static void main (String[] args) 
	{ 
		BigInteger n = new BigInteger("34234213412423452354234524523452345635675894647200"); 
		Vector<BigInteger> factorVec = new Vector<BigInteger>();
		//System.out.println("n: " + n);
		factorVec = primeFactors(n, THREE, factorVec); 
		System.out.println("factors: " + factorVec);
		System.out.println(n + " has " + n.bitLength() + " bits");
		System.out.println(n + " has " + n.toString().length() + " digits");
	} 
} 