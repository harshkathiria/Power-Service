package org.example;
/**
 * Java class to represent the Point class,
 * which contains x coordinate and y coordinate
 */
public class Point {
	//attributes
	private int x;
	private int y;
	
	//constructor
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	//setter and getter functions for attributes
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return y;
	}

	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + "]";
	}
}
