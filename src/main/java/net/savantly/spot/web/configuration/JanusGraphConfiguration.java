package net.savantly.spot.web.configuration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.util.config.YamlConfiguration;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties("janus")
public class JanusGraphConfiguration {

	private static final Logger log = LoggerFactory.getLogger(JanusGraphConfiguration.class);
	private Resource remoteConfigurationFile;
	private Map<String, String> server = new HashMap<String, String>();
	
	@Bean
	public Graph graph() {
		Graph graph = JanusGraphFactory.open(janusServerConfiguration());
		return graph;
	}
	
	@Bean
	public GremlinServer gremlinServer() throws IOException {
		GremlinServer server = new GremlinServer(getGremlinServerSettings());
		return server;
	}
	
	private Settings getGremlinServerSettings() throws IOException {
		Settings settings = new Settings();
		settings.read(this.getRemoteConfigurationFile().getInputStream());
		// settings.graphs
		return null;
	}

	@Bean
	public TraversalSource traversalSource(Graph graph) {
		GraphTraversalSource g = graph.traversal();
		return g;
	}

	@Bean
	public Cluster cluster() throws ConfigurationException, IOException {
		YamlConfiguration config = configuration();
		log.info(config.getString("hosts"));
		Cluster cluster = Cluster.open(config);
		return cluster;
	}
	
	@Bean public Client client(Cluster cluster) {
		Client client = cluster.connect();
		return client;
	}
	
	private YamlConfiguration configuration() throws ConfigurationException, IOException{
		YamlConfiguration yaml = new YamlConfiguration();
		yaml.load(this.getRemoteConfigurationFile().getInputStream());
		return yaml;
	}

	private PropertiesConfiguration janusServerConfiguration() {
		PropertiesConfiguration config = new PropertiesConfiguration();
		this.getServer().keySet().forEach(k -> {
			config.addProperty(k, this.getServer().get(k));
		});
		return config;
	}

	public Resource getRemoteConfigurationFile() {
		return remoteConfigurationFile;
	}

	public void setRemoteConfigurationFile(Resource remoteConfigurationFile) {
		this.remoteConfigurationFile = remoteConfigurationFile;
	}

	public Map<String, String> getServer() {
		return server;
	}

	public void setServer(Map<String, String> server) {
		this.server = server;
	}
}
