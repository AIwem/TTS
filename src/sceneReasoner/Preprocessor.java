package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.SourceType;
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
import model.CONTEXT;
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
	private TTSEngine _ttsEngine = null;
	
	private String verb_root_name = "verb#v";
	private ArrayList<PlausibleStatement> default_contexts = null;
	
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */
	//private String SentenceInfosFileName = "inputStory/sentenceInfos2_simple.txt";
	//private String SentenceInfosFileName = "inputStory/sentenceInfos_SS.txt";
	private String SentenceInfosFileName = "inputStory/sentenceInfos4.txt";
		
	
	
	public Preprocessor(KnowledgeBase kb, SemanticReasoner re, TTSEngine ttsEngine) {
		this._kb = kb;
//		this._re = re;
		this._ttsEngine = ttsEngine;
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
	 * preprocessScene preprocesses input sentenceModel and adds it to the primarySceneModel.    
	 * 
	 * @param sentenceModel the SenetenceModel to be converted. 
	 * @return SceneModel equivalent to input sentenceModel. 
	 */	 
	public SceneModel preprocessScene(SentenceModel sentenceModel){		
		
		if(sentenceModel == null){
			MyError.error("senetecenModel should not be null! " + sentenceModel);
			return null;
		}
		SceneModel primarySceneModel = new SceneModel();

		primarySceneModel.addSentence(sentenceModel);
		sentenceModel.setScene(primarySceneModel);
		
		preprocessSubject(sentenceModel, primarySceneModel);		
				
		preprocessObject(sentenceModel, primarySceneModel);
		
		preprocessAdverb(sentenceModel, primarySceneModel);
		print("\n before verb");
		_ttsEngine.printDictionary();
		
		preprocessVerb(sentenceModel, primarySceneModel);
		print("\n after verb");
		_ttsEngine.printDictionary();
		
		print("primarySceneModel\n" + primarySceneModel);
		return primarySceneModel;
	}	

	/**
	 * This method maps part's wsd parameter to a concept in _kb based on part's wsd_name parameter.
	 * if part's wsd_name is "-" no mapping occurs.
	 * if part's wsd_name has just one part, it is the main concept name, so it must directly maps to a node in _kb.
	 * if part's wsd_name has more than one part which includes one MAIN and probably a PRE or POST, so it must be mapped to a plausible statement in a _kb.
	 * 
	 * @param part the part its wsd parameter to be set.
	 * @param _ttsEngine the sceneModel which part belongs to.
	 */
	private void allocate_wsd(SentencePart part){
		if(part == null || _ttsEngine == null)
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
					argument = _ttsEngine.findorCreateInstance(main_sub_part._wsd_name, false);
					main_sub_part.set_wsd(argument);
					break;					
				case("PRE"):
					pre_sub_part = part.getPreSub_part();
					Node pre = _ttsEngine.findorCreateInstance(pre_sub_part._wsd_name, false);
					pre_sub_part.set_wsd(pre);				
					//TODO: to complete the "PRE" DEP  
					MyError.error("I don't know what to do with this PRE DEP sub_part " + pre_sub_part);
					break;
				case("POST"):										
					post_sub_part = part.getPostSub_part();
					referent = _ttsEngine.findorCreateInstance(post_sub_part._wsd_name, false);
					post_sub_part.set_wsd(referent);
					break;
				//node_pos is a descriptor_name.
				default:
					relation_name = node_pos;
					wsd = _ttsEngine.findRelationInstance(relation_name);
					if(wsd == null){
						//it must got directly fetched from kb, and then addRelation will clone it.
						descriptor = _kb.addConcept(relation_name);
					}
				}
			}
			
			if(descriptor != null){//it means that findRelation has not found it and it is newly fetched from kb.
				wsd = _kb.addRelation(argument, referent, descriptor, SourceType.TTS);				
				_ttsEngine.addRelationInstance(descriptor.getName(), wsd);
			}
			else{//it means that findRelation has found this relation.
				
				//it means that this relation must be different with the seen one! I have checked this logic it seems to be correct! 
				if(wsd.argument != argument || wsd.referent != referent){
					
					ArrayList<PlausibleStatement> allInst = _ttsEngine.getRelationAllInstances(relation_name);
					if(allInst != null)
						for(PlausibleStatement relInst:allInst)						
							if(relInst.argument == argument && relInst.referent == referent){
								descriptor = relInst;
								break;
							}
						
					if(descriptor == null){
						descriptor = _kb.addConcept(relation_name);
						wsd = _kb.addRelation(argument, referent, descriptor, SourceType.TTS);					
						_ttsEngine.addRelationInstance(relation_name, wsd);
					}
				}
			}
			part.set_wsd(main_sub_part._wsd);
			//return;
		}
		else{
			//it means this part wsd_name is just one concept name, so we find or add it in sceneModel.			
			Node wsd = _ttsEngine.findorCreateInstance(wsd_name, false);
			part.set_wsd(wsd);
		}
		if(part.hasSub_parts())
			for(SentencePart p:part.sub_parts)
				if(p._wsd == null){																				
					Node wsd = _ttsEngine.findorCreateInstance(p._wsd_name, false);
					p.set_wsd(wsd);
				}
	}

	/**
	 * this method based on the ScenePart of the part adds a Role, DynamicObject, StaticObject, or ... to primarySceneModel.
	 * TODO: we have assumed for simplicity which every scene has a unique Role, DyanamicObject, and StaticObject with a one name.
	 * for example all «پسرک» refer to just one Role. 
	 * 
	 * @param part the _wsd of this SentencePart object is set.
	 * @param primarySceneModel
	 */
	private void addToPrimarySceneModel(SentencePart part, SceneModel primarySceneModel){
		if(part == null)
			return;
		
		ScenePart sp = _ttsEngine.whichScenePart(part._wsd);				
		
		if(sp == ScenePart.ROLE){
			if(!primarySceneModel.hasRole(part._wsd)){				
				Role role = new Role(part._name, part._wsd);				
				primarySceneModel.addRole(role);			
			}		
		}
		else if(sp == ScenePart.DYNAMIC_OBJECT){		
			if(!primarySceneModel.hasDynamic_object(part._wsd)){				
				DynamicObject dynObj = new DynamicObject(part._name, part._wsd);
				primarySceneModel.addDynamic_object(dynObj);				
			}
		}
		else if(sp == ScenePart.STATIC_OBJECT){
			if(!primarySceneModel.hasStatic_object(part._wsd)){				
				StaticObject staObj = new StaticObject(part._name, part._wsd);
				primarySceneModel.addStatic_object(staObj);
			}
		}
		else if(sp == ScenePart.LOCATION){//TODO: check what else shall I do for this case!
			Location location = new Location(part._name, part._wsd);
			primarySceneModel.setLocation(location);			
		}
	}
	
	/**
	 * this method based on the ScenePart of the subject(s) of the sentenceModel adds RoleAction(s) or ObjectAction(s) to primarySceneModel. 
	 * It is important to note that when this method is called _wsd parameter of  
	 * all subject(s) of this sentenceModel has been allocated ! 
	 * 
	 * @param verbRelation the relation indicating the action of this verb. 
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void addVerbToPrimarySceneModel(PlausibleStatement verbRelation, SceneModel primarySceneModel){
		if(verbRelation == null)
			MyError.error("verb parameter of this method should not be null!");
		
		Node sbj = verbRelation.argument;				
										
		ScenePart sbjSp = _ttsEngine.whichScenePart(sbj);
		
		if(sbjSp == null || sbjSp == ScenePart.UNKNOWN){
			MyError.error("this subject \"" + sbj + "\" has no ScenePart");
			return;
		}
		
		if(sbjSp == ScenePart.ROLE){			
			
			Role role = primarySceneModel.getRole(sbj);
			if(role == null){
				MyError.error(primarySceneModel + " SceneModel has not such a " + sbj + " Role.");
				return;
			}					
			
			RoleAction role_action = new RoleAction(verbRelation.getName(), verbRelation);				
			role.addRole_action(role_action);
		}
		else if(sbjSp == ScenePart.DYNAMIC_OBJECT){
			
			DynamicObject dyn_obj = primarySceneModel.getDynamic_object(sbj);
			if(dyn_obj == null){
				MyError.error(primarySceneModel + " SceneModel has not such a " + sbj + " DynamicObject.");
				return;
			}
			
			ObjectAction obj_act = new ObjectAction(verbRelation.getName(), verbRelation);				
			dyn_obj.addObejct_action(obj_act);
		}			
		
	}
	
	private void preprocessSubject(SentenceModel sentenceModel, SceneModel primarySceneModel){
		
		ArrayList<SentencePart> subjects = sentenceModel.getSubjects();

		for(SentencePart sbj:subjects){
		
			if(sbj == null || !sbj.isSubject()){
				MyError.error("bad subjct part " + sbj);
				return;
			}
			
			//_wsd of sbj is set to proper Node of KB.
			allocate_wsd(sbj);	
			
			if(sbj._wsd != null)
				addToPrimarySceneModel(sbj, primarySceneModel);			
			else
				MyError.error(sbj._wsd_name + " couldn't get allocated!");
		}
	}
	
	private void preprocessObject(SentenceModel sentenceModel, SceneModel primarySceneModel){
		
		ArrayList<SentencePart> objects = sentenceModel.getObjects();
		
		for(SentencePart obj:objects){
			
			if(obj != null && !obj.isObject()){
				MyError.error("bad obejct part " + obj);
				return;
			}	
			else if(obj != null && obj.isObject()){
			
				//_wsd of obj is set to proper Node of KB.
				allocate_wsd(obj);
				
				if(obj._wsd != null)			
					addToPrimarySceneModel(obj, primarySceneModel);
				else
					MyError.error(obj._wsd_name + " couldn't get allocated!");
			}
		}
	}		
	
	
	private void preprocessAdverb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		ArrayList<SentencePart> adverbs = sentenceModel.getAdverbs();
		
		for(SentencePart adv:adverbs){
			if(adv != null && !adv.isAdverb()){
				MyError.error("bad adverb part " + adv);
				return;
			}
			else if(adv != null && adv.isAdverb()){
				
				//_wsd of adv is set to proper Node of KB.
				allocate_wsd(adv);
				
				if(adv._wsd != null)			
					addToPrimarySceneModel(adv, primarySceneModel);
				else
					MyError.error(adv._wsd_name + " couldn't get allocated!");
			}
		}
		
	}
	/**
	 * TODO:
	 * 1- allocate_wsd verb 									   				--> done
	 * 2- adding proper RoleActoin or ObjectAction to sceneModel.  				--> done
	 * 3- create relation of verb!		   						   				--> done
	 * 
	 * 4- defining verb capacities in INJUREDPIGEON kb.							--> done as test
	 * 5- loading these capacities from kb for SynSetof verbs.					--> done
	 * 6- preparing values for these capacities --> in SceneReasoner,next phase.
	 *   															  
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		SentencePart verb = sentenceModel.getVerb();
		
		if(verb == null || !verb.isVerb()){
			MyError.error("bad verb part " + verb);
			return;
		}		
			
		//_wsd of verb is set to proper Node of KB.
		allocate_wsd(verb);
		
		if(verb._wsd == null){
			MyError.error(verb._wsd_name + " couldn't get allocated!");
			return;
		}
		
		Node pure_verb = _ttsEngine.getPureNode(verb._wsd);
		
		if(pure_verb == null){
			MyError.error("the pure version of " + verb._wsd + " could not be found");
			return;
		}
		
		//load verb capacities from kb.
		ArrayList<PlausibleStatement> verb_cxs = loadVerbCapacities(pure_verb);
		
		print("loaded contexts " + verb_cxs + "\n");

		//here _wsd of subject(s) and object(s) of this sentence has been allocated!
		ArrayList<PlausibleStatement> verbRelations = defineVerbRelation(verb, sentenceModel, primarySceneModel);
		
		for(PlausibleStatement verbRel: verbRelations){
			
			addVerbToPrimarySceneModel(verbRel, primarySceneModel);	
		
			setLocationContext(verbRel, primarySceneModel.getLocation(), verb_cxs);
			
			setTimeContext(verbRel, primarySceneModel.getTime(), verb_cxs);				
		}
	}	

	/**
	 * this method defines the proper relation resulted from the verb of this sentence.
	 * It is important to note that when this method is called _wsd parameter of  
	 * all subject(s) and object(s) of this sentence has been allocated! 
	 * 
	 * @param verb the verb its relation are to be defined. guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private ArrayList<PlausibleStatement> defineVerbRelation(SentencePart verb, SentenceModel sentenceModel, SceneModel primarySceneModel){
		
		ArrayList<PlausibleStatement> verbRelations = new ArrayList<PlausibleStatement>();
		
		//here _wsd of subject(s), object(s), and adverb(s) of this sentence has been allocated!								
		ArrayList<SentencePart> subjects = sentenceModel.getSubjects();
		
		if(subjects == null || subjects.size() == 0){
			MyError.error("sentence with verb " + verb + " has no subject part! " + sentenceModel);
			return verbRelations;
		}
		
		for(SentencePart sbj:subjects){				
			
			ArrayList<SentencePart> objects = sentenceModel.getObjects();
			
			boolean transitive_verb = false;
			
			if(objects != null && objects.size() > 0)
				
				for(SentencePart obj:objects)
					
					if(obj != null){						
						//it is a transitive verb.
						transitive_verb = true;
						
						//adding the relation of this sentence to kb.
						PlausibleStatement rel = _kb.addRelation(sbj._wsd, obj._wsd, verb._wsd, SourceType.TTS);
						verbRelations.add(rel);
						print("relation added ------------- : " + rel.argument.getName() + " -- " + rel.getName() + " -- " + rel.referent.getName());
														
					}				
			
			if(!transitive_verb){
				//TODO check if using "KnowledgeBase.HPR_ANY" as referent is correct?!
				//adding the relation of this sentence to kb. 
				PlausibleStatement rel = _kb.addRelation(sbj._wsd, KnowledgeBase.HPR_ANY, verb._wsd, SourceType.TTS);
				verbRelations.add(rel);
				print("relation added ------------- : " + rel.argument.getName() + " -- " + rel.getName() + " -- " + rel.referent.getName());				
			}
		}
		return verbRelations;
	}

	/**
	 * load verb capacities from kb.
	 * 
	 * @param pure_verb guaranteed not to be null.
	 */
	private ArrayList<PlausibleStatement> loadVerbCapacities(Node pure_verb) {	
		
		ArrayList<PlausibleStatement> cxs = new ArrayList<PlausibleStatement>();
		
		Node synSet = pure_verb.getSynSet();
		
		print("\nSysSet of " + pure_verb + " is " + synSet);
		
		if(synSet == null)
			MyError.error("the verb " + pure_verb + " has no synset!");
		else
			cxs = synSet.loadCXs();
		
		if(default_contexts == null){
			
			Node verb_root = _kb.addConcept(verb_root_name);
			default_contexts = verb_root.loadCXs();
		}
		cxs.addAll(default_contexts);	
		
		return cxs;
	}
	
	private void setLocationContext(PlausibleStatement verbRelation, Location location, ArrayList<PlausibleStatement> CXs){
		if(verbRelation == null || location == null)
			return;
		
		for(PlausibleStatement cx : CXs){		
			
			if(cx.relationType == null){
				MyError.error("this " + cx + " has no relationType!");
				return;
			}			
			
			String cxName = cx.relationType.getContextName();			
			
			if(CONTEXT.parse(cxName) == CONTEXT.LOCATION){				
				PlausibleStatement locCx = _kb.addRelation(verbRelation, location._node, cx.relationType);
				print("" + locCx + " (" + verbRelation + ")= " + location._node + "\n");			
			}
		}
	}
	
	
	private void setTimeContext(PlausibleStatement verbRelation, Time time, ArrayList<PlausibleStatement> CXs) {
		// TODO Auto-generated method stub
		
	}
}
