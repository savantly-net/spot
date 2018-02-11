package net.savantly.spot.web.modules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.savantly.spot.web.configuration.SpotAutoConfiguration;
import net.savantly.sprout.core.content.contentItem.ContentItemRepository;

@RequestMapping("/rest/modules/spot")
@RestController
public class SpotController {
	
	@Autowired
	private ContentItemRepository contentItems;

	@RequestMapping(value="/labels")
	public Map<String, List<String>> getLabels() {
		List<String> linkLabels = new ArrayList<String>();
		this.contentItems.findAllByContentType_id(SpotAutoConfiguration.LINK_LABEL_CONTENT_TYPE_KEY).forEach((o) -> {
			linkLabels.add(o.getName());
		});
		List<String> nodeLabels = new ArrayList<String>();
		this.contentItems.findAllByContentType_id(SpotAutoConfiguration.NODE_LABEL_CONTENT_TYPE_KEY).forEach((o) -> {
			nodeLabels.add(o.getName());
		});

		Map<String, List<String>> payload = new HashMap<>();
		payload.put("node", nodeLabels);
		payload.put("link", linkLabels);
		return payload;
	}
	
}
