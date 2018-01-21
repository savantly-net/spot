package net.savantly.spot.web.modules;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import net.savantly.spot.web.SpotWebApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={SpotWebApplication.class})
public class SpotControllerTest {
	
	@Autowired
	SpotController controller;

	@Test
	public void testAddVertex() {
		Map<String, String> options = new HashMap<>();
		options.put("label", "example");
		controller.addVertex(options);
	}
}
