package org.example;
/**
 * Class to represent the Hub impact, which stores the
 * hub id and impact value.
 */
public class HubImpact {
	private String hubId;
	private double impactValue;
	
	//constructor
	public HubImpact(String hubId, double impactValue) {
		this.hubId = hubId;
		this.impactValue = impactValue;
	}

	//setter and getter functions for attributes
	public void setHubId(String hubId) {
		this.hubId = hubId;
	}
	
	public String getHubId() {
		return hubId;
	}
	
	public void setImpactValue(double impactValue) {
		this.impactValue = impactValue;
	}
	
	public double getImpactValue() {
		return impactValue;
	}
}
