package utn.ai.ag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
	
	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(value = "/solution", method = RequestMethod.POST)
    public ResponseEntity<Solution> createClient(@RequestBody Inputs req) {
		
		Solution response  = Algorithm.execute(req.getN_queens(), 
												req.getPoblation_count(), 
												req.getMax_generations(), 
												req.getP_mutations(),
												req.getF_method(),
												req.getSelection_type(),
												req.getCrossover_point()
												);
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
	
}
