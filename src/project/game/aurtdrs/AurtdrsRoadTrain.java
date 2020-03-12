package project.game.aurtdrs;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class AurtdrsRoadTrain implements Serializable {

	private static final long serialVersionUID = -1311382588005252017L;
	
	private static final int TRAIN_LENGTH = 3;
	
	public static enum CarState{
		
		NORMAL,
		LEFT,
		RIGHT
		
	}
	
	private double acceleration;
	private double distance;
	private double speed;
	
	private ArrayList<Point> cars;
	private HashMap<Point,CarState> renderOptions;
	
	public AurtdrsRoadTrain() { 
		this.renderOptions = new HashMap<Point,CarState>();
		this.cars = new ArrayList<Point>();
		
		this.resetValues();
		
		for(int i = 0; i < TRAIN_LENGTH+1; i++) {
			Point car = new Point(0,i);
			this.cars.add(car);
			this.renderOptions.put(car, CarState.NORMAL);
		}
		
	}
	
	public Iterable<Point> getRelativePositions(){
		return this.cars;
	}
	
	public CarState getRenderState(Point car){
		return this.renderOptions.get(car);
	}
	
	public void incrementAcceleration(double increment) {
		this.acceleration += increment;
	}
	
	public void incrementValues() {
		this.speed += this.acceleration;
		this.distance += this.speed;
	}
	
	public void resetValues() {
		this.acceleration = 0;
		this.distance = 0;
		this.speed = 0;
		
		int i = 0;
		for(Point point : this.cars) {
			point.setLocation(0, i);
			i++;
		}
	}

	public double getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public double getSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
