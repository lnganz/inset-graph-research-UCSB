package ganz.lennon.gtdgraph.search;

import ganz.lennon.gtdgraph.PropertyEdge;
import ganz.lennon.gtdgraph.PropertyVertex;

import java.util.*;

import org.jgrapht.*;

public class ReachabilityBFS {

	private static final boolean OUTGOING = true;
	private static final boolean INCOMING = false;

	DirectedGraph<PropertyVertex, PropertyEdge> g;

	// Set<PropertyVertex> active, visited, visitedS, visitedT;
	// Map<PropertyVertex, PropertyVertex> parents, parentsS, parentsT;

	public ReachabilityBFS(DirectedGraph<PropertyVertex, PropertyEdge> g) {
		this.g = g;
	}

	// Finds (using BFS) whether a path exists between two vertices
	// Parameters: PropertyVertex - start, end
	// Returns: boolean isReachable
	public boolean isReachableBFS(PropertyVertex start, PropertyVertex end) {
		if (start.equals(end))
			return true;
		Queue<PropertyVertex> q = new LinkedList<PropertyVertex>();
		Set<PropertyVertex> active, visited;
		visited = new HashSet<PropertyVertex>();
		Set<PropertyEdge> edges;
		PropertyVertex adjacentVertex, currentVertex, temp;
		currentVertex = start;
		visited.add(currentVertex); // visit first vertex
		q.add(currentVertex); // add first vertex to queue
		while (!q.isEmpty()) { // while queue is not empty
			currentVertex = q.poll(); // poll next vertex
			edges = g.outgoingEdgesOf(currentVertex); // get edges
			for (PropertyEdge e : edges) { // for each edge
				adjacentVertex = g.getEdgeTarget(e); // get adjacent vertex
				if (!visited.contains(adjacentVertex)) { // if not visited
					if (adjacentVertex.equals(end)) // if equal to end node
						return true;
					visited.add(adjacentVertex); // add to visited set
					q.add(adjacentVertex); // add to queue
				}
			}
		}
		return false;
	}

	public ArrayList<PropertyVertex> getPathBFS(PropertyVertex start,
			PropertyVertex end) {
		ArrayList<PropertyVertex> path = new ArrayList<PropertyVertex>();
		if (start.equals(end))
			return path;
		Queue<PropertyVertex> q = new LinkedList<PropertyVertex>();
		Map<PropertyVertex, PropertyVertex> parents;
		parents = new HashMap<PropertyVertex, PropertyVertex>(100);
		ArrayList<PropertyVertex> adjacent;
		PropertyVertex currentVertex, tempVertex;
		int pathLength = 0;
		currentVertex = start;
		parents.put(currentVertex, null); // visit first vertex
		q.add(currentVertex); // add first vertex to queue
		while (!q.isEmpty()) { // while queue is not empty
			currentVertex = q.poll(); // poll next vertex
			adjacent = getAdjacentVertices(currentVertex, OUTGOING); // get edges
			for (PropertyVertex adjacentVertex : adjacent) { // for each edge
				if (!parents.containsKey(adjacentVertex)) { // if not visited
					parents.put(adjacentVertex, currentVertex); // add to
					// visited set
					if (adjacentVertex.equals(end)) // if equal to end node
					{
						return getPath(parents, null, adjacentVertex);
					}
					q.add(adjacentVertex); // add to queue
				}
			}
		}
		return path;
	}

	// Finds (using BFS) length of path between two vertices
	// Parameters: PropertyVertex - start, end
	// Returns: int Length of path, or -1 if no path exists
	public int pathLengthBFS(PropertyVertex start, PropertyVertex end) {
		return getPathBFS(start, end).size() - 1;
	}

