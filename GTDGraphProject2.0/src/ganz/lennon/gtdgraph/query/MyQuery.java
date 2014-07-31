package ganz.lennon.gtdgraph.query;

import java.io.IOException;
import java.util.*;

import ganz.lennon.gtdgraph.*;
import ganz.lennon.gtdgraph.search.SubgraphIsomorphism;

import javax.management.Query;

import org.jgrapht.DirectedGraph;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;

public class MyQuery extends Query {

	DirectedGraph<PropertyVertex, PropertyEdge> g;

	Map<String, Map<?, HashSet<PropertyVertex>>> indexes;
	Map<String, Map<String, HashMap<String, PropertyVertex>>> doubleIndexes;
	HashMap<Long, PropertyVertex> mainIndex;

	public MyQuery(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
		indexes = new HashMap<String, Map<?, HashSet<PropertyVertex>>>();
		doubleIndexes = new HashMap<String, Map<String, HashMap<String, PropertyVertex>>>();
	}

	public HashSet<PropertyVertex> aggregationQuery(String indexKey,
			Object value) {
		if (value != null) {
			if (indexes.containsKey(indexKey))
				return new HashSet<PropertyVertex>(indexes.get(indexKey).get(
						value));

			else if (doubleIndexes.containsKey(indexKey))
				return new HashSet<PropertyVertex>(doubleIndexes.get(indexKey)
						.get(value).values());

			else {
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (PropertyVertex v : g.vertexSet()) {
					if (v.hasProperty(indexKey)
							&& v.getValue(indexKey).equals(value))
						toReturn.add(v);
				}
				return toReturn;
			}
		} else {
			if (indexes.containsKey(indexKey)) {
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (HashSet<PropertyVertex> set : indexes.get(indexKey)
						.values()) {
					toReturn.addAll(set);
				}
				return toReturn;
			} else if (doubleIndexes.containsKey(indexKey)) {
				HashMap<String, PropertyVertex> tempMap;
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (String gname : doubleIndexes.get(indexKey).keySet()) {
					tempMap = doubleIndexes.get(indexKey).get(gname);
					toReturn.addAll(tempMap.values());
				}
				return toReturn;
			} else {
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (PropertyVertex v : g.vertexSet()) {
					if (v.hasProperty(indexKey)
							&& v.getValue(indexKey).equals(value))
						toReturn.add(v);
				}
				return toReturn;
			}
		}
	}

	public HashSet<PropertyVertex> findGreatest(String key) {

		long max = -99;
		long temp;
		HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
		for (PropertyVertex v : g.vertexSet()) {
			if (v.hasProperty(key)) {
				temp = Long.parseLong("" + v.getValue(key));
				if (temp == max){
					toReturn.add(v);
				}
				if (temp > max){
					max = temp;
					toReturn.clear();
					toReturn.add(v);
				}
			}
		}
		return toReturn;

	}

	/**
	 * 1 --> 2 <br>
	 * 2 --> 3
	 */
	public void lineQuery(Property p1, Property p2, Property p3,
			String edge12Label, String edge23Label) {

		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();

		PropertyVertex v1, v2, v3;
		PropertyEdge e12, e23;

		v1 = new PropertyVertex(1);
		v1.addProperty(p1.getKey(), p1.getValue());
		queryGraph.addVertex(v1);
		v2 = new PropertyVertex(2);
		v2.addProperty(p2.getKey(), p2.getValue());
		queryGraph.addVertex(v2);
		v3 = new PropertyVertex(3);
		v3.addProperty(p3.getKey(), p3.getValue());
		queryGraph.addVertex(v3);
		e12 = queryGraph.addEdge(v1, v2);
		e12.addLabel(edge12Label);
		e23 = queryGraph.addEdge(v2, v3);
		e23.addLabel(edge23Label);
		testIso(queryGraph, ansMap, mainIndex);

	}
	

	/**
	 * 1 --> 2 <br>
	 * 3 --> 2
	 */
	public void lineQuery2(Property p1, Property p2, Property p3,
			String edge12Label, String edge32Label) {

		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();

		PropertyVertex v1, v2, v3;
		PropertyEdge e12, e32;

		v1 = new PropertyVertex(1);
		v1.addProperty(p1.getKey(), p1.getValue());
		queryGraph.addVertex(v1);
		v2 = new PropertyVertex(2);
		v2.addProperty(p2.getKey(), p2.getValue());
		queryGraph.addVertex(v2);
		v3 = new PropertyVertex(3);
		v3.addProperty(p3.getKey(), p3.getValue());
		queryGraph.addVertex(v3);
		e12 = queryGraph.addEdge(v1, v2);
		e12.addLabel(edge12Label);
		e32 = queryGraph.addEdge(v3, v2);
		e32.addLabel(edge32Label);
		testIso(queryGraph, ansMap, mainIndex);
	}
	

