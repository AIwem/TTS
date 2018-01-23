package evaluation;



import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.collections15.map.HashedMap;

import model.ScenePart;




public class EvaluateModel {
	
	private ArrayList<Instance> testData = new ArrayList<Instance>();
	private Map<ScenePart, Map<ScenePart, Integer>> numericalConfusionMatrix = new HashedMap<ScenePart, Map<ScenePart, Integer>>();
	private Map<ScenePart, Map<ScenePart, ArrayList<Instance>>> confusionMatrix = new HashedMap<ScenePart, Map<ScenePart,ArrayList<Instance>>>();
	private Map<ScenePart, Double> precisions = new HashedMap<ScenePart, Double>();
	private Map<ScenePart, Double> recalls = new HashedMap<ScenePart, Double>();
	private double averageAccuracy = -1;
	private int testDataNumber = -1;
		
	public EvaluateModel(String testFilename, String outputFilename){
		
		importInstances(testFilename, outputFilename);
	}
	
	@SuppressWarnings("resource")
	private void importInstances(String testFilename, String outputFilename){		
				
		BufferedReader testStream = null;
		BufferedReader outputStream = null;
		
		try
		{	int testDataNum = 0;
			testStream = new BufferedReader(new InputStreamReader(new FileInputStream(testFilename), "utf-8"));		
			outputStream = new BufferedReader(new InputStreamReader(new FileInputStream(outputFilename), "utf-8"));
		
			String testLine = "";
			String outputLine = "";
			
			while (testLine != null && outputLine != null)
			{
				//line format: N ADVRB  ArgM_MNR خصوصيت_رواني§n-12725 role_state
				testLine = testStream.readLine();
				
				//output format: static_object 
				outputLine = outputStream.readLine();
				
				if(testLine != null && outputLine != null){
					
					testLine = testLine.trim();
					outputLine = outputLine.trim();
					
					if(testLine.equalsIgnoreCase("") || outputLine.equalsIgnoreCase("")){
						System.out.println();	
						continue;
					}
					testDataNum++;
//					System.out.println(testLine);
					String[] recordElem = testLine.split("( )+");
					
					Instance record = null;
					
					if(recordElem != null && recordElem.length == 5){
						record = new Instance(testLine, recordElem[4], outputLine);
						testData.add(record);
					}
					else{
						System.out.println(recordElem.toString());
						throw new Exception("bad format in testFile: " + testFilename);
					}
				}
			}
			testDataNumber = testDataNum;
			
			testStream.close();
			outputStream.close();			
		}
		catch(Exception e){
			e.printStackTrace();
		}			
	}
	
	private void populateConfusionMatrix(){
		if(testData == null || testData.size() == 0)
			return;
		
		for(Instance record : testData){
			
			System.out.println(record + " --- " + record.classifiedSceneElement);
			
			boolean hasRow = false;
			
			hasRow = confusionMatrix.containsKey(record.trueSceneElement);
			
			Map<ScenePart, ArrayList<Instance>> row = null;
			
			if(hasRow == false){//it is a new row!
				ArrayList<Instance> newList = new ArrayList<Instance>();
				newList.add(record);
				
				row = new HashedMap<ScenePart, ArrayList<Instance>>();
				
				row.put(record.classifiedSceneElement, newList);
				
				confusionMatrix.put(record.trueSceneElement, row);
			}
			else{
				row = confusionMatrix.get(record.trueSceneElement);
				if(row != null){
					ArrayList<Instance> intendedList = null;

					if(row.containsKey(record.classifiedSceneElement)){
						intendedList = row.get(record.classifiedSceneElement);
						intendedList.add(record);
					}
					else{
						ArrayList<Instance> newList = new ArrayList<Instance>();
						newList.add(record);
						row.put(record.classifiedSceneElement, newList);
					}
				}				
			}	
		}
	}
	
	private void populateNumericalConfusionMatrix(){		

		for(ScenePart row: confusionMatrix.keySet()){			
			Map<ScenePart, ArrayList<Instance>> rowMap  = confusionMatrix.get(row);
			numericalConfusionMatrix.put(row, new HashedMap<ScenePart, Integer>());
			
			for(ScenePart column:rowMap.keySet()){				
				ArrayList<Instance> list = rowMap.get(column);
				numericalConfusionMatrix.get(row).put(column, list.size());

			}
		}
	}
	
