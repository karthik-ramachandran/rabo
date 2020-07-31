package com.rabo.statementprocessing.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import com.google.gson.Gson;
import com.rabo.statementprocessing.constants.Constant;
import com.rabo.statementprocessing.services.CustomerValidationService;
import com.rabo.statementprocessing.utils.Result;
import com.rabo.statementprocessing.utils.Result.ErrorRecords;

@RunWith(MockitoJUnitRunner.class)
public class CustomerValidationServiceImplTest {

	@Spy
	@InjectMocks
	private CustomerValidationService customerValidationServiceMoce  = new CustomerValidationServiceImpl();
	@Mock
	private Result resultMock;
	@Mock
	private ErrorRecords errorRecordsMock;
	@Mock
	private Gson gson;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		customerValidationServiceMoce = new CustomerValidationServiceImpl();
	}
	
	@Test
	public void testValidtionSuccess() {
		Result result = customerValidationServiceMoce.validate(buildJsonString());
		assertEquals(Constant.SUCCESS_CODE, result.getCode());
		assertEquals(Constant.SUCCESSFUL, result.getResult());
	}
	
	@Test
	public void testValidtion_WhenDuplicateRefrenceAndCorrectCount() {
		Result result = customerValidationServiceMoce.validate(buildDuplicateRefrenceCorrectCount());
		assertEquals(Constant.SUCCESS_CODE, result.getCode());
		assertEquals(Constant.ERROR_DUPLICATE_REFERENCE, result.getResult());
		assertEquals(1, result.getErrorRecords().size());
		assertEquals("account_number_of_error_record", result.getErrorRecords().get(0).getAccountNumber());
		assertEquals("reference_of_error_record", result.getErrorRecords().get(0).getReference());
	}
	
	@Test
	public void testValidtion_WhenIncorrectBalance() {
		Result result = customerValidationServiceMoce.validate(buildJsonStringWithIncorrectBalance());
		assertEquals(Constant.SUCCESS_CODE, result.getCode());
		assertEquals(Constant.ERROR_INCORRECT_END_BALANCE, result.getResult());
		assertEquals(1, result.getErrorRecords().size());
		assertEquals("account_number_of_error_record", result.getErrorRecords().get(0).getAccountNumber());
		assertEquals("reference_of_error_record",result.getErrorRecords().get(0).getReference());
	}
	
	@Test
	public void testValidtion_WhenParsingExcepion() {
		Result result = customerValidationServiceMoce.validate(buildInvalidJSON());
		assertEquals(Constant.ERROR_PARSING, result.getCode());
		assertEquals(Constant.ERROR_BAD_REQUEST, result.getResult());
		assertEquals(0, result.getErrorRecords().size());
	}
	
	@Test
	public void testValidtion_WhenExcepionOccuredWhilePassingNull() {
		Result result = customerValidationServiceMoce.validate(null);
		assertEquals(Constant.ERROR_SERVER, result.getCode());
		assertEquals(Constant.ERROR_INTERNAL_SERVER_ERROR, result.getResult());
		assertEquals(0, result.getErrorRecords().size());
	}

	private String buildJsonString() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"reference\": 194261,\r\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\r\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\r\n" + 
				"    \"startBalance\": 21.6,\r\n" + 
				"    \"mutation\": -41.83,\r\n" + 
				"    \"endBalance\": -20.23\r\n" + 
				"  }\r\n" + 
				"]";
	}
	
	private String buildJsonStringWithIncorrectBalance() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"reference\": 194261,\r\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\r\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\r\n" + 
				"    \"startBalance\": 21.6,\r\n" + 
				"    \"mutation\": -41.83,\r\n" + 
				"    \"endBalance\": -10.23\r\n" + 
				"  }\r\n" + 
				"]";
	}
	
	private String buildInvalidJSON() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"reference\": 194261,\r\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\r\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\r\n" + 
				"    \"startBalance\": 21.6,\r\n" + 
				"    \"mutation\": -41.83,\r\n" + 
				"    \"endBalance\": -10.23\r\n";
	}
	
	
	private String buildDuplicateRefrenceCorrectCount() {
		return "[\r\n" + 
				"  {\r\n" + 
				"    \"reference\": 194261,\r\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\r\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\r\n" + 
				"    \"startBalance\": 21.6,\r\n" + 
				"    \"mutation\": -41.83,\r\n" + 
				"    \"endBalance\": -20.23\r\n" + 
				"  },\r\n" + 
				"  {\r\n" + 
				"    \"reference\": 194261,\r\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\r\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\r\n" + 
				"    \"startBalance\": 21.6,\r\n" + 
				"    \"mutation\": -41.83,\r\n" + 
				"    \"endBalance\": -20.23\r\n" + 
				"  }\r\n" + 
				"]";
	}
	
	
}
