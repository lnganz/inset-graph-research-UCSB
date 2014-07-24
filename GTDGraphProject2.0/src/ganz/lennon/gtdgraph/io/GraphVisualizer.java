package ganz.lennon.gtdgraph.io;
//package ganz.lennon.gtdgraph;
//
//import java.awt.*;
//import java.util.*;
//
//import javax.swing.JApplet;
//
//import org.jgraph.JGraph;
//import org.jgraph.graph.*;
//import org.jgrapht.DirectedGraph;
//import org.jgrapht.ext.JGraphModelAdapter;
//import org.jgrapht.graph.ListenableDirectedGraph;
//
//public class GraphVisualizer extends JApplet {
//
//	private static final Color DEFAULT_BG_COLOR = Color.decode("#FAFBFF");
//	private static final Dimension DEFAULT_SIZE = new Dimension(530, 320);
//	private JGraphModelAdapter m_jgAdapter;
//	private JGraph jgraph;
//	private ListenableDirectedGraph<PropertyVertex, PropertyEdge> lgraph;
//
//	public GraphVisualizer(DirectedGraph<PropertyVertex, PropertyEdge> g) {
//		lgraph = new ListenableDirectedGraph<PropertyVertex, PropertyEdge>(g);
//		jgraph = new JGraph(
//				new JGraphModelAdapter<PropertyVertex, PropertyEdge>(lgraph));
//	}
//
//	public void init() {
//		// create a JGraphT graph
//
//		// create a visualization using JGraph, via an adapter
//		m_jgAdapter = new JGraphModelAdapter(g);
//
//		JGraph jgraph = new JGraph(m_jgAdapter);
//
//		adjustDisplaySettings(jgraph);
//		getContentPane().add(jgraph);
//		resize(DEFAULT_SIZE);
//
//		// add some sample data (graph manipulated via JGraphT)
//		lgraph.addVertex();
//		lgraph.addVertex("v2");
//		lgraph.addVertex("v3");
//		lgraph.addVertex("v4");
//
//		lgraph.addEdge("v1", "v2");
//		lgraph.addEdge("v2", "v3");
//		lgraph.addEdge("v3", "v1");
//		lgraph.addEdge("v4", "v3");
//
//		// position vertices nicely within JGraph component
//		positionVertexAt("v1", 130, 40);
//		positionVertexAt("v2", 60, 200);
//		positionVertexAt("v3", 310, 230);
//		positionVertexAt("v4", 380, 70);
//
//		// that's all there is to it!...
//	}
//
//	private void adjustDisplaySettings(JGraph jg) {
//		jg.setPreferredSize(DEFAULT_SIZE);
//
//		Color c = DEFAULT_BG_COLOR;
//		String colorStr = null;
//
//		try {
//			colorStr = getParameter("bgcolor");
//		} catch (Exception e) {
//		}
//
//		if (colorStr != null) {
//			c = Color.decode(colorStr);
//		}
//
//		jg.setBackground(c);
//	}
//
//	private void positionVertexAt(Object vertex, int x, int y) {
//		DefaultGraphCell cell = m_jgAdapter.getVertexCell(vertex);
//		Map attr = cell.getAttributes();
//		Rectangle b = (Rectangle) GraphConstants.getBounds(attr);
//
//		GraphConstants.setBounds(attr, new Rectangle(x, y, b.width, b.height));
//
//		Map cellAttr = new HashMap();
//		cellAttr.put(cell, attr);
//		m_jgAdapter.edit(cellAttr, null, null, null, null);
//	}
//}