package net.savantly.spot.web.modules;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rest/modules/spot")
@RestController
public class SpotController {

	@RequestMapping(value="/")
	public String index() {
		return "index";
	}
	
}
