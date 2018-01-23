package net.savantly.spot.web.configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.driver.Client;
import org.apache.tinkerpop.gremlin.driver.Cluster;
import org.apache.tinkerpop.gremlin.process.traversal.TraversalSource;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.apache.tinkerpop.gremlin.server.util.ServerGremlinExecutor;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.util.config.YamlConfiguration;
import org.janusgraph.core.JanusGraphFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import io.netty.channel.EventLoopGroup;

@Configuration
@ConfigurationProperties("janus")
public class JanusGraphConfiguration {

	private static final Logger log = LoggerFactory.getLogger(JanusGraphConfiguration.class);
	private boolean gremlinEmbedded = true;
	private Resource gremlinServerConfiguration;
	private Resource gremlinRemoteConfiguration;
	private Resource groovyInitScript;
	private Map<String, String> server = new HashMap<String, String>();

	@Configuration
	@ConditionalOnProperty("janus.gremlin-embedded")
	public class EmbeddedGremlinServer {
		@Bean
		public GremlinServer gremlinServer(Graph g) throws Exception {
			GremlinServer server = new GremlinServer(getGremlinServerSettings());
			CompletableFuture<ServerGremlinExecutor<EventLoopGroup>> completable = server.start();
			completable.thenRun(() -> {
				server.getServerGremlinExecutor().getGraphManager().putGraph("graph", g);
				try {
					String script = new BufferedReader(new InputStreamReader(groovyInitScript.getInputStream()))
							  .lines().collect(Collectors.joining("\n"));
					log.debug("executing script: {}", script);
					server.getServerGremlinExecutor().getGremlinExecutor().eval(script, "gremlin-groovy", new HashMap<String, Object>());
				} catch (IOException e) {
					log.error("Failed to run groovy init script", e);
				}
				
			});
			return server;
		}
		
		private Settings getGremlinServerSettings() throws IOException {
			Settings settings = Settings.read(getGremlnServerConfiguration().getInputStream());
			return settings;
		}
	}
	
	@Configuration
	public class RemoteConnection {
		@Bean
		public Cluster cluster() throws ConfigurationException, IOException {
			YamlConfiguration config = getRemoteConfiguration();
			log.info(config.getString("hosts"));
			Cluster cluster = Cluster.open(config);
			return cluster;
		}
		
		@Bean public Client client(Cluster cluster) {
			Client client = cluster.connect();
			return client;
		}
		
		private YamlConfiguration getRemoteConfiguration() throws ConfigurationException, IOException{
			YamlConfiguration yaml = new YamlConfiguration();
			yaml.load(getGremlinRemoteConfiguration().getInputStream());
			return yaml;
		}
	}
	
	@Bean
	public Graph graph() {
		Graph graph = JanusGraphFactory.open(janusServerConfiguration());
		return graph;
	}
	
	@Bean
	public TraversalSource traversalSource(Graph graph) {
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

	public boolean isGremlinEmbedded() {
		return gremlinEmbedded;
	}

	public void setGremlinEmbedded(boolean gremlinEmbedded) {
		this.gremlinEmbedded = gremlinEmbedded;
	}

	public Resource getGremlnServerConfiguration() {
		return gremlinServerConfiguration;
	}

	public void setGremlinServerConfiguration(Resource gremlinServerConfiguration) {
		this.gremlinServerConfiguration = gremlinServerConfiguration;
	}

	public Resource getGremlinRemoteConfiguration() {
		return gremlinRemoteConfiguration;
	}

	public void setGremlinRemoteConfiguration(Resource gremlinRemoteConfiguration) {
		this.gremlinRemoteConfiguration = gremlinRemoteConfiguration;
	}

	public Map<String, String> getServer() {
		return server;
	}

	public void setServer(Map<String, String> server) {
		this.server = server;
	}
	
	public Resource getGroovyInitScript() {
		return groovyInitScript;
	}

	public void setGroovyInitScript(Resource groovyInitScript) {
		this.groovyInitScript = groovyInitScript;
	}
}
