package ganz.lennon.gtdgraph;

import java.util.HashMap;

import org.jgrapht.graph.DefaultDirectedGraph;

public class MyGraph extends DefaultDirectedGraph<PropertyVertex, PropertyEdge> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<String, PropertyVertex> index;
	
	public MyGraph() {
		super(PropertyEdge.class);
		index = new HashMap<String, PropertyVertex>(50);
		// TODO Auto-generated constructor stub
	}
	
	public boolean addVertex(PropertyVertex v){
		index.put(v.getTag(), v);
		return super.addVertex(v);
	}
	
	public PropertyVertex getVertex(String tag){
		return index.get(tag);
	}

}
