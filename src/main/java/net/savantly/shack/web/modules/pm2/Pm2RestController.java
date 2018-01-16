package net.savantly.shack.web.modules.pm2;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import net.savantly.pm2j.Pm2Connector;
import net.savantly.pm2j.Pm2ProcessInfo;

@RequestMapping("/rest/modules/pm2")
@RestController
public class Pm2RestController {
	
	@Autowired
	Pm2Configuration config;
	@Autowired
	Pm2Connector pm2;
	
	@RequestMapping(path="/start", method=RequestMethod.POST)
	public void startPm2(){
		pm2.startPm2(config.getConfig());
	}
	
	@RequestMapping(path="/status", method=RequestMethod.GET)
	public List<Pm2ProcessInfo> getPm2Status(){
		return pm2.getPm2Processes();
	}
	
	@RequestMapping(path="/status/{id}", method=RequestMethod.GET)
	public Pm2ProcessInfo getPm2Status(int id){
		return pm2.getPm2Processes(id);
	}

}
