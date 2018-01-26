# Spot: Janusgraph explorer based on the Sprout Platform  

[Sprout Platform](https://github.com/savantly-net/sprout-platform) 

Visualization was politely stolen from GraphExp  
Graphexp is a lightweight web interface to explore and display a graph stored in a Gremlin graph database, via the Gremlin server (version 3.2.x or 3.3.x) .

Graphexp is under the Apache 2.0 license.  
A live demo of Graphexp is available on the [project Github page](https://bricaud.github.io/graphexp/).

![spot](https://github.com/savantly-net/spot/blob/master/examples/example.PNG) 

A live demo of Graphexp is available on the [project Github page](https://bricaud.github.io/graphexp/).  

## Getting started  

To use Spot you can check out this project and run it -  
`mvn spring-boot:run`  
  
## Configuration

Create a custom 'application.properties' file to configure your own JanusServer, or just run the project to start the embedded version.  
See more here -> [application.properties](./src/main/resources/application.properties)  
And here -> [Spring Boot Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html#boot-features-external-config-application-property-files) 

  
## Additional Information  

### Installing a Gremlin server

If you have not yet installed a gremlin server, download the last release of the [Gremlin server](http://tinkerpop.apache.org/) and follow the [documentation](http://tinkerpop.apache.org/docs/current/reference/#gremlin-server). In the server folder just run
```
bin/gremlin-server.sh conf/gremlin-server-rest-modern.yaml
```
or on windows
```
bin/gremlin-server.bat conf/gremlin-server-rest-modern.yaml
```
This default server comes with a small graph database of 6 nodes.
The server should start on port `8182`. Replace `gremlin-server-rest-modern.yaml` by `gremlin-server-modern.yaml` if you want to use websocket.


Alternatively, if you have Docker installed on your machine, you may run a Docker container with an already configured Gremlin server. You can find one on [this page](https://hub.docker.com/r/bricaud/gremlin-server-with-demo-graph/). This server has a graph database containing a demo graph: the tree of life, with 35960 nodes and 35959 edges. You can download it and run it using
```
docker pull bricaud/gremlin-server-with-demo-graph
docker run -p 8182:8182 -it --name gremlin-server-rest bricaud/gremlin-server-with-demo-graph
```
or for the websocket version:
```
docker pull bricaud/gremlin-with-demo-graph:websocket
docker run -p 8182:8182 -it --name gremlin-server-websocket bricaud/gremlin-server-with-demo-graph:websocket
```


### Graphexp guidelines
To display a node, type in a property name and value, then click on the search button. The input is case-sensitive.
Leaving a blank value will display a part of the graph limited to the first 50 nodes found (with their connections).
The node and edge properties can be automatically retrieved using the `get graph info` button. Pushing this button will also display some graph properties on the left side of the page.

When a node of the visualization is clicked, it will become 'active' with a circle surrounding it and its information will be display on the right side of the page. Moreover, this action will trigger the display of its neighbors.
Clicking on an edge will show its properties (without highlighting the edge). 

When appearing for the first time the nodes will be positioned following a force layout. Drag and drop can be used to pin them in a particular position. Once dragged the nodes will stay at their position. Drag and drop is allowed only for the nodes on the active layer (most recent layer) with no connection with nodes in other layers. See "Visualization concepts" section for more information on the layers.

## Visualization concept

The visualization is based on a concept of layers of visualization. The idea is to progress in the graph as in a jungle. The clicked node immediately shows its neighbors, opening new paths for the exploration. If not clicked, a node vanishes little by little as we progress in the exploration. Coming back during the exploration is allowed. Before it completely disappears, a node can be clicked and will become active again.
This visualization concept is aimed at providing a precise, local view rather than a global one.

During your exploration, you can set up milestones by clicking on the small circle on the upper right side of a node. This will pin the node in time, preventing it from disappearing.

You may also freeze the exploration, by ticking the appropriate checkbox. The evolution of the exploration will stop, allowing to gather information on the nodes displayed, without displaying their neighbors.

## Node and edge information

The Id and label of each node can be displayed by hovering the cursor over the node. The full information on the properties is displayed on the right of the page when clicking on the node or edges. Once the `get graph info` button has been clicked, a choice of properties to display appears on the left side.

## Node color

If a node property called 'color' exists in the node properties with a hexadecimal color code (string), it will be displayed automatically on the graph. Otherwise, the default node color can be set in the `graphConf.js` file.  The node color can be set interactively after the `get graph info` button has been pressed. A select tab appears on the left sidebar allowing to set the color according to one of the property values present in the graph.

## Program description

The program uses:
* the D3.js library to visualize a graph in an interactive manner, [API Reference](https://github.com/d3/d3/blob/master/API.md),
* an ajax request (with Jquery) that query the graph database (Gremlin Tinkerpop via REST).