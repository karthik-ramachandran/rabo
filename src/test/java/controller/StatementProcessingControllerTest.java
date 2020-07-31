package controller;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.rabo.statementprocessing.services.CustomerValidationService;
import com.rabo.statementprocessing.services.impl.CustomerValidationServiceImpl;

@RunWith(PowerMockRunner.class)
@AutoConfigureMockMvc
public class StatementProcessingControllerTest {

	private MockMvc mockMvc;
	
	@InjectMocks
	private StatementProcessingController stateProcessingControllerMock;
	
	@Mock
	private CustomerValidationService customerValidationServiceMock = new CustomerValidationServiceImpl();
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(stateProcessingControllerMock).build();
		System.out.println("Befor");
	}
	
	@Test
	void validateCustomerTestSuccess() {
		MvcResult mvcResult;
		try {
			mockMvc = MockMvcBuilders.standaloneSetup(new StatementProcessingController()).build();
			mvcResult = mockMvc.perform(MockMvcRequestBuilders
			        .post("/process/customer")
			                .contentType(MediaType.TEXT_HTML)
			                .content(buildCustomer())
			        )
			        .andDo(MockMvcResultHandlers.print())
			        .andExpect(status().isOk())
			        .andReturn();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void validateCustomerTestFiler() {
	
		String invalidInput = null;
		MvcResult mvcResult;
		try {
			mockMvc = MockMvcBuilders.standaloneSetup(new StatementProcessingController()).build();
			mvcResult = mockMvc.perform(MockMvcRequestBuilders
			        .post("/process/customer")
			                .contentType(MediaType.TEXT_HTML)
			                .content(invalidInput)
			        )
			        .andDo(MockMvcResultHandlers.print())
			        .andExpect(status().isOk())
			        .andReturn();
				assertEquals(null, mvcResult.getResponse().getContentAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String buildCustomer() {
		return "[\n" + 
				"  {\n" + 
				"    \"reference\": 194261,\n" + 
				"    \"accountNumber\": \"NL91RABO0315273637\",\n" + 
				"    \"description\": \"Clothes from Jan Bakker\",\n" + 
				"    \"startBalance\": 21.6,\n" + 
				"    \"mutation\": -41.83,\n" + 
				"    \"endBalance\": -20.23\n" + 
				"  }\n" + 
				"]";
	}
}
