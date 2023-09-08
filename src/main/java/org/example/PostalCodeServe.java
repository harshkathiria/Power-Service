package org.example;
/**
 * Java class to store the fraction served by postal code and its id
 */
public class PostalCodeServe implements Comparable<PostalCodeServe>{
	private String id;
	private double servedFraction;
	
	//constructor
	public PostalCodeServe(String id, double servedFraction) {
		this.id = id;
		this.servedFraction = servedFraction;
	}
	
	//Function for getter and setters
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public double getServedFraction() {
		return servedFraction;
	}
	
	public void setServedFraction(double servedFraction) {
		this.servedFraction = servedFraction;
	}

	@Override
	public int compareTo(PostalCodeServe o) {
		if (servedFraction < o.servedFraction) {
			return -1;
		}
		else if(servedFraction > o.servedFraction) {
			return 1;
		}
		return 0;
	}
}
