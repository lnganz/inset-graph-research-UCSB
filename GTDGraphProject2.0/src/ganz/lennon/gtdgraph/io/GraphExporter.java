package ganz.lennon.gtdgraph.io;

import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.*;

public class GraphExporter {

	DirectedGraph<PropertyVertex, PropertyEdge> g;
	GmlExporter<PropertyVertex, PropertyEdge> ge;

	public GraphExporter(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
		ge = new GmlExporter<PropertyVertex, PropertyEdge>();
		ge.setPrintLabels(3);
	}

	public boolean writeGraphToFile(String filename) {
		try {
			ge.export(new FileWriter(filename), g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	public boolean writeIndexToFile(Map<Object, HashSet<PropertyVertex>> index, String filename) {
		try {
			FileWriter outFile = new FileWriter(filename);
			for (Object o : index.keySet()) {
				outFile.write(o + "=");
				for (PropertyVertex v : index.get(o)){
					outFile.write(v.getID() + " ");
				}
				outFile.write("\n");
			}
			outFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return true;
	}
}
