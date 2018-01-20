package net.savantly.spot.web.modules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.savantly.sprout.core.module.SimpleSproutModuleExecutionResponse;
import net.savantly.sprout.core.module.SproutModuleAdapter;
import net.savantly.sprout.core.module.SproutModuleExecutionResponse;

@Component
public class SpotModule extends SproutModuleAdapter {
	
	public static final String BEAN_NAME = "spotModule";
	
	private static final Logger log = LoggerFactory.getLogger(SpotModule.class);
	private static final String VERSION = "0.0.1";
	private static final String NAME = "spot";
	private static final String PATH = "/rest/modules/spot/";
	private static final String DESCRIPTION = "Web interface for a graph abstraction of networked objects";
	
	@Autowired
	private SpotFixture fixture;

	public SpotModule() {
		log.info("Instantiated SpotModule");
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public String getWelcomeUrl() {
		return PATH;
	}
	
	@Override
	public String getVersion() {
		return VERSION;
	}

	@Override
	public String getDescription() {
		return DESCRIPTION;
	}

	@Override
	public SproutModuleExecutionResponse install() {
		try {
			fixture.install();
			return new SimpleSproutModuleExecutionResponse(true, 0, "Install completed");
		} catch (Exception e) {
			return new SimpleSproutModuleExecutionResponse(false, 1, e.getMessage());
		}
	}

	@Override
	public SproutModuleExecutionResponse uninstall() {
		return new SimpleSproutModuleExecutionResponse(false, 1, "Cannot uninstall Shack");
	}

}