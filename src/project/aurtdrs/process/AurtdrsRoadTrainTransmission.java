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

	public double acceleration;
	public double speed;
	public double distance;
	public ArrayList<Point> relativePositions;

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

	public AurtdrsRoadTrain cast() {
		AurtdrsRoadTrain train = new AurtdrsRoadTrain();
		train.setAcceleration(this.acceleration);
		train.setDistance(this.distance);
		train.setSpeed(this.speed);
		train.setRelativePositions(this.relativePositions);
		return train;
	}

}
