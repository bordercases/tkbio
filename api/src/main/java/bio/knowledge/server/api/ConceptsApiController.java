package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse200;
import bio.knowledge.server.model.InlineResponse2001;

import io.swagger.annotations.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import javax.validation.constraints.*;
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-19T14:17:46.341-07:00")

@Controller
public class ConceptsApiController implements ConceptsApi {



    public ResponseEntity<List<InlineResponse200>> getConceptDetails(@ApiParam(value = "local identifier of concept of interest",required=true ) @PathVariable("conceptId") Integer conceptId) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse200>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse2001>> getConcepts( @NotNull @ApiParam(value = "string of comma delimited keywords to match against concept names and aliases", required = true) @RequestParam(value = "textFilter", required = true) String textFilter,
         @ApiParam(value = "(1-based) number of the page to be returned in a paged set of query results ") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
         @ApiParam(value = "number of concepts per page to be returned in a paged set of query results ") @RequestParam(value = "pageSize", required = false) Integer pageSize,
         @ApiParam(value = "string of comma-delimited semantic type names to apply against set of concepts matched by the main search string ") @RequestParam(value = "semanticType", required = false) String semanticType) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2001>>(HttpStatus.OK);
    }

}