package net.savantly.spot.web.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import net.savantly.sprout.core.content.contentType.ContentType;
import net.savantly.sprout.core.content.contentType.ContentTypeRepository;

@Configuration
public class SpotAutoConfiguration {

	public static final String LINK_LABEL_CONTENT_TYPE_KEY = "Link Label";
	public static final String NODE_LABEL_CONTENT_TYPE_KEY = "Node Label";
	
	@Autowired
	ContentTypeRepository contentTypes;
	
	@PostConstruct
	public void postConstruct() {
		ensureContentTypeExist(NODE_LABEL_CONTENT_TYPE_KEY);
		ensureContentTypeExist(LINK_LABEL_CONTENT_TYPE_KEY);
	}

	private void ensureContentTypeExist(String string) {
		ContentType contentType = contentTypes.findByName(string);
		if (contentType == null) {
			contentType = new ContentType();
			contentType.setId(string);
			contentType.setName(string);
			contentType.setUpdateable(false);
			contentType.setRequiresTemplate(false);
			contentTypes.save(contentType);
		}
	}

}
