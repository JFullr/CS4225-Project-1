package project.aurtdrs.process;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * The Class AurtdrsRoadTrain.
 *
 * @author Joseph Fuller, James Irwin, Timothy Brooks
 * @version Spring 2020
 */
public class AurtdrsRoadTrainTransmission implements Serializable {

	private static final long serialVersionUID = -6008263441901760035L;

	private double acceleration;
	private double speed;
	private double distance;
	private ArrayList<Point> relativePositions;

	/**
	 * Instantiates a new aurtdrs road train transmission.
	 *
	 * @param train the train
	 */
	public AurtdrsRoadTrainTransmission(AurtdrsRoadTrain train) {
		this.acceleration = train.getAcceleration();
		this.distance = train.getDistance();
		this.speed = train.getSpeed();
		this.relativePositions = train.getRelativePositions();
	}

	/**
	 * Cast.
	 *
	 * @return the aurtdrs road train
	 */
	public AurtdrsRoadTrain cast() {
		AurtdrsRoadTrain train = new AurtdrsRoadTrain();
		train.setAcceleration(this.acceleration);
		train.setDistance(this.distance);
		train.setSpeed(this.speed);
		train.setRelativePositions(this.relativePositions);
		return train;
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
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
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
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
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
	 * @param distance the distance to set
	 */
	public void setDistance(double distance) {
		this.distance = distance;
	}

	/**
	 * Gets the relative positions.
	 *
	 * @return the relativePositions
	 */
	public ArrayList<Point> getRelativePositions() {
		return this.relativePositions;
	}

	/**
	 * Sets the relative positions.
	 *
	 * @param relativePositions the relativePositions to set
	 */
	public void setRelativePositions(ArrayList<Point> relativePositions) {
		this.relativePositions = relativePositions;
	}

	/**
	 * Gets the serialversionuid.
	 *
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
