package net.savantly.spot.web.configuration;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Strings;

import net.savantly.sprout.core.content.contentField.ContentField;
import net.savantly.sprout.core.content.contentField.ContentFieldRepository;
import net.savantly.sprout.core.content.contentItem.ContentItem;
import net.savantly.sprout.core.content.contentItem.ContentItemRepository;
import net.savantly.sprout.core.content.contentType.ContentType;
import net.savantly.sprout.core.content.contentType.ContentTypeRepository;
import net.savantly.sprout.core.content.fieldType.FieldType;
import net.savantly.sprout.settings.AppSetting;
import net.savantly.sprout.settings.SettingName;
import net.savantly.sprout.settings.UISettings;

@Configuration
public class SpotAutoConfiguration {

	public static final String GRAPH_LABELS = "Graph Labels";
	public static final String GRAPH_LABELS_JSON = "GRAPH_LABELS_JSON";
	public static final String GRAPH_LABELS_CONTENT = "GRAPH_LABELS_CONTENT";
	
	@Autowired
	ContentTypeRepository contentTypes;
	@Autowired
	ContentItemRepository contentItems;
	@Autowired
	ContentFieldRepository contentFields;
	@Autowired
	UISettings settings;
	
	@PostConstruct
	public void postConstruct() {
		ensureContentTypeExist(GRAPH_LABELS);
		ensureDefaultSettings();
		ensureDefaultContentItemExists();
	}

	private void ensureDefaultContentItemExists() {
		if(contentItems.findAllByContentType_id(GRAPH_LABELS).size() == 0) {
			String defaultValue = "{\r\n" + 
					"    \"node\": [\"SERVER\", \"APPLID\", \"VIRTUAL_MACHINE\"],\r\n" + 
					"    \"link\": [\"CALLS\", \"MOUNTED_ON\", \"DEPENDS_ON\", \"ASSOCIATED_WITH\"]\r\n" + 
					"}";
			Map<ContentField, String> fieldValues = new HashMap<>();
			fieldValues.put(contentFields.findOne(GRAPH_LABELS_JSON), defaultValue);
			
			ContentItem content = new ContentItem();
			content.setName("Default graph labels");
			content.setContentType(contentTypes.findOne(GRAPH_LABELS));
			content.setId(GRAPH_LABELS_CONTENT);
			content.setFieldValues(fieldValues);
			contentItems.save(content);
		}
	}

	private void ensureDefaultSettings() {
		AppSetting siteName = settings.getSiteName();
		if (siteName == null || Strings.isNullOrEmpty(siteName.getValue())) {
			siteName = new AppSetting(SettingName.SITE_NAME, "EAI admin");
			settings.save(siteName);
		}
	}

	private void ensureContentTypeExist(String string) {
		ContentType contentType = contentTypes.findByName(string);
		if (contentType == null) {
			contentType = new ContentType();
			contentType.setId(string);
			contentType.setName(string);
			contentType.setUpdateable(false);
			contentType.setRequiresTemplate(false);
			contentType.setFields(getFields(contentType));
			contentTypes.save(contentType);
		}
	}

	private Set<ContentField> getFields(ContentType contentType) {
		ContentField field = new ContentField();
		field.setFieldType(FieldType.json);
		field.setContentType(contentType);
		field.setId(GRAPH_LABELS_JSON);
		field.setDisplayName(GRAPH_LABELS);
		field.setRequired(true);
		field.setName(GRAPH_LABELS);
		field.setSortOrder(0);
		HashSet<ContentField> set = new HashSet<ContentField>();
		set.add(field);
		return set;
	}

}
