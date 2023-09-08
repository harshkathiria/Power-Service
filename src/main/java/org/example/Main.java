package org.example;

/**
 * Java program to simulate the power service system,
 * where each hub will contain set of postal codes 
 * and each postal code will have certain amount of population.
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {
	private static Scanner scanner;
	private static PowerService powerService;

	/**
	 * Function to display the menu
	 */
	private static void showMenu() {
		System.out.println("---------------------------------------");
		System.out.println("Menu");
		System.out.println("---------------------------------------");
		System.out.println("0.Add Postal Codes");
		System.out.println("1.Create New Hub");
		System.out.println("2.Set Hub Status");
		System.out.println("3.Show most significant hubs");
		System.out.println("4.Set service restoration rate");
		System.out.println("5.Show People Out of Service");
		System.out.println("6.Show Most Damaged Postal Codes");
		System.out.println("7.Schedule a repair plan");
		System.out.println("8.Under served  Postal By population");
		System.out.println("9.Under served Postal By Area");
		System.out.println("10.Quit\n");
		System.out.print("Enter your choice:");
		
	}

	//DRIVER CODE
	public static void main(String[] args) {
		int choice ;
		System.out.println("----------------------------------");
		System.out.println("Welcome To Power Service System");
		System.out.println("----------------------------------");
		scanner = new Scanner(System.in);
		powerService = new PowerService();
		//load existing details from file hubs.txt and postalcodes.txt
		loadPostalCodes();
		loadDistributionHubs();
		
		// loop to handle the simulation 
		while(true) {
			showMenu();
			choice = scanner.nextInt();
			if (choice == 10)
				break;
			switch (choice) {

			case 0: {
				addPostalCodes();
				break;
			}

			case 1: {
				createNewHub();
				break;
			}
			case 2: {
				setHubStatus();
				break;
			}
			case 3: {
				showMostSigHubs();
				break;
			}
			case 4: {
				showServiceRestoration();
				break;
			}
			case 5: {
				showOutOfService();
				break;
			}
			case 6: {
				showMostDamaged();
				break;
			}
			case 7: {
				scheduleRepairPlan();
				break;
			}
			case 8: {
				underServedByPopulation();
				break;
			}
			case 9: {
				underServedByArea();
				break;
			}
			default:
				System.out.println("Invalid choice!");
				break;
			}
		}
		powerService.saveToFile();
		System.out.println("Thank you!");
	}

	/**
	 * Function to load distribution hubs from hubs.txt
	 */
	private static void loadDistributionHubs() {
		try {
			Scanner fileScanner = new Scanner(new File("hubs.txt"));
			int count = 0;
			//read the file line by line
			while(fileScanner.hasNext()) {
				String line = fileScanner.nextLine();
				if (line.isEmpty()) {
					continue;
				}
				String[] data = line.split(",");
				if (data.length <3) {
					System.out.println("Invalid Hub details..\n"
							+ "Quitting application");
					System.exit(0);
				}
				String id =  data[0].strip();
				int x =  (int)Float.parseFloat(data[1].strip());
				int y  = (int)Float.parseFloat(data[2].strip());
				double area  = Double.parseDouble(data[3].strip());
				float repairHours  = Float.parseFloat(data[4].strip());
				int impactValue  = (int)Float.parseFloat(data[5].strip());
				
				Set<String> areas = new HashSet<>();
				for (int i=6; i < data.length; i++) {
					String code = data[i].strip();
					areas.add(code);
				}
				//add to list
				powerService.addDistributionHub(id, new Point(x,y), areas);
				powerService.hubDamage(id, repairHours);
				powerService.addHubImpact(id, impactValue);
			}
			System.out.println("Successfully loaded hub details.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function to add new postal code,
	 * by prompting the user
	 */
	private static void addPostalCodes() {
		System.out.println("please enter id: ");
		String id = scanner.next();
		System.out.println("please enter Population: ");
		int numPeople = scanner.nextInt();
		System.out.println("please enter Area covered: ");
		int area = scanner.nextInt();
		boolean result = powerService.addPostalCode(id, numPeople, area);
		if (result) {
			System.out.println("Added successfully.");
		}
		else {
			System.out.println("Updated existing successfully.");
		}
	}
	
	/**
	 * Function to lad postal codes from file.
	 */
	private static void loadPostalCodes() {
		try {
			Scanner fileScanner = new Scanner(new File("postal_codes.txt"));
			int count = 0;
			// read eac line of file
			while(fileScanner.hasNext()) {
				String line = fileScanner.nextLine();
				if (line.isEmpty()) {
					continue;
				}
				
				String[] data = line.split(",");
				if (data.length !=4) {
					System.out.println("Invalid Postal codes..\n"
							+ "Quitting application");
					System.exit(0);
				}
				String id =  data[0].strip();
				int np =  (int)Float.parseFloat(data[1].strip());
				int area  = (int)Float.parseFloat(data[2].strip());
				int numRepairs  = (int)Float.parseFloat(data[3].strip());
				powerService.addPostalCode(id, np, area);
				powerService.addDamagedHub(id,numRepairs);
				count++;
			}
			System.out.println("Successfully loaded postal codes.");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function to determine the under served postal codes by area
	 */
	private static void underServedByArea() {
		System.out.println("please enter the limit: ");
		int limit = scanner.nextInt();
		List<String> areas = powerService.underservedPostalByArea(limit);
		int i = 1;
		System.out.println("--------------------------");
		System.out.println("Under served by Area");
		System.out.println("--------------------------");
		for(String area : areas) {
			System.out.println( i + ". " +area);
			i++;
		}
	}


	/**
	 * Function to determine the under served postal codes by population
	 */
	private static void underServedByPopulation() {
		System.out.println("please enter the limit: ");
		int limit = scanner.nextInt();
		List<String> areas = powerService.underservedPostalByPopulation(limit);
		int i = 1;
		System.out.println("--------------------------");
		System.out.println("Under served by Population");
		System.out.println("--------------------------");
		for(String area : areas) {
			System.out.println( i + ". " +area);
			i++;
		}
	}

	/**
	 * Helper function to schedule the repair plan
	 */
	private static void scheduleRepairPlan() {
		//prompt the user to enter details
		System.out.println("please enter the start hub id: ");
		String startHub = scanner.next();
		System.out.println("please enter maximum distance to be covered: ");
		int maxDistance = scanner.nextInt();
		System.out.println("please enter maximum time: ");
		float maxTime = scanner.nextFloat();
		//schedule a repair plan
		List<HubImpact> result = powerService.repairPlan(startHub, maxDistance, maxTime);
		System.out.println("Repair  plan");
		//print the most optimal path
		for (HubImpact impact : result) {
			System.out.print(" > " + impact.getHubId());
		}
		System.out.println("\n");
	}

	/**
	 * Function to display the most damaged hubs
	 */
	private static void showMostDamaged() {
		//prompt for limit
		System.out.println("Please enter the limit: ");
		int limit = scanner.nextInt();
		//use helper function to do the task
		List<DamagedPostalCodes> codes = powerService.mostDamagedPostalCodes(limit);
		if (codes.isEmpty()) {
			return;
		}
		//print the result
		System.out.println("------------------------------------");
		System.out.println("PostalCode | HoursToRepair");
		System.out.println("------------------------------------");
		for (DamagedPostalCodes code : codes) {
			System.out.println("    " +code.getPostalCode() + "    " + code.getNumberOfRepairs());
		}
	}

	/**
	 * Function to show hubs out of service
	 */
	private static void showOutOfService() {
		//prompt for hub id
		System.out.println("Please enter hub id: ");
		String hubId = scanner.next();
		// retrieve the hub from map
		DistributionHub hub  = powerService.getHub(hubId);
		//use utility function to do the task
		int os = powerService.peopleOutOfService(hub);
		//print result
		System.out.println("Number of people out of service are " + os);
	}

	/**
	 * Function to display the restoration rates in table format
	 */
	private static void showServiceRestoration() {
		System.out.println("Please enter the restoration rate(%): ");
		float rate = scanner.nextFloat();
		List<Integer> restores =  powerService.rateOfServiceRestoration(rate);
		System.out.println("-----------------");
		System.out.println("Restoration Table");
		System.out.println("-----------------");
		System.out.println("Hours    Recovered%");
		for (int i=0;i < restores.size(); i++) {
			System.out.println("  " +i + "      " +restores.get(i));
		}
	}

	/**
	 * Function to show the most significant hubs
	 */
	private static void showMostSigHubs() {
		System.out.println("Please enter the limit to show: ");
		int limit = scanner.nextInt();
		List<HubImpact> impacts = powerService.fixOrder(limit);
		System.out.println("Most significant hubs to be fixed\n");
		for (HubImpact impact : impacts) {
			System.out.println(impact.getHubId());
		}
	}

	/**
	 * Function to set the hub status by prompting the 
	 * user to enter details
	 */
	private static void setHubStatus() {
		System.out.println("Please enter hub id: ");
		String hubId = scanner.next();
		System.out.println("Please enter employee id: ");
		String employee = scanner.next();
		System.out.println("Please enter repair time: ");
		float repairTime = scanner.nextFloat();
		System.out.println("Is repair over ? (y/n) : ");
		String inService = scanner.next();
		boolean inFlag = false;
		if (inService.equals("y")) {
			inFlag = true;
		}
		powerService.hubRepair(hubId, employee, repairTime, inFlag);
	}

	/**
	 * Function to create a new hub by prompting the user to enter details
	 */
	private static void createNewHub() {
		//prompt for details
		System.out.println("please enter id: ");
		String id = scanner.next();
		System.out.println("please enter coordinates (x,y): ");
		String coords = scanner.next();
		String[] xy = coords.split(",");
		int x = Integer.parseInt(xy[0]);
		int y = Integer.parseInt(xy[1]);
		Set<String> areas = new HashSet<>();
		while(true) {
			System.out.println("Enter serviced postal area code: ");
			String area = scanner.next();
			areas.add(area);
			System.out.println("Do you want to add more (y/n)? ");
			String choice = scanner.next();
			if (choice.equals("n")) {
				break;
			}
		}
		//add to existing list
		boolean result = powerService.addDistributionHub(id, new Point(x,y), areas);
		if (result) {
			System.out.println("Added successfully.");
		}
		else {
			System.out.println("Updated existing successfully.");
		}
	}
}
