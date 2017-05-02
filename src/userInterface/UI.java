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
import model.Word;
import sceneReasoner.TTSEngine;


@SuppressWarnings("unused")
public class UI {
	

//	private String inputStoryFilePath = "inputStory/inputStrory8.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory2-1.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory3.txt";
//	private String inputStoryFilePath = "inputStory/inputStory4.txt";

	private String inputDataSetHeaderFilePath = "inputStory/dataSetHeader.txt";
//	private String inputSRLDataSetFilePath = "inputStory/story4DataSetToformat.txt";
//	private String inputDataSetFilePath = "inputStory/srl-stories.conll";
//	private String inputDataSetFilePath = "inputStory/sampleManualInputDataSetToformat.arff";
//	private String inputDataSetFilePath = "inputStory/96-01-28cleanedJunkSRLDataSet.arff";
	private String inputDataSetFilePath = "inputStory/96-02-12cleanedJunk.arff";
	private String inputDataSetSceneCorrectedFilePath = "inputStory/96-02-12cleanedJunkScene.arff";
	private String inputDataSetSceneCorrectedDashedTunedFilePath = "inputStory/96-02-12cleanedJunkSceneDash.arff";
	
	private String persianStopWordFilePath = "inputStory/persianStopWrods.txt";

	//all sentences from CSRI
//	private String rawDataSetFilePath = "inputStory/cleanedSRLDataSet - Copy.arff";
	//2000 words with no semantic tags were deleted manually.
//	private String rawDataSetFilePath = "inputStory/cleanedSRLDataSet.arff";
	//2500 more words with no semantic tags were deleted manually.	
	private String rawDataSetFilePath = "inputStory/cleanedSRLDataSet3.arff";
//	private String rawDataSetFilePath = "inputStory/96-01-28cleanedJunkSRLDataSet.arff";
//	private String rawDataSetFilePath = "inputStory/96-01-21cleanedSRLDataSet-checked.arff";
	
	private String outCleanedWrongSemanticDataSetFilePath = "output/cleanedWrongSemSRLDataSet.arff";
	private String outCleanedJunkSRLDataSet = "output\\cleanedJunkSRLDataSet.arff";

//	private String mainKbFilePath = "kb/farsnet--24.txt";
	private String mainKbFilePath = "kb/farsnet.txt";
	private String myKbFilePath = "kb/injuredPigeon9.txt";
//	private String myKbFilePath = "kb/injuredPigeon_simple.txt";
//	private String myKbFilePath = "kb/injuredPigeon_SS.txt";
	private String verbSemanticCapacitiesPath = "kb/verbSemanticCapacities.txt";
	private String verbVisualCapacitiesPath = "kb/verbVisualCapacities.txt";
	private TTSEngine tts;
	
	
	
	public UI(){		
		//tts = new TTSEngine(mainKbFilePath, myKbFilePath);
	}
	
	/**
	 * main cycle of reading input sentences from user input file, and giving sentence to TTSEngine,
	 * and showing its output to the user.
	 */
//	public StoryModel makeSyntaxDataset(){
//		
//		//It must read sentence from user input.
//		//temporarily it reads all input story from file instead of getting from user!
//		ArrayList<String> inputs = importInputTexts(inputStoryFilePath);
//
//		ArrayList<String> current_scene_lines = new ArrayList<String>();
//		
//		int story_num = 0;		
//		StoryModel storyModel = new StoryModel("story"+story_num);		
//		
//		PrintWriter writer = null;
//		PrintWriter writer2 = null;
//		try {
////			writer = new PrintWriter("output\\sceneOutput.txt", "UTF-8");
//			writer = null;//new PrintWriter("output\\sentenceSyntax.txt", "UTF-8");
//			writer2 = new PrintWriter("output\\sentencePhrases.txt", "UTF-8");
//		} catch (FileNotFoundException | UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		
//		for(String line:inputs){
//			
//			if(line.equals("«داستان جدید»")){				
//				storyModel = new StoryModel("stroy" + ++story_num);
//				tts = new TTSEngine(mainKbFilePath, myKbFilePath, verbSemanticCapacitiesPath, verbVisualCapacitiesPath);
//				continue;
//			}
//			else if(line.equals("«صحنه جدید»")){
//				
//				//Then give the sentences of a scene to TTSEngine to enrich. 
//			 	tts.TextToScene(current_scene_lines, storyModel, false, writer, writer2);		 	
//				
//				current_scene_lines = new ArrayList<String>();
//				continue;
//			}			
//			current_scene_lines.add(line);			
//		}
//		//sentences of the last scene will be sent to TTSEngine to enrich. 
//	 	tts.TextToScene(current_scene_lines, storyModel, true, writer, writer2);
//
////		writer.close();
//		writer2.close();
//			
//	 	return storyModel;
//	}

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
	
