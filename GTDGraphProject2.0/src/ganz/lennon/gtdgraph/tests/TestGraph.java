package ganz.lennon.gtdgraph.tests;

import org.jgraph.JGraph;
import org.jgrapht.*;
import org.jgrapht.alg.*;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.GmlExporter;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.graph.*;

import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;
import ganz.lennon.gtdgraph.io.GraphImporterExcel;
import ganz.lennon.gtdgraph.io.GraphImporterText;
import ganz.lennon.gtdgraph.io.TableLoader;
import ganz.lennon.gtdgraph.search.ReachabilityBFS;
import ganz.lennon.gtdgraph.search.SubgraphMatcher;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class TestGraph {

	public static void main(String args[]) {

		String[] countryCodes; // String representations of countries where
								// index is country code
		String[] weaponSubtypeCodes; // String representations of weapon
										// subtypes
		Map<Object, HashSet<PropertyVertex>> iCC;// reverse indexed by
													// Country Code
		Map<Object, HashSet<PropertyVertex>> iCN;// Corp name
		Map<Object, HashSet<PropertyVertex>> iGN;// Group name
		DirectedGraph<PropertyVertex, PropertyEdge> dg = new DirectedMultigraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));

		// NeighborIndex ind = new NeighborIndex(dg);

		TableLoader tl = new TableLoader();
		countryCodes = tl.loadCountryCodes(); // Load country names into array
		weaponSubtypeCodes = tl.loadWeaponSubtypes(); // Load weapon subtype
														// names into array

		double startTime = System.currentTimeMillis();
		System.out.println("Importing GTD");

		GraphImporterExcel imp = new GraphImporterExcel();
		imp.importFromExcel("test.xlsx", dg);

		iCC = imp.getIndexCountryCode();
		iCN = imp.getIndexCorpName();
		iGN = imp.getIndexGroupName();

		SubgraphMatcher matcher = new SubgraphMatcher(dg);
		matcher.importIndex(iCC, "COUNTRY_CODE");
		matcher.importIndex(iCN, "CORPORATION_NAME");
		matcher.importIndex(iGN, "GROUP_NAME");
		
		Set<PropertyVertex> set1, set2;
		set1 = matcher.getVerticesByValue("GROUP_NAME", "Taliban");
		System.out.println(set1);
		set2 = matcher.getVerticesByValue("COUNTRY_CODE", 4);
		System.out.println(set2);
		Set<PropertyEdge> edges = matcher.getSuitableEdges(set1, set2, "PERPETRATED");
		System.out.println(edges);
		for (PropertyEdge e : edges){
			System.out.println(dg.getEdgeTarget(e));
		}
		

		// GraphImporterText gimp = new GraphImporterText(dg);
		// gimp.importGraph("TestGraphData3.txt");

		// imp.importFromExcel("C:\\Users\\Sigma\\Desktop\\GTD06_12Subset.xlsx",
		// dg);

		// imp.importFromExcel(
		// "C:\\Users\\Sigma\\Desktop\\gtd_201312dist\\gtd_06to12_1213dist.xlsx",
		// dg);
		// System.out.println((System.currentTimeMillis() - startTime) / 1000);
		// imp.importFromExcel(
		// "C:\\Users\\Sigma\\Desktop\\gtd_201312dist\\gtd_90to05_1213dist.xlsx",
		// dg);
		// System.out.println((System.currentTimeMillis() - startTime) / 1000);
		// imp.importFromExcel(
		// "C:\\Users\\Sigma\\Desktop\\gtd_201312dist\\gtd_70to89_1213dist.xlsx",
		// dg);
		// System.out.println((System.currentTimeMillis() - startTime) / 1000);
		// System.out.println("Number of vertices: " + dg.vertexSet().size());
		// System.out.println("Import Successful!");
		// SearchTester search = new SearchTester();
		// search.test();

		// DOTExporter<PropertyVertex, PropertyEdge> dot = new
		// DOTExporter<PropertyVertex, PropertyEdge>();
		//
		// try {
		// dot.export(new FileWriter("testDOT1.dot"), dg);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// GmlExporter<PropertyVertex, PropertyEdge> ge = new
		// GmlExporter<PropertyVertex, PropertyEdge>();
		//
		// System.out.println("Writing to file...");
		// try {
		// ge.setPrintLabels(3);
		// ge.export(new FileWriter("TestGraphData06_12.gml"), dg);
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

		System.out.println("Done!");
	}

	public Set<PropertyVertex> getAdjacentVertices(PropertyVertex v,
			Graph<PropertyVertex, PropertyEdge> g) {
		Set<PropertyVertex> vertices = new HashSet<PropertyVertex>();

		for (PropertyEdge e : g.edgesOf(v))
			vertices.add(g.getEdgeTarget(e));

		return vertices;
	}
	
}
