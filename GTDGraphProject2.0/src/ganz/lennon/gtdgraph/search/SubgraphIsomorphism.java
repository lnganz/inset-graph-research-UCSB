package ganz.lennon.gtdgraph.search;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import ganz.lennon.gtdgraph.MyGraph;
import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;

import org.jgrapht.DirectedGraph;
import org.jgrapht.alg.BronKerboschCliqueFinder;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DirectedMultigraph;

public class SubgraphIsomorphism {

	public DirectedGraph<PropertyVertex, PropertyEdge> pg; // Pattern Graph
															// (subgraph)
	public DirectedGraph<PropertyVertex, PropertyEdge> dg; // Data Graph
	HashMap<Long, PropertyVertex> mainIndex;
	HashMap<String, PropertyVertex> tagIndex;
	public HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap;
	public HashMap<Object, HashSet<PropertyVertex>> gIndex;
	public Vector<HashMap<PropertyVertex, HashSet<PropertyVertex>>> matchesList;
	public int matchnum;

	public SubgraphIsomorphism(DirectedGraph<PropertyVertex, PropertyEdge> pgs,
			DirectedGraph<PropertyVertex, PropertyEdge> dgs,
			HashMap<Long, PropertyVertex> mainIndex) throws IOException {
		pg = pgs;
		dg = dgs;
		this.mainIndex = mainIndex;
		tagIndex = new HashMap<String, PropertyVertex>(100);
		// gIndex = new gpm_revIndex(dg);
		matchesList = new Vector<HashMap<PropertyVertex, HashSet<PropertyVertex>>>();
		ansMap = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();
		initAnsMap();
	}

	public void setAnsMap(
			HashMap<PropertyVertex, HashSet<PropertyVertex>> ansMap) {
		this.ansMap = ansMap;
	}

	// using index to fill in the matrix.
	public void initAnsMap() throws IOException {
		// gIndex.buildIndex(true);
		// Vector<AVpair> avlist = null;
		// for (PropertyVertex pn : pg.vertexSet()) {
		// HashSet<PropertyVertex> anset = new HashSet<PropertyVertex>();
		// avlist = pn.getAVlist();
		// HashSet<String> ans = gIndex.getAnswer(avlist);
		// if (ans == null || ans.size() == 0) {
		// System.out.println("empty sim set.");
		// return;
		// }
		// for (String col : ans) {
		// anset.add(dg.getVertex(col));
		// // matrix[Integer.parseInt(pn.tag)][Integer.parseInt(col)] = 1;
		// }
		// // System.out.println(pn.tag + ":" + anset);
		// ansMap.put(pn, anset);
	}

	public PropertyVertex[] getVpair(PropertyVertex anode) {
		String ln = anode.getTag().substring(0, anode.getTag().indexOf(":"));
		String rn = anode.getTag().substring(anode.getTag().indexOf(":") + 1);
		PropertyVertex[] npair = new PropertyVertex[2];
		npair[0] = getVertexByTag(pg, ln);
		// npair[1] = getVertexByTag(dg, rn);
		npair[1] = getVertexByIndexedID(rn);
		return npair;
	}

	public PropertyVertex getVertexByTag(
			DirectedGraph<PropertyVertex, PropertyEdge> graph, String tag) {
		for (PropertyVertex v : graph.vertexSet()) {
			if (v.getTag().equals(tag))
				return v;
		}
		return null;
	}

	public PropertyVertex getVertexByIndexedID(String tag) {
		return mainIndex.get(Long.parseLong(tag));
	}

