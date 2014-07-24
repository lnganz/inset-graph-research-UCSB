package ganz.lennon.gtdgraph;

public class Property {
	
	String key;
	Object value;
	
	public Property(String key, Object value){
		this.key = key;
		this.value = value;
	}
	
	public Property(String key){
		this.key = key;
		this.value = null;
	}
	
	public String getKey(){
		return key;
	}
	
	public Object getValue(){
		return value;
	}
}
