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
		HashMap<Object, HashSet<PropertyVertex>> iCC;// reverse indexed by
														// Country Code
		HashMap<Object, HashSet<PropertyVertex>> iCN;// Corp name
		HashMap<Object, HashSet<PropertyVertex>> iGN;// Group name
		HashMap<Long, PropertyVertex> iID; // vID
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
//		imp.importFromExcel("test.xlsx", dg);
		 imp.importFromExcel(
		 "C:\\Users\\Lennon\\Desktop\\gtd_06to12_1213dist.xlsx", dg);

		System.out.println((System.currentTimeMillis() - startTime) / 1000);

		iCC = imp.getIndexCountryCode();
		iCN = imp.getIndexCorpName();
		iGN = imp.getIndexGroupName();
		iID = imp.getIndexID();

		SubgraphMatcher matcher = new SubgraphMatcher(dg, iID);
		matcher.importIndex(iCC, "COUNTRY_CODE");
		matcher.importIndex(iCN, "CORPORATION_NAME");
		matcher.importIndex(iGN, "GROUP_NAME");
		matcher.importMainIndex(iID);

		// System.out.println(iCC.toString());
		// imp.writeIndexToFile(iCC, "COUNTRY_CODE");
//		matcher.testDiamond();
		// matcher.testIsomorphism();

		// GraphImporterText gimp = new GraphImporterText(dg);
		// gimp.importGraph("Data06_12.txt");

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
		// System.out.println("Number of edges: " + dg.edgeSet().size());

		// System.out.println("Import Successful!");
		// SearchTester search = new SearchTester();
		// search.test();

		GmlExporter<PropertyVertex, PropertyEdge> ge = new GmlExporter<PropertyVertex, PropertyEdge>();

		System.out.println("Writing to file...");
		try {
			ge.setPrintLabels(3);
			ge.export(new FileWriter("Test06_12.txt"), dg);
		} catch (IOException e) {
			e.printStackTrace();
		}

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
