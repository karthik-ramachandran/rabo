package com.rabo.statementprocessing.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.rabo.statementprocessing.constants.Constant;
import com.rabo.statementprocessing.services.CustomerValidationService;
import com.rabo.statementprocessing.utils.Result;
import com.rabo.statementprocessing.utils.Result.ErrorRecords;
import com.rabo.statementprocessing.vo.CustomerVO;

import controller.StatementProcessingController;

public class CustomerValidationServiceImpl implements CustomerValidationService{
	
	private static final Logger logger = LoggerFactory.getLogger(StatementProcessingController.class);
	
	@Override
	public Result validate(String customerJSON) {
		
		ArrayList<ErrorRecords> errorRecords = new ArrayList<ErrorRecords>();
		try {
			Gson gson = new Gson();
			CustomerVO[] customerVO = gson.fromJson(customerJSON, CustomerVO[].class);
			List<CustomerVO> customerLst = Arrays.asList(customerVO);
			
			//When there are no duplicate reference and correct end balance 
			if(checkDuplicateReference(customerLst)  && checkCorrectEndBalance(customerLst)) {
				
				return Result.success(Constant.SUCCESS_CODE, Constant.SUCCESSFUL, errorRecords);
			}		
			//When there are duplicate reference and correct balance 
			else if(checkDuplicateReference(customerLst) == false  && checkCorrectEndBalance(customerLst)) {
				errorRecords.add(new ErrorRecords("reference_of_error_record","account_number_of_error_record"));
				return Result.success(Constant.SUCCESS_CODE, Constant.ERROR_DUPLICATE_REFERENCE, errorRecords);
			}
			//When there are no duplicate reference and In correct balance
			else if(checkDuplicateReference(customerLst) && checkCorrectEndBalance(customerLst) == false) {
				errorRecords.add(new ErrorRecords("reference_of_error_record","account_number_of_error_record"));
				return Result.success(Constant.SUCCESS_CODE, Constant.ERROR_INCORRECT_END_BALANCE, errorRecords);
			}
			//When there are duplicate reference and In correct balance
			else if(checkDuplicateReference(customerLst) == false && checkCorrectEndBalance(customerLst) == false) {
				errorRecords.add(new ErrorRecords("reference_of_duplicate_record","account_number_of_duplicate_record"));
				errorRecords.add(new ErrorRecords("reference_of_inCorrectEndBalance_record","account_number_of_ inCorrectEndBalance _record"));
				return Result.success(Constant.SUCCESS_CODE, Constant.ERROR_DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, errorRecords);
			}
		}catch(JsonParseException ex){
			logger.error(ex.getMessage());
			errorRecords.clear();
			return Result.failed(Constant.ERROR_PARSING, Constant.ERROR_BAD_REQUEST, errorRecords);
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			return Result.failed(Constant.ERROR_SERVER, Constant.ERROR_INTERNAL_SERVER_ERROR, errorRecords);
		}
		return null;
	}
	/**
	 * Check the any Reference duplication is there
	 * */
	public boolean checkDuplicateReference(List<CustomerVO> customerLst) {
		
		if(customerLst.size() == customerLst.stream().map(e -> e.getReference()).distinct().collect(Collectors.toList()).size())
			return true;
		else
			return false;
	}
	
	/**
	 * Check the correct end balance is there
	 * */
	public boolean checkCorrectEndBalance(List<CustomerVO> customerLst) {
		
		if(customerLst.size() == customerLst.stream().filter(e -> Math.round((e.getStartBalance() + e.getMutation())*100D)/100D == e.getEndBalance()).collect(Collectors.toList()).size())
			return true;
		else
			return false;
	}
	
	public CustomerValidationServiceImpl() {
		super();
	}

}
