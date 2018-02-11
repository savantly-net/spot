package net.savantly.spot.web.configuration;

import java.io.IOException;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ConfigurationProperties("gremlin")
@ConditionalOnProperty("gremlin.embedded")
public class EmbeddedGremlinServer implements InitializingBean, DisposableBean  {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedGremlinServer.class);
	
	Resource graphProperties;
	Resource serverConfig;
	
	private GremlinServer server;
	private String host;
	private int port;

	private Settings getSettings() throws IOException {
		Settings settings;
		if(this.serverConfig != null) {
			settings = Settings.read(serverConfig.getInputStream());
		} else {
			settings = new Settings();
			settings.host = this.host;
			settings.port = this.port;
		}
		return settings;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Graph graph = this.createGraph();
		GraphTraversalSource g = graph.traversal();

		this.server = new GremlinServer(getSettings());
		this.server.getServerGremlinExecutor().getGraphManager().putGraph("graph", graph);
		this.server.getServerGremlinExecutor().getGremlinExecutor().getGlobalBindings().put("graph", graph);
		this.server.getServerGremlinExecutor().getGremlinExecutor().getGlobalBindings().put("g", g);
		
		this.server.start();
	}

	private Graph createGraph() throws ConfigurationException, IOException {
		PropertiesConfiguration configuration = new PropertiesConfiguration();
		configuration.load(this.graphProperties.getInputStream());
		TinkerGraph graph = TinkerGraph.open(configuration);
		return graph;
	}

	@Override
	public void destroy() throws Exception {
		this.server.stop();
	}


	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Resource getGraphProperties() {
		return graphProperties;
	}

	public void setGraphProperties(Resource graphProperties) {
		this.graphProperties = graphProperties;
	}
	public Resource getServerConfig() {
		return serverConfig;
	}

	public void setServerConfig(Resource serverConfig) {
		this.serverConfig = serverConfig;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