	public void calculateRecalls(){
		
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			
			int sorat = 0;
			int makhraj = 0;
			
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
			if(rowMap.containsKey(row)){
				sorat = rowMap.get(row);
				makhraj = sorat;
				for(ScenePart column:rowMap.keySet()){
					if(column != row)
						makhraj += rowMap.get(column);
				}
				if(makhraj != 0)
					recalls.put(row, (sorat*1.0)/(makhraj*1.0));
				else
					recalls.put(row, 0.0);
			}
			else
				recalls.put(row, 0.0);			
		}
	}	
	
	public void calculatePrecisions(){		
		
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			
			int sorat = 0;
			int makhraj = 0;
			
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
			if(rowMap.containsKey(row)){
				sorat = rowMap.get(row);
//				makhraj = sorat;
				
				for(ScenePart row2:numericalConfusionMatrix.keySet()){
					Map<ScenePart, Integer> row2Map = numericalConfusionMatrix.get(row2);
					if(row2Map != null && row2Map.containsKey(row))
						makhraj += row2Map.get(row);					
				}
				if(makhraj != 0){
//					System.out.println(row + ": sorat " + sorat + "\tmakhraj " + makhraj);
					precisions.put(row, (sorat*1.0)/(makhraj*1.0));
				}
				else					
					precisions.put(row, 0.0);
	
			}
			else
				precisions.put(row, 0.0);			
		}		
	}

	public void printConfusionMatrix(){
		System.out.println("\nconfusionMatrix\n");
		
		for(ScenePart row: confusionMatrix.keySet()){
			System.out.println("true------------------row: " + row);
			Map<ScenePart, ArrayList<Instance>> rowMap  = confusionMatrix.get(row);
			for(ScenePart column:rowMap.keySet()){				
				ArrayList<Instance> list = rowMap.get(column);
				System.out.println("+++column " + column + " size:" + list.size());
//				for(Instance rcd:list)
//					System.out.println(rcd);
			}
		}
	}
	
	public void printNumericalConfusionMatrix(){
		System.out.println("\nNumericalConfusionMatrix\n");
		
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			System.out.println("true------------------row: " + row);
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
			
//			System.out.println();
			for(ScenePart column:rowMap.keySet())				
				System.out.println(column + "\t" + rowMap.get(column));
			System.out.println();
		}
	}
	
	public void printRecalls(){
		System.out.println("\nrecalls\n");
		
		for(ScenePart row: recalls.keySet())
			System.out.println(row + ":\t------------------\t" + recalls.get(row)*100);
	}
	
	public void printPrecisions(){
		System.out.println("\nprecisions\n");
		
		for(ScenePart row: precisions.keySet())
			System.out.println(row + ":\t------------------\t" + precisions.get(row)*100);
	}

	public void printPrecisions_recalls(){
		System.out.println("\nprecisions_recalls\n");
		
		System.out.println("sceneElem:\t------------------\tprecisions\t\trecalls");
		for(ScenePart row: precisions.keySet()){		
			System.out.println(row + ":\t------------------\t" + (precisions.get(row)*100) + "\t" + (recalls.get(row)*100));
		}
	}
	
	public void printAverageAccuracy(){
		
		System.out.println("\naverage accuracy\n");
		
		int sorat = 0;
		int makhraj = testDataNumber;//1465
		
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
		
			if(rowMap.containsKey(row)){
				sorat += rowMap.get(row);
			}
		}
				
		System.out.println(sorat + " sorat");
		System.out.println(makhraj + " makhraj");
		averageAccuracy = (sorat*1.0)/(makhraj*1.0);	
		System.out.println(averageAccuracy*100);		
	}
	
	public void printAverageAccuracyForPositiveClasses(){
		
		System.out.println("\naverage accuracy for positive classes (excluding no and junk)\n");
		
		int sorat = 0;
		int makhraj = 0;
		
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
			
			if(row != ScenePart.NO && row != ScenePart.JUNK)
				if(rowMap.containsKey(row))				
					sorat += rowMap.get(row);		
		}
				
		for(ScenePart row: numericalConfusionMatrix.keySet()){
			Map<ScenePart, Integer> rowMap  = numericalConfusionMatrix.get(row);
		
			if(row != ScenePart.NO && row != ScenePart.JUNK)
				for(ScenePart column:rowMap.keySet())
					makhraj += rowMap.get(column);			
		}
		
		System.out.println(sorat + " sorat");
		System.out.println(makhraj + " makhraj");
		
		averageAccuracy = (sorat*1.0)/(makhraj*1.0);	
		System.out.println(averageAccuracy*100);		
	}
	
	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله");
//		EvaluateModel evaluator = new EvaluateModel("dataset/96-10-25CRFTestdataset.arff", "dataset/96-10-25out.arff");
//		EvaluateModel evaluator = new EvaluateModel("dataset/96-10-26CRFTestdataset-junk.arff", "dataset/96-10-26out-junk.arff");
//		EvaluateModel evaluator = new EvaluateModel("dataset/96-10-26CRFTestdataset-junk-num.arff", "dataset/96-10-26out-junk-num.arff");
//		EvaluateModel evaluator = new EvaluateModel("dataset/96-10-26CRFTestdataset-junk-num2.arff", "dataset/96-10-26out-junk-num2.arff");
//		EvaluateModel evaluator = new EvaluateModel("dataset/96-11-01CRFTestdataset-junk-num3.arff", "dataset/96-11-01out-junk-num3.arff");
		EvaluateModel evaluator = new EvaluateModel("dataset/96-11-03CRFTestdataset-junk-num3-remove.arff", "dataset/96-11-03out-junk-num3-remove.arff");
		
		evaluator.populateConfusionMatrix();
		
		evaluator.populateNumericalConfusionMatrix();
		
//		evaluator.printConfusionMatrix();
		
		evaluator.printNumericalConfusionMatrix();

		evaluator.calculatePrecisions();
		
		evaluator.calculateRecalls();
		
//		evaluator.printPrecisions();
		
//		evaluator.printRecalls();		
		
		evaluator.printPrecisions_recalls();
		
		evaluator.printAverageAccuracy();
		
		evaluator.printAverageAccuracyForPositiveClasses();
		
		System.out.println("الحمدلله رب العالمین");
	}	
}
