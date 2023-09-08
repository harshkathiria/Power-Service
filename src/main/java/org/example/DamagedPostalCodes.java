package org.example;
/**
 * Java class to store the damaged postal code details.
 * It contains postal code and number of repairs as attributes.
 */
public class DamagedPostalCodes implements Comparable<DamagedPostalCodes> {
	//attributes
	private String postalCode;
	private int numberOfRepairs;
	//constructor
	public DamagedPostalCodes(String postalCode, int numberOfRepairs) {
		this.postalCode = postalCode;
		this.numberOfRepairs = numberOfRepairs;
	}

	//Sets the postal code to local attribute
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	//Returns the postal code
	public String getPostalCode() {
		return postalCode;
	}

	//Sets the number of repairs to local attribute
	public void setNumberOfRepairs(int numberOfRepairs) {
		this.numberOfRepairs = numberOfRepairs;
	}

	//Returns the number of repairs
	public int getNumberOfRepairs() {
		return numberOfRepairs;
	}

	/**
	 * Function to compare calling object with the
	 * other passed as parameter. Returns 0, if
	 * both the number of repairs are equal and if less, then -1
	 * otherwise 1
	 */

	@Override
	public int compareTo(DamagedPostalCodes other) {
		if (numberOfRepairs < other.numberOfRepairs) {
			return -1;
		}
		else if (numberOfRepairs > other.numberOfRepairs) {
			return 1;
		}
		else {
			return 0;
		}
	}
}