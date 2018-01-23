package net.savantly.spot.web.modules;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/modules/gremlin")
@RestController
public class GremlinController {
	
	
	@RequestMapping("/")
	public Object query(String gremlinQuery) {
		return null;
	}

}
