package edu.cuny.csi.csc330.discordbot.game;

import java.util.*;

/**
 * Useful utility class around Random Number Generation ... uses the Math.random() 
 * common library Class/method. 
 */

public class Randomizer {
	
	/**
     * Return a random integer between low & high 
     */
    public static int generateInt(int low, int high) {
        return ( (int) ( (Math.random() * 1000000000 )  % ((high + 1) - low) )  + low );
    }
    public static int generateInt(double low, double high) {
        return generateInt((int) low, (int) high);
    }
	
    /*
     * return some random decimal number
     */
    public static double generateDecimal() {
        return Math.random() * 1000000000; 
    }
    
    public static int generateInt() {
        return ( (int) generateDecimal() );
    }
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		// Test some of the key methods provided 
		System.out.println(Randomizer.generateDecimal());
		System.out.println(Randomizer.generateInt());
		
		
		int from = 11;
		int to = 20; 
		Integer number = Randomizer.generateInt(from, to); 
		System.out.printf("Number between %d and %d:  %d\n", from, to, number); 
		
		int intCount = 10; 

		int [] integers = new int[intCount]; 
		
		for(int i = 0 ; i < intCount ; ++i) {
			integers[i] = Randomizer.generateInt(1, 8);
		}
		
		Arrays.sort(integers);
		
		for(int i = 0 ; i < intCount ; ++i) {
			System.out.printf("[%d] = %d\n",  i,  integers[i] );
		}

	}

}

