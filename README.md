# HCODA
## Implementation of "Community-based Outlier Detection in Edge-attributed Graphs"

This is a Java implementation of Holistic Community Outlier Detection Algorithm(HCODA) from Community-based Outlier Detection for Edge-attributed Graphs. The algorithm identifies communities and outliers in static graph using graph structure, node attributes as well as edge attributes.

The repository contains code and DBLP data used in the experiment.

Supriya Pandhre, Manish Gupta, Vineeth N Balasubramanian, [Community-based Outlier Detection for Edge-attributed Graphs](https://arxiv.org/abs/1612.09435)


## To run the code, use following command:
```
cd code
javac FileOperate.java
javac IndexArray.java
javac HCODA.java
java HCODA 42844 118618 4 20 4 2.333 0.02 1.0 0.01 ../data/ nodeLabel.txt edgeNodeLabel.txt node.txt edgeNode.txt node_node.txt node_edge.txt output_results.txt
```

The command structure is:

```
java HCODA numNode numEdgeNode numCluster numNodeAttr numEdgeNodeAttr lambda1 lambda2 lambda3 ratio directory initNodeLabelFile initEdgeNodeLabelFile nodeDataFile edgeNodeDataFile node_nodeLinkFile node_edgeNodeLinkFile resultFile
```

where input paramters are:

param-0:numNode = number of nodes in graph

param-1:numEdgeNode = number of edges in graph(number of edgeNodes in graph)

param-2:numCluster = number of clusters

param-3:numNodeAttr = number of node attributes

param-4:numEdgeNodeAttr = number of edgeNode attributes

param-5:lambda1 = algorithm parameterlambda-1 value mentioned in paper

param-6:lambda2 = algorithm parameter lambda-2 value mentioned in paper

param-7:lambda3 = algorithm parameter lambda-3 value mentioned in paper

param-8:ratio = the parameter r mentioned in paper

param-9:directory = location of folder where all input files are stored

param-10:initNodeLabelFile = name of file containing initial node labels

param-11:initEdgeNodeLabelFile = name of file containing initial edgeNode labels

param-12:nodeDataFile = name of file containing node attribute

param-13:edgeNodeDataFile = name of file containing edgeNode attributes

param-14:node_nodeLinkFile = name of file containing edge attributes between two nodes

param-15:node_edgeNodeLinkFile = name of file containing edge attributes between node and edgeNode

param-16:resultFile = name of file in which output result will be stored.


## Data
We have experimented on Four-area subset of DBLP dataset: data mining (DM), databases (DB), information retrieval (IR) and machine learning (ML). 

The "data" folder contains following files:

 1) node.txt: contains node attributes.
 
 2) edgeNode.txt: contains edgeNode attributes.
 
 3) nodeLabel.txt: contains initial labels for nodes.
 
 4) edgeNodeLabel.txt: contains initial label for edgeNodes.
 
 5) node_node.txt: contains the edge attributes between two nodes.
 
 6) node_edge.txt: contains the edge attributes between node and edgeNode.
 

## Cite

Please cite our paper if you use this code in your work:
Paper title: [Community-based Outlier Detection for Edge-attributed Graphs](https://arxiv.org/abs/1612.09435)

Authors: Supriya Pandhre, Manish Gupta, Vineeth N Balasubramanian

```
@article{pandhre2016community,
  title={Community-based Outlier Detection for Edge-attributed Graphs},
  author={Pandhre, Supriya and Gupta, Manish and Balasubramanian, Vineeth N},
  journal={arXiv preprint arXiv:1612.09435},
  year={2016}
}
```

