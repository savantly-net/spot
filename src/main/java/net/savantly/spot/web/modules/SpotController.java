package net.savantly.spot.web.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.PropertyKey;
import org.janusgraph.core.schema.JanusGraphManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.savantly.spot.web.configuration.JanusGraphConfiguration;
import net.savantly.spot.web.modules.graphexp.GraphExpService;

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

	@Autowired
	private JanusGraphConfiguration config;
	@Autowired
	private GraphExpService graphExpService;
	
	@RequestMapping("/")
	public ModelAndView index() {
		JanusGraph graph = config.getGraph();
		JanusGraphManagement mgmt = graph.openManagement();
		Iterable<PropertyKey> keys = mgmt.getRelationTypes(PropertyKey.class);
		List<String> vertexKeys = new ArrayList<>();
		keys.forEach(k -> {
			vertexKeys.add(k.toString());
		});
		ModelAndView modelAndView = new ModelAndView("spot/index");
		modelAndView.addObject("keys", vertexKeys);
		return modelAndView;
	}
	
	@RequestMapping("/hello")
	public ModelAndView hello() {
		ModelAndView modelAndView = new ModelAndView("spot/partials/text");
		modelAndView.addObject("text", "<h1>Hello World</h1>");
		return modelAndView;
	}
	
	@RequestMapping(value = "/vertex", method=RequestMethod.POST)
	public ModelAndView addVertex(Map<String, String> options) {
		Vertex v = config.getGraph().addVertex(options.get("label"));
		ModelAndView modelAndView = new ModelAndView("spot/partials/text");
		modelAndView.addObject("text", v.toString());
		return modelAndView;
	}
	
}
