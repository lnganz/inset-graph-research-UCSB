package ganz.lennon.gtdgraph.tests;

import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.*;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.*;

public class TestQueries {

	Map<Integer, ArrayList<PropertyVertex>> iCC;// reverse indexed by Country
												// Code

	private DirectedGraph<PropertyVertex, PropertyEdge> g;

	public TestQueries(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
	}

	public boolean importCountryIndex(Map indexed) {
		iCC = indexed;
		return true;
	}

	public int numIncidentsInCountry(int countryCode) {
		if (iCC.get(countryCode) != null)
			return iCC.get(countryCode).size();
		else
			return 0;
	}

	// Get incidents that occurred in given country
	// Parameters: Country Code
	// Returns: ArrayList of incidents, or null if there are none
	public ArrayList<PropertyVertex> getIncidentsInCountry(int countryCode) {
		if (iCC.get(countryCode) != null && iCC.get(countryCode).size() > 0)
			return iCC.get(countryCode);
		else
			return null;
	}
	
	// Get perpetrator names from list of incidents
	// Parameters: ArrayList of incidents
	// Returns: ArrayList of String representations of names
	public ArrayList<String> getGroupNames(ArrayList<PropertyVertex> incidents) {
		ArrayList<String> names = new ArrayList<String>(10);
		for (PropertyVertex v : incidents) {
			for (PropertyEdge e : g.edgesOf(v)) {
				if (e.hasLabel("PERPETRATED_BY"))
					names.add((String) g.getEdgeTarget(e).getValue(
							"GROUP_NAME"));
			}
		}
		return names;
	}
	
	public void testPaths(int cc) {
		ArrayList<PropertyVertex> list;
		PropertyVertex startVertex, endVertex;
		DijkstraShortestPath<PropertyVertex, PropertyEdge> pathFinder;
		GraphPath<PropertyVertex, PropertyEdge> path;
		List<PropertyEdge> edgelist;

		startVertex = iCC.get(cc).get(0);
		System.out.println(startVertex);

		for (int i = 1; i < iCC.get(cc).size(); i++) {
			endVertex = iCC.get(cc).get(i);
			pathFinder = new DijkstraShortestPath<PropertyVertex, PropertyEdge>(
					g, startVertex, endVertex);
			System.out.println("LENGTH: " + pathFinder.getPathLength());
			if (pathFinder.getPath() != null) {
				System.out.println(pathFinder.getPathLength());
				edgelist = pathFinder.getPathEdgeList();
				for (PropertyEdge e : edgelist) {
					System.out.println(g.getEdgeTarget(e).getValue("GROUP_NAME"));
				}
			}
		}
	}

	// System.out.print("Enter country code: ");
	// input = kb.nextInt();
	// for (int i = 0; i < iCC.get(input).size(); i++){
	// System.out.println(iCC.get(input).get(i).getValue("CITY"));
	// for (PropertyEdge edge : dg.edgesOf(iCC.get(input).get(i))){ //In United
	// States
	// if (edge.getLabels().contains("TARGET_CORP")){ //Target Corp
	// System.out.println(dg.getEdgeTarget(edge).getValue("CORP_NAME") + "\n");
	// //Return target Name
	// }
	// }
	// }

}
