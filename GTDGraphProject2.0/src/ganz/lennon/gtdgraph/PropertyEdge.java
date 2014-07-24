package ganz.lennon.gtdgraph;

import java.util.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;

public class PropertyEdge extends DefaultEdge {

	// Don't really understand this
	private static final long serialVersionUID = 1L;

	List<Property> properties = new ArrayList<Property>(10);
	List<String> labels = new ArrayList<String>(10);

//	public PropertyEdge(PropertyVertex v1, PropertyVertex v2, String propName){
//	super()
//	}
		
	// add property
	public void addProperty(String key, Object value) {
		properties.add(new Property(key, value));
	}

	// check whether Edge has certain property
	public boolean hasProperty(String key) {
		for (Property p : properties) {
			if (p.getKey().equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	//return the list of properties
	public List<Property> getProperties(){
		return properties;
	}
	
	//add String label
	public void addLabel(String label){
		labels.add(label);
	}
	
	//check whether Edge has certain label
	public boolean hasLabel(String label){
		return labels.contains(label);
	}
	
	//return the list of labels
	public List<String> getLabels(){
		return labels;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
//		s.append("From: " + this.getSource() + "\n");
//		s.append("To: " + this.getTarget() + "\n");
		s.append(labels.toString());
		for (Property p : properties){
			s.append(p.getKey() + ":" + p.getValue() + "\n");
		}
		
		return s.toString();
	}
}
