package classification.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import classification.data.BinaryLabel;
import classification.data.Dataset;
import classification.data.Label;
import classification.data.Sample;
import classification.data.SupervisedSample;
import classification.linalg.Vector;

public class SVM<S extends Sample<? super S>> implements BinaryClassifier<S> {

	private Vector weights;
	private double bias;
	private double lambda;
	
	public SVM(int dimension, double lambda) {
		this.weights = Vector.random(dimension);
		var random = new Random();
		this.bias = random.nextGaussian();
		this.lambda = lambda;
	}
	
	public Vector getWeights() {
		return weights;
	}

	public double getBias() {
		return bias;
	}


	private <L extends Label> double loss(SupervisedSample<? extends S, L> sample) {
		double y = (sample.getLabel() == BinaryLabel.POSITIVE) ? 1 : -1;
		return Math.max(0, 1 - y*(sample.getFeatures().dot(getWeights()) + getBias())) + lambda/2 * (getWeights().norm()*getWeights().norm() + getBias()*getBias());
	}

	
	private <L extends Label> Vector gradWeights(SupervisedSample<? extends S, L> sample) {
		Vector result;
		double y = (sample.getLabel() == BinaryLabel.POSITIVE) ? 1 : -1;
		if (1 - y*(sample.getFeatures().dot(getWeights()) + getBias()) <= 0) {
			result = getWeights().mul(lambda);
		} else {
			result = getWeights().mul(lambda).add(sample.getFeatures().mul(-y));
		}
		return result;
	}
	
	private  <L extends Label> double gradBias(SupervisedSample<? extends S, L> sample) {
		double result;
		double y = (sample.getLabel() == BinaryLabel.POSITIVE) ? 1 : -1;
		if (1 - y*(sample.getFeatures().dot(getWeights()) + getBias()) <= 0) {
			result = lambda * getBias();
		} else {
			result = lambda * getBias() - y;
		}
		return result;
	}
	
	
	@Override
	public <L extends Label> double evaluate(Dataset<? extends SupervisedSample<? extends S, L>> data) {
		// TODO Auto-generated method stub
		int tp = 0, tn = 0, fp = 0, fn = 0;
		for (SupervisedSample<? extends S, L> sample : data) {
			Label prediction = ((sample.getFeatures().dot(getWeights()) + getBias()) >= 0) ? BinaryLabel.POSITIVE : BinaryLabel.NEGATIVE;
			if (sample.getLabel() == BinaryLabel.POSITIVE) {
				if (prediction == sample.getLabel()) {
					tp++;
				} else {
					fn++;
				}
			} else {
				if (prediction == sample.getLabel()) {
					tn++;
				} else {
					fp++;
				}
			}
			
		}
		return (double)(tp+tn)/(double)(tp+tn+fp+fn);
	}
	
	@Override
	public <L extends Label> List<Double> fit(Dataset<? extends SupervisedSample<? extends S, L>> data, int epochs) {
		// TODO Auto-generated method stub
		LinkedList<Double> seq = new LinkedList<>();	//Folge l_k
		double n  = 0;
		for (SupervisedSample<? extends S, L> sample : data) {
			n = n+1;
		}
		
		for (int k = 0; k<epochs; k++) {
			data.shuffle();
			seq.add((double) 0 );
			for (SupervisedSample<? extends S, L> sample : data) {
				seq.set(k, seq.get(k) + 1/(double)n * this.loss(sample));
				weights = getWeights().add(this.gradWeights(sample).mul(-1/((double)(k+1))));
				bias -= 1/(double)(k+1) * this.gradBias(sample);
			}
		}
		return seq;
	}

	@Override
	public <L extends Label, T extends S> List<BinaryLabel> predict(Dataset<? extends Sample<? super T>> data) {
		// TODO Auto-generated method stub
		ArrayList<BinaryLabel> result = new ArrayList<>();
		for (Sample<? super T> sample : data) {
			BinaryLabel prediction = ((sample.getFeatures().dot(getWeights()) + getBias()) >= 0) ? BinaryLabel.POSITIVE : BinaryLabel.NEGATIVE;
			result.add(prediction);
		}
		return result;
	}

}
