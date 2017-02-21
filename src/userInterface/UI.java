package userInterface;

import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.tika.parser.ParseContext;

import model.SentenceModel;
import model.StoryModel;
import sceneReasoner.TTSEngine;


@SuppressWarnings("unused")
public class UI {
	

//	private String inputStoryFilePath = "inputStory/inputStrory8.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory2-1.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory3.txt";
	private String inputStoryFilePath = "inputStory/inputStory4.txt";
	private String inputSRLDataSetFilePath = "inputStory/story4DataSetToformat.txt";
	private String inputDataSetFilePath = "inputStory/srl-stories.conll";
	private String inputDataSetHeaderFilePath = "inputStory/dataSetHeader.txt";
//	private String mainKbFilePath = "kb/farsnet--24.txt";
	private String mainKbFilePath = "kb/farsnet.txt";
	private String myKbFilePath = "kb/injuredPigeon9.txt";
	//private String myKbFilePath = "kb/injuredPigeon_simple.txt";
	//private String myKbFilePath = "kb/injuredPigeon_SS.txt";
	private String verbSemanticCapacitiesPath = "kb/verbSemanticCapacities.txt";
	private String verbVisualCapacitiesPath = "kb/verbVisualCapacities.txt";
	private TTSEngine tts;
	
	public UI(){		
		//tts = new TTSEngine(mainKbFilePath, myKbFilePath);
	}
	
