package net.savantly.spot.web.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/rest/modules/spot")
@Controller
public class SpotController {
	
	private static final Logger log = LoggerFactory.getLogger(SpotController.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	static{
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("spot/index");
		return modelAndView;
	}
	
	@RequestMapping("/hello")
	public ModelAndView hello() {
		ModelAndView modelAndView = new ModelAndView("spot/partials/text");
		modelAndView.addObject("text", "<h1>Hello World</h1>");
		return modelAndView;
	}
	
}
