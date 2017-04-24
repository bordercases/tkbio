package bio.knowledge.server.api;

import bio.knowledge.server.model.Identifiers;
import bio.knowledge.server.model.InlineResponse2002;
import bio.knowledge.server.model.InlineResponse2003;
import bio.knowledge.server.model.InlineResponse201;
import java.util.List;

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
public class XrefsApiController implements XrefsApi {



    public ResponseEntity<List<InlineResponse2002>> getConceptXRefs(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2002>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse2003>> getXRefConcepts( @NotNull @ApiParam(value = "identifier of the query session established by prior POST'ing of a list of cross-references identifiers of interest ", required = true) @RequestParam(value = "queryId", required = true) String queryId) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2003>>(HttpStatus.OK);
    }

    public ResponseEntity<InlineResponse201> postXRefQuery(@ApiParam(value = "list of cross-reference identifiers to be used in a search for equivalent concepts " ,required=true ) @RequestBody List<Identifiers> identifiers) {
        // do some magic!
        return new ResponseEntity<InlineResponse201>(HttpStatus.OK);
    }

}