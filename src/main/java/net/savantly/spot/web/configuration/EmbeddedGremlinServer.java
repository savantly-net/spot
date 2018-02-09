package net.savantly.spot.web.configuration;

import org.apache.tinkerpop.gremlin.server.GremlinServer;
import org.apache.tinkerpop.gremlin.server.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("gremlin")
@ConditionalOnProperty("gremlin.embedded")
public class EmbeddedGremlinServer implements InitializingBean, DisposableBean  {

	private static final Logger log = LoggerFactory.getLogger(EmbeddedGremlinServer.class);
	private GremlinServer server;

	private Settings getSettings() {
		Settings settings = new Settings();
		return settings;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.server = new GremlinServer(getSettings());
		this.server.start();
	}

	@Override
	public void destroy() throws Exception {
		this.server.stop();
	}

}
