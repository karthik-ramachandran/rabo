package com.rabo.statementprocessing.utils;

import java.io.Serializable;
import java.util.ArrayList;
import lombok.Data;

@Data
public class Result implements Serializable {

	/** Error code */
	private Integer code;

	/** Result state */
	private String result;

	/** Error records */
	private ArrayList<ErrorRecords> errorRecords;
	
	public Result(Integer code, String result, ArrayList<ErrorRecords> errorRecords) {
		super();
		this.code = code;
		this.result = result;
		this.errorRecords = errorRecords;
	}

	public static class ErrorRecords {
		private String reference;
		private String accountNumber;

		public ErrorRecords() {} 

		public ErrorRecords(String reference, String accountNumber) { 
			super();
			this.reference = reference; this.accountNumber = accountNumber; 
		}
	

		public String getReference() {
			return reference;
		}

		public void setReference(String reference) {
			this.reference = reference;
		}

		public String getAccountNumber() {
			return accountNumber;
		}

		public void setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
		}
	}

	/* Filer message builder */
	public static Result failed(int code, String result, ArrayList<ErrorRecords> errorRecord) {
		return new Result(code, result, errorRecord);
	}

	/* Success message builder */
	public static Result success(Integer successCode, String successful, ArrayList<ErrorRecords> errorRecord) {
		return new Result(successCode, successful, errorRecord);
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ArrayList<ErrorRecords> getErrorRecords() {
		return errorRecords;
	}

	public void setErrorRecords(ArrayList<ErrorRecords> errorRecords) {
		this.errorRecords = errorRecords;
	}

}
