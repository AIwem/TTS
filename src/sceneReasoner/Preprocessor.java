package sceneReasoner;

import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sceneElement.*;
import model.SentencePart;
import model.SceneModel;
import model.ScenePart;
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
	
	private KnowledgeBase _kb;
//	private SemanticReasoner _re;
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */
	//private String SentenceInfosFileName = "inputStory/sentenceInfos2_simple.txt";
	//private String SentenceInfosFileName = "inputStory/sentenceInfos_SS.txt";
	private String SentenceInfosFileName = "inputStory/sentenceInfos3.txt";
		
	public Preprocessor(KnowledgeBase kb, SemanticReasoner re) {
		this._kb = kb;
	//	this._re = re;
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
	private SentencePart createPart(String partStr, SentenceModel senteceModel){
		//print(partStr);			
		String[] parts = partStr.split("\t");
		
		if(parts.length != 6){			
			//MyError.error("Bad information format " + partStr);
			print("Bad sentence information format " + partStr);
			return null;
		}
					
		for(int i = 0;i<parts.length;i++)
			parts[i] = parts[i].substring(parts[i].indexOf(":")+1);				
		
		SentencePart newPart = new SentencePart(parts[0], senteceModel);			
		
		newPart.set_pos(parts[1]);
			
		newPart.set_srl(parts[2]);
		
		newPart.set_wsd_name(parts[3]);
		
		
		if(parts[4] != null && !parts[4].trim().equals("-")){				
			String[] subs = parts[4].split("،");
			
			ArrayList<SentencePart> subParts = new ArrayList<SentencePart>();
			for(String s:subs){					
				subParts.add(new SentencePart(s, senteceModel));
			}
			newPart.sub_parts = subParts;
		}
		
		newPart.set_dep(parts[5]);	
		
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
		SentenceModel sentence = new SentenceModel(NLsentence);
		
		//this array has information of all parts of this sentence.
		ArrayList<String> senPartStrs = findSentenceInfos(NLsentence);
		
		if(senPartStrs == null)
			return sentence; // it is null
		
		ArrayList<SentencePart> senParts = new ArrayList<SentencePart> ();
		
		for(int i = 0; i<senPartStrs.size();i++){
			String currentPartStr = senPartStrs.get(i);
			SentencePart currentPart = createPart(currentPartStr, sentence);		
			
			//it means next line are informations of sub_parts of this current_part.
			// we have assumed that sub_parts has depth of 1. It means each sub_part has no sub_part in itself.
			if(currentPart != null && currentPart.hasSub_parts()){
				ArrayList<SentencePart> subParts = new ArrayList<SentencePart> (currentPart.sub_parts.size());
				
				for(int j = 0; j < currentPart.sub_parts.size() && (i+1)<senPartStrs.size(); j++){
					i++;
					String subPartStr = senPartStrs.get(i);
					SentencePart sPart = createPart(subPartStr, sentence);					
					if(sPart != null)
						subParts.add(sPart);							
				}
				currentPart.sub_parts = subParts;							
			}						
			senParts.add(currentPart);
		}

		//now senParts has all part objects of this sentence.						
		sentence.arrageSentenceParts(NLsentence, senParts);			
		return sentence;
		
	}
	
	
	/**
	 * preprocessScene preprocesses input SentenceModel and adds it to the primarySceneModel.
	 * TODO: we have temporarily assumed that every sentence has single subject, single object (if any), and single adverb (if any).   
	 * 
	 * @param sentenceModel the SenetenceModel to be converted.
	 * @param primarySceneModel the primary SceneModel which information of sentenceModel is to be added.
	 * @return SceneModel equivalent to input SentenceModel 
	 */	 
	public void preprocessScene(SentenceModel sentenceModel, SceneModel primarySceneModel){
		
		if(sentenceModel == null){
			MyError.error("senetecenModel should not be null! " + sentenceModel);
			return;		
		}		
		if(primarySceneModel == null){
			MyError.error("primarySceneModel should not be null! " + primarySceneModel);
			return;		
		}
		preprocessSubject(sentenceModel, primarySceneModel);		
		print("\nafter subject");
		primarySceneModel.printDictionary();
				
		preprocessObject(sentenceModel, primarySceneModel);		
		print("\nafter object");
		primarySceneModel.printDictionary();
		
		preprocessAdverb(sentenceModel, primarySceneModel);		
		print("\nafter adverb");
		primarySceneModel.printDictionary();
		
		preprocessVerb(sentenceModel, primarySceneModel);		
		print("after verb");
		primarySceneModel.printDictionary();		 
	}	

	/**
	 * This method maps part's wsd parameter to a concept in _kb based on part's wsd_name parameter.
	 * if part's wsd_name is "-" no mapping occurs.
	 * if part's wsd_name has just one part, it is the main concept name, so it must directly maps to a node in _kb.
	 * if part's wsd_name has more than one part which includes one MAIN and probably a PRE or POST, so it must be mapped to a plausible statement in a _kb.
	 * 
	 * @param part the part its wsd parameter to be set.
	 * @param sceneModel the sceneModel which part belongs to.
	 */
	private void allocate_wsd(SentencePart part, SceneModel sceneModel){
		if(part == null || sceneModel == null)
			return;
			
		String wsd_name = part._wsd_name;
		
		if(wsd_name == null || wsd_name.equals("-"))
			return;
		
		//it means that it is not just node but plausible statement for example MAIN_وضعیت سلامتی_POST
		if(wsd_name.indexOf("_") != -1){
			String[] sp = wsd_name.split("_"); //MAIN_وضعیت سلامتی_POST --> [MAIN, وضعیت سلامتی, POST]
		
			Node argument = null;
			Node referent = null;
			Node descriptor = null;
			
			PlausibleStatement wsd = null;
			
			String relation_name = null;
			
			SentencePart main_sub_part = null;
			SentencePart pre_sub_part = null;
			SentencePart post_sub_part = null;
			
			//node position in plausible statement, PRE, MAIN, POST, or a descriptor name
			for(String node_pos:sp){				
						
				//node_pos is node's position in plausible statement, PRE, MAIN, POST, or a descriptor name
				switch(node_pos){
				case("MAIN"):
					main_sub_part = part.getMainSub_part();					
					argument = sceneModel.findorCreateInstance(main_sub_part._wsd_name, false);
					main_sub_part.set_wsd(argument);
					break;					
				case("PRE"):
					pre_sub_part = part.getPreSub_part();
					Node pre = sceneModel.findorCreateInstance(pre_sub_part._wsd_name, false);
					pre_sub_part.set_wsd(pre);				
					//TODO: to complete the "PRE" DEP  
					MyError.error("I don't know what to do with this PRE DEP sub_part " + pre_sub_part);
					break;
				case("POST"):										
					post_sub_part = part.getPostSub_part();
					referent = sceneModel.findorCreateInstance(post_sub_part._wsd_name, false);
					post_sub_part.set_wsd(referent);
					break;
				//node_pos is a descriptor_name.
				default:
					relation_name = node_pos;
					wsd = sceneModel.findRelationInstance(relation_name);
					if(wsd == null){
						//it must got directly fetched from kb, and then addRelation will clone it.
						descriptor = _kb.addConcept(relation_name);
					}
				}
			}
			
			if(descriptor != null){//it means that findRelation has not found it and it is newly fetched from kb.
				wsd = _kb.addRelation(argument, referent, descriptor);				
				sceneModel.addRelationInstance(descriptor.getName(), wsd);
			}
			else{//it means that findRelation has found this relation.
				
				//it means that this relation must be different with the seen one! I have checked this logic it seems to be correct! 
				if(wsd.argument != argument || wsd.referent != referent){
					descriptor = _kb.addConcept(relation_name);
					wsd = _kb.addRelation(argument, referent, descriptor);					
					sceneModel.addRelationInstance(relation_name, wsd);					
				}
			}
			part.set_wsd(main_sub_part._wsd);
			//return;
		}
		else{
			//it means this part wsd_name is just one concept name, so we find or add it in sceneModel.			
			Node wsd = sceneModel.findorCreateInstance(wsd_name, false);
			part.set_wsd(wsd);
		}
		if(part.hasSub_parts())
			for(SentencePart p:part.sub_parts)
				if(p._wsd == null){																				
					Node wsd = sceneModel.findorCreateInstance(p._wsd_name, false);
					p.set_wsd(wsd);
				}
	}

	/**
	 * this method based on the ScenePart of the part adds a Role, DynamicObject, StaticObject, or ... to primarySceneModel. 
	 * 
	 * @param part the _wsd of this SentencePart object is set.
	 * @param primarySceneModel
	 */
	private void addToPrimarySceneModel(SentencePart part, SceneModel primarySceneModel){
		if(part == null)
			return;
		
		ScenePart sp = primarySceneModel.whichScenePart(part._wsd);				
		
		if(sp == ScenePart.ROLE){				
			Role role = new Role(part._name, part._wsd);				
			primarySceneModel.addRole(role);				
		}
		else if(sp == ScenePart.DYNAMIC_OBJECT){				
			DynamicObject dynObj = new DynamicObject(part._name, part._wsd);
			primarySceneModel.addDynamic_object(dynObj);				
		}
		else if(sp == ScenePart.STATIC_OBJECT){				
			StaticObject staObj = new StaticObject(part._name, part._wsd);
			primarySceneModel.addStatic_object(staObj);				
		}
		else if(sp == ScenePart.LOCATION){
			Location location = new Location(part._name, part._wsd);
			primarySceneModel.setLocation(location);			
		}
		else if(sp == ScenePart.ACTION){
			print(part._senteceModel.getOriginalSentence());
		}
	}
	
	private void preprocessSubject(SentenceModel sentenceModel, SceneModel primarySceneModel){
		SentencePart sbj = sentenceModel.getSingleSubject();
		
		if(sbj != null && !sbj.isSubject()){
			MyError.error("bad subjct part " + sbj);
			return;
		}		
		if(sbj != null && sbj.isSubject()){
			
			//_wsd of sbj is set to proper Node of KB.
			allocate_wsd(sbj, primarySceneModel);	
			
			if(sbj._wsd != null)
				addToPrimarySceneModel(sbj, primarySceneModel);			
			else
				MyError.error(sbj._wsd.getName() + " couldn't get allocated!");
		}		
	}
	
	private void preprocessObject(SentenceModel sentenceModel, SceneModel primarySceneModel){
		SentencePart obj = sentenceModel.getSingleObject();
		
		if(obj != null && !obj.isObject()){
			MyError.error("bad obejct part " + obj);
			return;
		}		
		if(obj != null && obj.isObject()){
			
			//_wsd of obj is set to proper Node of KB.
			allocate_wsd(obj, primarySceneModel);
			
			if(obj._wsd != null)			
				addToPrimarySceneModel(obj, primarySceneModel);
			else
				MyError.error(obj._wsd.getName() + " couldn't get allocated!");
		}		
	}
	
	private void preprocessAdverb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		SentencePart adv = sentenceModel.getSingleAdverb();
		
		if(adv != null && !adv.isAdverb()){
			MyError.error("bad adverb part " + adv);
			return;
		}		
		if(adv != null && adv.isAdverb()){
			
			//_wsd of adv is set to proper Node of KB.
			allocate_wsd(adv, primarySceneModel);
			
			if(adv._wsd != null)			
				addToPrimarySceneModel(adv, primarySceneModel);
			else
				MyError.error(adv._wsd.getName() + " couldn't get allocated!");
		}	
		
	}
	
	private void preprocessVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		SentencePart verb = sentenceModel.getVerb();
		
		if(verb != null && !verb.isVerb()){
			MyError.error("bad verb part " + verb);
			return;
		}		
		if(verb != null && verb.isVerb()){
			
			//_wsd of verb is set to proper Node of KB.
			allocate_wsd(verb, primarySceneModel);	
			
			if(verb._wsd != null)
				addToPrimarySceneModel(verb, primarySceneModel);			
			else
				MyError.error(verb._wsd_name + " couldn't get allocated!");
		}		


	}	
}
