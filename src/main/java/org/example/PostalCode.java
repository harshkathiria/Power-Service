package org.example;
/**
 * Java class to represent the
 * Postal code , which contains unique id,
 * population, area covered by postal code.
 */
public class PostalCode {
	//attributes
	private String id;
	private int numPeople;
	private double areaCovered;
	
	//constructors
	public PostalCode() {
		
	}

	public PostalCode(String id, int numPeople,
			double areaCovered) {
		this.id = id;
		this.numPeople = numPeople;
		this.areaCovered = areaCovered;
	}

	//setter and getter functions for attributes
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getNumPeople() {
		return numPeople;
	}

	public void setNumPeople(int numPeople) {
		this.numPeople = numPeople;
	}

	public double getAreaCovered() {
		return areaCovered;
	}

	public void setAreaCovered(double areaCovered) {
		this.areaCovered = areaCovered;
	}
	
	
}
