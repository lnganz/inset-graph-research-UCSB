package ganz.lennon.gtdgraph.query;

import java.util.*;
import ganz.lennon.gtdgraph.*;
import javax.management.Query;
import org.jgrapht.Graph;

public class MyQuery extends Query {

	Graph<PropertyVertex, PropertyEdge> g;

	public MyQuery(Graph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
	}

	public HashSet<PropertyVertex> verticesWithPropertyAndValue(String key,
			Object value, Set<PropertyVertex> vertices) {
		HashSet<PropertyVertex> matches = new HashSet<PropertyVertex>(100);
		for (PropertyVertex v : vertices) {
			if (v.hasProperty(key))
				if (v.getValue(key).equals(value))
					matches.add(v);
		}
		return matches;
	}
}