	// breathestretchshake
	public boolean isReachableBiBFS(PropertyVertex start, PropertyVertex end) {
		Queue<PropertyVertex> qS = new LinkedList<PropertyVertex>();
		Queue<PropertyVertex> qT = new LinkedList<PropertyVertex>();
		Set<PropertyVertex> visitedS, visitedT;
		visitedS = new HashSet<PropertyVertex>(10000);
		visitedT = new HashSet<PropertyVertex>(10000);
		Set<PropertyEdge> edgesS, edgesT;
		PropertyVertex adjacentVertexS, adjacentVertexT, currentVertexS, currentVertexT;
		currentVertexS = start;
		currentVertexT = end;
		qS.add(currentVertexS);
		qT.add(currentVertexT);
		visitedS.add(currentVertexS);
		visitedT.add(currentVertexT);
		while (!qS.isEmpty() && !qT.isEmpty()) {
			if (!qS.isEmpty()) {
				currentVertexS = qS.poll();
				edgesS = g.outgoingEdgesOf(currentVertexS);
				for (PropertyEdge e : edgesS) {
					adjacentVertexS = g.getEdgeTarget(e);
					if (visitedT.contains(adjacentVertexS))
						return true;
					if (!visitedS.contains(adjacentVertexS)) {
						if (adjacentVertexS.equals(end)) // if equal to end node
							return true;
						visitedS.add(adjacentVertexS); // add to visited set
						qS.add(adjacentVertexS);
					}
				}
			}
			if (!qT.isEmpty()) {
				currentVertexT = qT.poll();
				edgesT = g.incomingEdgesOf(currentVertexT);
				for (PropertyEdge e : edgesT) {
					adjacentVertexT = g.getEdgeSource(e);
					if (visitedS.contains(adjacentVertexT))
						return true;
					if (!visitedT.contains(adjacentVertexT)) {
						if (adjacentVertexT.equals(end)) // if equal to end node
							return true;
						visitedT.add(adjacentVertexT); // add to visited set
						qT.add(adjacentVertexT);
					}
				}
			}
		}
		return false;
	}

	public ArrayList<PropertyVertex> getPathBiBFS(PropertyVertex start,
			PropertyVertex end) {
		Queue<PropertyVertex> qS = new LinkedList<PropertyVertex>();
		Queue<PropertyVertex> qT = new LinkedList<PropertyVertex>();
		Queue<PropertyVertex> qSA = new LinkedList<PropertyVertex>();
		Queue<PropertyVertex> qTA = new LinkedList<PropertyVertex>();
		Map<PropertyVertex, PropertyVertex> parentsS, parentsT;
		parentsS = new HashMap<PropertyVertex, PropertyVertex>(1000);
		parentsT = new HashMap<PropertyVertex, PropertyVertex>(1000);
		Set<PropertyEdge> edgesS, edgesT;
		ArrayList<PropertyVertex> adjacentS = null, adjacentT = null;
		int iteratorS = 0, iteratorT = 0, numNeighborsS = 0, numNeighborsT = 0;
		boolean needNewS = true, needNewT = true;
		PropertyVertex adjacentVertexT, currentVertexS, currentVertexT;
		PropertyVertex adjacentVertexS;
		currentVertexS = start;
		currentVertexT = end;
		qS.add(currentVertexS);
		qT.add(currentVertexT);
		parentsS.put(currentVertexS, null);
		parentsT.put(currentVertexT, null);
		while (!qS.isEmpty() && !qT.isEmpty()) {
			if (needNewS) {
				currentVertexS = qS.poll();
				adjacentS = getAdjacentVertices(currentVertexS, OUTGOING);
				numNeighborsS = adjacentS.size();
				iteratorS = 0;
				needNewS = false;
			}
			if (iteratorS < numNeighborsS) {
				adjacentVertexS = adjacentS.get(iteratorS);
				iteratorS++;
				if (!parentsS.containsKey(adjacentVertexS)) {
//					if (adjacentVertexS.equals(end)) // if equal to end node
//						return getPath(parentsS, parentsT, adjacentVertexS);
					parentsS.put(adjacentVertexS, currentVertexS); // visit
					qS.add(adjacentVertexS);
				}
				if (parentsT.containsKey(adjacentVertexS))
					return getPath(parentsS, parentsT, adjacentVertexS);
			} else {
				needNewS = true;
			}

			if (needNewT) {
				currentVertexT = qT.poll();
				adjacentT = getAdjacentVertices(currentVertexT, INCOMING);
				numNeighborsT = adjacentT.size();
				iteratorT = 0;
				needNewT = false;
			}
			if (iteratorT < adjacentT.size()) {
				adjacentVertexT = adjacentT.get(iteratorT);
				iteratorT++;
				if (!parentsT.containsKey(adjacentVertexT)) {
//					if (adjacentVertexT.equals(end)) // if equal to end node
//						return getPath(parentsS, parentsT, adjacentVertexT);
					parentsT.put(adjacentVertexT, currentVertexT); // visit
					qT.add(adjacentVertexT);
				}
				if (parentsS.containsKey(adjacentVertexT))
					return getPath(parentsS, parentsT, adjacentVertexT);
			} else {
				needNewT = true;
			}

//			currentVertexT = qT.poll();
//			edgesT = g.incomingEdgesOf(currentVertexT);
//			for (PropertyEdge e : edgesT) {
//				adjacentVertexT = g.getEdgeSource(e);
//				if (!parentsT.containsKey(adjacentVertexT)) {
//					parentsT.put(adjacentVertexT, currentVertexT); // visit
//					qT.add(adjacentVertexT);
//				}
//				if (parentsS.containsKey(adjacentVertexT))
//					return getPath(parentsS, parentsT, adjacentVertexT);
//			}
		}
		return new ArrayList<PropertyVertex>();
	}

