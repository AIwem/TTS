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
		
//		ArrayList<Prediction> wrongClassified = new ArrayList<Prediction>();
		
		for (int i = 0; i < predictions.size(); i++) {
			NominalPrediction np = (NominalPrediction) predictions.get(i);
			if (np.predicted() == np.actual()) {
				correct++;
			}
//			else{
//				wrongClassified.add(np);
//			}
		}
//		for(Prediction wr:wrongClassified)
//			System.out.println(">>> wrongly classified " + wr.actual() + " as " + wr.predicted());
		
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
 
public static void withoutCrossValidation(Instances trainData, Instances testData) throws Exception {		
		
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
 
			// train and test the classifier
			
			Evaluation validation = classify(models[j], trainData, testData);
 
			predictions.addAll(validation.predictions());
 
			// Uncomment to see the summary for each training-testing pair.
			System.out.println("\nmodel.toStr");
			System.out.println(models[j].toString());
			
			System.out.println("\ntoclassDetailStr");
			System.out.println(validation.toClassDetailsString());
			
			System.out.println("\ntoMatrixStr");
			System.out.println(validation.toMatrixString());
								
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
 
			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy)
					+ "\n-------------------------------------------------------------");			
		}			
		/*with separate train(110) and test(34) set and withoutCrossValidation
		Accuracy of J48: 82.35%
		---------------------------------
		Accuracy of PART: 82.35%
		---------------------------------
		Accuracy of DecisionTable: 73.53%
		---------------------------------
		Accuracy of DecisionStump: 94.12%
		---------------------------------
		
		sample Confusion Matrix for J48 and PART
		=== Confusion Matrix ===
		
		  a  b   <-- classified as
		  0  6 |  a = roleState
		  0 28 |  b = not_roleState
		  
		 sample Confusion Matrix for Decision Table		 
		=== Confusion Matrix ===
		
		  a  b   <-- classified as
		  5  1 |  a = roleState
		  8 20 |  b = not_roleState
		  
		  sample Confusion Matrix for Decision Stump		  
		=== Confusion Matrix ===
		
		  a  b   <-- classified as
		  4  2 |  a = roleState
		  0 28 |  b = not_roleState
		 */
	}
	
	public static void withCrossValidation(Instances trainData, Instances testData) throws Exception {
		
		
		// Do 10-split cross validation
		Instances[][] split = crossValidationSplit(trainData, 10);
	
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
				System.out.print("\n" + i + ": model.toStr ");
				System.out.println(models[j].toString());
				
				System.out.println("\ntoclassDetailStr");
				System.out.println(validation.toClassDetailsString());
		
				System.out.println("\ntoMatrixStr");
				System.out.println(validation.toMatrixString());
								
			}
			System.out.println("\nmodel.toStr after 10");
			System.out.println(models[j].toString());
			
			// Calculate overall accuracy of current classifier on all splits
			double accuracy = calculateAccuracy(predictions);
	
			// Print current classifier's name and accuracy in a complicated,
			// but nice-looking way.
			System.out.println("Accuracy of " + models[j].getClass().getSimpleName() + ": "
					+ String.format("%.2f%%", accuracy)
					+ "\n---------------------------------");	
		}
		/*
		for(Instance inst:data){
			
			double classified = models[3].classifyInstance(inst);
			
			if(classified != 1.0){
				System.out.println("data.classValue :   ****** " + inst.classValue());
				System.out.println("data == roleState : *************** " + inst);
				System.out.println("");
			}
		}*/		
		
		/* with separate train(110) and test(34) set (test set not used) and crossValidation
		 Accuracy of J48: 94.55%
		---------------------------------
		Accuracy of PART: 94.55%
		---------------------------------
		Accuracy of DecisionTable: 92.73%
		---------------------------------
		Accuracy of DecisionStump: 92.73%
		---------------------------------
		
		sample confusion matrix for J48 and PART and Decision table and Decision Stump
		=== Confusion Matrix ===

		 a  b   <-- classified as
		 0  1 |  a = roleState
		 0 10 |  b = not_roleState
		  
		 one case in each of Decision table and Decision Stump
		 === Confusion Matrix ===
		
		 a b   <-- classified as
		 0 0 | a = roleState
		 2 9 | b = not_roleState

		 */
}

	public static void main(String[] args) throws Exception {		
		
//		BufferedReader datafile = readDataFile("dataset/95-10-06DatasetStaticObj.arff");//stroy1
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObj.arff");//story 1&2
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObj.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetStaticObjState.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRole.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleAction.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleState-2.arff");//story1&2&3
		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetRoleIntent-2.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObj.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObjAction.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetDynamicObjState.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetLocation.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetTime.arff");//story1&2&3
//		BufferedReader datafile = readDataFile("dataset/95-10-13DateSetSceneGoal.arff");//story1&2&3
		
		Instances trainData = new Instances(datafile);
		trainData.setClassIndex(trainData.numAttributes() - 1);
		
//		BufferedReader testDatafile = readDataFile("dataset/95-10-13DateSetRoleState-test.arff");//story1&2&3
		BufferedReader testDatafile = readDataFile("dataset/95-10-13DateSetRoleIntent-test.arff");//story1&2&3
		
		Instances testData = new Instances(testDatafile);
		testData.setClassIndex(testData.numAttributes() - 1);
		
//		withoutCrossValidation(testData, testData);
		withCrossValidation(trainData, testData);
	}
}
