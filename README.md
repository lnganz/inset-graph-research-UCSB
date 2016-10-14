# inset-graph-research-UCSB
#### Lennon Ganz
#### Department/Program: Computer Science
#### Year: 2014

## Finding Patterns in Complex Social Networks

Networks are being used now more than ever in emerging social networks, cyber security, and web data analysis, among others.
To understand these complex networks, a common method is to ask questions (queries) of a graph.
When networks are large and complex it becomes increasingly difficult to write these queries and the algorithms which work to find the answers become slow.
In this project we developed a graph analysis system as an initial step towards a full-fledged network analysis tool.
We have developed a graph extraction engine to transform Global Terrorism Dataset to a social network.
We then considered two classes of “cornerstone” queries: reachability and pattern matching. We implemented efficient algorithms, e.g., breadth first search (BFS), to answer these queries, and we have designed optimization techniques such as bidirectional searching (BIBFS) and inverted indexing to improve the efficiency.
We have experimentally shown that these algorithms can efficiently find answers, and our optimizations techniques significantly improve the baseline algorithms.
For example, when the size of a graph increases BIBFS quickly outpaces BFS and can perform 30x faster or more.
However, when the density increases the performance improvement is less significant.
Additionally, we performed a case study to demonstrate our new pattern matching algorithm. 
We envision that such a system can be used to analyze a wide variety of networks in the “big data era”.
