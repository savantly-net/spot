package net.savantly.spot.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import net.savantly.spot.web.SpotWebApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpotWebApplication.class})
@WebAppConfiguration
public class SpotWebApplicationTests {
	Logger log = LoggerFactory.getLogger(SpotWebApplicationTests.class);
	
	@Autowired
	WebApplicationContext ctx;
	
	private MockMvc mvc;

	@Before
	public void setup() {
		mvc = MockMvcBuilders
				.webAppContextSetup(ctx)
				.build();
	}

	@Test
	public void contextLoads() {
		SpotWebApplication bean = ctx.getBean(SpotWebApplication.class);
		log.debug("{}", bean);
	}
	
	@Test
	public void loadIndexPage() throws Exception {
		mvc.perform(get("/")).andExpect(status().isOk());
	}

}
