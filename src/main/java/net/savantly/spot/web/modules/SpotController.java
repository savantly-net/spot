package net.savantly.spot.web.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.savantly.spot.web.configuration.SpotAutoConfiguration;
import net.savantly.sprout.core.content.contentField.ContentField;
import net.savantly.sprout.core.content.contentField.ContentFieldRepository;
import net.savantly.sprout.core.content.contentItem.ContentItem;
import net.savantly.sprout.core.content.contentItem.ContentItemRepository;

@RequestMapping("/rest/modules/spot")
@RestController
public class SpotController {
	
	@Autowired
	private ContentItemRepository contentItems;
	@Autowired
	private ContentFieldRepository contentFields;
	ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value="/labels")
	public JsonNode getLabels() throws JsonProcessingException, IOException {

		ContentField field = contentFields.findOne(SpotAutoConfiguration.GRAPH_LABELS_JSON);
		
		Comparator<ContentItem> cmp = Comparator.comparing(ContentItem::getLastModifiedDate);
		Optional<ContentItem> latestContent = this.contentItems.findAllByContentType_id(SpotAutoConfiguration.GRAPH_LABELS).stream().max(cmp);
		if (latestContent.isPresent()) {
			Map<ContentField, String> fieldValues = latestContent.get().getFieldValues();
			String json = fieldValues.get(field);
			return mapper.readTree(json);
		} else {
			List<String> linkLabels = new ArrayList<String>();
			List<String> nodeLabels = new ArrayList<String>();
			Map<String, List<String>> payload = new HashMap<>();
			payload.put("node", nodeLabels);
			payload.put("link", linkLabels);
			return mapper.convertValue(payload, JsonNode.class);
		}
	}
	
}
