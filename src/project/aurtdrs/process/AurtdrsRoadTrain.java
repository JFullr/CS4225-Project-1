package project.aurtdrs.process;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The Class AurtdrsRoadTrain.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsRoadTrain implements Serializable {

	private static final long serialVersionUID = -1311382588005252017L;
	private static final int TRAIN_LENGTH = 3;

	/**
	 * The Enum CarState.
	 */
	public enum CarState {

		NORMAL, 
		LEFT, 
		RIGHT

	}

	private double acceleration;
	private double distance;
	private double speed;
	
	private ArrayList<Point> cars;
	private HashMap<Point, CarState> renderOptions;

	/**
	 * Instantiates a new aurtdrs road train.
	 */
	public AurtdrsRoadTrain() {
		this.renderOptions = new HashMap<Point, CarState>();
		this.cars = new ArrayList<Point>();

		this.resetValues();

		for (int i = 0; i < TRAIN_LENGTH + 1; i++) {
			Point car = new Point(0, i);
			this.cars.add(car);
			this.renderOptions.put(car, CarState.NORMAL);
		}

	}

	/**
	 * Gets the relative positions.
	 *
	 * @return the relative positions
	 */
	public Iterable<Point> getRelativePositions() {
		return this.cars;
	}

	/**
	 * Gets the render state.
	 *
	 * @param car the car
	 * @return the render state
	 */
	public CarState getRenderState(Point car) {
		return this.renderOptions.get(car);
	}

	/**
	 * Increment acceleration.
	 *
	 * @param increment the increment
	 */
	public void incrementAcceleration(double increment) {
		this.acceleration += increment;
	}

	/**
	 * Increment values.
	 */
	public void incrementValues() {
		this.speed += this.acceleration;
		this.distance += this.speed;
	}

	/**
	 * Reset values.
	 */
	public void resetValues() {
		this.acceleration = 0;
		this.distance = 0;
		this.speed = 0;

		int i = 0;
		for (Point point : this.cars) {
			point.setLocation(0, i);
			i++;
		}
	}

	/**
	 * Gets the acceleration.
	 *
	 * @return the acceleration
	 */
	public double getAcceleration() {
		return this.acceleration;
	}

	/**
	 * Sets the acceleration.
	 *
	 * @param acceleration the new acceleration
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Gets the distance.
	 *
	 * @return the distance
	 */
	public double getDistance() {
		return this.distance;
	}

	/**
	 * Sets the distance.
	 *
	 * @param distance the new distance
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Gets the speed.
	 *
	 * @return the speed
	 */
	public double getSpeed() {
		return this.speed;
	}

	/**
	 * Sets the speed.
	 *
	 * @param speed the new speed
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

}
