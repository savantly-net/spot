package net.savantly.spot.web.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.example.GraphOfTheGodsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("janus")
public class JanusGraphConfiguration {

	private static final Logger log = LoggerFactory.getLogger(JanusGraphConfiguration.class);
	private Map<String, String> server = new HashMap<String, String>();
	private boolean loadDemo = false;

	@Bean
	public Graph graph() {
		JanusGraph graph = JanusGraphFactory.open(janusServerConfiguration());
		if (loadDemo) {
			GraphOfTheGodsFactory.load(graph);
		}
		return graph;
	}
	
	@Bean
	public GraphTraversalSource traversalSource(Graph graph) {
		GraphTraversalSource g = graph.traversal();
		return g;
	}

	private PropertiesConfiguration janusServerConfiguration() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		this.getServer().keySet().forEach(k -> {
			config.addProperty(k, this.getServer().get(k));
		});
		return config;
	}
	
	public Map<String, String> getServer() {
		return server;
	}

	public void setServer(Map<String, String> server) {
		this.server = server;
	}

}
