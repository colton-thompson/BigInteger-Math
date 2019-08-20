// Name: sqrtTest
// Author: Colton Thompson
// Date: April 24th, 2019
// Purpose: find sqrt of BigInteger numbers
// Notes: preforms division in Integer logic so final answer will not be exact but rather a 
// 		  rounded version to the nearest whole number

import java.math.*; 
  
public class sqrtBigInteger { 
  
	public static BigInteger sqrt(BigInteger x) {
		BigInteger div = BigInteger.ZERO.setBit(x.bitLength()/2);
		BigInteger div2 = div, y;
		// Loop until we hit the same value twice in a row, or wind up alternating.
		do {
			//update variables 
			y = div.add(x.divide(div)).shiftRight(1);
			div2 = div;
			div = y;
		} while(!(y.equals(div) || y.equals(div2))); //do while y != either val option
		return y;
	}
	
	public static void main (String[] args) 
	{ 
		BigInteger n = new BigInteger("3423421341242346964974976496749764952354234524523452345675467389456787531323"); 
		System.out.println("the sqrt of " + n + " is " + sqrt(n));
	} 
} 