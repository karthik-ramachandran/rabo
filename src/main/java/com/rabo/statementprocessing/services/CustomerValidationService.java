package com.rabo.statementprocessing.services;

import com.rabo.statementprocessing.utils.Result;
import com.rabo.statementprocessing.vo.CustomerVO;

public interface CustomerValidationService {		
		Result validate(String customer);
}
