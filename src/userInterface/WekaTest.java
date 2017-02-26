package userInterface;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.evaluation.NominalPrediction;
import weka.classifiers.evaluation.Prediction;
import weka.classifiers.rules.DecisionTable;
import weka.classifiers.rules.PART;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.gui.*;
 
public class WekaTest {
	public static BufferedReader readDataFile(String filename) {
		BufferedReader inputReader = null;
 
		try {
			inputReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException ex) {
			System.err.println("File not found: " + filename);
		}
 
		return inputReader;
	}
 
	public static Evaluation classify(Classifier model, Instances trainingSet, Instances testingSet) throws Exception {
		Evaluation evaluation = new Evaluation(trainingSet);
 
		model.buildClassifier(trainingSet);
		evaluation.evaluateModel(model, testingSet);
 		return evaluation;
	}
 
	public static double calculateAccuracy(ArrayList<Prediction> predictions) {
		double correct = 0;
 
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.get(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
		}
 
		return 100 * correct / predictions.size();
	}
 
	public static Instances[][] crossValidationSplit(Instances data, int numberOfFolds) {
		Instances[][] split = new Instances[2][numberOfFolds];
 
		for (int i = 0; i < numberOfFolds; i++) {
			split[0][i] = data.trainCV(numberOfFolds, i);
			split[1][i] = data.testCV(numberOfFolds, i);
		}
 
		return split;
	}
 
	public static void main(String[] args) throws Exception {		
		
//		BufferedReader datafile = readDataFile("dataset/95-10-06DatasetStaticObj.arff");//stroy1
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObj.arff");//story 1&2
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObj.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObjState.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRole.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleAction.arff");//story1&2&3
		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleState.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleIntent.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObj.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObjAction.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObjState.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetLocation.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetTime.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetSceneGoal.arff");//story1&2&3
		
		BufferedReader dataTestfile = readDataFile("dataset/95-10-13DateSetRoleState-test.arff");//story1&2&3
 
		Instances data = new Instances(datafile);
		data.setClassIndex(data.numAttributes() - 1);
 
		Instances testData = new Instances(dataTestfile);
		testData.setClassIndex(testData.numAttributes() - 1);

		
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(data, 10);
 
		// Separate split into training and testing arrays
		Instances[] trainingSplits = split[0];
		Instances[] testingSplits = split[1];
 
		// Use a set of classifiers
		Classifier[] models = { 
				new J48(), // a decision tree
				new PART(), 
				new DecisionTable(),//decision table majority classifier
				new DecisionStump() //one-level decision tree
		};
 
		// Run for each model
		for (int j = 0; j < models.length; j++) {
 
			// Collect every group of predictions for current model in a FastVector
			ArrayList<Prediction> predictions = new ArrayList<Prediction>();
 
			// For each training-testing split pair, train and test the classifier
			for (int i = 0; i < trainingSplits.length; i++) {
				Evaluation validation = classify(models[j], trainingSplits[i], testingSplits[i]);
 
				predictions.addAll(validation.predictions());
 
				// Uncomment to see the summary for each training-testing pair.
//				System.out.println(models[j].toString());
			}
 
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
 
			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy)
					+ "\n---------------------------------");			
		}
		
		ArrayList<Prediction> testPpred = new ArrayList<Prediction>();
		
		for (int j = 0; j < models.length; j++) {
						
			Evaluation testEval = classify(models[j], data, testData);
			
			testPpred.addAll(testEval.predictions());
			
			System.out.println(testEval.predictions() + "\n");
			
			System.out.println("Test accu " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", calculateAccuracy(testPpred))
					+ "\n---------------------------------");
		}
 
		
	}
}
