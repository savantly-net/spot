package net.savantly.shack.web.modules.pm2;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.savantly.pm2j.Pm2Connector;

@Configuration
@ConfigurationProperties("pm2")
public class Pm2Configuration {

	private String executablePath;
	private String home;
	private String config;
	private String pm2HttpApi = "http://0.0.0.0:9615";
	
	@Bean
	public Pm2Connector pm2Connector(){
		return new Pm2Connector();
	}
	
	public String getExecutablePath() {
		return executablePath;
	}
	public void setExecutablePath(String executablePath) {
		this.executablePath = executablePath;
	}
	public String getHome() {
		return home;
	}
	public void setHome(String home) {
		this.home = home;
	}
	public String getConfig() {
		return config;
	}
	public void setConfig(String config) {
		this.config = config;
	}

	public String getPm2HttpApi() {
		return pm2HttpApi;
	}

	public void setPm2HttpApi(String pm2HttpApi) {
		this.pm2HttpApi = pm2HttpApi;
	}
	
}
