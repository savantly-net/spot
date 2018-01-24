package net.savantly.spot.web.modules;

import org.apache.tinkerpop.gremlin.driver.message.ResponseStatus;

public class GremlinResponse {
	String requestId;
	Object result;
	ResponseStatus status;
}
