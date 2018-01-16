package net.savantly.shack.web.modules;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import net.savantly.pm2j.Pm2Connector;
import net.savantly.shack.web.modules.pm2.Pm2Configuration;

@RequestMapping("/rest/modules/shack")
@Controller
public class ShackController {
	
	@Autowired
	Pm2Connector pm2;
	@Autowired
	Pm2Configuration config;

	@RequestMapping("/")
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("shack/index");
		modelAndView.addObject("config", config.getConfig());
		return modelAndView;
	}
	
	@RequestMapping("/processes")
	public ModelAndView getProcesses() {
		ModelAndView modelAndView = new ModelAndView("shack/partials/processes");
		modelAndView.addObject("processes", pm2.getPm2Processes());
		return modelAndView;
	}
}
