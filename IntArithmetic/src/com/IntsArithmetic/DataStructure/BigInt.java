package com.IntsArithmetic.DataStructure;
/**
 * @file BigInt.java
 * @brief BigInt class
 *
 * @version 0.1
 * @author José Ignacio Carmona Villegas <joseicv@correo.ugr.es>
 * @author Juan Hernandez García <juanhg@correo.ugr.es>
 * @date 07/October/2013
 *
 * @section LICENSE
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details at
 * http://www.gnu.org/copyleft/gpl.html
 * 
 */

import java.util.Vector;
import java.util.Collections;

/**
 * @brief Big Integer.
 * Represents a Big Integer of variable length.
 */
public class BigInt
{
	// Maximum number of digits of any valid number in our base
	private final int DIGITS_PER_PART = 9;
	// Base
	private final int MAX_INTEGER_PER_PART = 1000000000;
	
	private Vector<Integer> data;
	private boolean isNegative;
	
	
	public BigInt(String input)
	{	
		data = new Vector<Integer>(0);
		isNegative = false;
		
		// The first chars are the most significative.
		// Read DIGITS_PER_PART decimal digits, and add them to an Integer.
		
		// Since the number may not be composed of a number of digits divisible by DIGITS_PER_PART,
		// first the rest will be read (most significative) and then the divisible amounts
		// as blocks of DIGITS_PER_PART characters (least significative).
		int quotant = (int) (input.length()/DIGITS_PER_PART);
		int rest = (int) (input.length()%DIGITS_PER_PART);
		
		//System.out.println("input length = "+input.length()+" quotant = "+quotant+" and rest = "+rest);
		
		String aux = "";
		for(int i=0; i<rest; ++i)
		{
			aux += input.charAt(i);
		}
		//System.out.println(aux);
		if(aux != "")
		{
			data.add(new Integer(Integer.parseInt(aux)));
		}
		
		int offset = rest;
		for(int j=0; j<quotant; ++j)
		{
			aux = "";
			for(int i=offset; i<offset+DIGITS_PER_PART; ++i)
			{
				aux += input.charAt(i);
			}
			//System.out.println(aux);
			data.add(new Integer(Integer.parseInt(aux)));
			
			offset += DIGITS_PER_PART;
		}
		
		//System.out.println("Vector size : "+data.size());
	}
	
	public BigInt()
	{
		data = new Vector<Integer>(0);
		isNegative = false;
	}
	
	public BigInt(BigInt data)
	{
		// TODO Copy constructor, implement if needed
	}
	
	public String toString()
	{
		String s = "";
		if(isNegative)
		{
			s += "-";
		}
		else
		{
			s += " ";
		}
		for(int i=0; i<data.size(); ++i)
		{
			// Fill with zeroes until the maximum number of digits of any given number in our base.
			// The first element is exempt from this rule.
			if(i != 0)
			{
				int digits;
				if(data.elementAt(i) == 0)
				{
					digits = 1;
				}
				else
				{
					digits = (int)(Math.log10(data.elementAt(i))+1);
				}
				for(int j=digits; j<DIGITS_PER_PART; ++j)
				{
					s += "0";
				}
			}
			
			s += data.elementAt(i).toString();
			//s += " ";
		}
		return s;
	}
	
	public BigInt add(BigInt data2)
	{
		// School algorithm
		boolean done = false;
		int index1 = this.data.size()-1;
		int index2 = data2.data.size()-1;
		long temp;
		int int1, int2;
		int carry = 0;
		
		BigInt result = new BigInt();
		while(!done)
		{
			if(index1 < 0)
			{
				int1 = 0;
			}
			else
			{
				int1 = this.data.elementAt(index1);
			}
			if(index2 < 0)
			{
				int2 = 0;
			}
			else
			{
				int2 = data2.data.elementAt(index2);
			}
			
			temp = int1 + int2 + carry;
			
			carry = (int) (temp/MAX_INTEGER_PER_PART);
			result.data.add(new Integer((int) (temp%MAX_INTEGER_PER_PART)));
			
			/*
			System.out.println("int1 = "+int1);
			System.out.println("int2 = "+int2);
			System.out.println("carry = "+carry);
			System.out.println("quotant = "+result.toString());
			*/
			
			--index1;
			--index2;
			if(index1 < 0 && index2 < 0)
			{
				done = true;
			}
		}
		
		// If once it's over, if there is some carry, add it in the next position.
		if(carry != 0)
		{
			result.data.add(new Integer(carry));
		}
		
		Collections.reverse(result.data);
		return result;
	}
	