	private SentenceModel makeSenteces(ArrayList<String> oneSentence, boolean isRecordFull){
		if(oneSentence == null)
			MyError.exit("bad input lines!");			
		
		String[] sentenceElem = new String[oneSentence.size()];
		
		for(int i = 0; i < sentenceElem.length; i++)				 
			sentenceElem[i] = oneSentence.get(i);
		
		return new SentenceModel("", sentenceElem, isRecordFull);
		
	}
	
	/**
	 * checks semantic tags of sentence to be correct.
	 * some sentences incorrectly have semantic tags in verbs instead of nouns depended to verbs.
	 * sample of incorrect: 
	 * 19	آمد	V	ROOT	0	Y	به‌دنیا‌آمدن.36	_	Arg1	
	 * 25	نمودند	V	ROOT	0	Y	دفن‌کردن.67	_	_	_	Arg0	 
	 */
	public void checkSemanticTags(){
		try {
			PrintWriter writer = new PrintWriter(outCleanedWrongSemanticDataSetFilePath, "UTF-8");//"output\\cleanedWrongSemSRLDataSet.arff", "UTF-8");
		
			PrintWriter wrongSemWriter = new PrintWriter("output\\wrongSems.arff", "UTF-8");
		
			ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(rawDataSetFilePath);
			
			//each oneSentence is an ArrayList containing the whole elements of one sentence.
			for(ArrayList<String> oneSentence:inputs){
				
				SentenceModel sentence = makeSenteces(oneSentence, false);
											
				if(sentence.hasWrongSemanticTags(wrongSemWriter))				
					continue;//this sentence semantic tags are incorrect so ignore it!					
								
				String toWrite = "";
				ArrayList<String> wrdRecs = sentence.getManualDataSetStr();
				if(!Common.isEmpty(wrdRecs))
					for(String rec:wrdRecs)
						writer.println(rec);
			
				//sign of new sentence
				writer.println();						
			}
			writer.close();			
			wrongSemWriter.close();
	

		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
			
	/**
	 * removes junk words from dataSet and transfer their
	 * _syntaxTag, _srcOfSynTag_number, _simpleSemanticTag, and _dataSetRecord 
	 * from junk words to the word that is depended to this junk word. 
	 */
	public void removeJunkWords(){
		
		try {			
			PrintWriter writer = new PrintWriter(outCleanedJunkSRLDataSet, "UTF-8");
			PrintWriter junksWriter = new PrintWriter("output\\junks.arff", "UTF-8");
			PrintWriter junkDepsWriter = new PrintWriter("output\\junkDeps.arff", "UTF-8");
			PrintWriter stopWordWriter = new PrintWriter("output\\deletedStopWords.arff", "UTF-8");			
			
			ArrayList<String> stopWords = importInputTexts(persianStopWordFilePath);
			
			//output\\cleanedWrongSemSRLDataSet.arff writed by method:checkSemanticTags();
			ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(outCleanedWrongSemanticDataSetFilePath);
			
			//each oneSentence is an ArrayList containing the whole elements of one sentence.
			for(ArrayList<String> oneSentence:inputs){
			
				SentenceModel sentence = makeSenteces(oneSentence, false);
				
				print(">>>>>>>>>>>>>>> before edit junk");
				print(sentence.getOrdinalDetailedStr());
				
				sentence.editJunkWords(stopWords, stopWordWriter, junksWriter, junkDepsWriter);	
				
				print(">>>>>>>>>>>>>>> after edit junk");
				print(sentence.getOrdinalDetailedStr() + "\n\n");
				
				String toWrite = "";
				ArrayList<String> wrdRecs = sentence.getManualDataSetStr();
				if(!Common.isEmpty(wrdRecs))
					for(String rec:wrdRecs)
						writer.println(rec);
				
				//sign of new sentence
				writer.println();						
			}																		
			
			writer.close();
			junksWriter.close();
			stopWordWriter.close();
			
		} 
		catch (FileNotFoundException | UnsupportedEncodingException e) {
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
	 * inserts a new line before and after scene marker; #.....
	 */
	public void correctSceneMarker(){
		try{
			PrintWriter writer = new PrintWriter(inputDataSetSceneCorrectedFilePath, "UTF-8");
			
			ArrayList<String> inputs = importInputTexts(inputDataSetFilePath);
			
			//correct writing scene marker
			for(String line:inputs)
				if(line != null && line.startsWith("#")){
					writer.println("\n" + line + "\n");
				}
				else 					
					writer.println(line);
			
			writer.println("");
			writer.close();
		}	 
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	/**
	 * first check the number of elements for all words of a sentence be equal.
	 * then shift _ from between of not_element to the end of not_elements. 
	 */
	public void checkAndCorrectDataSet(){
		try{
			PrintWriter writer = new PrintWriter(inputDataSetSceneCorrectedDashedTunedFilePath, "UTF-8");
			
			//file format: 4	برادر	N	OBJ	5	برادر§n-14090	_	نفر§n-13075	_	role	Arg1	_
			ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(inputDataSetSceneCorrectedFilePath);
					
			//each oneSentence is an ArrayList containing the whole elements of one sentence.
			for(ArrayList<String> oneSentence:inputs){				
				
				int partsNum = -1;
				
				for(String word:oneSentence){
				
					String[] parts = word.split("(\t)+");
					
					if(parts != null){
						if(partsNum == -1)
							partsNum = parts.length;
						else if(partsNum != parts.length)
							MyError.exit("wrong record format must be " + partsNum + "\n" + parts.length + ": " + word + " must have " + partsNum + " elments!");
					}
					else
						MyError.exit("bad word format + " + word);
				}
//				print("" + partsNum);
				
				//here we are sure that all words of a sentence have equal number of elements.
				for(String word:oneSentence){
					
	//				print("\nword before correct>>>>>>>>>>");
	//				print(word);
					
					String[] parts = word.split("(\t)+");
					
					if(parts != null){
						
						ArrayList<String> elems = new ArrayList<String>();
						for(String par:parts)						
							elems.add(par.trim());
											
						int _nums = 0;					
						
						for(int index = 0; index < 10  && index  < elems.size(); index++)
							if(elems.get(index).equalsIgnoreCase("_"))
								_nums++;
						
						for(int i = _nums; i > 0; i--)						
							elems.remove("_");	
						
						for(int i = 0; i < _nums; i++)								
							elems.add((8 + i), "_");			
	
	//					print("word after correct----------");
						String newWord = "";
						for(String elem:elems)
							newWord += elem + "\t";
						writer.println(newWord);
					}				
				}	
				writer.println("");
			}
			writer.close();
		}	 
		catch (FileNotFoundException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void getStatistics(){
		//file format: 4	برادر	N	OBJ	5	برادر§n-14090	_	نفر§n-13075	_	role	Arg1	_
		ArrayList<ArrayList<String>> inputs = importInputDataSetInfo(inputDataSetSceneCorrectedDashedTunedFilePath);
		
		int sentenceNum = countSentences(inputDataSetSceneCorrectedDashedTunedFilePath);
		
		int wordsNum = countWords(inputDataSetSceneCorrectedDashedTunedFilePath);
				
		print("sentence numbers: " + sentenceNum + " -- words numner " + wordsNum);
		
		//each oneSentence is an ArrayList containing the whole elements of one sentence.
		for(ArrayList<String> oneSentence:inputs){
			
			SentenceModel sentence = makeSenteces(oneSentence, true);
			
			if(sentence != null && sentence.getWords() != null){
				ArrayList<Word> words = sentence.getWords();
				
				for(Word wrd:words){
					print("" + wrd._multiClassTag);
				}
			}
			else
				MyError.exit("bad sentence generated from " + oneSentence);
		}
	}

	

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
			writer1 = new PrintWriter("output\\95-12-15sampleDataSetRole.arff", "UTF-8");
			writer2 = new PrintWriter("output\\95-12-15sampleDataSetrole_action.arff", "UTF-8");
			writer3 = new PrintWriter("output\\95-12-15sampleDataSetrole_state.arff", "UTF-8");
			writer4 = new PrintWriter("output\\95-12-15sampleDataSetrole_emotion.arff", "UTF-8");
			writer5 = new PrintWriter("output\\95-12-15sampleDataSetrole_intent.arff", "UTF-8");
			writer6 = new PrintWriter("output\\95-12-15sampleDataSetDyanmicObject.arff", "UTF-8");
			writer7 = new PrintWriter("output\\95-12-15sampleDataSetdynamic_object_action.arff", "UTF-8");
			writer8 = new PrintWriter("output\\95-12-15sampleDataSetdynamic_object_state.arff", "UTF-8");
			writer9 = new PrintWriter("output\\95-12-15sampleDataSetstatic_objectect.arff", "UTF-8");
			writer10 = new PrintWriter("output\\95-12-15sampleDataSetstatic_object_state.arff", "UTF-8");
			writer11 = new PrintWriter("output\\95-12-15sampleDataSetLocation.arff", "UTF-8");
			writer12 = new PrintWriter("output\\95-12-15sampleDataSetTime.arff", "UTF-8");
			writer13 = new PrintWriter("output\\95-12-15sampleDataSetscene_goal.arff", "UTF-8");
			writer14 = new PrintWriter("output\\95-12-15sampleDataSetscene_emotion.arff", "UTF-8");
			
			
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
				
			//each oneSentence is an ArrayList containing the whole elements of one sentence.
			for(ArrayList<String> oneSentence:inputs){
//				writer.println(tts.generateFullRecordAnalysis(sentenceInfo));
				
				//-----
				if(oneSentence == null)
					MyError.exit("bad input lines!");			
				
				String[] sentenceElem = new String[oneSentence.size()];
				
				for(int i = 0; i < sentenceElem.length; i++)				 
					sentenceElem[i] = oneSentence.get(i);
				
				SentenceModel sentence = new SentenceModel("", sentenceElem, true);
				
				print(sentence.getOrdinalDetailedStr());
									
				//corrected up to here **********************
		/*		
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
				
				*/
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

	private int countSentences(String filename) {
		
		int sentNum = 0;
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
				
			String line = stream.readLine();
			
			String beforeLine = null;
						
			while (line != null)
			{
				line = line.trim();
								
				if(line.length() == 0 && beforeLine != null){
					beforeLine = beforeLine.trim();
					
					if(beforeLine.length() != 0 && !beforeLine.startsWith("#"))
						sentNum++;
				}
				
				
				beforeLine = line;
				line = stream.readLine();			
			}
			
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		

		return sentNum;
	}

	
	private int countWords(String filename) {
		
		int wordsNum = 0;
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
				
			String line = stream.readLine();
						
			while (line != null)
			{
				line = line.trim();
								
				if(line.length() != 0 && !line.startsWith("%") && !line.startsWith("#"))
					wordsNum++;
					
				line = stream.readLine();			
			}
			
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}		

		return wordsNum;
	}

	
	/**
	 * returns all lines of input, including blank lines, excluding lines starts with % and # 
	 * @param filename
	 * @return guaranteed not to be null!
	 */
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
		
		try{			
		
			while (line != null)
			{
				line = stream.readLine();
				
				if (line == null)
					break;
				
				line = line.trim();				
							
				if (line.startsWith("%"))//|| line.startsWith("#")) // comment lines
					continue;
							
				inputs.add(line);

			}
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
	 * excluds lines starts with % and #
	 * sentences are separated with a blank line. 
	 * sample:
	 * 2	یوسف		N		MOZ		1	یوسف§n-23957	نفر§n-13075		role		_	_	_		_		_	
	 *  
	 * @param filename file name of input dataSet.
	 * @return guaranteed not to be null!
	 */
	public ArrayList<ArrayList<String>> importInputDataSetInfo(String filename)
	{
		ArrayList<ArrayList<String>> inputs = new ArrayList<ArrayList<String>>();
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
				
			String line = "";
			ArrayList<String> oneSentence = new ArrayList<String>();
			
			while (line != null)
			{
				line = stream.readLine();
				
				if(line != null)
					line = line.trim();
				else
					break;
				
				//sign of new sentence
				if(line.length() == 0){
					inputs.add(oneSentence);
					oneSentence = new ArrayList<String>();
					continue;
				}
				
				if(!line.startsWith("%")) //&& !line.startsWith("#"))
					oneSentence.add(line);										
			
			}
			
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

	private void print(String s){
		System.out.println(s);
	}
	
	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله ");
		
		UI ui = new UI();

//		ui.clearSRLDataset();
		
//		ui.checkSemanticTags();
		
//		ui.removeJunkWords();
		
//		ui.correctSceneMarker();
		
//		ui.checkAndCorrectDataSet();
		
		ui.getStatistics();

		//TODO: correct relation name in all of generating dataSet
		//&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
//		ui.reformatDataset();
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ الحمدلله");
		
	}
}





