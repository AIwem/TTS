package userInterface;

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.util.zip.*;

import org.apache.commons.codec.binary.*;

import cc.mallet.fst.*;
import cc.mallet.pipe.*;
import cc.mallet.pipe.iterator.*;
import cc.mallet.pipe.tsf.*;
import cc.mallet.types.*;
import cc.mallet.util.*;


public class TrainCRF {
	
	public TrainCRF(String trainingFilename, String testingFilename) throws IOException {
		
		ArrayList<Pipe> pipes = new ArrayList<Pipe>();

		int[][] conjunctions = new int[2][];
		conjunctions[0] = new int[] { -1 };
		conjunctions[1] = new int[] { 1 };

//		pipes.add(new SimpleTaggerSentence2TokenSequence());
		pipes.add(new OffsetConjunctions(conjunctions));
		//pipes.add(new FeaturesInWindow("PREV-", -1, 1));
		pipes.add(new TokenTextCharSuffix("C1=", 1));
		pipes.add(new TokenTextCharSuffix("C2=", 2));
		pipes.add(new TokenTextCharSuffix("C3=", 3));
		pipes.add(new RegexMatches("CAPITALIZED", Pattern.compile("^\\p{Lu}.*")));
		pipes.add(new RegexMatches("STARTSNUMBER", Pattern.compile("^[0-9].*")));
		pipes.add(new RegexMatches("HYPHENATED", Pattern.compile(".*\\-.*")));
		pipes.add(new RegexMatches("DOLLARSIGN", Pattern.compile(".*\\$.*")));
		pipes.add(new TokenFirstPosition("FIRSTTOKEN"));
//		pipes.add(new TokenSequence2FeatureVectorSequence());

		Pipe pipe = new SerialPipes(pipes);

		InstanceList trainingInstances = new InstanceList(pipe);
		InstanceList testingInstances = new InstanceList(pipe);
		
//		FileInputStream inpFile = ;
//		(base64Data)
//		trainingInstances.addThruPipe(new LineGroupIterator(new BufferedReader(new InputStreamReader(new GZIPInputStream(Base64.decodeBase64(new FileInputStream(trainingFilename).toString())))), Pattern.compile("^\\s*$"), true));
		
		new FileInputStream(trainingFilename);
		trainingInstances.addThruPipe(new LineGroupIterator(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(trainingFilename)))), Pattern.compile("^\\s*$"), true));
		/////&&&&&&&&&&&
//			String str = "salam";
//			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("UTF-8"))); 
//			BufferedReader bf = new BufferedReader(new InputStreamReader(gis, "UTF-8")); 
//			String outStr = ""; 
//			String line; 
//			while ((line=bf.readLine())!=null) { 
//	             outStr += line; 
//			} 
//			System.out.println("Output String lenght : " + outStr.length()); 
////			return outStr;
//			//>>>>>>>>>>>>>>>>
//			GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(Base64.decodeBase64(str))); 
//			String outStr = ""; 
//			ByteArrayOutputStream out = new ByteArrayOutputStream();
//			byte[] buffer = new byte[256];
//			int n;
//			while ((n = gis.read(buffer)) >= 0) {
//			   out.write(buffer, 0, n);
//			}
//			System.out.println("Output String lenght : " + outStr.length()); 
//			return new String(out.toByteArray()); 
			
		/////&&&&&&&&&&&
//		String infoBase64Encode = new String(Base64.encodeBase64(out.toByteArray()))	
		
		testingInstances.addThruPipe(new LineGroupIterator(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(testingFilename)))), Pattern.compile("^\\s*$"), true));
		
			
		CRF crf = new CRF(pipe, null);
		//crf.addStatesForLabelsConnectedAsIn(trainingInstances);
		crf.addStatesForThreeQuarterLabelsConnectedAsIn(trainingInstances);
		crf.addStartState();

		CRFTrainerByLabelLikelihood trainer = 
			new CRFTrainerByLabelLikelihood(crf);
		trainer.setGaussianPriorVariance(10.0);

		//CRFTrainerByStochasticGradient trainer = 
		//new CRFTrainerByStochasticGradient(crf, 1.0);

		//CRFTrainerByL1LabelLikelihood trainer = 
		//	new CRFTrainerByL1LabelLikelihood(crf, 0.75);

		//trainer.addEvaluator(new PerClassAccuracyEvaluator(trainingInstances, "training"));
		trainer.addEvaluator(new PerClassAccuracyEvaluator(testingInstances, "testing"));
		trainer.addEvaluator(new TokenAccuracyEvaluator(testingInstances, "testing"));
		trainer.train(trainingInstances);
		
	}

	public static void main (String[] args) throws Exception {
		System.out.println("here " + args[0] + " -- " + args[1]);
		TrainCRF trainer = new TrainCRF(args[0], args[1]);

	}

}
