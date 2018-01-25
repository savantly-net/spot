package net.savantly.spot.web.modules;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tinkerpop.gremlin.driver.ser.SerializationException;
import org.apache.tinkerpop.gremlin.process.traversal.P;
import org.apache.tinkerpop.gremlin.process.traversal.Pop;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Column;
import org.apache.tinkerpop.gremlin.structure.Edge;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONMapper;
import org.apache.tinkerpop.gremlin.structure.io.graphson.GraphSONVersion;
import org.apache.tinkerpop.shaded.jackson.databind.ObjectMapper;
import org.janusgraph.diskstorage.BackendException;
import org.janusgraph.example.GraphOfTheGodsFactory;
import org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Strings;

import net.savantly.spot.web.configuration.JanusGraphConfiguration;

@RequestMapping("/rest/modules/graphexp")
@RestController
public class GraphExpController {

	@Autowired
	private JanusGraphConfiguration config;
	private ObjectMapper mapper = GraphSONMapper.build()
			.addRegistry(JanusGraphIoRegistry.getInstance())
			.version(GraphSONVersion.V1_0).create().createMapper();
	private com.fasterxml.jackson.databind.ObjectMapper om = new com.fasterxml.jackson.databind.ObjectMapper();
	
	@RequestMapping(value="/", method=RequestMethod.DELETE)
	public void deleteGraphData() throws BackendException {
		config.clearGraph();
	}
	
	@RequestMapping(value="/demo", method=RequestMethod.POST)
	public void addDemoData() {
		GraphOfTheGodsFactory.load(config.getGraph());
	}

	@RequestMapping(value="/", method= {RequestMethod.GET, RequestMethod.POST})
	public JsonNode graphInfo() throws IOException {
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

		JsonNode json = serialize(data);
		return json;
	}

	@RequestMapping("/search")
	public JsonNode search(@RequestBody SearchOptions options) throws SerializationException, IOException, JSONException {
		Map<String, Object> data = new HashMap<>();

		final List<Vertex> nodes = new ArrayList<>();
		final List<Object> edges = new ArrayList<>();
		final GraphTraversalSource g = config.getGraph().traversal();
		
		if (Strings.isNullOrEmpty(options.getText()) || Strings.isNullOrEmpty(options.getField())) {
			GraphTraversal<Vertex, Vertex> nTrav = g.V().limit(options.getLimit());
			nTrav.forEachRemaining(n -> {
				nodes.add(n);
			});
			GraphTraversal<Vertex, Object> eTrav = g.V().limit(options.getLimit()).aggregate("node")
					.outE().as("edge").inV().where(P.within("node"))
					.select("edge");
			eTrav.forEachRemaining(e -> {
					edges.add(e);
				});
		} else {
			GraphTraversal<Vertex, Vertex> nTrav = g.V().has(options.getField(), options.getText());
			nTrav.forEachRemaining(n -> {
				nodes.add(n);
			});
			
			GraphTraversal<Vertex, Object> eTrav = 
					g.V().has(options.getField(), options.getText()).aggregate("node").outE().as("edge").inV()
					.where(P.within("node")).select("edge");
			eTrav.forEachRemaining(e -> {
				edges.add(e);
			});
		}
		data.put("nodes", nodes);
		data.put("links", edges);

		JsonNode json = serialize(data);
		return json;
	}
	
	@RequestMapping("/node/{id}")
	public JsonNode nodeDetails(@PathVariable("id") int id) throws SerializationException, IOException, JSONException {
		Map<String, Object> data = new HashMap<>();

		final List<Object> nodes = new ArrayList<>();
		final List<Object> edges = new ArrayList<>();
		final GraphTraversalSource g = config.getGraph().traversal();
		
		GraphTraversal<Vertex, Object> nTrav = g.V(id).as("node").both().as("node").select(Pop.all, "node").inject(g.V(id)).unfold();
		nTrav.forEachRemaining(n -> {
			nodes.add(n);
		});
		
		GraphTraversal<Vertex, Edge> eTrav = g.V(id).bothE();
		eTrav.forEachRemaining(e -> {
			edges.add(e);
		});
		
		data.put("nodes", nodes);
		data.put("links", edges);

		JsonNode json = serialize(data);
		return json;
	}	

	private JsonNode serialize(Object object) throws IOException {
		String string = mapper.writeValueAsString(object);
		return om.readTree(string);
	}

}
