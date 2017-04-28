package bio.knowledge.server.api;

import bio.knowledge.database.repository.ConceptRepository;
import bio.knowledge.model.Concept;
import bio.knowledge.model.neo4j.Neo4jConcept;
import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;
import bio.knowledge.server.model.InlineResponse2001DataPage;
import io.swagger.annotations.*;

import org.neo4j.ogm.json.JSONArray;
import org.neo4j.ogm.json.JSONException;
import org.neo4j.ogm.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {
	
	@Value("${application.baseurl:}")
	private String BASE_URL;

	@Autowired
	ConceptRepository conceptRepository;
	
	private String sendGet(
			String filter,
			String semanticGroups,
			int pageNumber,
			int pageSize
	) throws Exception {
		
		
		String url = BASE_URL + "concepts?";
		url += "pageSize=" + String.valueOf(pageSize) + "&pageNumber=" + String.valueOf(pageNumber);
		url += "&q=" + filter.replace(" ", "%2C");
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		//print result
		return response.toString();

	}

    public ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "local object identifier of concept of interest",required=true ) @PathVariable("conceptId") String conceptId) {
    	try {
			Integer conceptIntegerId = Integer.valueOf(conceptId);
			Concept concept = conceptRepository.apiGetConceptById(conceptIntegerId);
			List<InlineResponse200> responses = new ArrayList<InlineResponse200>();

			if (concept != null) {
				InlineResponse200 response = new InlineResponse200();
				response.setDefinition(concept.getDescription());
				response.setId(String.valueOf(concept.getId()));
				response.setName(concept.getName());
				String semanticType = concept.getSemanticGroup().name();
				response.setSemanticGroup(semanticType);
				response.setSynonyms(Arrays.asList(concept.getSynonyms().split("\\|")));

				responses.add(response);
			}

			return ResponseEntity.ok(responses);
		} catch (NumberFormatException e) {
			// There are no concepts with id's that are not integers, so return an empty list
			return ResponseEntity.ok(new ArrayList<InlineResponse200>());
		}
    }

    @SuppressWarnings("unused")
	public ResponseEntity<InlineResponse2001> getConcepts( @NotNull @ApiParam(value = "a (urlencoded) space delimited set of keywords or substrings against which to match concept names and synonyms", required = true) @RequestParam(value = "keywords", required = true) String keywords,
         @ApiParam(value = "a (urlencoded) space-delimited set of semantic groups (specified as codes CHEM, GENE, ANAT, etc.) to which to constrain concepts matched by the main keyword search (see [SemGroups](https://metamap.nlm.nih.gov/Docs/SemGroups_2013.txt) for the full list of codes) ") @RequestParam(value = "sg", required = false) String sg,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        
    	if (pageNumber == null || pageNumber < 0) {
			pageNumber = 0;
		}
		if (pageSize == null || pageSize < 1) {
			pageSize = 10;
		}
		
		InlineResponse2001 response = new InlineResponse2001();
		List<InlineResponse2001DataPage> dataPages = new ArrayList<InlineResponse2001DataPage>();
		
		try {
			String httpResponse = sendGet(keywords, sg, pageNumber, pageSize);
			JSONObject jsonObject = new JSONObject(httpResponse);
			JSONArray jsonArray = jsonObject.getJSONArray("dataPage");
			
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject concept = jsonArray.getJSONObject(i);
				
				String name = concept.getString("label");
				String id = concept.getString("id");
				String semanticTypes = concept.getString("types");
				String description = concept.getString("description");
				String synonoms = concept.getString("aliases");
				
				InlineResponse2001DataPage dataPage = new InlineResponse2001DataPage();
				
				dataPage.setId(id);
				dataPage.setName(name);
				dataPage.setSemanticGroup(semanticTypes);
				
				dataPages.add(dataPage);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setDataPage(dataPages);

		return ResponseEntity.ok(response);
    }

}
