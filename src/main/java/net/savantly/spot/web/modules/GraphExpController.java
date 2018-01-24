package net.savantly.spot.web.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.tinkerpop.gremlin.driver.message.ResponseMessage;
import org.apache.tinkerpop.gremlin.driver.message.ResponseMessage.Builder;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.base.Strings;

import groovy.lang.Script;

@RequestMapping("/rest/modules/graphexp")
@RestController
public class GraphExpController {
	
	@Autowired
	private GraphTraversalSource g;

	@RequestMapping("/")
	public ResponseMessage graphInfo() {
		// gremlin_query_nodes = "nodes = g.V().groupCount().by(label);"
		// gremlin_query_edges = "edges = g.E().groupCount().by(label);"
		// gremlin_query_nodes_prop = "nodesprop = g.V().valueMap().select(keys).groupCount();"
		// gremlin_query_edges_prop = "edgesprop = g.E().valueMap().select(keys).groupCount();"

		UUID requestId = UUID.randomUUID();
		Builder responseBuilder = ResponseMessage.build(requestId);
		List<Object> data = new ArrayList<>();
		responseBuilder.result(data);
		
		List<Map<Object, Long>> nodes = g.V().groupCount().by(T.label).toList();
		List<Map<Object, Long>> edges = g.E().groupCount().by(T.label).toList();
		List<Map<Object, Long>> nodesprop = g.V().valueMap().select("keys").groupCount().toList();
		List<Map<Object, Long>> edgesprop = g.E().valueMap().select("keys").groupCount().toList();
		
		data.add(nodes);
		data.add(edges);
		data.add(nodesprop);
		data.add(edgesprop);
		
		return responseBuilder.create();
	}
	
	@RequestMapping("/search")
	public ResponseMessage search(SearchOptions options) {
		List<Object> data = new ArrayList<>();
		Builder responseBuilder = getResponseBuilder(data);
		
		List<Vertex> nodes;
		List<Object> edges;
		
		if (Strings.isNullOrEmpty(options.getText()) || Strings.isNullOrEmpty(options.getField())) {
			nodes = g.V().limit(options.getLimit()).toList();
			edges = g.V().limit(options.getLimit()).aggregate("node").outE().as("edge").inV().where(P.within("node")).select("edge").toList();
		} else {
			nodes = g.V().has(options.getField(), options.getText()).toList();
			edges = g.V().has(options.getField(), options.getText()).aggregate("node").outE().as("edge").inV().where(P.within("node")).select("edge").toList();
		}
		
		data.add(nodes);
		data.add(edges);
		
		return responseBuilder.create();
	}
	
	
	private Builder getResponseBuilder(List<Object> data) {
		UUID requestId = UUID.randomUUID();
		Builder responseBuilder = ResponseMessage.build(requestId);
		responseBuilder.result(data);
		return responseBuilder;
	}

}
