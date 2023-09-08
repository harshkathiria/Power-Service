package org.example;

/**
 * java class to represent the power service system, 
 * and its various operations including adding a new hub,
 * determining the out of service hubs,schedule a repair plan,
 * determine the restoration rates, etc.
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PowerService {
	// variable to hold all the postal codes
	private Map<String, PostalCode> totalPostalCodes = new HashMap<String, PostalCode>();
	//variable to hold all the hubs
	private Map<String, DistributionHub> distributionHubs = 
			new HashMap<String, DistributionHub>();
	//variable to hold all hub impacts
	private Map<String,HubImpact> hubImpacts = new HashMap<String,HubImpact>();
	// variable to hold all damaged postal codes
	private List<DamagedPostalCodes> damagedPostalCodes = new ArrayList<DamagedPostalCodes>();

	//map to hold the postal code name to hub
	private Map<String, List<String>> postalCodeToHub = 
			new HashMap<String, List<String>>();
	//Map to hold the employee details
	private Map<String, List<String>> employeeMap = new HashMap<String, List<String>>();

	/**
	 * Function to add new postal code to existing list
	 * @param postalCode
	 * @param population
	 * @param area
	 * @return true, if added successfully, else false.
	 */
	public boolean addPostalCode (String postalCode, 
			int population, int area ) {
		//check for invalid inputs
		if (postalCode == null || postalCode.equals("")) {
			System.out.println("Postal code cannot be null or empty");
			return false;
		}

		if (population <= 0|| area <=0) {
			System.out.println("Population or area should be positive integer.");
			return false;
		}

		PostalCode pCode = totalPostalCodes.get(postalCode);
		// code exists, then update it
		if(pCode != null) {
			pCode.setNumPeople(population);
			pCode.setAreaCovered(area);
			totalPostalCodes.put(postalCode, pCode);
			return true;
		}
		else {
			//else add as new code
			pCode = new PostalCode(postalCode, population, area);
			totalPostalCodes.put(postalCode, pCode);
			return true;
		}
	}

	/**
	 * @param hubIdentifier
	 * @param location
	 * @param servicedAreas
	 * @return true or false
	 */
	public boolean addDistributionHub(String hubIdentifier,Point location,
			Set<String> servicedAreas) {
		//check for invalid inputs
		if (hubIdentifier == null || hubIdentifier.equals("")) {
			System.out.println("Hub id cannot be null or empty");
			return false;
		}

		if (location == null || servicedAreas == null) {
			System.out.println("Location or serviced aread cannot be null");
			return false;
		}

		DistributionHub hub = distributionHubs.get(hubIdentifier);
		Set<PostalCode> areas = getServedCodes(servicedAreas);
		boolean result = false;
		//if not exists already then add as new
		if (hub == null) {
			hub = new DistributionHub(hubIdentifier, location, areas);
			distributionHubs.put(hubIdentifier, hub);
			result  = true;
		}
		else {// else update
			hub.setLocation(location);
			hub.setPostalCodesServed(areas);
			distributionHubs.put(hubIdentifier, hub);
			result = false;
		}

		for (String  area : servicedAreas) {
			List<String> hubs =  postalCodeToHub.get(area);
			if (hubs == null) {
				hubs = new ArrayList<String>();
			}
			hubs.add(hub.getId());
			postalCodeToHub.put(area, hubs);
		}
		return result;
	}

	/**
	 * Function to return the postal code objects from given
	 * list of postal code ids
	 * @param servicedAreas
	 * @return set of postal code objects
	 */
	private Set<PostalCode> getServedCodes(Set<String> servicedAreas) {
		Set<PostalCode> codes = new HashSet<PostalCode> ();
		for (String c : servicedAreas) {
			codes.add(totalPostalCodes.get(c));
		}
		return codes;
	}

	/**
	 * Function to set the hub as damaged and print the
	 * employee details
	 * @param hubIdentifier
	 * @param repairEstimate
	 */
	public void hubDamage(String hubIdentifier, float repairEstimate) {
		//check for invalid inputs
		if (hubIdentifier == null || hubIdentifier.equals("")) {
			System.out.println("Hub id cannot be null or empty");
			return;
		}

		if (repairEstimate <=0) {
			System.out.println("Repair estimate should be positive");
			return;
		}
		
		
		DistributionHub hub = distributionHubs.get(hubIdentifier);

		
		//update the details to hub
		if (hub!=null) {
			hub.setHoursToRepair(repairEstimate);
			HubImpact hubImpact = hubImpacts.get(hubIdentifier);
			if (hubImpact == null) 
				hubImpact = new HubImpact(hubIdentifier, 0);
			hubImpacts.put(hubIdentifier, hubImpact);
		}
		else {
			System.out.println("Hub id does not exists!");
		}
	}

	/**
	 * Function to repair a particular hub, with given employee
	 * 
	 * @param hubIdentifier
	 * @param employeeId
	 * @param repairTime
	 * @param inService
	 */
	public void hubRepair(String hubIdentifier, String employeeId, 
			float repairTime, boolean inService ) {
		//check for invalid inputs
		if (hubIdentifier == null || hubIdentifier.equals("")) {
			System.out.println("Hub id cannot be null or empty");
			return;
		}

		if (repairTime <=0) {
			System.out.println("Repair estimate should be positive");
			return;
		}
		
		if (employeeId == null || employeeId.equals("")) {
			System.out.println("Employee id cannot be null or empty");
			return;
		}
		
		if (distributionHubs.get(hubIdentifier) == null) {
			System.out.println("Hub id does not exists");
			return;
		}
		
		List<String> hubList  = employeeMap.get(employeeId);
		if (hubList == null) {
			hubList = new ArrayList<>();
		}
		hubList.add(hubIdentifier);
		employeeMap.put(employeeId, hubList);
		// if its in service, then print the message 
		if (inService) {
			System.out.println("Employee " + employeeId + " has done repair for " 
					+ repairTime + " in hub id " + hubIdentifier);
			hubDamage(hubIdentifier, repairTime);
		}
		else { // else consider it as damaged
			hubDamage(hubIdentifier, repairTime);
			System.out.println("Hub id " + hubIdentifier + " is being repaired by "+ employeeId);
		}
	}

	/**
	 * Function to determine the people out of service for all hubs
	 * @return integer
	 */
	public int peopleOutOfService () {
		int outOfService = 0;
		for (DistributionHub hub : distributionHubs.values()) {
			if (hub.getHoursToRepair() > 0) {
				for (PostalCode code : hub.getPostalCodesServed()) {
					outOfService += code.getNumPeople();
				}
			}
		}
		return outOfService;
	}

	/**
	 * Function to determine the people out of service for given hub
	 * @return integer 
	 */
	public int peopleOutOfService(DistributionHub hub) {
		if (hub == null) {
			System.out.println("Hub id does not exists");
			return 0;
		}
		
		int outOfService = 0;
		if (hub.getHoursToRepair() > 0) {
			for (PostalCode code : hub.getPostalCodesServed()) {
				outOfService += code.getNumPeople();
			}
		}
		return outOfService;
	}

	/**
	 * FUnction to return the hub object for given hub id
	 * @param hubId
	 * @return hub object
	 */
	public DistributionHub getHub(String hubId) {
		return distributionHubs.get(hubId);
	}

	/**
	 * Function to print the most important hubs to be fixed
	 * @param limit
	 * @return list of hubs
	 */
	public List<HubImpact> fixOrder (int limit) {
		List<HubImpact> result = new ArrayList<>();
		if (limit <=0) {
			System.out.println("Limit should be positive");
			return result;
		}
		
		Collection<HubImpact> impacts = hubImpacts.values();
		List<HubImpactPopulation> impPopList = new ArrayList<HubImpactPopulation>();
		for (HubImpact imp : impacts) {
			DistributionHub hub = distributionHubs.get(imp.getHubId());
			int os = peopleOutOfService(hub);
			// if impact value is positive, then hub is damaged 
			if (os > 0) {
				HubImpactPopulation hip = new HubImpactPopulation(imp, os);
				impPopList.add(hip);
			}
		}

		//sort the list
		Collections.sort(impPopList);
		
		//retrieve for given limit
		for (int i=0; i<limit; i++) {
			result.add(i, impPopList.get(i).getImpact());
		}
		return result;
	}

	/**
	 * Funciton to determine the most damaged postal codes
	 * @param limit
	 * @return list of codes
	 */
	public  List<DamagedPostalCodes> mostDamagedPostalCodes(int limit) {
		List<DamagedPostalCodes> mostDamaged = new ArrayList<DamagedPostalCodes>();
		if (limit <=0) {
			System.out.println("Limit should be positive");
			return mostDamaged;
		}
		
		if (damagedPostalCodes.isEmpty()) {
			System.out.println("There are no damaged postal codes");
			return damagedPostalCodes;
		}

		//sort the codes
		Collections.sort(damagedPostalCodes);
		

		//set the limit
		if (limit> damagedPostalCodes.size()) {
			limit = damagedPostalCodes.size();
		}

		//determine most damaged
		for (int i=0; i<limit; i++) {
			if (damagedPostalCodes.get(i).getNumberOfRepairs() > 0)
				mostDamaged.add(damagedPostalCodes.get(i));
		}
		return mostDamaged;
	}

	/**
	 * Function to print the rate of restoration for 
	 * given percentage of increment
	 * @param increment
	 * @return list of hours 
	 */
	public  List<Integer> rateOfServiceRestoration(float increment) {
		List<Integer> percents = new ArrayList<Integer>();
		if (increment <=0) {
			System.out.println("Increment should be a positive integer");
			return percents;
		}
		
		float currentPercentage = 0;
		float initial = increment / 100; 
		int i=0;
		//generate the percentage for various hours  
		while(true) {
			if (currentPercentage>1) {
				break;
			}
			int percent = (int)(currentPercentage * 100);
			percents.add(i, percent);
			currentPercentage += initial;
			i++;
		}
		//return result
		return percents;
	}


	/**
	 * Function to schedule the repair plan 
	 * for given start hub and optimal end hub
	 * 
	 * @param startHub
	 * @param maxDistance
	 * @param maxTime
	 * @return path as list of hubs
	 */
	public List<HubImpact> repairPlan(String startHub, int maxDistance, 
			float maxTime ) {
		//variable to hold the result hub impacts
		List<HubImpact> result = new ArrayList<>();

		if (startHub == null || startHub.equals("")) {
			System.out.println("Start hub cannot be null or empty");
			return result;
		}
		
		if (maxDistance<=0 || maxTime <=0) {
			System.out.println("Maximum distance or time cannot be negative");
			return result;
		}
		
		//start hub 
		DistributionHub start  = distributionHubs.get(startHub);
		if (start == null) {
			System.out.println("Start hub does not exists");
			return result;
		}

		//determine hubs in range	
		List<DistributionHub> hubsInRange = getHubsInRange(start, maxDistance, maxTime);

		//determine max impact hub, and set it as end hub
		DistributionHub endHub = getMaxImpactHub(hubsInRange);
		if (startHub.equals(endHub)) {
			result.add(hubImpacts.get(startHub));
			return result;
		}

		//remove end hub from list of hubs, so that we can 
		//determine the remaining hubs in rectangle
		hubsInRange.remove(endHub);
		List<DistributionHub> hubsInRect = getHubsInRect(start, endHub, hubsInRange);

		//Generate graph for hubs in rectangle, hence we can
		//determine the path for repair plan
		HubGraph hubGraph = new HubGraph(hubsInRect, start, endHub);

		// get the monotonic hubs within the rectangular region
		Set<List<DistributionHub>> monotonicHubs = hubGraph.getMonotonicHubs();

		//determine the optimal set of hubs from monotonic hubs
		int optimalSet = -1;
		double maxImpact = 0;
		Object[] hsets = monotonicHubs.toArray();
		for (int l=0; l< monotonicHubs.size(); l++) {
			double totalImpact = 0;
			List<DistributionHub> hubs= (List<DistributionHub>)hsets[l];
			for (DistributionHub h : hubs) {
				HubImpact impact = hubImpacts.get(h.getId());
				totalImpact += impact.getImpactValue();
			}
			if (totalImpact > maxImpact) {
				maxImpact = totalImpact;
				optimalSet = l;
			}
		}
		List<HubImpact> optimalImpacts = new ArrayList<>();
		if (optimalSet >= 0) {
			List<DistributionHub> optimalHubs = (List<DistributionHub>)hsets[optimalSet];
			for (DistributionHub dh : optimalHubs) {
				optimalImpacts.add(hubImpacts.get(dh.getId()));
			}
		}
		else {
			optimalImpacts.add(hubImpacts.get(start.getId()));
			optimalImpacts.add(hubImpacts.get(endHub.getId()));
		}

		return optimalImpacts;
	}

	/**
	 * Function to determine the list of hubs in a 
	 * rectangular region
	 * 
	 * @param startHub
	 * @param endHub
	 * @param hubsInRange
	 * @return list of hubs
	 */
	private List<DistributionHub> getHubsInRect(DistributionHub startHub, DistributionHub endHub,
			List<DistributionHub> hubsInRange) {
		List<DistributionHub> hubs = new ArrayList<DistributionHub> ();
		int startX,endX,startY, endY;
		//determine the start hub and end hub by their coordinates
		if (startHub.getLocation().getX() < endHub.getLocation().getX()) {
			startX = startHub.getLocation().getX();
			endX = endHub.getLocation().getX();
			if (startHub.getLocation().getY() < endHub.getLocation().getY()) {
				startY = startHub.getLocation().getY();
				endY = endHub.getLocation().getY();
			}
			else {
				startY = endHub.getLocation().getY();
				endY = startHub.getLocation().getY();
			}
		}
		else {
			startX = endHub.getLocation().getX();
			endX = startHub.getLocation().getX();
			if (startHub.getLocation().getY() < endHub.getLocation().getY()) {
				startY = startHub.getLocation().getY();
				endY = endHub.getLocation().getY();
			}
			else {
				startY = endHub.getLocation().getY();
				endY = startHub.getLocation().getY();
			}
		}

		// check whether coordinated of other hubs lies within the range
		for (DistributionHub hub : hubsInRange) {
			if (hubImpacts.get(hub.getId()) != null) {
				Point p1 = hub.getLocation();
				if (p1.getX()>=startX && p1.getX() <=endX 
						&& p1.getY()>=startY && p1.getY() <=endY) {
					hubs.add(hub);
				}
			}
		}
		return hubs;
	}

	/**
	 * Function to determine the list of under served postal codes
	 * by population
	 * @param limit
	 * @return postal codes list
	 */
	public List<String> underservedPostalByPopulation ( int limit ) {
		List<String> underServed = new ArrayList<String>();
		List<PostalCodeServe> servedRates = new ArrayList<>();
		if (limit <=0) {
			System.out.println("Limit should be positive");
			return underServed;
		}
		//iterate through the list to determine the under served
		for (String area : postalCodeToHub.keySet()) {
			List<String> hubs = postalCodeToHub.get(area);
			if (hubs!=null) {
				double res = (double) hubs.size() / totalPostalCodes.get(area).getNumPeople();
				servedRates.add(new PostalCodeServe(area, res));
			}
		}

		//sort list
		Collections.sort(servedRates);
		int i = 0;
		//for the given limit, generate the under served
		for (PostalCodeServe s : servedRates ) {
			if (i>=limit)
				break;
			underServed.add(s.getId());
			i++;
		}

		return underServed;
	}

	/**
	 * Function to determine the list of under served postal codes
	 * by area
	 * @param limit
	 * @return postal codes list
	 */
	public List<String> underservedPostalByArea ( int limit ) {
		List<String> underServed = new ArrayList<String>();
		List<PostalCodeServe> servedRates = new ArrayList<>();
		//check input values 
		if (limit <=0) {
			System.out.println("Limit should be positive");
			return underServed;
		}
		
		for (String area : postalCodeToHub.keySet()) {
			List<String> hubs = postalCodeToHub.get(area);
			if (hubs!=null) {
				double res = (double) hubs.size() / totalPostalCodes.get(area).getAreaCovered();
				servedRates.add(new PostalCodeServe(area, res));
			}
		}

		Collections.sort(servedRates);
		
		int i = 0;
		for (PostalCodeServe s : servedRates ) {
			if (i>=limit)
				break;
			underServed.add(s.getId());
			i++;
		}

		return underServed;
	}

	/**
	 * Function to determine the maximum impact hub
	 * @param hubsInRange
	 * @return hub 
	 */
	private DistributionHub getMaxImpactHub(List<DistributionHub> hubsInRange) {
		DistributionHub maxHub = hubsInRange.get(0);
		HubImpact maxImpact = hubImpacts.get(maxHub.getId());
		double maxImpactValue = (maxImpact!=null)? maxImpact.getImpactValue() : 0; 
		for (DistributionHub hub : hubsInRange) {	
			HubImpact impact2 = hubImpacts.get(hub.getId());
			if (impact2 == null) {
				continue;
			}

			if (impact2.getImpactValue() > maxImpactValue) {
				maxImpactValue = impact2.getImpactValue();
				maxImpact = impact2;
			}
		}

		return distributionHubs.get(maxImpact.getHubId());
	}

	/**
	 * Function to determine the hubs in given range
	 * of distance and maximum time to repair
	 *  
	 * @param startHub
	 * @param maxDistance
	 * @param maxTime
	 * @return list of hubs
	 */
	private List<DistributionHub> getHubsInRange(DistributionHub startHub, int maxDistance, float maxTime) {
		List<DistributionHub> hubs = new ArrayList<DistributionHub> ();
		Point location1 = startHub.getLocation();
		for (DistributionHub hub : distributionHubs.values()) {
			if (startHub.equals(hub)) {
				continue;
			}
			Point location2 =  hub.getLocation();
			float distance = getDistance(location1, location2);
			if (distance <= maxDistance && hub.getHoursToRepair() <= maxTime) {
				hubs.add(hub);
			}
		}
		return hubs;
	}

	/**
	 * Function to determine the distance between 
	 * given two points , the formula is 
	 * sqrt ((x2-x1)^2 + (y2-y1)^2)
	 * 
	 * @param location1
	 * @param location2
	 * @return distance as float
	 */
	private float getDistance(Point location1, Point location2) {
		int x1,y1,x2,y2;
		x1 = location1.getX();
		y1 = location1.getY();
		x2 = location2.getX();
		y2 = location2.getY();

		int xsq = (x2-x1) * (x2-x1);
		int ysq = (y2-y1) * (y2-y1);
		return (float) Math.sqrt(xsq + ysq);
	}

	/**
	 * Function to add new hub impact value to list
	 * @param id
	 * @param impactValue
	 */
	public void addHubImpact(String id, int impactValue) {
		HubImpact impact = new HubImpact(id, impactValue);
		hubImpacts.put(id, impact);
	}

	/**
	 * Function to add given hub to damaged hub list
	 * @param id
	 * @param numRepairs
	 */
	public void addDamagedHub(String id, int numRepairs) {
		DamagedPostalCodes dm = new DamagedPostalCodes(id, numRepairs);
		damagedPostalCodes.add(dm);
	}

	/**
	 * Function to save the existing status of hubs
	 * and postal codes to hubs.txt and postal_codes.txt
	 */
	public void saveToFile() {
		try {
			BufferedWriter codeWriter = new BufferedWriter(new FileWriter("postal_codes.txt"));
			BufferedWriter hubWriter = new BufferedWriter(new FileWriter("hubs.txt"));

			for (PostalCode pcode : totalPostalCodes.values()) {
				float repairCount = getRepairCount(pcode.getId());
				String result = pcode.getId() +","+pcode.getNumPeople()+","+
						pcode.getAreaCovered()+","+repairCount+"\n";
				codeWriter.write(result);
			}

			for (DistributionHub dhub : distributionHubs.values()) {
				double impactValue = 0;
				if (hubImpacts.get(dhub.getId()) != null) {
					impactValue = hubImpacts.get(dhub.getId()).getImpactValue();
				}
				String result = dhub.getId() +","+dhub.getLocation().getX()+","+
						dhub.getLocation().getY()+","+dhub.getAreaCovered()
						+","+dhub.getHoursToRepair()
						+","+impactValue+","+ getCodesServed(dhub.getId())
						+"\n";
				hubWriter.write(result);
			}
			codeWriter.close();
			hubWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Function to return the codes served by a hub
	 * as comma separated string 
	 * @param id
	 * @return
	 */
	private String  getCodesServed(String id) {
		DistributionHub hub = distributionHubs.get(id);
		String result = "";
		int i =0, size;
		size = hub.getPostalCodesServed().size();
		//iterate through the list
		for (PostalCode pcode:hub.getPostalCodesServed()) {
			if (pcode == null) {
				continue;
			}
			//construct the result
			result +=pcode.getId();
			if (i!=size-1) {
				result+=",";
			}
			i++;
		}
		//return result
		return result;
	}

	/**
	 * Function to determine the repair count for given
	 * postal code id 
	 * @param id
	 * @return repair count as integer
	 */
	private float getRepairCount(String id) {
		for (DamagedPostalCodes dcode : damagedPostalCodes) {
			if (dcode.getPostalCode().equals(id)) {
				return dcode.getNumberOfRepairs();
			}
		}
		return 0;
	}
}