	/**
	 * 1 --> 3 <br>
	 * 2 --> 3 <br>
	 * 1 --> 3
	 */
	public void triangleQuery(Property p1, Property p2, Property p3,
			String edge12Label, String edge23Label, String edge13Label) {

		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();

		PropertyVertex v1, v2, v3;
		PropertyEdge e12, e23, e13;

		v1 = new PropertyVertex(1);
		v1.addProperty(p1.getKey(), p1.getValue());
		queryGraph.addVertex(v1);
		v2 = new PropertyVertex(2);
		v2.addProperty(p2.getKey(), p2.getValue());
		queryGraph.addVertex(v2);
		v3 = new PropertyVertex(3);
		v3.addProperty(p3.getKey(), p3.getValue());
		queryGraph.addVertex(v3);
		e12 = queryGraph.addEdge(v1, v2);
		e12.addLabel(edge12Label);
		e23 = queryGraph.addEdge(v2, v3);
		e23.addLabel(edge23Label);
		e13 = queryGraph.addEdge(v1, v3);
		e13.addLabel(edge13Label);
		testIso(queryGraph, ansMap, mainIndex);
	}

	public void diamondQuery(Property p1, Property p2, Property p3,
			Property p4, String edge12Label, String edge24Label,
			String edge13Label, String edge34Label) {

		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();

		PropertyVertex v1, v2, v3, v4;
		PropertyEdge e12, e13, e24, e34;

		v1 = new PropertyVertex(1);
		v1.addProperty(p1.getKey(), p1.getValue());
		queryGraph.addVertex(v1);
		ansMap.put(v1, getVerticesByValue(p1.getKey(), p1.getValue()));
		v2 = new PropertyVertex(2);
		v2.addProperty(p2.getKey(), p2.getValue());
		queryGraph.addVertex(v2);
		ansMap.put(v2, getVerticesByValue(p2.getKey(), p2.getValue()));
		v3 = new PropertyVertex(3);
		v3.addProperty(p3.getKey(), p3.getValue());
		queryGraph.addVertex(v3);
		ansMap.put(v3, getVerticesByValue(p3.getKey(), p3.getValue()));
		v4 = new PropertyVertex(4);
		v4.addProperty(p4.getKey(), p4.getValue());
		queryGraph.addVertex(v4);
		ansMap.put(v4, getVerticesByValue(p4.getKey(), p4.getValue()));
		e12 = queryGraph.addEdge(v1, v2);
		e12.addLabel(edge12Label);
		e24 = queryGraph.addEdge(v2, v4);
		e24.addLabel(edge24Label);
		e13 = queryGraph.addEdge(v1, v3);
		e13.addLabel(edge13Label);
		e34 = queryGraph.addEdge(v3, v4);
		e34.addLabel(edge34Label);
		testIso(queryGraph, ansMap, mainIndex);
	}

	public void importIndex(String indexedOn,
			Map<Object, HashSet<PropertyVertex>> index) {
		indexes.put(indexedOn, index);
	}


	private void testIso(
			DirectedGraph<PropertyVertex, PropertyEdge> queryGraph,
			HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap,
			HashMap<Long, PropertyVertex> mainIndex) {
		try {
			SubgraphIsomorphism si = new SubgraphIsomorphism(queryGraph,
					this.g, mainIndex);
			si.setAnsMap(ansMap);
			si.computeMatch();
			si.displayIsos();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public HashSet<PropertyVertex> getVerticesByValue(String key, Object value) {
		if (value != null) {
			if (indexes.containsKey(key))
				return indexes.get(key).get(value);
			else if (doubleIndexes.containsKey(key)
					&& (doubleIndexes.get(key).get(value) != null))
				return new HashSet<PropertyVertex>(doubleIndexes.get(key)
						.get(value).values());
			else
				return new HashSet<PropertyVertex>();
		} else {
			if (indexes.containsKey(key)) {
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (HashSet<PropertyVertex> set : indexes.get(key).values()) {
					toReturn.addAll(set);
				}
				return toReturn;
			} else if (doubleIndexes.containsKey(key)) {
				HashMap<String, PropertyVertex> tempMap;
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (String gname : doubleIndexes.get(key).keySet()) {
					tempMap = doubleIndexes.get(key).get(gname);
					toReturn.addAll(tempMap.values());
				}
				return toReturn;
			}
			return new HashSet<PropertyVertex>();
		}
	}
	

	public void importIndex(Map<Object, HashSet<PropertyVertex>> index,
			String indexedOn) {
		indexes.put(indexedOn, index);
	}
	
	public void importMainIndex(HashMap<Long, PropertyVertex> mainIndex) {
		this.mainIndex = mainIndex;
	}


	public void importDoubleIndex(
			Map<String, HashMap<String, PropertyVertex>> doubleIndex,
			String indexedOn) {
		doubleIndexes.put(indexedOn, doubleIndex);
	}

}
