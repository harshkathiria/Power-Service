package org.example;
/**
 * Java class to represent the hubs in a power service system as graph 
 * data structure, each hub will act as vertex in the graph and the
 * connection between one hub to another will be represented as an edge with
 * a weight. This class also provides the functionality to find the
 * monotonic hubs and maximum impact path between source and destination
 * hub.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HubGraph {
	
	//list to store the set of hubs in a given rectangular area 
	private List<DistributionHub> hubsInRect;
	//map to hold hub id as key and list of adjacent hubs as value
	private Map<String, List<String>> adjacencyList;
	//variable to represent the start hub in a given rectangular region
	private DistributionHub startHub;
	//variable to represent the end hub in a given rectangular region
	private DistributionHub endHub;
	//value to represent the slope of the diagonal between start and end hubs
	private double diagonalSlope;
	//Variable to hold set of all possible paths from start hub to end hub 
	private Set<List<DistributionHub>> allPossiblePaths = new HashSet<List<DistributionHub>>();
	// Map to hold the hub data as the value and hub id as the key
	private Map<String, DistributionHub> hubMap = new HashMap<>();

	//constructor
	public HubGraph(List<DistributionHub> hubsInRect, DistributionHub startHub,
			DistributionHub endHub)
	{
		adjacencyList = new HashMap<String, List<String>>();
		this.hubsInRect = hubsInRect;
		this.startHub = startHub;
		this.endHub = endHub;
		this.diagonalSlope = getSlope(startHub, endHub);
		createGraph();
	}

	/**
	 * Function to create the graph with start hub as the starting
	 * vertex and end hub as an ending vertex. The generated graph will
	 * be stored in an adjacency list, where in each vertex will have
	 * weighted edge.
	 */
	private void createGraph() {
		//add edge between start hub and end hub
		addEdge(startHub.getId(), endHub.getId());
		//put start hub to hub map
		hubMap.put(startHub.getId(), startHub);
		//put end hub to hub map
		hubMap.put(endHub.getId(), endHub);
		// iterate the hubs in rectangular area, which is given as list
		for (int i=0; i < hubsInRect.size(); i++) {
			// put current hub to hub map
			hubMap.put(hubsInRect.get(i).getId(), hubsInRect.get(i));
			//add edge between start hub and current hub
			addEdge(startHub.getId(), hubsInRect.get(i).getId());
			//add edge between end hub and current hub
			addEdge(hubsInRect.get(i).getId(), endHub.getId());
			//iterate through all the hubs in rect and edge between current hub and other hubs
			for (int j=0; j < hubsInRect.size(); j++) {
				if (i==j) 
					continue;
				// check if edge can be added 
				if (canAddEdge(hubsInRect.get(i), hubsInRect.get(j)) ) {
					addEdge(hubsInRect.get(i).getId(), hubsInRect.get(j).getId());
				}
			}
		}
	}

	/**
	 * Function to determine the slope between given hubs
	 * slope formula is y2 - y1 / x2 -x1
	 * @param hub1
	 * @param hub2
	 * @return slope as double value
	 */
	private double getSlope(DistributionHub hub1, DistributionHub hub2) {
		int startX=0,endX=0,startY=0, endY=0;
		// check whether hub1 is having lower coordinates than hub2 
		if (hub1.getLocation().getX() < hub2.getLocation().getX()) { 
			startX = hub1.getLocation().getX();
			endX = hub2.getLocation().getX();
			// check if hub1  y coordinates are also lower than hub2
			if (hub1.getLocation().getY() < hub2.getLocation().getY()) {
				//set y1 as hub1 y coordinate
				startY = hub1.getLocation().getY();
				//set y2 as hub2 y coordinate
				endY = hub2.getLocation().getY();
			}
			else {
				//set y1 as hub2 y coordinate
				startY = hub2.getLocation().getY();
				//set y2 as hub1 y coordinate
				endY = hub1.getLocation().getY();
			}
		}
		else {
			startX = hub2.getLocation().getX();
			endX = hub1.getLocation().getX();
			if (hub1.getLocation().getY() < hub2.getLocation().getY()) {
				startY = hub1.getLocation().getY();
				endY = hub2.getLocation().getY();
			}
			else {
				startY = hub2.getLocation().getY();
				endY = hub1.getLocation().getY();
			}
		}
		//determine the slope value
		double slope = (endY- startY) / (endX- startX);
		return slope;
	}

	/**
	 * Function to check whether an edge can be added between
	 * given hubs, if slope is greater than diagonal, then we won't add edge
	 * or else if edge already exists, then we won't add it.
	 * @param hub1
	 * @param hub2
	 * @return true or false
	 */
	private boolean canAddEdge(DistributionHub hub1, DistributionHub hub2) {
		double currentSlope = getSlope(hub1, hub2);
		if (currentSlope > diagonalSlope) {
			return false;
		}
		if (adjacencyList.get(hub2.getId()).contains(hub1.getId())) {
			return false;
		}
		return true;
	}

	/**
	 * Function to add edge between given two vertex i.e. hubs
	 * @param v1
	 * @param v2
	 */
	public void addEdge(String v1, String v2)
	{
		// retrieve the existing adjacent hubs from adjacency list
		List<String> adjHubs = adjacencyList.get(v1);
		//check if nothing exists 
		if (adjHubs == null) {
			//then create new list
			adjHubs = new ArrayList<String>();
		}
		//add the given vertex to it
		adjHubs.add(v2);
		//update the map
		adjacencyList.put(v1, adjHubs);
	}

	/**
	 * Function to return set of monotonic hubs,
	 * where each list will satisfy the condition of 
	 * monotonicity, i.e. it should be either x increasing or decreasing
	 * or y increasing or decreasing, but not both.
	 * @return set of monotonic hubs list
	 */
	public Set<List<DistributionHub>> getMonotonicHubs()
	{
		Map<String,Boolean> visitedMap = new HashMap<String, Boolean>();
		ArrayList<String> finalPathList = new ArrayList<String>();
		finalPathList.add(startHub.getId());
		// generate the paths between start hub and end hub
		generatePaths(startHub.getId(), endHub.getId(), visitedMap, finalPathList);
		//determine the optimal paths 
		Set<List<DistributionHub>> optimalPaths = getOptimalPaths();
		//return it 
		return optimalPaths;
	}

	/**
	 * Helper function to determine the optimal paths between start hub
	 * and end hub. Here optimal path refers to the path which satisfies the
	 * condition of monotonicity.
	 * @return set of monotonic hubs list
	 */
	private Set<List<DistributionHub>> getOptimalPaths() {
		Set<List<DistributionHub>> optimalPaths = new HashSet<List<DistributionHub>>();
		if (allPossiblePaths.isEmpty()) {
			return optimalPaths;
		}

		// iterate through all paths
		for (List<DistributionHub> set : allPossiblePaths) {
			if (set.size()<2) {
				continue;
			}
			int xLeft = 0;
			int xRight = 0;
			int yUp = 0;
			int yDown = 0;
			Point startPoint = set.get(0).getLocation();
			Point endPoint = null;
			//iterate through path
			for (int i=1 ; i < set.size(); i++) {
				endPoint = set.get(i).getLocation();
				//count the directions taken by the path
				if (endPoint.getX() < startPoint.getX()) {
					xLeft++;
				}
				else {
					xRight++;
				}
				if (endPoint.getY() < startPoint.getY()) {
					yDown++;
				}
				else {
					yUp++;
				}
				startPoint = endPoint;
			}
			
			// if path leads in all directions, then its not monotonic
			// ignore such paths
			if (xLeft >0 && xRight>0 && yUp >0 && yDown >0) {
				continue;
			}
			else { // otherwise add to result
				optimalPaths.add(set);
			}
		}
		return optimalPaths;
	}

	/**
	 * Function to generate the paths' given vertices i.e. hubs
	 * @param v1
	 * @param v2
	 * @param visitedMap
	 * @param finalPathList
	 */
	private void generatePaths(String v1, String v2,
			Map<String,Boolean> visitedMap,
			List<String> finalPathList)
	{

		// if both vertices are same, then there exists
		// a path , hence add to all possible paths list
		if (v1.equals(v2)) {
			List<DistributionHub> result = new ArrayList<>();
			for(String entry : finalPathList) {
				DistributionHub h = hubMap.get(entry);
				result.add(h);
			}
			allPossiblePaths.add(result);
			return;
		}
		// make the vertex as visited
		visitedMap.put(v1, true);
		// iterate through the adj list and check for unvisited vertices
		for (String i : adjacencyList.get(v1)) {
			if (visitedMap.get(i) ==null) {
				finalPathList.add(i);
				generatePaths(i, v2, visitedMap, finalPathList);
				finalPathList.remove(i);
			}
		}
		//update the map
		visitedMap.put(v1, false);
	}

}