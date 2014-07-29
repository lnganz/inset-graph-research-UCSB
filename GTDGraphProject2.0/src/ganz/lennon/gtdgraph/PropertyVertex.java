package ganz.lennon.gtdgraph;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class PropertyVertex {
	private long vID; // Vertex ID
	private List<String> labels = new ArrayList<String>(); // Vertex Labels
	// private List<Property> properties = new ArrayList<Property>(10); //
	// Vertex Properties
	private Map<String, Object> properties = new HashMap<String, Object>();
	// private List<PropertyEdge> edges = new ArrayList<PropertEdge>(10); //
	// Connected Edges
	private static String[] propcodes = new String[140];
	private String tag;

	public PropertyVertex() {
	}

	public PropertyVertex(long vID) {
		properties.put("vID", vID);
		tag = vID + "";
	}
	
	public PropertyVertex(String tag){
		this.tag = tag;
		if (!tag.contains(":"))
			properties.put("vID", Long.parseLong(tag));
	}

	// add label
	public void addLabel(String label) {
		labels.add(label);
	}

	// check if vertex has specified label
	public boolean hasLabel(String label) {
		return labels.contains(label);
	}

	public String getLabels() {
		StringBuffer s = new StringBuffer();
		for (String label : labels) {
			s.append(label + " ");
		}
		return s.toString();
	}

	// remove label
	public void removeLabel(String label) {
		labels.remove(label);
	}

	// add property
	public void addProperty(String key, Object value) {
		properties.put(key, value);
		if (key.equals("vID"))
			tag = value + "";
	}

	// check if vertex has specified property
	public boolean hasProperty(String key) {
		return properties.containsKey(key);
	}

	// get the value of the property with the given key
	public Object getValue(String key) {
		return properties.get(key);
	}

	public String getProperties() {
		return properties.toString();
	}

	// remove property
	public void removeProperty(String key) {
		properties.remove(key);
	}

	public long getID() {
		return (long) properties.get("vID");
	}

	public String getTag(){
		return tag;
	}
	
	public void setTag(String newTag){
		tag = newTag;
	}
	public String toString() {
		String temp;
		StringBuilder s = new StringBuilder();
//		s.append(properties.toString()); //for printing complete file
		
//		if (labels.contains("TGROUP"))
//			s.append(properties.get("GROUP_NAME"));
//		else if(labels.contains("INCIDENT"))
//			s.append(properties.get("vID"));
//		else if (labels.contains("TARGET"))
//			s.append(properties.get("TARGET_NAME"));
//		else if (labels.contains("CORPORATION")){
//			temp = properties.get("CORPORATION_NAME").toString();
//			if (!temp.contains("\""))
//				s.append(temp);
//			else
//				s.append("ERROR");
//		}
//		s.append(properties.get("vID"));
		s.append(tag);

		return s.toString();
	}
}