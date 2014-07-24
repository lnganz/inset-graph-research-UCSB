package ganz.lennon.gtdgraph.io;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import ganz.lennon.gtdgraph.*;

import org.jgrapht.DirectedGraph;

public class GraphImporterText {

	DirectedGraph<PropertyVertex, PropertyEdge> g;
	HashMap<Integer, PropertyVertex> addedVertices;

	public GraphImporterText(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
	}

	public Map<Object, HashSet<PropertyVertex>> indexOn(String key) {
		Map<Object, HashSet<PropertyVertex>> index = new HashMap<Object, HashSet<PropertyVertex>>(
				10000);
		Object temp;
		String tempString;
		HashSet<PropertyVertex> set;
		for (PropertyVertex v : g.vertexSet()) {
			if (v.hasProperty(key)) {
				temp = v.getValue(key);
				if (!index.containsKey(temp)) {
					set = new HashSet<PropertyVertex>(10);
					set.add(v);
					index.put(temp, set);
				} else
					index.get(temp).add(v);
			}
		}
		return index;
	}

	public boolean importIndex(String filename) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		HashMap<Object, HashSet<PropertyVertex>> index;
		Integer key;
		PropertyVertex v;
		while (sc.hasNextLine()) {

		}

		return true;
	}

	public boolean importGraph(String filename) {
		FileInputStream inputStream = null;
		Scanner sc = null;
		addedVertices = new HashMap<Integer, PropertyVertex>(10000);
		String line, label;
		String[] splitLine, props;
		int src, tgt;
		PropertyVertex tempVertex;
		PropertyEdge tempEdge;
		try {
			inputStream = new FileInputStream(filename);
			sc = new Scanner(inputStream);
			for (int i = 0; i < 6; i++) {
				sc.nextLine();
			}
			while (sc.hasNextLine()) {
				line = sc.nextLine().trim();
				if (line.equals("node")) {
					sc.nextLine(); // ignore opening '['
					splitLine = sc.nextLine().trim().split(" ");
					tempVertex = new PropertyVertex();
					g.addVertex(tempVertex);
					addedVertices.put(Integer.parseInt(splitLine[1].trim()),
							tempVertex);
					splitLine = sc.nextLine().trim().split("\\{"); // inside
					splitLine[1] = splitLine[1].split("\\}")[0]; // curly braces
					splitLine = splitLine[1].split(","); //properties
					for (int i = 0; i < splitLine.length; i++) {
						props = splitLine[i].split("=");
						if (props.length > 1)
							tempVertex.addProperty(props[0].trim(), props[1].trim());
					}
					sc.nextLine(); // ignore closing ']'
				} else if (line.equals("edge")) {
					sc.nextLine();
					sc.nextLine();
					src = Integer.parseInt(sc.nextLine().split(" ")[1]);
					tgt = Integer.parseInt(sc.nextLine().split(" ")[1]);
					tempEdge = g.addEdge(addedVertices.get(src),
							addedVertices.get(tgt));
					label = sc.nextLine().split("\\[")[1].split("\\]")[0];
					tempEdge.addLabel(label);
					sc.nextLine();
				}
			}
			sc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return true;
	}

}
