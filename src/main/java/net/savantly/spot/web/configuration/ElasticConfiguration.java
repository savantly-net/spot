package net.savantly.spot.web.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties("elastic")
@ConditionalOnProperty("elastic.embedded")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ElasticConfiguration {
	
	private String nodeName;
	private String dataPath;
	private String clusterName;
	
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getDataPath() {
		return dataPath;
	}

	public void setDataPath(String dataPath) {
		this.dataPath = dataPath;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

/*	@Bean
	public Node embeddedElastic() throws Exception {
		return new ElasticSearchNodeFactoryBean().getObject();
	}*/
	
	@Component
	public class ElasticSearchNodeFactoryBean  {

	}

}
