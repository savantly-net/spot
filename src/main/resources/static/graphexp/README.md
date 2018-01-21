# Graphexp: graph explorer with D3.js

Graphexp is a lightweight web interface to explore and display a graph stored in a Gremlin graph database, via the Gremlin server (version 3.2.x or 3.3.x) .

Graphexp is under the Apache 2.0 license.

![graphexp](https://github.com/bricaud/graphexp/blob/master/images/graphexp2.png "Graph exploration")

A live demo of Graphexp is available on the [project Github page](https://bricaud.github.io/graphexp/). See the [Tutorial](#tutorial-with-the-tree-of-life) section below.

## Configuration

To use Graph Explorer, you need a [Gremlin server](http://tinkerpop.apache.org/) running with REST or websocket protocol and a *recent* web browser to display the visualization.
On your web browser, just access the file `graphexp.html`.

If the access to the Gremlin server is not `localhost:8182`, the address can be configured in `graphConf.js`. Use this latter file to configure the communication protocol `REST` (default) or `websocket`. Depending on the version of your Gremlin server you also need to configure the `COMMUNICATION_METHOD`, `GraphSON1` for Gremlin server version 3.2.x or `GraphSON3` for versions 3.3.x.

Graphexp now works with Amazon Neptune thanks to a pull request of [jwalton922](https://github.com/jwalton922). With this database, set `SINGLE_COMMANDS_AND_NO_VARS = true` in `graphConf.js`.

![graphexpzoom](https://github.com/bricaud/graphexp/blob/master/images/graphexpzoom.png "Exploration of the Tinkerpop modern graph")


## Getting started

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

## Tutorial with the tree of life
Once your gremlin server is up and running (from the [Docker repository](https://hub.docker.com/r/bricaud/gremlin-server-with-demo-graph/)), click on the `get graph info` button. Information should appear on the left side of the page, like on the following image.
![graphexptol1](https://github.com/bricaud/graphexp/blob/master/images/graphexptol1.png "Graph exploration Tree of life")

This graph has a single type of nodes (label 'vertex') and a single type of edges (label 'edge'). Each node is a species (taxon) living on earth or extinct, and directed edges represent the link ancestor-descendant.
The different node properties are displayed on the left. 
* `CHILDCOUNT` the number of descendent nodes
* `name` the name of the species
* `HASPAGE` whether there is a page of information on the [Tree Of Life Project website](http://tolweb.org/tree/home.pages/downloadtree.html)
* `ID` Tree of Life Project unique id
* `CONFIDENCE` confidence in classification, from confident (0) to less confident (1) and (2)
* `EXTINCT` whether the node is extinct (2) or not (0)
* `LEAF` the node is a leaf of the tree (1) or the node does not represent a leaf (it has or will have descendent nodes on the Tree of Life) (0)
* `PHYLESIS` (0) monophyletic, (1) uncertain, (2) not monophyletic

On the top navigation bar, choose the field `name`, enter 'Dinosauria' as value in the input and click on the `Search` button. Do not forget the capital letter, as the search is case-sensitive. A single node, corresponding to the Dinosaurs clade, should appear in the middle of the page. Click on the node to display node details on the right as well as its ancestors and descendants on the graph. 
Check the box `name` on the left bar to display the node names.
You should see appearing the two subgroups of dinosaurs `Saurischia` and `Ornithischia`, as in the [Wikipedia dinosaur page](https://en.wikipedia.org/wiki/Dinosaur_classification) and an additional `none` node which is the ancestor. This latter node is a taxon that has ancestors and descendants but does not have a name. Note that there are different versions of the tree of life and it is always evolving as researchers find new species.
![graphexptol2](https://github.com/bricaud/graphexp/blob/master/images/graphexptol2.png "Graph exploration Tree of life")
You may now enjoy the exploration of the dinosaur order by clicking on nodes and following ascendant and descendant lines. The oldest nodes will vanish as you explore the data and if you want more nodes to be displayed, just increase the number of layers on the top navigation bar.

You may also color the nodes according to the values of some of their properties by clicking on the color tab on the left side. The color scale is computed using the range of values of the nodes already displayed and a palette of 20 colors. You should refresh the color after a few steps of exploration.

![graphexptol3](https://github.com/bricaud/graphexp/blob/master/images/graphexptol3.png "Graph exploration Tree of life")

During the exploration of the `Dinosauria` clade you may find the [bird](https://en.wikipedia.org/wiki/Bird) class `Aves`. They are the only survivors of the Dinosaur group and descendant of dinosaurs with feathers. To see it, enter `Aves` in the value field, press search and climb up the tree.

If you want to explore the world of insects, you may start with the taxon `Insecta` and follow the links. Did you know that spiders are not insects but have they own group `Arachnida`? Can you tell what is the common ancestor between spiders and insects? 

You may also be interested in the `Homo` group. 

Have a try on the live demo of Graphexp on the [project Github page](https://bricaud.github.io/graphexp/).



