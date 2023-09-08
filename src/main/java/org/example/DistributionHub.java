package org.example;
/**
 * Java class to represent the distribution hub in 
 * a power service system, where each will have
 * a unique id, location as co-ordinates, area covered
 * set of postal codes served by hub and total number of
 * hours required to repair the hub.
 */

import java.util.Set;

public class DistributionHub {
	//attributes
	private String id;
	private Point location;
	private double areaCovered;
	private Set<PostalCode> postalCodesServed;
	private double hoursToRepair;

	//constructor
	public DistributionHub(String id, Point location, Set<PostalCode> served) {
		//set the attributes
		this.id = id;
		this.location = location;
		postalCodesServed = served;
		hoursToRepair = 0;
		calculateAreaCovered();
	}
	
	/** Function to calculate the area covered by hub.
	 * It is the summation of all postal code areas.
	 */
	private void calculateAreaCovered() {
		//iterate through the served postal codes
		for (PostalCode pcode : postalCodesServed) {
			if (pcode == null) {
				continue;
			}

			//add the current postal code area to 
			//total area covered
			areaCovered += pcode.getAreaCovered();
		}
	}

	/**
	 * Function to return the id 
	 * @return id as String 
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Function to set the id
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Function to return the location
	 * @return location as Point object
	 */
	public Point getLocation() {
		return location;
	}
	
	/**
	 * Function to set the location
	 * @param location
	 */
	public void setLocation(Point location) {
		this.location = location;
	}
	
	/**
	 * Function to return the total area covered
	 * by a hub
	 * @return area covered as double
	 */
	public double getAreaCovered() {
		return areaCovered;
	}
	
	/**
	 * Function to the set the area covered
	 * @param areaCovered
	 */
	public void setAreaCovered(double areaCovered) {
		this.areaCovered = areaCovered;
	}
	
	
	/**
	 * Function to set the postal codes under this hub
	 * @param postalCodesServed
	 */
	public void setPostalCodesServed(Set<PostalCode> postalCodesServed) {
		this.postalCodesServed = postalCodesServed;
	}
	

	/**
	 * Function to return the postal codes under this hub
	 * @return set of postal codes
	 */
	public Set<PostalCode> getPostalCodesServed() {
		return postalCodesServed;
	}


	/**
	 * Function to add the given postal code to existing set
	 * @param code
	 */
	public void addToServed(PostalCode code) {
		postalCodesServed.add(code);
	}
	

	/**
	 * Function to return the hours to repair
	 * @return hours as double
	 */
	public double getHoursToRepair() {
		return hoursToRepair;
	}
	

	/**
	 * Function to set the hours to repair
	 * @param hoursToRepair
	 */
	public void setHoursToRepair(double hoursToRepair) {
		this.hoursToRepair = hoursToRepair;
	}

	/**
	 * Function to generate hash code
	 * @return hash code as integer
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		//calculate the long value for area
		temp = Double.doubleToLongBits(areaCovered);
		//do shifting and prime number multiplication for area
		result = prime * result + (int) (temp ^ (temp >>> 32));
		//calculate the long value for hours to repair
		temp = Double.doubleToLongBits(hoursToRepair);
		//do shifting and prime number multiplication for hours
		result = prime * result + (int) (temp ^ (temp >>> 32));
		//get hash code of id
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		//get hash code of location
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		//return the result
		return result;
	}

	/**
	 * Function to compare current object with other object and 
	 * check whether both are equal or not
	 * @return result as boolean 
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistributionHub other = (DistributionHub) obj;
		//compare are covered
		if (Double.doubleToLongBits(areaCovered) != Double.doubleToLongBits(other.areaCovered))
			return false;
		//compare hours
		if (Double.doubleToLongBits(hoursToRepair) != Double.doubleToLongBits(other.hoursToRepair))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id)) //compare id
			return false;
		//compare location
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}

	/**
	 * Function to return the hub details as string
	 */
	@Override
	public String toString() {
		return "DistributionHub [id=" + id + ", location=" + location + ", areaCovered=" + areaCovered
				+ ", postalCodesServed=" + postalCodesServed + ", hoursToRepair=" + hoursToRepair + "]";
	}
}
