package net.savantly.spot.web.modules.graphexp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.savantly.spot.web.configuration.JanusGraphConfiguration;

@Service
public class GraphExpService {
	
	@Autowired
	private JanusGraphConfiguration config;

	public Map<String, Object> getGraphInfo() {
		final GraphTraversalSource g = config.getGraph().traversal();
		
		Map<String, Object> data = new HashMap<>();

		List<Map<Object, Long>> nodes = g.V().groupCount().by(T.label).toList();
		List<Map<Object, Long>> edges = g.E().groupCount().by(T.label).toList();
		List<Map<Object, Long>> nodesprop = g.V().valueMap().select(Column.keys).groupCount().toList();
		List<Map<Object, Long>> edgesprop = g.E().valueMap().select(Column.keys).groupCount().toList();

		data.put("nodes", nodes);
		data.put("edges", edges);
		data.put("nodesprop", nodesprop);
		data.put("edgesprop", edgesprop);
		return data;
	}
}