	public boolean isGreaterThan(BigInt data2)
	{
		if(this.isNegative && !data2.isNegative)
		{
			return false;
		}
		else if(!this.isNegative && data2.isNegative)
		{
			return true;
		}
		else
		{
			if(this.data.size() < data2.data.size())
			{
				return false;
			}
			else if(this.data.size() > data2.data.size())
			{
				return true;
			}
			else
			{
				for(int i=0; i<this.data.size(); ++i)
				{
					if(this.data.elementAt(i) < data2.data.elementAt(i))
					{
						return false;
					}
					else if(this.data.elementAt(i) > data2.data.elementAt(i))
					{
						return true;
					}
				}
				return false;
			}
		}
	}
	
	public BigInt substract(BigInt data2)
	{
		BigInt result;
		
		if(data2.isGreaterThan(this))
		{
			result = data2.substractKernel(this);
			result.isNegative = true;
		}
		else
		{
			result = this.substractKernel(data2);
		}
		
		// Eliminate zeros on the left
		for(int i=0; i<result.data.size()-1; ++i)
		{
			if(result.data.elementAt(i) == 0 && i==0)
			{
				result.data.remove(i);
				--i;
			}
		}
		
		return result;
	}
	
	private BigInt substractKernel(BigInt data2)
	{
		// School algorithm
		boolean done = false;
		int index1 = this.data.size()-1;
		int index2 = data2.data.size()-1;
		long temp;
		int int1, int2;
		int carry = 0;
		
		BigInt result = new BigInt();
		while(!done)
		{
			if(index1 < 0)
			{
				int1 = 0;
			}
			else
			{
				int1 = this.data.elementAt(index1);
			}
			if(index2 < 0)
			{
				int2 = 0;
			}
			else
			{
				int2 = data2.data.elementAt(index2);
			}
			
			temp = int1 - int2 - carry;
			
			if(temp < 0)
			{
				temp = MAX_INTEGER_PER_PART + temp;
				carry = 1;
			}
			else
			{
				carry = 0;
			}
			
			result.data.add(new Integer((int) (temp)));
			
			/*
			System.out.println("int1 = "+int1);
			System.out.println("int2 = "+int2);
			System.out.println("carry = "+carry);
			System.out.println("quotant = "+result.toString());
			*/
			
			--index1;
			--index2;
			if(index1 < 0 && index2 < 0)
			{
				done = true;
			}
		}
		
		Collections.reverse(result.data);
		return result;
	}
	
	public BigInt multiplySchool(BigInt data2)
	{
		BigInt result = new BigInt();
		long temp;
		int carry = 0;
		int resultIndex = 0;
		int resultOffset = 0;
		
		// @pre Doesn't admit negative numbers
		// result = this * data2
		for(int i=data2.data.size()-1; i>=0; --i)
		{
			carry = 0;
			resultIndex = 0;
			for(int j=this.data.size()-1; j>=0; --j)
			{
				temp = ((long)(this.data.elementAt(j)) * (long)(data2.data.elementAt(i))) + (long)(carry);
				//System.out.println("("+this.data.elementAt(j)+" * "+data2.data.elementAt(i)+") + "+carry+" = "+temp);
				
				if(result.data.size() <= resultIndex + resultOffset)
				{
					// If there's no result value in this position, store as much as possible.
					result.data.add(new Integer((int) (temp%MAX_INTEGER_PER_PART)));
					//System.out.println("Adding new "+(int) (temp%MAX_INTEGER_PER_PART)+" to the vector ("+result.data.lastElement()+")");
				}
				else
				{
					// If there was a result value in this position, add it to the result, and store as much as possible.
					temp += result.data.elementAt(resultIndex + resultOffset);
					result.data.set(resultIndex + resultOffset, new Integer((int) (temp%MAX_INTEGER_PER_PART)));
					//System.out.println("Adding mod "+(int) (temp%MAX_INTEGER_PER_PART)+" to the vector ("+result.data.lastElement()+")");
				}
				
				// Whatever the case, keep the rest for the next iteration.
				carry = (int) (temp/MAX_INTEGER_PER_PART);
				//System.out.println("Carry = "+(int) (temp/MAX_INTEGER_PER_PART));
				
				++resultIndex;
			}
			// If once it's over, if there is some carry, add it in the next position.
			if(carry != 0)
			{
				if(result.data.size() <= resultIndex + resultOffset)
				{
					result.data.add(new Integer(carry));
				}
				else
				{
					temp = carry + result.data.elementAt(result.data.size()-1);
					result.data.set(resultIndex + resultOffset, new Integer((int) (temp%MAX_INTEGER_PER_PART)));
					carry = (int) (temp/MAX_INTEGER_PER_PART);
					if(carry != 0)
					{
						result.data.add(new Integer(carry));
					}
				}
			}
			++resultOffset;
		}
		
		Collections.reverse(result.data);
		return result;
	}
	
