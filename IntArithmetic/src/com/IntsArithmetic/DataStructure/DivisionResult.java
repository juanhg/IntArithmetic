package com.IntsArithmetic.DataStructure;

public class DivisionResult {
	
	BigInt quotant;
	BigInt r;
	
	DivisionResult(){
		quotant = new BigInt();
		r = new BigInt();
	}

	public BigInt getQuotant() {
		return quotant;
	}

	public void setQuotant(BigInt quotant) {
		this.quotant = quotant;
	}

	public BigInt getR() {
		return r;
	}

	public void setR(BigInt r) {
		this.r = r;
	}
	
	

}
