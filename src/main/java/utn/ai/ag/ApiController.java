package utn.ai.ag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<Boolean> test() {
		return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/solution", method = RequestMethod.POST)
    public ResponseEntity<Solution> createClient(@RequestBody Inputs req) {
		
		Solution response  = Algorithm.execute(req.getN_queens(), req.getPoblation_count(), req.getMax_generations(), req.getP_mutations());  
        
        return new ResponseEntity<Solution>(response, HttpStatus.OK);
    }
	
}
