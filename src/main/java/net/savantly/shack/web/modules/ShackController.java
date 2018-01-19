package net.savantly.shack.web.modules;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.savantly.pm2j.Pm2Connector;
import net.savantly.pm2j.Pm2ProcessInfo;
import net.savantly.shack.web.modules.pm2.Pm2Configuration;

@RequestMapping("/rest/modules/shack")
@Controller
public class ShackController {
	
	private static final Logger log = LoggerFactory.getLogger(ShackController.class);
	private static ObjectMapper objectMapper = new ObjectMapper();
	static{
		objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
		objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}
	
	@Autowired
	private Pm2Connector pm2;
	@Autowired
	private Pm2Configuration config;

	private RestTemplate rest = new RestTemplate();

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("shack/index");
		ResponseEntity<JsonNode> apiResponse = queryApi();
		modelAndView.addObject("apiRunning", !(apiResponse == null));
		modelAndView.addObject("config", config.getConfig());
		return modelAndView;
	}
	
	@RequestMapping("/processes")
	public ModelAndView getProcesses() {
		ResponseEntity<JsonNode> apiResponse = queryApi();
		if (apiResponse == null) {
			throw new RuntimeException("pm2 web api is not running");
		}
		List<Pm2ProcessInfo> pm2Processes = new ArrayList<Pm2ProcessInfo>();
		try {
			apiResponse.getBody().get("processes").forEach(n -> {
				try {
					pm2Processes.add(objectMapper.readValue(n.toString(), Pm2ProcessInfo.class));
				} catch (IOException e) {
					log.error("failed to deserialize process info", e);
				}
			});
			
		} catch (Exception ex) {
			log.error("", ex);
		}
		
		ModelAndView modelAndView = new ModelAndView("shack/partials/processes");
		modelAndView.addObject("processes", pm2Processes);
		return modelAndView;
	}
	
	@RequestMapping("/start")
	public ModelAndView start() {
		pm2.startPm2(config.getConfig());
		ModelAndView modelAndView = new ModelAndView("shack/partials/processes");
		modelAndView.addObject("processes", pm2.getPm2Processes());
		return modelAndView;
	}
	
	@RequestMapping("/startApi")
	public ModelAndView startApi() {
		pm2.executePm2Command("web");
		return getProcesses();
	}

	@RequestMapping("/show/{processId}")
	public ModelAndView show(@PathVariable("processId") String processId) {
		ModelAndView modelAndView = new ModelAndView("shack/partials/text");
		modelAndView.addObject("text", pm2.showDetail(processId));
		return modelAndView;
	}

	@RequestMapping("/logs/{processId}")
	public ModelAndView logs(@PathVariable("processId") String processId) {
		ModelAndView modelAndView = new ModelAndView("shack/partials/text");
		modelAndView.addObject("text", "not implemented");
		return modelAndView;
	}
	
	@RequestMapping("/stop")
	public ModelAndView stop() {
		pm2.stopPm2();
		ModelAndView modelAndView = new ModelAndView("shack/partials/processes");
		modelAndView.addObject("processes", pm2.getPm2Processes());
		return modelAndView;
	}
	
	@RequestMapping("/kill")
	public ModelAndView kill() {
		pm2.killPm2();
		ModelAndView modelAndView = new ModelAndView("shack/partials/processes");
		modelAndView.addObject("processes", new Object[0]);
		return modelAndView;
	}
	
	private ResponseEntity<JsonNode> queryApi() {
		try {
			return this.rest.getForEntity(new URI(this.config.getPm2HttpApi()), JsonNode.class);
		} catch (RestClientException | URISyntaxException e) {
			log.error("", e);
			return null;
		}
	}
}
