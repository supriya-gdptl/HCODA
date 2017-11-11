## Data

This folder contains data used in the experiment. It is a subset of DBLP dataset from four areas of research: data mining (DM), databases (DB), information retrieval (IR) and machine learning (ML). We have considered 20 conferences, 5 from each research area.

Each author is represented as node and there exist an edge between two nodes(i.e. authors) if they have co-authored a paper.

*  **node.txt** : contains node attributes. Each author is represented by a vector of size 20 which holds a count of papers the author has published in each of 20 conferences. This captures the summary of all papers published so far by the author, showing his/her research interest.

*  **edgeNode.txt** : contains edgeNode attributes. It is summary vector (of size 4) of count of papers published in the four research areas we have considered. The conference where maximum co-authored paper gets published captures the behavior of interaction between two author. This might be different than authors' own research area. **For example**, author A and B usually publish work in conferences of 'databases' and 'machine learning', respectively. However, when they collaborate, they publish work in 'information retrieval'. Hence, we are able to capture such novel interactions because we are using edge data, along with node data and graph structure.
 
*  **nodeLabel.txt** : contains initial labels for nodes.
 
*  **edgeNodeLabel.txt**: contains initial label for edgeNodes.

*  **node_node.txt**: contains the edge attributes between two nodes. It is count of papers published together.

*  **node_edge.txt**: contains the edge attributes between node and edgeNode. 
