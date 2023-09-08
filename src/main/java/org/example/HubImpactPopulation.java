package org.example;
/**
 * Class to represent the Hub impact Population, which stores the 
 * hub impact object and total population covered by the hub.
 */
public class HubImpactPopulation implements Comparable<HubImpactPopulation> {
	//attributes
	private HubImpact impact;
	private int population;
	
	//constructors
	public HubImpactPopulation() {
	}
	
	public HubImpactPopulation(HubImpact impact, int population) {
		this.impact = impact;
		this.population = population;
	}

	//setter and getter functions for attributes
	public HubImpact getImpact() {
		return impact;
	}

	public void setImpact(HubImpact impact) {
		this.impact = impact;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}

	/**
	 * Function to compare objects using population
	 */
	@Override
	public int compareTo(HubImpactPopulation other) {
		if (population < other.population) {
			return -1;
		}
		else if (population > other.population) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
