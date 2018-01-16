package net.savantly.shack.web.modules.autoconfigure;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import net.savantly.shack.web.modules.ShackModule;
import net.savantly.sprout.core.module.registration.SproutModuleRegistration;
import net.savantly.sprout.core.module.registration.SproutModuleRegistrationRepository;

@Configuration
public class ShackAutoConfiguration {
	
	@Autowired
	SproutModuleRegistrationRepository registrations;
	@Autowired
	ShackModule module;
	
	@PostConstruct
	public void postConstruct() {
		SproutModuleRegistration shackInstallation = registrations.findOne(ShackModule.BEAN_NAME);
		if (shackInstallation == null || !shackInstallation.isInstalled()) {
			module.install();
		}
	}

}
