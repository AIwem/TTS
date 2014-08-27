package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
//import ir.ac.itrc.qqa.semantic.util.MyError;







import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sceneElement.Role;
import model.DEP;
import model.Part;
import model.SRL;
import model.SceneModel;
import model.SentenceModel;

/**
 * Preprocessor preprocesses the input natural language sentences.  
 * It can convert sentence in natural language to their SentenceModel.
 * It can also convert the SentenceModel to primary sceneModel.
 *  
 * @author hashemi
 *
 */
public class Preprocessor {
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */
	private String SentenceInfosFileName = "inputStory/sentenceInfos.txt";
	private KnowledgeBase _kb;
		
	public Preprocessor(KnowledgeBase kb) {
		this._kb = kb;
	}
		
	/**
	 * This method gets the stream of SentenceInfosFileName file and reads information 
	 * related to a specific sentence from it, 
	 * then return those lines of file which have information of different parts of this sentence.
	 * 
	 * @param stream  stream of SentenceInfosFileName file.
	 * @return those lines of file which have information of different parts of this sentence.
	 */
	private ArrayList<String> readSentenceParts(BufferedReader stream){		
				
		ArrayList<String> senParts = new ArrayList<String>(); 
		String content = "";

		try {
			content = stream.readLine();
			
			if (content == null)
				return null;
			//when loop terminates, stream has reached to the information of the next sentence.
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
		
	/**
	 * This method gets NLsentence as input and finds its processed information in SentenceInfosFileName file.
	 * 
	 * @param NLsentence
	 * @return informations of parts of this sentence.
	 */
	private ArrayList<String> findSentenceInfos(String NLsentence){		
		ArrayList<String> senPartStrs = null;
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
								
				//it means the next sentence in file has reached!
				if (line.equals("sentence:" + NLsentence)){

					//this array has information of all parts of this sentence. 
					senPartStrs = readSentenceParts(stream);					
					break;
				}
			}
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return senPartStrs;		
	}
	
	/**
	 * This method gets partStr and return the its equivalent Part object.
	 * partStr has all information about current Part. 
	 * this information has a format like this:
	 * name:پسرک	POS:NOUN	SRL:SBJ	WSD:پسرک	sub_part:پسر+ک
	 * name:پسر	POS:NOUN	SRL:SBJ-P	WSD:پسر#n2	sub_part:-
	 * name:ک	POS:UNKNOWN	SRL:SBJ-P	WSD:کوچک#a2	sub_part:-
	 * 
	 * @param partStr partStr has all information about current Part.
	 * @return equivalent Part Object.
	 */
	private Part createPart(String partStr){
		//print(partStr);			
		String[] parts = partStr.split("\t");
		
		if(parts.length != 6){
			//MyError.exit("Bad information format " + partStr);
			//MyError.error("Bad information format " + partStr);
			print("Bad information format " + partStr);
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
		if(!parts[3].equals("") && !parts[3].equals("-"))
			wsd = _kb.addConcept(parts[3]);
			//wsd = _kb.findConcept(parts[3]);  why addConcept instead of findConcept		
		
		newPart._wsd = wsd;
		
		if(parts[4] != null && !parts[4].trim().equals("-")){				
			String[] subs = parts[4].split("،");
			
			ArrayList<Part> subParts = new ArrayList<Part>();
			for(String s:subs){					
				subParts.add(new Part(s));
			}
			newPart.sub_parts = subParts;
		}
		
		switch(parts[5]){
			case "MAIN": newPart._dep = DEP.MAIN; break;
			case "PRE": newPart._dep = DEP.PRE; break;
			case "POST": newPart._dep = DEP.POST; break;
			default: newPart._dep = DEP.UNKOWN;
		}		
		//print(newPart.getStr() + "\n");
		return newPart;		
	}

	private void print(String s){
		System.out.println(s);
	}
		
	/**
	 * preprocessSentence first finds preprocessed information of this sentence from its related file.
	 * then convert this information to SentenceModel object 
	 * @param NLsentence sentence in natural language.
	 * @param senPartStrs contains informations of parts of this sentence.
	 * @return
	 */
	public SentenceModel preprocessSentence(String NLsentence) {
		SentenceModel sentence = null;
		
		//this array has information of all parts of this sentence.
		ArrayList<String> senPartStrs = findSentenceInfos(NLsentence);
		
		if(senPartStrs == null)
			return sentence; // it is null
		
		ArrayList<Part> senParts = new ArrayList<Part> ();
		
		for(int i = 0; i<senPartStrs.size();i++){
			String currentPartStr = senPartStrs.get(i);
			Part currentPart = createPart(currentPartStr);		
			
			//it means next line are informations of sub_parts of this current_part.
			// we have assumed that sub_parts has depth of 1. It means each sub_part has no sub_part in itself.
			if(currentPart != null && currentPart.hasSub_parts()){
				ArrayList<Part> subParts = new ArrayList<Part> (currentPart.sub_parts.size());
				
				for(int j = 0; j < currentPart.sub_parts.size() && (i+1)<senPartStrs.size(); j++){
					i++;
					String subPartStr = senPartStrs.get(i);
					Part sPart = createPart(subPartStr);					
					if(sPart != null)
						subParts.add(sPart);							
				}
				currentPart.sub_parts = subParts;							
			}						
			senParts.add(currentPart);
		}

		//now senParts has all part objects of this sentence.						
		sentence = SentenceModel.arrageSentenceParts(NLsentence, senParts);			
		return sentence;
		
	}
	
	/**
	 * preprocessScene preprocesses input SentenceModel and convert it to its equivalent SceneModel object.
	 * TODO: we have temporarily assumed that every sentence has single subject, single object (if any), and single adverb (if any).
	 *   
	 * @param sentenceModel the SenetenceModel to be converted.
	 * @return SceneModel equivalent to input SentenceModel
	 */
	public SceneModel preprocessScene(SentenceModel sentenceModel){
		
		SceneModel sm = new SceneModel();
		
		if(sentenceModel == null)
			return null;
		
		//Part sbj = sentenceModel.getSingleSubject();
		//if(sbj != null && sbj.isSubject()){
		Part obj = sentenceModel.getSingleObject();
		if(obj != null && obj.isObject()){
			Node wsd = obj._wsd;			
			if(wsd != null){// it means that the subject itself has a WSD object and has an equivalent concept in KB.
				Role role = new Role(obj._name, wsd);			
				sm.addRole(role);							
			}
			else{ // it means that the subject is a noun-phrase and has sub_parts.
				if(obj.hasSub_parts()){
					Part mainPart = obj.getMainSub_part();
					if(mainPart == null){
						//MyError.error("subject's sub_parts has no main part!");
						return null;
					}					
					wsd = mainPart._wsd;		
					if(wsd != null){// TODO: create pluasibleTerm
						obj._wsd = wsd;
						Role role = new Role(obj._name, wsd);			
						sm.addRole(role);							
					}
					
				}
				else{
					//MyError.error("bad sentence obj, no wsd in it nor in sub_parts! " + sentenceModel);
					return null;					
				}				
			}
			return sm;
		}
		else{
			//MyError.error("bad sentence obj, no subject included! " + sentenceModel);
			return null;
		}				
	}

	/*public static void main(String[] args) {

	}*/

}
