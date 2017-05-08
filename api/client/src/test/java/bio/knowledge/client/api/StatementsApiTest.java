/*
 * Translator Knowledge.Bio API
 * Documentation of the Translator Knowledge.Bio (TKBio) knowledge sources query web service application programming interfaces. Learn more about [TKBio](https://github.com/STARInformatics/tkbio) 
 *
 * OpenAPI spec version: 4.0.4
 * Contact: richard@starinformatics.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package bio.knowledge.client.api;

import bio.knowledge.client.ApiClient;
import bio.knowledge.client.ApiException;
import bio.knowledge.client.model.InlineResponse2003;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for StatementsApi
 */
//@Ignore
public class StatementsApiTest {
	private static final boolean RUNNING_CLIENT_LOCALLY = true;

    private static final StatementsApi api = new StatementsApi();
    static {
    	if (RUNNING_CLIENT_LOCALLY) {
	    	ApiClient apiClient = new ApiClient();
	    	apiClient.setBasePath("http://localhost:8080/api/");
	    	api.setApiClient(apiClient);
    	}
    }
    
    /**
     * 
     *
     * Given a list of concept identifiers to a given KS endpoint,  retrieves a paged list of concept-relations with either the subject or object concept matching a concept in the input list 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getStatementsTest() throws ApiException {
    	// Just testing to see if it reaches the server
//    	api.getStatements("", null, null, null, null);
    	fail();
    }
    
}
