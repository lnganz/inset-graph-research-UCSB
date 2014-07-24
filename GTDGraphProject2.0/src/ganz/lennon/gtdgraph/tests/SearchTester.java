package ganz.lennon.gtdgraph.tests;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

import ganz.lennon.gtdgraph.*;
import ganz.lennon.gtdgraph.search.ReachabilityBFS;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.ext.GmlExporter;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.DirectedMultigraph;

public class SearchTester {

	DirectedGraph<PropertyVertex, PropertyEdge> g;
	private static final int NUM_TESTS_CONSOLE = 1000;
	private static final int NUM_TESTS_EXCEL = 1000;

	public SearchTester() {
		g = new DirectedMultigraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
	}

	public SearchTester(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
	}

	private void repeatedTest() {
		ArrayList<PropertyVertex> vertices;
		int numVertices, numTrials;
		double edgeFactor, startEF, endEF, incrementEF;
		try {
			FileWriter out = new FileWriter("SearchData.txt");

			Scanner kb = new Scanner(System.in);
			// while (going) {
			System.out.print("Enter number of vertices: ");
			numVertices = kb.nextInt();
			System.out.print("Enter starting edge probability: ");
			startEF = kb.nextDouble();
			System.out.print("Enter ending edge probability: ");
			endEF = kb.nextDouble();
			System.out.print("Enter increment value: ");
			incrementEF = kb.nextDouble();
			System.out.println("Enter # trials for each: ");
			numTrials = kb.nextInt();
			ArrayList<Double> averages;
			double average;
			vertices = constructErdosGraph(numVertices, startEF);
			for (double i = startEF; i <= endEF; i += incrementEF) {
				averages = new ArrayList<Double>();
				System.out.println();
				System.out.println("Vertices: " + g.vertexSet().size());
				System.out.println("Edges: " + g.edgeSet().size());
				for (int j = 0; j < numTrials; j++) {
					// System.out.println("Edge Factor: " + i);
					if (j % 10 == 0) {
						System.out.println("Processing...");
					}
					
//					reset();
					// vertices = constructRandomGraph(numVertices, edgeFactor);
//					vertices = constructErdosGraph(numVertices, i);
					// System.out.println("New Graph: " + g.vertexSet().size()
					// + " vertices");
					// System.out.println("Enter output filename");
					// filename = kb.next();
					averages.add(consoleTest(vertices));
				}
				average = calculateAverage(averages);
				out.append("Factor: " + i + "\nAverage: " + average + "\n");
				System.out.println("Average time with edge factor " + i + ": "
						+ average);
				increaseDensity(vertices, incrementEF);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void test() {
		boolean going = true;
		int numVertices;
		double startEF;
		ArrayList<PropertyVertex> vertices;
		PropertyVertex v1, v2, v3, v4;
		String filename;
		repeatedTest();
		// reset();
//		Scanner kb = new Scanner(System.in);
//		System.out.print("Enter number of vertices: ");
//		numVertices = kb.nextInt();
//		System.out.print("Enter starting edge probability: ");
//		startEF = kb.nextDouble();
//		vertices = constructErdosGraph(numVertices, startEF);

	}

	@SuppressWarnings("unused")
	private ArrayList<PropertyVertex> constructErdosGraph(int numVertices,
			double densityFactor) {
		ArrayList<PropertyVertex> vertices = new ArrayList<PropertyVertex>(
				10000);
		fillWithVertices(vertices, numVertices);
		fillWithEdgesByDensity(vertices, densityFactor);
		return vertices;
	}

	@SuppressWarnings("unused")
	private void fillWithEdgesByDensity(ArrayList<PropertyVertex> vertices,
			double densityFactor) {
		PropertyVertex v1, v2;
		for (int i = 0; i < vertices.size(); i++) {
			v1 = vertices.get(i);
			for (int j = 0; j < vertices.size(); j++) {
				if (i != j) {
					v2 = vertices.get(j);
					if (Math.random() < densityFactor)
						g.addEdge(v1, v2);
				}
			}
		}
	}

	private void increaseDensity(ArrayList<PropertyVertex> vertices, double incrementFactor) {
		int numEdgesToAdd = (int) (vertices.size() * incrementFactor);
		int numEdgesAdded;
		PropertyVertex v1, v2;
		for (int i = 0; i < vertices.size(); i++) {
			v1 = vertices.get(i);
			numEdgesAdded = 0;
			while (numEdgesAdded < numEdgesToAdd) {
				v2 = randomVertex(vertices);
				if (!v1.equals(v2) && !g.containsEdge(v1, v2)){
					g.addEdge(v1, v2);
					numEdgesAdded++;
				}
			}
		}
	}

	private ArrayList<PropertyVertex> constructRandomGraph(int numVertices,
			double edgeFactor) {
		ArrayList<PropertyVertex> vertices = new ArrayList<PropertyVertex>(
				10000);
		fillWithVertices(vertices, numVertices);
		fillWithEdges(vertices, numVertices, edgeFactor);
		return vertices;
	}

	void fillWithVertices(ArrayList<PropertyVertex> vertices, int numVertices) {
		PropertyVertex v;
		for (int i = 0; i < numVertices; i++) {
			v = new PropertyVertex(i);
			g.addVertex(v);
			vertices.add(v);
		}
	}

	// Fills graph with random edges
	// Parameters - graph to connect, list of vertices, number of vertices,
	// edgefactor
	// Returns - nothing
	void fillWithEdges(ArrayList<PropertyVertex> vertices, int numVertices,
			double edgeFactor) {
		PropertyVertex v1, v2;
		for (int i = 0; i < edgeFactor * numVertices; i++) {// Connect random
															// vertices
			v1 = randomVertex(vertices);
			v2 = randomVertex(vertices);
			if ((!v1.equals(v2)) && (!g.containsEdge(v1, v2)))
				g.addEdge(v1, v2);
		}
	}

	long calculateAverageTime(ArrayList<Long> timeset) {
		Long total = (long) 0;
		for (Long l : timeset) {
			total += l;
		}
		return total / timeset.size();
	}

	double calculatePercentDifference(long num1, long num2) {
		return ((Math.abs(num1 - num2)) / ((num1 + (double) num2) / 2)) * 100;
	}

	void reset() {
		this.g = new DirectedMultigraph<PropertyVertex, PropertyEdge>(
				new ClassBasedEdgeFactory<PropertyVertex, PropertyEdge>(
						PropertyEdge.class));
	}

	void excelTest(ArrayList<PropertyVertex> vertices, String filename) {
		long t1, t2;
		PropertyVertex start, end;
		ReachabilityBFS reach = new ReachabilityBFS(g);
		DecimalFormat df = new DecimalFormat("#.00");
		ArrayList<Long> timesBFS = new ArrayList<Long>(30), timesBIBFS = new ArrayList<Long>(
				30);
		ArrayList<Integer> pathLengths = new ArrayList<Integer>(30);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheet = workbook.createSheet("Test Results");
		int cellnum = 0, rownum = 0;
		Row row = sheet.createRow(rownum++);
		Cell cell = row.createCell(cellnum++);
		cell.setCellValue("# of Vertices");
		cell = row.createCell(cellnum++);
		cell.setCellValue("# of Edges");
		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellValue(vertices.size());
		cell = row.createCell(cellnum++);
		cell.setCellValue(g.edgeSet().size());
		row = sheet.createRow(rownum++);
		for (int i = 0; i < NUM_TESTS_EXCEL; i++) {
			start = randomVertex(vertices);
			end = randomVertex(vertices);

			if (i % 2 == 0) {
				t1 = System.nanoTime();
				pathLengths.add(reach.pathLengthBFS(start, end));
				t1 = System.nanoTime() - t1;
				timesBFS.add(t1);

				t2 = System.nanoTime();
				reach.pathLengthBiBFS(start, end);
				t2 = System.nanoTime() - t2;
				timesBIBFS.add(t2);
			} else {
				t2 = System.nanoTime();
				pathLengths.add(reach.pathLengthBiBFS(start, end));
				t2 = System.nanoTime() - t2;
				timesBIBFS.add(t2);

				t1 = System.nanoTime();
				reach.pathLengthBFS(start, end);
				t1 = System.nanoTime() - t1;
				timesBFS.add(t1);
			}
		}
		for (int j = 0; j < NUM_TESTS_EXCEL; j++) {
			cellnum = 0;
			row = sheet.createRow(rownum++);
			cell = row.createCell(cellnum++);
			cell.setCellValue(pathLengths.get(j));
			cell = row.createCell(cellnum++);
			cell.setCellValue(timesBFS.get(j));
			cell = row.createCell(cellnum++);
			cell.setCellValue(timesBIBFS.get(j));
		}
		row = sheet.createRow(rownum++);
		cellnum = 0;
		cell = row.createCell(cellnum++);
		cell.setCellValue("Average Times");
		cell = row.createCell(cellnum++);
		cell.setCellValue(calculateAverageTime(timesBFS));
		cell = row.createCell(cellnum++);
		cell.setCellValue(calculateAverageTime(timesBIBFS));
		try {
			System.out.println("Writing to excel file...");
			FileOutputStream out = new FileOutputStream(new File(filename));
			workbook.write(out);
			out.close();
			System.out.println("Writing complete");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	PropertyVertex randomVertex(ArrayList<PropertyVertex> vertices) {
		return vertices.get((int) (Math.random() * vertices.size()));
	}

	double consoleTest(ArrayList<PropertyVertex> vertices) {
		long t1, t2, averageBFS, averageBIBFS;
		double percentDifference, avgToReturn;
		PropertyVertex start, end;
		ReachabilityBFS reach = new ReachabilityBFS(g);
		DecimalFormat df = new DecimalFormat("#.00");
		ArrayList<Long> timesBFS = new ArrayList<Long>(30), timesBIBFS = new ArrayList<Long>(
				30);
		ArrayList<Integer> lengths = new ArrayList<Integer>(30);
		int length = 0;
		for (int i = 0; i < NUM_TESTS_CONSOLE; i++) {
			start = randomVertex(vertices);
			end = randomVertex(vertices);

			if (i % 2 == 0) {
				System.out.print("");
				reach.pathLengthBFS(randomVertex(vertices),
						randomVertex(vertices));

				t1 = System.nanoTime();
				length = reach.pathLengthBFS(start, end);
				// System.out.println(reach.pathLengthBFS(start, end));
				t1 = System.nanoTime() - t1;
				// System.out.println("BFS Time: " + t1 + " ns");
				timesBFS.add(t1);
				lengths.add(length);

				System.out.print("");
				t2 = System.nanoTime();
				reach.pathLengthBiBFS(start, end);
				t2 = System.nanoTime() - t2;
				timesBIBFS.add(t2);
			} else {
				System.out.print("");
				reach.pathLengthBFS(randomVertex(vertices),
						randomVertex(vertices));

				t2 = System.nanoTime();
				reach.pathLengthBiBFS(start, end);
				t2 = System.nanoTime() - t2;
				timesBIBFS.add(t2);

				System.out.print("");
				t1 = System.nanoTime();
				length = reach.pathLengthBFS(start, end);
				t1 = System.nanoTime() - t1;
				timesBFS.add(t1);
				lengths.add(length);
			}
		}

		int count = 0;
		long bfsSum = 0, bibfsSum = 0;
		for (int i = 0; i < lengths.size(); i++) {
			if (lengths.get(i) > 0) {
				bfsSum += timesBFS.get(i);
				bibfsSum += timesBIBFS.get(i);
				count++;
			}
		}
		// System.out.println("Paths found: " + count);
		if (count > 0) {
			avgToReturn = (100 * (bibfsSum / count) / (double) (bfsSum / count));
			// System.out.println("Average Percent: " + df.format(avgToReturn)
			// + "%");
			return avgToReturn;
		} else
			return -1;

		// averageBFS = calculateAverageTime(timesBFS);
		// System.out.println("BFS Average Time: " + averageBFS);
		// averageBIBFS = calculateAverageTime(timesBIBFS);
		// System.out.println("BIBFS Average Time: " + averageBIBFS);
		// percentDifference = calculatePercentDifference(averageBFS,
		// averageBIBFS);
		// System.out.println("Average Percent Time: "
		// + df.format((averageBIBFS / (double) averageBFS) * 100) + "%");
	}

	int[] calculateAverage(ArrayList<Long> bfs, ArrayList<Long> bibfs) {
		int[] averages = { 0, 0 };
		for (int i = 0; i < bfs.size(); i++)
			;
		return averages;
	}

	double calculateAverage(ArrayList<Double> values) {
		double sum = 0;
		for (Double d : values) {
			sum += d;
		}
		return sum / values.size();
	}
}
