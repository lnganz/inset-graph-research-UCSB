package ganz.lennon.gtdgraph.search;

import ganz.lennon.gtdgraph.*;
import ganz.lennon.gtdgraph.query.MyQuery;

import java.io.IOException;
import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

public class SubgraphMatcher {

	private static final boolean OUTGOING = true;
	private static final boolean INCOMING = false;

	Map<String, Map<?, HashSet<PropertyVertex>>> indexes = new HashMap<String, Map<?, HashSet<PropertyVertex>>>();
	Map<String, Map<String, HashMap<String, PropertyVertex>>> doubleIndexes = new HashMap<String, Map<String, HashMap<String, PropertyVertex>>>();

	HashMap<Long, PropertyVertex> mainIndex;

	DirectedGraph<PropertyVertex, PropertyEdge> g;
	MyQuery query;

	public SubgraphMatcher(DirectedGraph<PropertyVertex, PropertyEdge> g,
			HashMap<Long, PropertyVertex> mainIndex) {
		this.g = g;
		this.mainIndex = mainIndex;
	}

	public SubgraphMatcher(MyQuery query) {
		this.query = query;
	}

	public void test() {
		testDiamond();
	}

	public void testLine1(){
		String key1, key2, key3, value1, value2, value3, edge12, edge13, edge23;
		PropertyVertex v1 = new PropertyVertex(1), v2 = new PropertyVertex(2), v3 = new PropertyVertex(
				3);
		PropertyEdge e12, e13, e23;
		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		int intVal2;
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();
		key1 = "GROUP_NAME";
		value1 = "Taliban";
		queryGraph.addVertex(v1);
		ansMap.put(v1, getVerticesByValue(key1, value1));
		key2 = "COUNTRY_CODE";
		intVal2 = 4;
		v2.addProperty(key1, intVal2);
		queryGraph.addVertex(v2);
		ansMap.put(v2, getVerticesByValue(key2, intVal2));
		edge12 = "PERPETRATED";
		queryGraph.addEdge(v1, v2).addLabel(edge12);
		key3 = "CORPORATION_NAME";
		value3 = "School";
		v3.addProperty(key3, value3);
		queryGraph.addVertex(v3);
		ansMap.put(v3, getVerticesByValue(key3, value3));
		edge23 = "TARGET_CORPORATION";
		queryGraph.addEdge(v2, v3).addLabel(edge23);

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
	
	public void testDiamond() {
		String key1, key2, key3, key4, value1, value2, value3, value4, edge12, edge13, edge24, edge34;
		PropertyVertex v1 = new PropertyVertex(1), v2 = new PropertyVertex(2), 
				v3 = new PropertyVertex(3), v4 = new PropertyVertex(4);
		PropertyEdge e12, e13, e24, e34;
		DirectedGraph<PropertyVertex, PropertyEdge> queryGraph = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		int intVal2, intVal3;
		HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();
		key1 = "GROUP_NAME";
		value1 = "Taliban";
		queryGraph.addVertex(v1);
		ansMap.put(v1, getVerticesByValue(key1, value1));
		key2 = "COUNTRY_CODE";
		intVal2 = 4;
		v2.addProperty(key1, intVal2);
		queryGraph.addVertex(v2);
		ansMap.put(v2, getVerticesByValue(key2, intVal2));
		edge12 = "PERPETRATED";
		queryGraph.addEdge(v1, v2).addLabel(edge12);
		key3 = "COUNTRY_CODE";
		intVal3 = 4;
		v3.addProperty(key3, intVal3);
		queryGraph.addVertex(v3);
		ansMap.put(v3, getVerticesByValue(key3, intVal3));
		edge13 = "PERPETRATED";
		queryGraph.addEdge(v1, v3).addLabel(edge13);
		key4 = "CORPORATION_NAME";
		value4 = null;
		queryGraph.addVertex(v4);
		ansMap.put(v4, getVerticesByValue(key4, value4));
		edge34 = "TARGET_CORPORATION";
		queryGraph.addEdge(v3, v4).addLabel(edge34);
		edge24 = "TARGET_CORPORATION";
		queryGraph.addEdge(v2, v4).addLabel(edge24);

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
	
	public void testDiamondQuery() {
		Property p1 = new Property() , p2 = new Property(), p3 = new Property(), p4 = new Property();
		String edge12, edge13, edge24, edge34;
		p1.set("GROUP_NAME", "Taliban");
		p2.set("COUNTRY_CODE", 4);
		p3.set("COUNTRY_CODE", 4);
		p4.set("CORPORATION_NAME", "School");
		edge12 = "PERPETRATED";
		edge13 = "PERPETRATED";
		edge34 = "TARGET_CORPORATION";
		edge24 = "TARGET_CORPORATION";
		query.diamondQuery(p1, p2, p3, p4, edge12, edge24, edge13, edge34);
	}

	public void testAggregationQuery(){
		System.out.println(query.aggregationQuery("WEAPON_TYPE_1", "Melee"));
	}
	
	public void testIsomorphism() {
		MyQuery query = new MyQuery(g);
		Scanner kb = new Scanner(System.in);
		String key, value, edge12, edge23;
		Property p1, p2, p3;
		System.out.print("Enter v1 key: ");
		key = kb.next();
		System.out.print("Enter v1 value: ");
		value = kb.nextLine();
		p1 = new Property(key, value);
		System.out.print("Enter v2 key: ");
		key = kb.next();
		System.out.print("Enter v2 value: ");
		value = kb.next();
		p2 = new Property(key, value);
		System.out.print("Enter v1->v2 Label: ");
		edge12 = kb.next();
		System.out.print("Enter v3 key: ");
		key = kb.next();
		System.out.print("Enter v3 value: ");
		value = kb.nextLine();
		p3 = new Property(key, value);
		System.out.print("Enter v2->v3 Label: ");
		edge23 = kb.next();
		query.lineQuery(p1, p2, p3, edge12, edge23);

	}

	public void importIndex(Map<Object, HashSet<PropertyVertex>> index,
			String indexedOn) {
		indexes.put(indexedOn, index);
	}
	
	public void importDoubleIndex(Map<String, HashMap<String, PropertyVertex>> doubleIndex, String indexedOn){
		doubleIndexes.put(indexedOn, doubleIndex);
	}

	public void importMainIndex(HashMap<Long, PropertyVertex> mainIndex) {
		this.mainIndex = mainIndex;
	}

	public HashSet<PropertyVertex> getVerticesByValue(String key, Object value) {
		if (value != null) {
			if (indexes.containsKey(key))
				return indexes.get(key).get(value);
			else if (doubleIndexes.containsKey(key) && (doubleIndexes.get(key).get(value) != null))
				return new HashSet<PropertyVertex>(doubleIndexes.get(key).get(value).values());
			else	
				return new HashSet<PropertyVertex>();
		} else {
			if (indexes.containsKey(key)) {
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (HashSet<PropertyVertex> set : indexes.get(key).values()) {
					toReturn.addAll(set);
				}
				return toReturn;
			} else if (doubleIndexes.containsKey(key)){
				HashMap<String, PropertyVertex> tempMap;
				HashSet<PropertyVertex> toReturn = new HashSet<PropertyVertex>();
				for (String gname : doubleIndexes.get(key).keySet()){
					tempMap = doubleIndexes.get(key).get(gname);
					toReturn.addAll(tempMap.values());
				}
				return toReturn;
			}
			return new HashSet<PropertyVertex>();
		}
	}

}
