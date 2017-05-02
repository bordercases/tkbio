package bio.knowledge.server.api;

import bio.knowledge.server.model.InlineResponse2002;

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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-05-02T09:12:42.779-07:00")

@Controller
public class ExactmatchesApiController implements ExactmatchesApi {



    public ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "(url-encoded) CURIE identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

    public ResponseEntity<List<InlineResponse2002>> getExactMatchesToConceptList( @NotNull @ApiParam(value = "a (urlencoded) space-delimited set of [CURIE-encoded](https://www.w3.org/TR/curie/) identifiers of exactly matching concepts, to be used in a search for additional exactly matching concepts ", required = true) @RequestParam(value = "emci", required = true) String emci) {
        // do some magic!
        return new ResponseEntity<List<InlineResponse2002>>(HttpStatus.OK);
    }

}