	/**
	 * Calculate the exponent "m" that makes the expresion "2^m"
	 * bigger than this.data.size()
	 * @return Integer value of m
	 */
	int nextPow2(){
		int m = 0;
		int size = this.data.size();
		boolean completed = false;
		
		while(!completed){
			if(size > (int)Math.pow(2.0, (double)m)){
				completed = true;
			}
			else
			{
				m++;
			}
		}
		return m;
	}
	
	/**
	 * Add left zeros this.data until this.data.size() is 2^m
	 * @param m 
	 */
	@SuppressWarnings("unchecked")
	void fillDigits(int m){
		BigInt reverse = this.clone();
		
		Collections.reverse(reverse.data);
		
		while(reverse.data.size() != Math.pow(2.0, (double)m)){
			reverse.data.add(0);
		}
		
		Collections.reverse(reverse.data);
		this.data = (Vector<Integer>) reverse.data.clone();
	}
	
	public BigInt multiplyKaratsuba(BigInt data2)
	{
		BigInt a = this.clone();
		BigInt b = data2.clone();
		BigInt result;
		
		int size1 = a.data.size();
		int size2 = b.data.size();
		int m = 0;
		
		if(size1 > size2)
		{
			m = a.nextPow2();
		}
		else{
			m = b.nextPow2();
		}
		
		a.fillDigits(m);
		b.fillDigits(m);
		
		result = this.multiplyKaratsubaKernel(a, b, m);
		return result;
	}
	
	/**
	 * Split the number and obtain his left part
	 * @return BigInt 
	 */
	public BigInt leftSplit(){
		BigInt result = new BigInt();
		int size = this.data.size();
		int end = size/2;
		
		if(end > 0)
		{	
			end = end - 1;
		}
		
		result.data = (Vector<Integer>) this.data.subList(0, end);
		result.isNegative = this.isNegative;
		
		return result;
	}
	
	
	
	/**
	 * Split the number and obtain his left part
	 * @return BigInt 
	 */
	public BigInt rightSplit(){
		BigInt result = new BigInt();
		int size = this.data.size();
		int begin = size/2;

		result.data = (Vector<Integer>) this.data.subList(begin, size-1);
		result.isNegative = this.isNegative;
		
		return result;
	}
	
	/**
	 * Added times zeros in this.data 
	 * @param times Zeros to be added
	 * @return BigInt with "times" mores zeros in data than the original
	 */
	public BigInt multiplyShift(int times){
		BigInt result = this.clone();
		
		for(int i = 0; i < times; i++){
			result.data.add(0);
		}
		
		return result;
	}
	
	public BigInt multiplyKaratsubaKernel(BigInt a, BigInt b, int m)
	{
		if(m == 0){
			return a.multiplySchool(b);
		}
		else
		{	
			BigInt a0 = a.leftSplit();
			BigInt a1 = a.rightSplit();
			BigInt b0 = b.leftSplit();
			BigInt b1 = b.rightSplit();
			
			BigInt a1_sub_a0 = a1.substract(a0);
			BigInt b0_sub_b1 = b0.substract(b1);
			
			BigInt t1 = this.multiplyKaratsubaKernel(a1, b1, m-1);
			BigInt t2 = this.multiplyKaratsubaKernel(a1_sub_a0, b0_sub_b1, m-1);
			BigInt t3 = this.multiplyKaratsubaKernel(a0, b0, m-1);
			
			BigInt t1_t2_t3 = t1.add(t2);
			t1_t2_t3 = t1_t2_t3.add(t3);
			
			BigInt result1 = t1.multiplyShift((int)Math.pow(2.0,(double)m));
			BigInt result2 = t1_t2_t3.multiplyShift((int)Math.pow(2.0,(double)(m-1)));
			
			BigInt result = result1.add(result2);
			result = result.add(t3);
			
			return result;
		}
	}
	
	public BigInt multiplyModular(BigInt data2)
	{
		return null;
	}
	
	@Override
    @SuppressWarnings(value = "unchecked")
    protected BigInt clone(){
		BigInt cloned = new BigInt();
		cloned.data = (Vector<Integer>) this.data.clone();
		cloned.isNegative = this.isNegative;
		return cloned;
	}
}
