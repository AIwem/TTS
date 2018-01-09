package userInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import sun.misc.CEFormatException;
import learningData.LWord;

public class LUI {
	
	public static String oldFullCorpora = "inputStory/96-02-24full_scene.arff";
	
	public static String removedCorpora = "inputStory/96-08-23full_scene_junk_dash_ArgM_WSD_space_corr2_ya_quran-dyn-made-rolle-loocation-rooz-dore.arff";
	
	public static String CRFcorpora = "dataset/96-10-18SemiCRFdataset";
	
	public static void LoadDataset(){		
		try {
			
			BufferedReader removedcorp = new BufferedReader(new InputStreamReader(new FileInputStream(removedCorpora),"utf-8"));			
			BufferedReader oldFullcorp = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullCorpora),"utf-8"));
//			BufferedReader oldFullcorp = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullCorpora),"utf-8"));
//			BufferedReader oldFullcorp = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullCorpora),"utf-8"));
//			BufferedReader oldFullcorp = new BufferedReader(new InputStreamReader(new FileInputStream(oldFullCorpora),"utf-8"));
			
			PrintWriter semiCRFcorp = new PrintWriter(CRFcorpora);
			
			String removedWordStr = "";			
			String allWordStr = "";
			
			while(removedWordStr != null /*|| allWordStr == null*/){
				
				removedWordStr = removedcorp.readLine();
				
				if(removedWordStr == null)
					break; //TODO: check if allWords remains!!!
				
				removedWordStr = removedWordStr.trim();
				
				if(removedWordStr == null || removedWordStr.equalsIgnoreCase("") || removedWordStr.startsWith("#") || removedWordStr.startsWith("%"))
					continue;				
								
				LWord crfWord = new LWord(removedWordStr);
								
				allWordStr = oldFullcorp.readLine();
				
			}
			
			removedcorp.close();
			oldFullcorp.close();
			semiCRFcorp.close();
			
		} catch (IOException e) {
			e.printStackTrace();			
		}		
	}
		
	/**
	 * this method adds those junk words (including verbs) which had semantic arguments in their records 
	 * from "96-02-24full_scene" file to "96-08-23full_scene_junk_dash_ArgM_WSD_space_corr2_ya_quran-dyn-made-rolle-loocation-rooz-dore" file.
	 * input file: those two mentioned above.
	 * output file: "96-10-18addedArgedJunks" 
	 */
	public static void addArgedJunks(){
		
//		"96-10-18addedArgedJunks"
		
	}
	
	private static void print(String s){
		System.out.println(s);
	}
	public static void main(String[] args){
		print("بسم الله الرحمن الرحیم و توکلت علی الله");
		LoadDataset();
		print("الحمدلله رب العالمین");
	}
}
