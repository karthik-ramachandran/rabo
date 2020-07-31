package controller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rabo.statementprocessing.constants.Constant;
import com.rabo.statementprocessing.services.CustomerValidationService;
import com.rabo.statementprocessing.utils.Result;
import com.rabo.statementprocessing.utils.Result.ErrorRecords;

@RestController
@RequestMapping("process")
public class StatementProcessingController {
	
	@Autowired 
	CustomerValidationService customerValidationService;
	

	private static final Logger logger = LoggerFactory.getLogger(StatementProcessingController.class);

	@PostMapping(value="/customer")
	public Result validateCustomer(@RequestBody String customer) {
		
		try {
			if(customer!=null)
				return customerValidationService.validate(customer);
			else
				Result.failed(Constant.ERROR_SERVER, Constant.ERROR_INTERNAL_SERVER_ERROR, new ArrayList<ErrorRecords>());
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
}
