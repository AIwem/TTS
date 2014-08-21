package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.Part;
import model.SRL;
import model.SceneModel;
import model.SentenceInfo;
/**
 * Preprocessor preprocesses the input natural language sentences and 
 * first converts it to the SentenceInfo
 * second converts the SentenceInfo to primary sceneModel. 
 * @author hashemi
 *
 */
public class Preprocessor {
	
	private String SentenceInfosFileName = "inputStory/sentenceInfos.txt";
	private KnowledgeBase _kb;
		
	public Preprocessor(KnowledgeBase kb) {
		this._kb = kb;
	}


	public SentenceInfo preprocessSentence(String NLsentence){
		
		SentenceInfo sentence = new SentenceInfo(NLsentence);
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(SentenceInfosFileName), "utf-8"));
		}
		catch(Exception e)
		{
			System.out.println("Error opening `" + SentenceInfosFileName + "` for reading input natural language texts!");
			e.printStackTrace();
		}
			
		try {
			String line = "";
			while (line != null)
			{				
				line = stream.readLine();
				
				if(line == null)
					break;
				
				if(line.equals(""))
					continue;	
												
				if (line.startsWith("#")) // comment line
					continue;
								
				if (line.equals("sentence:" + NLsentence)){					
					ArrayList<String> senParts = readSentenceParts(stream, sentence);
					Part p = createPart(senParts);
					break;
				}
			}
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return sentence;		
	}
	
	
	private ArrayList<String> readSentenceParts(BufferedReader stream, SentenceInfo sentence){		
				
		ArrayList<String> senParts = new ArrayList<String>(); 
		String content = "";

		try {
			content = stream.readLine();
			
			if (content == null)
				return null;
			
			while(!content.contains("sentence:")){
				
				if(!content.startsWith("#"))// not comment line
					
					if(!content.equals(""))
						senParts.add(content);
				
				content = stream.readLine();
				
				if(content == null)
					break;
			}
		
		} catch (IOException e) {				
			e.printStackTrace();
		}
		return senParts;
	}
	
	/**senParts has all parts of preprocessed  information about current sentence.
	 * this information has a format like this:
	 * sentence:پسرک در راه خانه چشمش به یک کبوتر زخمی افتاد
	 * name:پسرک	POS:NOUN	SRL:SBJ	WSD:پسرک	sub_part:پسر+ک
	 * name:پسر	POS:NOUN	SRL:SBJ-P	WSD:پسر#n2	sub_part:-
	 * name:ک	POS:UNKNOWN	SRL:SBJ-P	WSD:کوچک#a2	sub_part:-
	 *
	 * @param stream
	 * @param sentence
	 * @return
	 */
	private Part createPart(ArrayList<String> senParts){
		String[] parts;
		
		for(String partStr:senParts){
			print(partStr);			
			parts = partStr.split("\t");
			
			if(parts.length != 5){
				print("bad sentence Info format: " + partStr);
				return null;
			}
						
			for(int i = 0;i<parts.length;i++)
				parts[i] = parts[i].substring(parts[i].indexOf(":")+1);				
			
			Part newPart = new Part(parts[0]);			
			
			switch(parts[1]){
				case "NOUN": newPart._pos = POS.NOUN; break;
				case "VERB": newPart._pos = POS.VERB; break;
				case "ADJECTIVE": newPart._pos = POS.ADJECTIVE; break;
				case "SETELLITE_ADJECTIVE": newPart._pos = POS.SETELLITE_ADJECTIVE; break;
				case "ADVERB": newPart._pos = POS.ADVERB; break;
				case "ANY": newPart._pos = POS.ANY; break;
				case "UNKNOWN": newPart._pos = POS.UNKNOWN; break;
				default: newPart._pos = POS.UNKNOWN;
			}
				
			switch(parts[2]){
				case "SBJ": newPart._srl = SRL.SBJ; break;
				case "SBJ_P": newPart._srl = SRL.SBJ_P; break;
				case "VERB": newPart._srl = SRL.VERB; break;
				case "VERB_P": newPart._srl = SRL.VERB_P; break;
				case "OBJ": newPart._srl = SRL.OBJ; break;
				case "OBJ_P": newPart._srl = SRL.OBJ_P; break;
				case "ADV": newPart._srl = SRL.ADV; break;
				case "ADV_P": newPart._srl = SRL.ADV_P; break;
				default: newPart._srl = SRL.UNKNOWN;
			}
			
			Node wsd = null;
			if(!parts[3].equals(""))
				wsd = _kb.findConcept(parts[3]);
			newPart._wsd = wsd;
			
			if(parts[4] != null & !parts[4].trim().equals("-")){
				String[] subs = parts[4].split("+");
				ArrayList<Part> subParts = new ArrayList<Part>();
				for(String s:subs){					
					subParts.add(new Part(s));
				}
				newPart.sub_parts = subParts;
				
			}
			
			print(newPart.toString() + "\n");
			
			/*
			 * ک		wsd
			 *  
			 * راه	wsd
			 * خانه	wsd
			 *  
			 * یک	wsd
			 * کبوتر	wsd
			 * زخمی	wsd
			 * 
			 * افتاد	wsd
			 */
			
			
		}
		return null;
		
		
	}
	
	public void print(String s){
		System.out.println(s);
	}

	
	public SceneModel preprocessScene(SentenceInfo sentenceInfo){
		return null;		
	}

	/*public static void main(String[] args) {

	}*/

}
