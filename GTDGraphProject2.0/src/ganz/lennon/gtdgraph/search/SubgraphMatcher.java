package ganz.lennon.gtdgraph.search;

import ganz.lennon.gtdgraph.*;

import java.util.*;

import org.jgrapht.*;

public class SubgraphMatcher {

	private static final boolean OUTGOING = true;
	private static final boolean INCOMING = false;
	
	Map<String, Map<?, HashSet<PropertyVertex>>> indexes = 
			new HashMap<String, Map<?, HashSet<PropertyVertex>>>();
	
	DirectedGraph<PropertyVertex, PropertyEdge> g;
	
	public SubgraphMatcher(DirectedGraph<PropertyVertex, PropertyEdge> g){
		this.g = g;
	}
	
	public void importIndex(Map<Object, HashSet<PropertyVertex>> index, String indexedOn){
		indexes.put(indexedOn, index);
	}
	
	public HashSet<PropertyVertex> getVerticesByValue(String key, Object value){
		if (indexes.containsKey(key))
			return indexes.get(key).get(value);
		else
			return new HashSet<PropertyVertex>();
	}
	
	public HashSet<PropertyEdge> getSuitableEdges(Set<PropertyVertex> set1, 
			Set<PropertyVertex> set2, String edgeLabel){
		HashSet<PropertyEdge> edgesToReturn = new HashSet<PropertyEdge>();
		Set<PropertyEdge> edgesToExplore;
		Set<PropertyVertex> adjacentVertices;
		PropertyEdge edge;
		if (set1.size() <= set2.size()){
			for (PropertyVertex v1 : set1){
				adjacentVertices = getAdjacentVertices(v1, OUTGOING);
				for (PropertyVertex v2 : adjacentVertices){
					edge = g.getEdge(v1,  v2);
					if ((edge != null) && (edge.getLabels().contains(edgeLabel))){
						edgesToReturn.add(edge);
					}
				}
			}
		}else{
			for (PropertyVertex v1 : set2){
				adjacentVertices = getAdjacentVertices(v1, INCOMING);
				for (PropertyVertex v2 : adjacentVertices){
					edge = g.getEdge(v1,  v2);
					if ((edge != null) && (edge.getLabels().contains(edgeLabel))){
						edgesToReturn.add(edge);
					}
				}
			}
		}
			
		return edgesToReturn;
	}
	
		Set<PropertyVertex> getAdjacentVertices(PropertyVertex rootV,
				boolean inOut) {
			Set<PropertyVertex> adjSet = new HashSet<PropertyVertex>();
			Set<PropertyEdge> edgeSet;
			if (inOut == OUTGOING) {
				edgeSet = g.outgoingEdgesOf(rootV);
				for (PropertyEdge e : edgeSet) {
					adjSet.add(g.getEdgeTarget(e));
				}
			} else {
				edgeSet = g.incomingEdgesOf(rootV);
				for (PropertyEdge e : edgeSet) {
					adjSet.add(g.getEdgeSource(e));
				}
			}
			return adjSet;
		}
	
}
