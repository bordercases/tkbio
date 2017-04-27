package bio.knowledge.server.api;


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
@javax.annotation.Generated(value = "io.swagger.codegen.languages.SpringCodegen", date = "2017-04-27T09:43:12.446-07:00")

@Controller
public class ExactmatchesApiController implements ExactmatchesApi {



    public ResponseEntity<List<String>> getExactMatchesToConcept(@ApiParam(value = "local object identifier of the concept to be matched",required=true ) @PathVariable("conceptId") String conceptId) {
        // do some magic!
        return new ResponseEntity<List<String>>(HttpStatus.OK);
    }

}