package userInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import learningData.LSentence;
import learningData.LWord;
import model.ScenePart;

public class LUI {
	
	public static String removedCorpora = "inputStory/96-10-23full_scene_junk_dash_ArgM_WSD_space_corr2_ya_quran-dyn-made-rolle-loocation-rooz-dore-rSharp.arff";
	public static String oldFullCorpora = "inputStory/96-10-24cleanedWrongSemSRLDataSet.arff";
//	public static String junkCorpora = "inputStory/junks.arff";
//	public static String deletedStopWordCorpora = "inputStory/deletedStopWords.arff";
	
	public static String CRFcorpora = "dataset/96-10-24CRFdataset.arff";
	
	public static ArrayList<LSentence> LoadDataset(String corporaName){
		
		ArrayList<LSentence> sentences = new ArrayList<LSentence>();
		
		try {
			
			BufferedReader corp = new BufferedReader(new InputStreamReader(new FileInputStream(corporaName),"utf-8"));			
								
			String wordStr = "";			
						
			ArrayList<LWord> words = new ArrayList<LWord>();
			
			int wordCount =  0;
			int sentCount = 0;
			
			while(wordStr != null){
				
				wordStr = corp.readLine();
				
				if(wordStr == null)
					break;
				
				wordStr = wordStr.trim();
				
				//marker of new scene or comment.
				if(wordStr == null || wordStr.startsWith("#") || wordStr.startsWith("%"))
					continue;				
					
				//end of sentence.
				if(wordStr.equalsIgnoreCase("")){
					if(words.size() > 0){
						LSentence sent = new LSentence(words);
						sentences.add(sent);
						sentCount++;
//						print(sent + "");
						words = new ArrayList<LWord>();
					}					
					continue;
				}
					
				LWord crfWord = new LWord(wordStr);
				words.add(crfWord);	
				wordCount++;
			}
			
			print("words: " + wordCount + "  sentences: " + sentCount + " in file: \"" + corporaName.substring(corporaName.indexOf("/")+1) + "\"");
			
			corp.close();
			
		} catch (IOException e) {
			e.printStackTrace();			
		}
		return sentences;		
	}
	/**
	 * replace sentences with full information from file:
	 * 96-10-23full_scene_junk_dash_ArgM_WSD_space_corr2_ya_quran-dyn-made-rolle-loocation-rooz-dore-rSharp
	 * into oldFullFile:
	 * cleanedWrongSemSRLDataSet
	 */
	public static void completeSentences2(){
		try {
			ArrayList<LSentence> removedSentences =  LoadDataset(removedCorpora);
			ArrayList<LSentence> oldFullSentences =  LoadDataset(oldFullCorpora);			
			ArrayList<LSentence> completeSentences = new ArrayList<LSentence>();
			
			PrintWriter CRFcorp = new PrintWriter(CRFcorpora, "UTF-8");
									
			for(int i = 0; i < oldFullSentences.size(); i++){
				//for each sentence
				LSentence removedSent = removedSentences.get(i);
				LSentence oldFullSent = oldFullSentences.get(i);
				
				LSentence compSent = new LSentence();
				completeSentences.add(compSent);
			
//				print("removed:");
//				print(removedSent);
//				print("oldFull:");
//				print(oldFullSent);
				
				for(LWord oldWord:oldFullSent.getWords()){
					if(removedSent.hasWordwithNumber(oldWord._number))
						compSent.addWord(removedSent.getWordwithNumber(oldWord._number));
					else{
						oldWord._wsd_name = "null";
						oldWord._super_wsd_name = "null";
						oldWord._scenePart = ScenePart.NO;
						compSent.addWord(oldWord);
					}
				}
				print(compSent);
				CRFcorp.println(compSent.getCompleteStr());			
			}
						
			CRFcorp.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
		}			

		
	}
		
	public static void completeSentences(){
			
		try {
			ArrayList<LSentence> removedSentences =  LoadDataset(removedCorpora);
			ArrayList<LSentence> oldFullSentences =  LoadDataset(oldFullCorpora);
//			ArrayList<LSentence> junks =  LoadDataset(junkCorpora);
//			ArrayList<LSentence> deletedStopWords =  LoadDataset(deletedStopWordCorpora);
//			@SuppressWarnings("unused")
			ArrayList<LSentence> cleanedWords =  LoadDataset("inputStory/cleanedWrongSemSRLDataSet.arff");
			
			
			ArrayList<LSentence> allFullSentences =  new ArrayList<LSentence>();
			
			int junkIndex = 0;
			int deletedSWIndex = 0;
			
			
			for(int i = 0; i < removedSentences.size(); i++){

				//for each sentence
				LSentence removedSent = removedSentences.get(i);
				LSentence oldFullSent = oldFullSentences.get(i);
				
//				LSentence junkSent = junks.get(junkIndex);
//				LSentence deletedSWSent = deletedStopWords.get(deletedSWIndex);
//				
				LSentence fullSent = new LSentence();
				allFullSentences.add(fullSent);
				
				ArrayList<Integer>  missedWordIndices = removedSent.getMissedWordIndices();
				ArrayList<Integer> foundIndices = new ArrayList<Integer>();
				
				
//				print("removed sentence: ");
//				print(removedSent);
//				print("oldFull sentence: ");
//				print(oldFullSent);
				print("-----------------------------------------------" + (i+1) + ": " + removedSent);
				printList(missedWordIndices);
				
				for(int miss:missedWordIndices){
					
					if(oldFullSent.hasWordwithNumber(miss)){
					
						LWord missWord = oldFullSent.getWordwithNumber(miss);
						
						if(missWord != null){
							
							fullSent.addWord(missWord);
							foundIndices.add(miss);
						}
						else
							throw new Exception("sentence " + oldFullSent + " has the word " + missWord + " but didn't get it!");
					}
				}
				
				for(int found:foundIndices)
					missedWordIndices.remove((Integer)found);
				
				print("after searching in oldFullSent");						
				printList(missedWordIndices);
	
//				print("junk sentence: ");
//				print(junkSent);	
				
				boolean evenOne = false;
				foundIndices = new ArrayList<Integer>();
				
//				for(int miss:missedWordIndices){
//					
//					if(junkSent.hasWordwithNumber(miss)){
//					
//						LWord missWord = junkSent.getWordwithNumber(miss);
//						
//						if(missWord != null){
//							
//							fullSent.addWord(missWord);
//							foundIndices.add(miss);
//							evenOne = true;
//						}
//						else
//							throw new Exception("sentence " + junkSent + " has the word " + missWord + " but didn't get it!");
//					}
//				}
//				
				for(int found:foundIndices)
					missedWordIndices.remove((Integer)found);
				
				if(evenOne)
					junkIndex++;
				
				print("after searching in junk");						
				printList(missedWordIndices);
				
			}
			
			PrintWriter CRFcorp = new PrintWriter(CRFcorpora);
			
			CRFcorp.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
		}			
	}
	
	private static void print(String s){
		System.out.println(s);
	}
	
	private static void print(LSentence sentence){
		if(sentence == null)
			System.out.println("");
		else
			System.out.println(sentence.getCompleteStr());
	}
	
	private static void printList(ArrayList<Integer> list){
		for(int obj:list)
			System.out.print(obj + " ");
		System.out.println();
	}

	
	public static void main(String[] args){
		print("بسم الله الرحمن الرحیم و توکلت علی الله");
		
//		ArrayList<LSentence> allSentences =  LoadDataset(removedCorpora);
		completeSentences2();
		
		print("الحمدلله رب العالمین");
	}
}