	// construct subgraph iso graphs.
	// first construct the adjacency graph, then perform clique checking.
	// adjacency graph is also a gpm_graph.
	public void computeMatch() {
		String tag;
		DirectedGraph<PropertyVertex, PropertyEdge> adg = new DefaultDirectedGraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
		for (PropertyVertex pn : ansMap.keySet()) {
			HashSet<PropertyVertex> aset = ansMap.get(pn);
			for (PropertyVertex an : aset) {
				tag = pn.getTag() + ":" + an.getTag();
				PropertyVertex gn = new PropertyVertex(tag);
				tagIndex.put(tag, gn);
				adg.addVertex(gn);
			}
		}

		System.out.println("Node size: " + adg.vertexSet().size());

		PropertyEdge edge1, edge2;
		for (PropertyVertex gn1 : adg.vertexSet()) {
			PropertyVertex[] pair1 = getVpair(gn1);
			for (PropertyVertex gn2 : adg.vertexSet()) {
				PropertyVertex[] pair2 = getVpair(gn2);
				edge1 = pg.getEdge(pair1[0], pair2[0]);
				if (edge1 != null) {
					edge2 = dg.getEdge(pair1[1], pair2[1]);
					// if ((edge2 != null) &&
					// (edge1.getLabel().equals(edge2.getLabel()))){
					if (edge2 != null) {
						adg.addEdge(gn1, gn2);
						adg.addEdge(gn2, gn1);
					}
				} else {
					if (!pair1[0].getTag().equals(pair2[0].getTag())
							&& !pg.containsEdge(pair2[0], pair1[0])) {
						adg.addEdge(gn1, gn2);
//						adg.addEdge(gn2, gn1);
					}
				}
			}
		}

		System.out.println("Adjacency Graph Init Done. Node size: "
				+ adg.vertexSet().size() + ", Edge size: "
				+ adg.edgeSet().size() + ", Computing...");

		BronKerboschCliqueFinder<PropertyVertex, PropertyEdge> cfinder = new BronKerboschCliqueFinder<PropertyVertex, PropertyEdge>(
				adg);
		Collection<Set<PropertyVertex>> cliques = cfinder
				.getBiggestMaximalCliques();
		// .getAllMaximalCliques();
		System.out.println("cliques:" + cliques.size());
		System.out.println(cfinder.getBiggestMaximalCliques().size());
		int count = 0;
		for (Set<PropertyVertex> ns : cliques) {
			HashMap<PropertyVertex, HashSet<PropertyVertex>> matches = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();
			HashSet<PropertyVertex> pnset = new HashSet<PropertyVertex>();
			// HashSet<String> npset = new HashSet<String>();
			for (PropertyVertex n : ns) {
				// npset.add(e)
				PropertyVertex[] pair = getVpair(n);
				pnset.add(pair[0]);
				if (matches.get(pair[0]) == null) {
					HashSet<PropertyVertex> part = new HashSet<PropertyVertex>();
					matches.put(pair[0], part);
				}
				matches.get(pair[0]).add(pair[1]);
			}
			if (!pnset.containsAll(pg.vertexSet())) {
				matches.clear();
			} else {
				count++;
				System.out.println("Iso " + count);
				// for (PropertyVertex pn : matches.keySet()) {
				// System.out.print("pattern node " + pn.tag+ " :");
				// HashSet<PropertyVertex> dnset = matches.get(pn);
				// for(gpm_node dn: dnset){
				// System.out.print(" "+dn.tag);
				// }
				// System.out.println();
				// }
				matchesList.add(matches);
			}
			// if(ns.containsAll(pg))
			// if(ns.size()>1){
			// ccount++;
			// pw.print("Clique "+ccount+ " : size "+ns.size());
			// for(gpm_node n: ns){
			// pw.print(" "+n.tag);
			// }
			// pw.println();
			// }
		}
		matchnum = 0;
		HashMap<PropertyVertex, HashSet<PropertyVertex>> totalmatch = new HashMap<PropertyVertex, HashSet<PropertyVertex>>();
		for (HashMap<PropertyVertex, HashSet<PropertyVertex>> maps : matchesList) {
			for (PropertyVertex n : maps.keySet()) {
				if (totalmatch.get(n) == null) {
					HashSet<PropertyVertex> mset = new HashSet<PropertyVertex>();
					totalmatch.put(n, mset);
				}
				totalmatch.get(n).addAll(maps.get(n));
			}
		}

		for (PropertyVertex n : totalmatch.keySet()) {
			matchnum += totalmatch.get(n).size();
		}

		System.out.println("Iso number:" + matchesList.size());
		System.out.println("MatchNum:" + matchnum);
	}

	public void displayIsos() {
		int count = 0;
		for (HashMap<PropertyVertex, HashSet<PropertyVertex>> maps : matchesList) {
			count++;
			System.out.println("Iso " + count);
			for (PropertyVertex pn : maps.keySet()) {
				System.out.print("pattern node " + pn + " :");
				HashSet<PropertyVertex> dnset = maps.get(pn);
				for (PropertyVertex dn : dnset) {
					System.out.print(" " + dn);
				}
				System.out.println();
			}
		}
	}
}