	private void print(String s){
		System.out.println(s);
	}
	/**
	 * main cycle of reading input sentences from user input file, and giving sentence to TTSEngine,
	 * and showing its output to the user.
	 */
	public StoryModel makeSyntaxDataset(){
		
		//It must read sentence from user input.
		//temporarily it reads all input story from file instead of getting from user!
		ArrayList<String> inputs = importInputTexts(inputStoryFilePath);

		ArrayList<String> current_scene_lines = new ArrayList<String>();
		
		int story_num = 0;		
		StoryModel storyModel = new StoryModel("story"+story_num);		
		
		PrintWriter writer = null;
		PrintWriter writer2 = null;
		try {
//			writer = new PrintWriter("output\\sceneOutput.txt", "UTF-8");
			writer = null;//new PrintWriter("output\\sentenceSyntax.txt", "UTF-8");
			writer2 = new PrintWriter("output\\sentencePhrases.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		for(String line:inputs){
			
			if(line.equals("«داستان جدید»")){				
				storyModel = new StoryModel("stroy" + ++story_num);
				tts = new TTSEngine(mainKbFilePath, myKbFilePath, verbSemanticCapacitiesPath, verbVisualCapacitiesPath);
				continue;
			}
			else if(line.equals("«صحنه جدید»")){
				
				//Then give the sentences of a scene to TTSEngine to enrich. 
			 	tts.TextToScene(current_scene_lines, storyModel, false, writer, writer2);		 	
				
				current_scene_lines = new ArrayList<String>();
				continue;
			}			
			current_scene_lines.add(line);			
		}
		//sentences of the last scene will be sent to TTSEngine to enrich. 
	 	tts.TextToScene(current_scene_lines, storyModel, true, writer, writer2);

//		writer.close();
		writer2.close();
			
	 	return storyModel;
	}

	/**
	 * clear SRLDataset from unwanted columns, and generate dataSet ready to be added WSD by human.
	 * srl format sample:
	 * 15	من	من	من	PR	PR	person=1|number=SING|senID=126_035	person=1|number=SING|senID=126_035	14	14	POSDEP	POSDEP	_	_	_	_	_	_	_
	 * 
	 * output format:
	 * 15	من		PR		POSDEP		14		_	_	_	_	_	_	_ 
	 * @return
	 */
	public void clearSRLDataset(){
		
		try {
			
			PrintWriter writer = new PrintWriter("output\\cleanedSRLDataSet.arff", "UTF-8");

			ArrayList<String> inputs = importInputTexts(inputDataSetFilePath);
				
			for(String record:inputs){
				
				String toWrtite = "";
				
				String[] parts = record.split("(\t)+");
				
				//discarding parts[2], [3], [5], [6], [7], [9], and [11]
				if(parts.length > 11){
					toWrtite += parts[0] + "\t";				
					toWrtite += parts[1] + "\t";
					toWrtite += parts[4] + "\t";
					toWrtite += parts[10] + "\t";
					toWrtite += parts[8] + "\t";
				}
				for(int i = 12; i < parts.length; i++)
					toWrtite +=  parts[i] + "\t";
								
				writer.println(toWrtite);				
			}							
			writer.close();
			
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 	
	}

		
//	public void makeFullDataset(){
//		
//		ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(inputDataSetFilePath);
//
//		PrintWriter writer = null;
//		
//		try {
//			writer = new PrintWriter("output\\sentencePhrases.txt", "UTF-8");
//		
//			tts = new TTSEngine(mainKbFilePath, myKbFilePath, verbSemanticCapacitiesPath, verbVisualCapacitiesPath);
//			
//			for(ArrayList<String> sentenceInfo:inputs)
//				writer.println(tts.generateFullRecordAnalysis(sentenceInfo));
//	
//			writer.close();
//			
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//	}

	/**
	 * reformat full human made dataSet file to the final format.
	 */
	public void reformatDataset(){
		
		BufferedReader stream4header = null;
		PrintWriter writer1 = null;PrintWriter writer2 = null;PrintWriter writer3 = null;
		PrintWriter writer4 = null;PrintWriter writer5 = null;PrintWriter writer6 = null;
		PrintWriter writer7 = null;PrintWriter writer8 = null;PrintWriter writer9 = null;
		PrintWriter writer10 = null;PrintWriter writer11 = null;PrintWriter writer12 = null;
		PrintWriter writer13 = null;PrintWriter writer14 = null;
		
		try
		{
			stream4header = new BufferedReader(new InputStreamReader(new FileInputStream(inputDataSetHeaderFilePath), "utf-8"));
			writer1 = new PrintWriter("output\\story4DataSetRole.arff", "UTF-8");
			writer2 = new PrintWriter("output\\story4DataSetrole_action.arff", "UTF-8");
			writer3 = new PrintWriter("output\\story4DataSetrole_state.arff", "UTF-8");
			writer4 = new PrintWriter("output\\story4DataSetrole_emotion.arff", "UTF-8");
			writer5 = new PrintWriter("output\\story4DataSetrole_intent.arff", "UTF-8");
			writer6 = new PrintWriter("output\\story4DataSetDyanmicObject.arff", "UTF-8");
			writer7 = new PrintWriter("output\\story4DataSetdynamic_object_action.arff", "UTF-8");
			writer8 = new PrintWriter("output\\story4DataSetdynamic_object_state.arff", "UTF-8");
			writer9 = new PrintWriter("output\\story4DataSetstatic_objectect.arff", "UTF-8");
			writer10 = new PrintWriter("output\\story4DataSetstatic_object_state.arff", "UTF-8");
			writer11 = new PrintWriter("output\\story4DataSetLocation.arff", "UTF-8");
			writer12 = new PrintWriter("output\\story4DataSetTime.arff", "UTF-8");
			writer13 = new PrintWriter("output\\story4DataSetscene_goal.arff", "UTF-8");
			writer14 = new PrintWriter("output\\story4DataSetscene_emotion.arff", "UTF-8");
			
			
			String header = "";
			
			while(header != null){
				
				header = stream4header.readLine();
				
				if (header == null)
					break;
				
				header = header.trim();
							
				writer1.println(header);writer2.println(header);writer3.println(header);
				writer4.println(header);writer5.println(header);writer6.println(header);
				writer7.println(header);writer8.println(header);writer9.println(header);
				writer10.println(header);writer11.println(header);writer12.println(header);
				writer13.println(header);writer14.println(header);		
				
			}
			
			ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(inputDataSetFilePath);
				
			for(ArrayList<String> sentenceInfos:inputs){
//				writer.println(tts.generateFullRecordAnalysis(sentenceInfo));
				
				//-----
				if(sentenceInfos == null || sentenceInfos.size() < 3)
					MyError.exit("bad input lines!");			
				
				String sentRecord = "";
				String NLSentence = sentenceInfos.get(0);
				sentRecord += NLSentence + "\n";		
				sentRecord += sentenceInfos.get(1);
				sentenceInfos.remove(0);
				sentenceInfos.remove(0);
				
				print(sentRecord);
				print("------------");
				
				writer1.println(sentRecord);writer2.println(sentRecord);writer3.println(sentRecord);
				writer4.println(sentRecord);writer5.println(sentRecord);writer6.println(sentRecord);
				writer7.println(sentRecord);writer8.println(sentRecord);writer9.println(sentRecord);
				writer10.println(sentRecord);writer11.println(sentRecord);writer12.println(sentRecord);
				writer13.println(sentRecord);writer14.println(sentRecord);
				
				
				String[] sentenceElem = new String[sentenceInfos.size()];				
				for(int i = 0; i < sentenceElem.length; i++)
					sentenceElem[i] = sentenceInfos.get(i);
					
				SentenceModel sentence = new SentenceModel(NLSentence, sentenceElem, true);	
									
				ArrayList<String> sentRecords = sentence.getDataSetStr();
				
				for(String wordRecord:sentRecords){
					
					boolean hasClassTag = wordRecord.contains("---");
					
					if(hasClassTag){
				
						String sharedRecord = wordRecord.substring(0, wordRecord.indexOf("---"));
						
						String record = sharedRecord;
						print(sharedRecord);
						print("------------");
						
						String classTag = wordRecord.substring(wordRecord.indexOf("---") + 3);
					
						print("classTag: " + classTag);
						print("************");
						
										
																			
						String record1 = new String(record);String record2 = new String(record);String record3 = new String(record);
						String record4 = new String(record);String record5 = new String(record);String record6 = new String(record);
						String record7 = new String(record);String record8 = new String(record);String record9 = new String(record);
						String record10 = new String(record);String record11 = new String(record);String record12 = new String(record);
						String record13 = new String(record);String record14 = new String(record);
										
						switch(classTag){
							case "unknown":{
								record1 += "not_role";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "role":{
								record1 += " role";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "role_action":{
								record2 += " role_action";
								record1 += "not_role";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "role_state":{
								record3 += " role_state";
								record2 += "not_role_action";record1 += "not_role";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "role_emotion":{
								record4 += " role_emotion";
								record2 += "not_role_action";record3 += "not_role_state";record1 += "not_role";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "role_intent":{
								record5 += " roleIntet";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record1 += "not_role";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "dynamic_object":{
								record6 += " dynamic_object";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record1 += "not_role";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "dynamic_object_action":{
								record7 += " dynamic_object_action";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record1 += "not_role";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "dynamic_object_state":{
								record8 += " dynamic_object_state";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record1 += "not_role";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "static_object":{
								record9 += " static_object";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record1 += "not_role";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "static_object_state":{
								record10 += " static_object_state";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record1 += "not_role";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "location":{
								record11 += " location";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record1 += "not_role";record12 += "not_time";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}
							case "time":{
								record12 += " time";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record1 += "not_role";record13 += "not_scene_goal";
								record14 += "not_scene_emotion";break;
							}						
							case "scene_goal":{
								record13 += " scene_goal";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record1 += "not_role";
								record14 += "not_scene_emotion";break;
							}
							case "scene_emotion":{
								record14 += " scene_emotion";
								record2 += "not_role_action";record3 += "not_role_state";record4 += "not_role_emotion";
								record5 += "not_role_intent";record6 += "not_dynamic_object";record7 += "not_dynamic_object_action";
								record8 += "not_dynamic_object_state";record9 += "not_static_object";record10 += "not_static_object_state";
								record11 += "not_location";record12 += "not_time";record13 += "not_scene_goal";
								record1 += "not_role";break;
							}
							default:{
								MyError.error(classTag +  " there isn't in none of cases.");
								continue;
							}		
						}
									
						writer1.println(record1);writer2.println(record2);writer3.println(record3);
						writer4.println(record4);writer5.println(record5);writer6.println(record6);
						writer7.println(record7);writer8.println(record8);writer9.println(record9);
						writer10.println(record10);writer11.println(record11);writer12.println(record12);
						writer13.println(record13);writer14.println(record14);
					}
					else
						MyError.error("No class tah found in " + wordRecord);					
				}
			}
			writer1.close();writer2.close();writer3.close();
			writer4.close();writer5.close();writer6.close();
			writer7.close();writer8.close();writer9.close();
			writer10.close();writer11.close();writer12.close();
			writer13.close();writer14.close();
			
		}
		catch (IOException e) {

			e.printStackTrace();
		}
		 	
	}


	
	
	public ArrayList<String> importInputTexts(String filename)
	{
		ArrayList<String> inputs = new ArrayList<>();
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening `" + filename + "` for reading input natural language texts!");
			e.printStackTrace();
		}
		
		String line = "";
		
		while (line != null)
		{
			try
			{
				line = stream.readLine();
				
				if (line == null)
					break;
				
				line = line.trim();				
							
				if (line.startsWith("#") || line.startsWith("%")) // comment lines
					continue;
							
				inputs.add(line);

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return inputs;
	}
	
	/**
	 * returns lines of input dataSet for each sentence in an ArrayList<String>.
	 * first two lines of each sentence are in the format %sentence:روزی بود. 
	 * sample:
	 * %sentence:روزی بود.
	 * %
	 * 1	روزی		N		MOS			ARG2_GOAL_ENDSTATE		روز#n8		دوره زمانی§n-12603		2		NO
	 * 2	بود		V		ROOT		NULL					بودن#v3		رخداد§n-13136			0		NO 
	 * @param filename file name of input dataSet.
	 * @return
	 */
	public ArrayList<ArrayList<String>> importInputDataSetInfo(String filename)
	{
		ArrayList<ArrayList<String>> inputs = new ArrayList<ArrayList<String>>();
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening `" + filename + "` for reading input natural language texts!");
			e.printStackTrace();
		}
		
		String line = "";
		ArrayList<String> sentenceInfos = new ArrayList<String>();
		
		while (line != null)
		{
			try
			{
				if(sentenceInfos != null && sentenceInfos.size() > 2)
					sentenceInfos = new ArrayList<String>();
				
				if(line != null && line.startsWith("%"))
					sentenceInfos.add(line);
				
				
				line = stream.readLine();
				
				if (line == null)
					break;
				
				line = line.trim();
				
				while(!line.startsWith("%")){					
					
					sentenceInfos.add(line);
					
					line = stream.readLine();
					
					if (line == null)
						break;
					
					line = line.trim();				
				}
				if(sentenceInfos.size() > 2){
					inputs.add(sentenceInfos);				
//					sentenceInfos = new ArrayList<String>();
				}
											
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return inputs;
	}

	private void testKB(){
		
		System.out.println(" ------------ TESTING IMP1 ------------ ");
		
		//query: مواجه شدن#v(f˸نفر§n-13075)={f˸حیوان#n1} ?				
		Node arg = tts._TTSKb.addConcept("نفر§n-13075");
		Node desNode =  tts._TTSKb.addConcept("مواجه شدن#v");
		Node ref = tts._TTSKb.addConcept("حیوان#n1");
		
//		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		//query: مواجه شدن#v(f˸پسر#n2 [1])={f˸حیوان#n1} ?				
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode =  tts._TTSKb.addConcept("مواجه شدن#v");
		ref = tts._TTSKb.addConcept("حیوان#n1");
		
		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		//query: ISA(f˸پسر#n2 [1])={f˸نفر§n-13075} ?				
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode = KnowledgeBase.HPR_ISA;
		ref = tts._TTSKb.addConcept("نفر§n-13075");
		
//		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		
		//rule: s˸مواجه شدن#v(f˸نفر§n-13075)={f˸حیوان#n1}	IMP	f˸حس§n-14738(f˸نفر§n-13075)={کلافه§a-118}
		//query: f˸حس§n-14738(s˸پسر#n2 [1])={?}
		
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode =  tts._TTSKb.addConcept("حس§n-14738");
//		PlausibleStatement desc = (PlausibleStatement)desNode; 
//		desc.relationType = tts._TTSKb.addConcept("دیدن#v5 [1]");
		ref = null; //tts._TTSKb.addConcept("حیوان#n1");
		
		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		System.out.println(" ------------ END OF TESTING IMP1 ------ ");
	}

	private void testKb2() {
		
		System.out.println(" ------------ TESTING IMP ------------ ");
		
		// adding the required statements
		// 1: stating that 'the boy is a person'
		Node ourBoy = tts._TTSKb.addConcept("پسر داستان ما");
		Node person = tts._TTSKb.addConcept("نفر§n-13075");
		tts._TTSKb.addRelation(ourBoy, person, KnowledgeBase.HPR_ISA);
		
		//2: stating that a pigeon is an animal
		Node ourPigeon = tts._TTSKb.addConcept("کفتر داستان ما");
		Node animal = tts._TTSKb.addConcept("حیوان#n1"); 
		tts._TTSKb.addRelation(ourPigeon, animal, KnowledgeBase.HPR_ISA);
		
		//3: stating that our boy encountered the pigeon in our story
		Node encounters =  tts._TTSKb.addConcept("مواجه شدن#v");
		tts._TTSKb.addRelation(ourBoy, ourPigeon, encounters);
		
		//4: asking what was the boy's emotion
		Node sense =  tts._TTSKb.addConcept("حس§n-14738");
		
		tts.checkSemanticReasoner1(ourBoy, sense, null);
	}

	private void testKb2_2() {


	     System.out.println(" ------------ TESTING IMP ------------ ");
	    

	     // adding the required statements

	     // 1: stating that 'the boy is a person'

	     Node ourBoy = tts._TTSKb.addConcept("پسر داستان ما");

	     Node person = tts._TTSKb.addConcept("نفر§n-13075");

	     tts._TTSKb.addRelation(ourBoy, person, KnowledgeBase.HPR_ISA);

	    

	     //2: stating that a pigeon is an animal

	     Node ourPigeon = tts._TTSKb.addConcept("کفتر داستان ما");

	     Node animal = tts._TTSKb.addConcept("حیوان#n1");

	     tts._TTSKb.addRelation(ourPigeon, animal, KnowledgeBase.HPR_ISA);

	    

	     //3: stating that our boy encountered the pigeon in our story

//	     Node encounters =  tts._TTSKb.addConcept("مواجه شدن#v");
//	     Node encounters =  tts._TTSKb.addConcept("دیدن#v5");	
	     Node encounters =  tts._TTSKb.addConcept("دیدن#v5 [1]");

	     tts._TTSKb.addRelation(ourBoy, ourPigeon, encounters);

	    

	     //4: asking what was the boy's emotion

	     Node sense =  tts._TTSKb.addConcept("حس§n-14738");

	    
	     tts.checkSemanticReasoner1(ourBoy, sense, null);

	     
	     System.out.println(" ------------ END OF TESTING IMP2 ------ ");
	     
	}

	private void testKb3() {
		
		/*
		 *	پسر#n2 [1] ISA پور§n-12058
		 *	کبوتر#n1 [1] ISA کبوتر§n-24403
		 *	دیدن#v5 [1] ISA رویت کردن§v-8581
		 *	پسر#n2 [1] دیدن#v5 [1] کبوتر#n1 [1] 
		 */

	     System.out.println(" ------------ TESTING IMP3 ------------ ");
	    

	     // adding the required statements

	     // 1: stating that 'the boy is a person'

	     Node ourBoy = tts._TTSKb.addConcept("پسر#n2 [1]",false);

	     Node person = tts._TTSKb.addConcept("پور§n-12058",false);

	     tts._TTSKb.addRelation(ourBoy, person, KnowledgeBase.HPR_ISA);

	    

	     //2: stating that a pigeon is an animal

	     Node ourPigeon = tts._TTSKb.addConcept("کبوتر#n1 [1]",false);

	     Node animal = tts._TTSKb.addConcept("کبوتر§n-24403",false);

	     tts._TTSKb.addRelation(ourPigeon, animal, KnowledgeBase.HPR_ISA);

	    

	     //3: stating that our boy encountered the pigeon in our story

//	     Node encounters =  tts._TTSKb.addConcept("مواجه شدن#v");
//	     Node encounters =  tts._TTSKb.addConcept("دیدن#v5");	
	     Node ourEncounters =  tts._TTSKb.addConcept("دیدن#v5 [1]",false);
	     
	     Node encounters =  tts._TTSKb.addConcept("رویت کردن§v-8581",false);
	     
	     tts._TTSKb.addRelation(ourEncounters, encounters, KnowledgeBase.HPR_ISA);
	     
	     tts._TTSKb.addRelation(ourBoy, ourPigeon, ourEncounters);

	    

	     //4: asking what was the boy's emotion

	     Node sense =  tts._TTSKb.addConcept("حس§n-14738");

	    
	     tts.checkSemanticReasoner1(ourBoy, sense, null);

	     
	     System.out.println(" ------------ END OF TESTING IMP3 ------ ");
	     
	}
		
	
	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله ");
		
		UI ui = new UI();

		ui.clearSRLDataset();
		
//		StoryModel sm = ui.makeSyntaxDataset();
//		ui.manualReformatDataset();
//		ui.reformatDataset();
		//TODO: correct relation name in all of generating dataSet
		//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ الحمدلله");
		
	}
}




