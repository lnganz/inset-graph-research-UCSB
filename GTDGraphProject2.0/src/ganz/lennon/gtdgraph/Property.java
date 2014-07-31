package ganz.lennon.gtdgraph;

public class Property {
	
	String key;
	Object value;
	
	public Property(){}
	
	public Property(String key, Object value){
		this.key = key;
		this.value = value;
	}
	
	public Property(String key){
		this.key = key;
		this.value = null;
	}
	
	public void set(String key, Object value){
		this.key = key;
		this.value = value;
	}
	public void setKey(String key){
		this.key = key;
	}
	
	public String getKey(){
		return key;
	}
	
	public void setValue(Object value){
		this.value = value;
	}
	public Object getValue(){
		return value;
	}
}
