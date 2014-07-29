package ganz.lennon.gtdgraph;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class PropertyEdge extends DefaultEdge {

	// Don't really understand this
	private static final long serialVersionUID = 1L;

//	List<Property> properties = new ArrayList<Property>(10);
	private Map<String, Object> properties = new HashMap<String, Object>();
	private Map<String, ArrayList<PropertyVertex>> lists = new HashMap<String, ArrayList<PropertyVertex>>();
	List<String> labels = new ArrayList<String>(10);

	public void addProperty(String key, Object value) {
		properties.put(key, value);
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

	public ArrayList<PropertyVertex> addList(String key, ArrayList<PropertyVertex> list){
		lists.put(key, list);
		return list;
	}
	
	public ArrayList<PropertyVertex> getList(String key){
		return lists.get(key);
	}
	// remove property
	public void removeProperty(String key) {
		properties.remove(key);
	}
	
//	public PropertyEdge(PropertyVertex v1, PropertyVertex v2, String propName){
//	super();
//	}
//		
	// add property
//	public void addProperty(String key, Object value) {
//		properties.add(new Property(key, value));
//	}
//
//	// check whether Edge has certain property
//	public boolean hasProperty(String key) {
//		for (Property p : properties) {
//			if (p.getKey().equals(key)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	//return the list of properties
//	public List<Property> getProperties(){
//		return properties;
//	}
//	
	//add String label
	public void addLabel(String label){
		labels.add(label);
	}
//	
	//check whether Edge has certain label
	public boolean hasLabel(String label){
		return labels.contains(label);
	}
//	
	//return the list of labels
	public List<String> getLabels(){
		return labels;
	}
	
//	public ArrayList<PropertyVertex> getList(String key){
//		properties.
//	}
//	
	public String toString(){
		StringBuilder s = new StringBuilder();
//		s.append("From: " + this.getSource() + "\n");
//		s.append("To: " + this.getTarget() + "\n");
		s.append(labels.toString());
		s.append(properties.toString());
		
		return s.toString();
	}

	public String getLabel() {
		if ((labels != null) && (labels.size() > 0)){
			return labels.get(0);
		}
		// TODO Auto-generated method stub
		return null;
	}
}