	public int pathLengthBiBFS(PropertyVertex start, PropertyVertex end) {
		return getPathBiBFS(start, end).size() - 1;
	}

	int countParents(Map<PropertyVertex, PropertyVertex> parents,
			PropertyVertex child) {
		int count = 0;
		PropertyVertex prev;
		while (parents.containsKey(child)) {
			prev = parents.get(child);
			if (prev != null)
				count++;
			child = prev;
		}
		return count;
	}

	/**
	 * Returns the path containing the given vertex and all of its parents and
	 * children
	 *
	 * @param parentsS
	 *            the map containing the leading part of the path
	 * @param parentsT
	 *            the map containing the trailing part of the path
	 * @param child
	 *            the vertex from which to find the path
	 * @return full path
	 */
	ArrayList<PropertyVertex> getPath(
			Map<PropertyVertex, PropertyVertex> parentsS,
			Map<PropertyVertex, PropertyVertex> parentsT, PropertyVertex child) {

		ArrayList<PropertyVertex> path = new ArrayList<PropertyVertex>();
		Stack<PropertyVertex> stk = new Stack<PropertyVertex>();
		PropertyVertex temp = child;
		while (temp != null) {
			stk.push(temp);
			temp = parentsS.get(temp);
		}
		while (!stk.isEmpty()) {
			path.add(stk.pop());
		}
		if (parentsT != null) {
			temp = parentsT.get(child);
			while (temp != null) {
				path.add(temp);
				temp = parentsT.get(temp);
			}
		}
		// System.out.println(path);
		return path;
	}

	ArrayList<PropertyVertex> getAdjacentVertices(PropertyVertex rootV,
			boolean inOut) {
		ArrayList<PropertyVertex> adjSet = new ArrayList<PropertyVertex>();
		Set<PropertyEdge> edgeSet;
		if (inOut == OUTGOING) {
			edgeSet = g.outgoingEdgesOf(rootV);
			for (PropertyEdge e : edgeSet) {
				adjSet.add(g.getEdgeTarget(e));
			}
		} else {
			edgeSet = g.incomingEdgesOf(rootV);
			for (PropertyEdge e : edgeSet) {
				adjSet.add(g.getEdgeSource(e));
			}
		}
		return adjSet;
	}
}
