package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.enums.SourceType;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import sceneElement.*;
import model.MainSemanticTag;
import model.ScenePart;
import model.SentencePart;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;

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
		
//	private ArrayList<PlausibleStatement> default_contexts = null;
	private String MainSemanticArgumet_name = "MainSemArg";
	private String zamir_enekasi = "خود§n-13459";
	
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */
//	private String sentenceInfosFileName = "inputStory/sentenceInfos2_simple.txt";
//	private String sentenceInfosFileName = "inputStory/sentenceInfos_SS.txt";
	private String sentenceInfosFileName = "inputStory/SentenceInfos11.txt";

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
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(sentenceInfosFileName), "utf-8"));			
		}
		catch(Exception e)
		{
			System.out.println("Error opening `" + sentenceInfosFileName + "` for reading input natural language texts!");
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
	 * 
	 * name:پسرک		sentence_part:پسرک	POS:NOUN		SYN:SBJ		SEM:ARG0		WSD:MAIN_وضعیت سنی#a_POST	sub_part:پسر،ک		dep:-		num:1
	 * name:پسر		sentence_part:پسر		POS:NOUN		SYN:SBJ_P	SEM:ARG0_P		WSD:پسر#n2				sub_part:-		dep:MAIN	num:1-0
	 * name:ک		sentence_part:ک		POS:UNKNOWN		SYN:SBJ_P	SEM:ARG0_P		WSD:خردسال#a1				sub_part:-		dep:POST	num:1-1
	 * 
	 * @param partStr partStr has all information about current Part.
	 * @return equivalent Part Object.
	 */
	private SentencePart createPart(String partStr, SentenceModel senteceModel){
//		print(partStr);			
		String[] parts = partStr.split("(\t)+");
		
		if(parts.length != 8){			
			MyError.error("Bad sentence information format " + partStr + " parts-num " + parts.length);
//			print("Bad sentence information format " + partStr + " parts-num " + parts.length);
			return null;
		}
					
		for(int i = 0; i < parts.length; i++)
			parts[i] = parts[i].substring(parts[i].indexOf(":")+1);				
		
		SentencePart newPart = new SentencePart(parts[0], parts[7], senteceModel);			
		
		if(parts[1] != null && !parts[1].equals("-"))
			newPart.set_pos(parts[1]);
			
		newPart.set_syntaxTag(parts[2]);
		
		newPart.set_sourceOfSynNum(parts[3]);
		
		if(parts[4] != null && !parts[4].equals("-"))
			newPart.set_semanticTag(parts[4]);
		
		newPart.set_wsd_name(parts[5]);
		
		if(parts[6] != null && !parts[6].equals("-")){				
			String[] subs = parts[6].split(",");
			
			ArrayList<SentencePart> subParts = new ArrayList<SentencePart>();
			for(String s:subs)			
				subParts.add(new SentencePart(s, senteceModel));
			
			newPart.setSub_parts(subParts);
		}		
				
		print(newPart.getStr() + "\n");
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
			return null;
		
		ArrayList<SentencePart> senParts = new ArrayList<SentencePart> ();
		
		for(int i = 0; i < senPartStrs.size(); i++){
			
			String currentPartStr = senPartStrs.get(i);
			SentencePart currentPart = createPart(currentPartStr, sentence);		
			
			//it means next line are informations of sub_parts of this current_part.
			// we have assumed that sub_parts has depth of 1. It means each sub_part has no sub_part in itself.
			if(currentPart != null && currentPart.hasSub_parts()){
				ArrayList<SentencePart> subParts = new ArrayList<SentencePart> (currentPart.getSub_parts().size());
				
				for(int j = 0; j < currentPart.getSub_parts().size() && (i+1)<senPartStrs.size(); j++){
					i++;
					String subPartStr = senPartStrs.get(i);
					SentencePart sPart = createPart(subPartStr, sentence);					
					if(sPart != null)
						subParts.add(sPart);							
				}
				currentPart.setSub_parts(subParts);							
			}						
			senParts.add(currentPart);

			//setting _wsd of currentPart to the proper Node of KB.			
			if(currentPart.isVerb())
				//isnewNode parameter is true, because every verb is new a one!
				allocate_wsd(sentence ,currentPart, true);
			else

				allocate_wsd(sentence, currentPart, false);
			
			if(currentPart._wsd == null)
				MyError.error(currentPart._wsd_name + " couldn't get allocated!");				
			
		}

		//now senParts has all parts' objects of this sentence, so we specify which is subject, object, adverb, ...						
		sentence.arrageSentenceParts(NLsentence, senParts);
		
		//adding verb relation to KB.
		//delayed to preprocessScene after preparing nullSemanticTags
		/*ArrayList<PlausibleStatement> verbRelations = */ //defineVerbRelation(sentence);
		
		//loading verb SemanticArguments.
		ArrayList<Node> semArgs = loadVerbSemanticArguments(sentence);
		
		SentencePart verb = sentence.getVerb();
		
		if(verb != null)
			verb.setCapacities(semArgs);
		else
			MyError.error("this sentnce has no verb! " + sentence);
		
		return sentence;
		
	}
	
//	
//	/**
//	 * preprocessScene preprocesses input sentenceModel and adds it to the primarySceneModel.    
//	 * 
//	 * @param sentenceModel the SenetenceModel to be converted. 
//	 * @return SceneModel equivalent to input sentenceModel. 
//	 */	 
//	public SceneModel preprocessScene(SentenceModel sentenceModel){		
//		
//		if(sentenceModel == null){
//			MyError.error("senetecenModel should not be null! " + sentenceModel);
//			return null;
//		}
//		SceneModel primarySceneModel = new SceneModel();
//
//		primarySceneModel.addSentence(sentenceModel);
//		sentenceModel.setScene(primarySceneModel);
//		
//		preprocessSubject(sentenceModel, primarySceneModel);		
//				
//		preprocessObject(sentenceModel, primarySceneModel);
//		
//		preprocessAdverb(sentenceModel, primarySceneModel);
//		print("\n before verb");
//		_ttsEngine.printDictionary();
//		
//		preprocessVerb(sentenceModel, primarySceneModel);
//		print("\n after verb");
//		_ttsEngine.printDictionary();
//		
//		print("primarySceneModel\n" + primarySceneModel);
//		return primarySceneModel;
//	}	

	/**
	 * preprocessScene preprocesses input sentenceModel and converts it to the primarySceneModel.    
	 * 
	 * @param sentenceModel the SenetenceModel to be converted. 
	 * @param primarySceneModel the SceneModel which this sentenceModel is to be added to.
	 * @param storyModel the StoryModel which the returned sceneModel is to be added to.
	 * @return SceneModel containing input sentenceModel 
	 */	 
	public SceneModel preprocessScene(SentenceModel sentenceModel, SceneModel primarySceneModel, StoryModel storyModel){		
		
		if(sentenceModel == null || primarySceneModel == null || storyModel == null){
			MyError.error("None of senetecenModel, sceneModel, and storyModel should be null! " + sentenceModel);
			return null;
		}		
		
		prepareNullSemanticTags(sentenceModel, primarySceneModel, storyModel);
		
		checkAllSemanticTagsWithUser(sentenceModel, primarySceneModel);
	
		preprocessArg0(sentenceModel, primarySceneModel);
		
		preprocessVerbArg(sentenceModel, primarySceneModel);
		
		preprocessArg1(sentenceModel, primarySceneModel);
		
		preprocessArg2(sentenceModel, primarySceneModel);
		
		preprocessArg3(sentenceModel, primarySceneModel);
		
		preprocessArg4(sentenceModel, primarySceneModel);
		
		preprocessArg5(sentenceModel, primarySceneModel);
		
		preprocessSecondaryArgs(sentenceModel, primarySceneModel);
		
		preprocessVisualTagVerb(sentenceModel, primarySceneModel);
		
		preprocessAllVisualTag(sentenceModel, primarySceneModel);
		
		print("primarySceneModel\n" + primarySceneModel);
		return primarySceneModel;
	}
	
	/**
	 * this method prepares the null semanticTags (only ARG0, ARG1) of the verb of sentenceModel as possible with the help of 
	 * other sentences of primarySceneModel and other scenes of stroyModel.
	 * 
	 * @param sentenceModel the sentenceModel which the semanticArgs of its verb is to be prepared. guaranteed not to be null.
	 * @param primarySceneModel the sceneModel which sentenceModel belongs to. guaranteed not to be null.
	 * @param storyModel the storyModel which primarySceneModel belongs to. guaranteed not to be null.
	 */
	private void prepareNullSemanticTags(SentenceModel sentenceModel, SceneModel primarySceneModel, StoryModel storyModel) {
		
		print("\n=============== in   prepareNullSemanticTags   =============");
		
		ArrayList<MainSemanticTag> existingSemTags = sentenceModel.getExistingMainSematicArgs();
		ArrayList<MainSemanticTag> necessarySemTags = sentenceModel.getNecessarySematicArgs();
						
//		print("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
//		print("existing " + existingSemTags);
//		print("necessar " + necessarySemTags);
		
		ArrayList<MainSemanticTag> missingMainArgs = new ArrayList<MainSemanticTag>();
		
		for(MainSemanticTag necess:necessarySemTags)
			if(!existingSemTags.contains(necess))
				missingMainArgs.add(necess);
//		print("missings " + missingMainArgs);
		
		prepareNullSemanticTagsForAScene(sentenceModel, missingMainArgs, primarySceneModel);
		
		while(!Common.isEmpty(missingMainArgs)){
//			print("new mis " + missingMainArgs + "scene ");
			ArrayList<SceneModel> allScene = storyModel.getScenes();
			
			if(!Common.isEmpty(allScene))
				for(SceneModel oldScene:allScene)
					if(!oldScene.equals(primarySceneModel))
						prepareNullSemanticTagsForAScene(sentenceModel, missingMainArgs, oldScene);
		}
		
		print("=============== end of prepareNullSemanticTags =============\n");
	}	
	
	private void prepareNullSemanticTagsForAScene(SentenceModel sentenceModel, ArrayList<MainSemanticTag> missingMainArgs, SceneModel sceneModel){		
		
		if(!Common.isEmpty(missingMainArgs)){
			
			ArrayList<MainSemanticTag> preparedMainArgs = new ArrayList<MainSemanticTag>();
			
			ArrayList<SentenceModel> allSentences = sceneModel.getSentences();
			
			if(!Common.isEmpty(allSentences)){		
				
				for(MainSemanticTag miss:missingMainArgs){
					
//					print("for missed " + miss);
					
					for(SentenceModel sent:allSentences){
						if(sent.equals(sentenceModel))
							continue;
						
//						print("In sentence: " + sent.getOriginalSentence());
						
						if(miss != null && miss.isArg0() && sent.hasArg0()){							
							SentencePart arg0SentencePart = sent.getArg0SentencePart();
							
							arg0SentencePart.set_semanticTag(miss.name());
							preparedMainArgs.add(arg0SentencePart._semanticTag.convertToMainSemanticTag());							
							sentenceModel.setPrerparedSentencePart(arg0SentencePart);						
							
//							print("prepared " + arg0SentencePart._semanticTag);
							break;
						}
						else if(miss != null && miss.isArg1() && sent.hasArg1()){
							SentencePart arg1SentencePart = sent.getArg1SentencePart();
							
							arg1SentencePart.set_semanticTag(miss.name());
							preparedMainArgs.add(arg1SentencePart._semanticTag.convertToMainSemanticTag());
							sentenceModel.setPrerparedSentencePart(arg1SentencePart);
							
//							print("prepared " + arg1SentencePart._semanticTag);
							break;
						}
					}					
				}
				for(MainSemanticTag prepared:preparedMainArgs)
					missingMainArgs.remove(prepared);
			}			
		}			
	}
	
	private void checkAllSemanticTagsWithUser(SentenceModel sentenceModel, SceneModel primarySceneModel){
		//TODO check with user!		
	}
	
	private void preprocessArg0(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		print("\n0000000000000000 in   preprocessArg0   0000000000000000");
		
		if(sentenceModel.hasArg0()){
			
			SentencePart arg0Part = sentenceModel.getArg0SentencePart();
			
			if(arg0Part == null){
				MyError.error("the sentenceModel hasArg0 but it didn't find!" + sentenceModel);
				return;
			}
						
			//reasoning ScenePart from KB and adding to primarySceneModel. 
			ScenePart scenePart = _ttsEngine.whichScenePart(arg0Part);
			SceneElement sceneElem = addToPrimarySceneModel(arg0Part, scenePart, primarySceneModel);
			 
			if(sceneElem == null){
				MyError.error(arg0Part + " could not be added to primarySceneModel!");
				return;
			}
				
			//------------------- pre-processing dependents of arg0 --------------------
			
						
			//It means that this sentencePart has some adjectives
			if(arg0Part.hasAdjectives()){
				
				print(arg0Part + " has some adjectives:" + arg0Part.adjectives);
				ArrayList<SentencePart> adjectives = arg0Part.adjectives;
				for(SentencePart adj:adjectives){
					if(scenePart == ScenePart.ROLE){
						 Role role = (Role)sceneElem;
						 RoleMood rm = new RoleMood(adj._name, adj._wsd);
						 role.addRole_mood(rm);
					}
					else if(scenePart == ScenePart.DYNAMIC_OBJECT){
						DynamicObject dynobj = (DynamicObject)sceneElem;
						ObjectState objState = new ObjectState(adj._name, adj._wsd);
//						if(dynobj.hasThisState(objState))
						dynobj.setCurrent_state(objState);
//						else
//							MyError.error(message);
					}
				}
			}
			
			//It means that this sentencePart has some mozad_elaih
			if(arg0Part.hasMozaf_elaih()){				
				
				print(arg0Part + " has mozaf_elaih: " + arg0Part.mozaf_elaih);
				ArrayList<SentencePart> mozafs = arg0Part.mozaf_elaih;
				for(SentencePart moz:mozafs){
					
					Node zamir_khod = _kb.addConcept(zamir_enekasi); 
					
					if(moz._wsd != zamir_khod){
						if(scenePart == ScenePart.ROLE){
							 Role role = (Role)sceneElem;
							 RoleMood rm = new RoleMood(moz._name, moz._wsd);
							 role.addRole_mood(rm);
						}	
						else if(scenePart == ScenePart.DYNAMIC_OBJECT){
							DynamicObject dynobj = (DynamicObject)sceneElem;
							ObjectState objState = new ObjectState(moz._name, moz._wsd);
//							if(dynobj.hasThisState(objState))
							dynobj.setCurrent_state(objState);
//							else
//								MyError.error(message);
						}							
					}
				}
			}
		}
		print("0000000000000000 end of preprocessArg0 0000000000000000\n");
	}
	
	private void preprocessVerbArg(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		defineVerbRelation(sentenceModel);
		// TODO Auto-generated method stub
		
	}
	
	private void preprocessArg1(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}
		
	private void preprocessArg2(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessArg3(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessArg4(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessArg5(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessSecondaryArgs(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessVisualTagVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	private void preprocessAllVisualTag(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}

	
	/**
	 * This method maps part's wsd parameter to a concept in _kb based on part's wsd_name parameter.
	 * if part's wsd_name is "-" no mapping occurs.
	 * if part's wsd_name has just one part, it is the main concept name, so it must directly maps to a node in _kb.
	 * if part's wsd_name has more than one part which includes one MAIN and probably a PRE or POST, so it must be mapped to a plausible statement in a _kb.
	 * 
	 * @param part the part its wsd parameter to be set.
	 * @param isNewNode is this part a new instance or is is the same as seen before.
	 * @param synTag the SyntaxTag of this node in the sentence.
	 */
	private void allocate_wsd(SentenceModel sentence, SentencePart part, boolean isNewNode){
		if(part == null)
			return;
			
		String wsd_name = part._wsd_name;
		
		if(wsd_name == null || wsd_name.equals("-"))
			return;
		
		//TODO: I have an assumption that sub_parts has simple (just concept name) _wsd_name.
		if(part.hasSub_parts())
			for(SentencePart p:part.getSub_parts())
				if(p._wsd == null && p._wsd_name != null && !p._wsd_name.equals("-")){																				
					Node wsd = _ttsEngine.findorCreateInstance(p._wsd_name, isNewNode);
					if(wsd != null)
						p.set_wsd(wsd);
				} 
		
		//it means that it is not just node but plausible statement for example MAIN_وضعیت سلامتی_POST
		if(wsd_name.indexOf("_") != -1){
			
			String[] sub_parts = wsd_name.split("_"); //1_وضعیت سنی#a_خردسال#a1 --> [part_num:1 --> وضعیت سلامتی --> خردسال#a1]
			
			if(sub_parts.length != 3){
				MyError.error("wrong wsd_name" + wsd_name);
				return;
			}
			
			Node argument = null;
			Node referent = null;
			Node descriptor = null;
			
			PlausibleStatement wsd = null;
			
			String relation_name = null;
			
			for(int i = 0; i < 3; i++){
				
				Node cur_part_wsd = null;
				
				try {
					int part_num = -1;
					
					//if it dosen't throw exception, it means that cur_part is a number
			        part_num = Integer.parseInt(sub_parts[i]);
			        
			        SentencePart cur_part = null;
			        
			        //it is a valid part_num. 
			        if(part_num != -1){
			        	cur_part = part.getSub_part(part_num);
			        	cur_part_wsd = cur_part._wsd;			        	
			        }
			        
			        if(i == 0 || i == 2)
			    		if(cur_part_wsd != null)
							if(argument == null)
								argument = cur_part_wsd;
							else if(referent == null)
								referent = cur_part_wsd;					
			    	
			        
			    } catch (NumberFormatException e) {
			    	//if it has thrown an exception it means that sub_part[i] is the name of a concept.
			    	//it is argument or referent
			    	if(i == 0 || i == 2){
			    		cur_part_wsd = _ttsEngine.findorCreateInstance(sub_parts[i], isNewNode);
			    		
			    		if(cur_part_wsd != null){
							
			    			if(argument == null)
								argument = cur_part_wsd;
							else if(referent == null)
								referent = cur_part_wsd;
						}
			    	}
			    	//it is descriptor
			    	else{
			    		relation_name = sub_parts[1];
						wsd = _ttsEngine.findRelationInstance(relation_name);
						if(wsd == null){
							//it must got directly fetched from kb, and then addRelation will clone it.
							descriptor = _kb.addConcept(relation_name);
						}			    		
			    	}
			    }	
			}
			
			
			if(descriptor != null){//it means that findRelation has not found it and it is newly fetched from kb.			
				
				wsd = _kb.addRelation(argument, referent, descriptor, SourceType.TTS);
				
				print("wsd relation added ------------- : " + wsd.argument.getName() + " --> " + wsd.getName() + " --> " + wsd.referent.getName() + "\n");
				
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
						
						print("wsd relation added ------------- : " + wsd.argument.getName() + " -- " + wsd.getName() + " -- " + wsd.referent.getName() + "\n");
						
						_ttsEngine.addRelationInstance(relation_name, wsd);
					}
				}
			}
			part.set_wsd(argument);
			
			add_adjective_mozaf(sentence, part, descriptor, referent);
			//return;
		}
		else{
			//it means this part wsd_name is just one concept name, so we find or add it in sceneModel.			
			Node wsd = _ttsEngine.findorCreateInstance(wsd_name, isNewNode);
			part.set_wsd(wsd);
		}		
	}

	
	private void add_adjective_mozaf(SentenceModel sentence, SentencePart mainPart, Node descriptor, Node referent) {
		if(sentence == null || mainPart == null || descriptor == null || referent == null){
			MyError.error("null input parameter for add_adjective_mozaf!");
			if(mainPart == null)
				MyError.error("no mainPart could be found!");
			return;
		}
		if(descriptor.getPos() == POS.ADJECTIVE){ //it means that descriptor is describing an adjective.
		
			SentencePart adjPart = new SentencePart(referent.getName(), POS.ADJECTIVE, DependencyRelationType.NPOSTMOD, null, referent, null, sentence);
					
			if(mainPart.adjectives == null)
				mainPart.adjectives = new ArrayList<SentencePart>();		
			
			mainPart.adjectives.add(adjPart);
			
			print("\n----------------" + adjPart + " adjective added to " + mainPart);
		}
		else if(descriptor.getPos() == POS.NOUN){ //it means that descriptor is describing a mozaf_alaih.
			
			SentencePart mozPart = new SentencePart(referent.getName(), POS.NOUN, DependencyRelationType.MOZ, null, referent, null, sentence);
			
			if(mainPart.mozaf_elaih == null)
				mainPart.mozaf_elaih = new ArrayList<SentencePart>();		
			
			mainPart.mozaf_elaih.add(mozPart);
			
			print("\n----------------" + mozPart + " mozaf_elaih added to " + mainPart);
		}
	}

	/**
	 * this method based on the ScenePart of the part adds a Role, DynamicObject, StaticObject, or ... to primarySceneModel.
	 * TODO: we have assumed for simplicity which every scene has a unique Role, DyanamicObject, and StaticObject with a one name.
	 * for example all «پسرک» refer to just one Role. 
	 * 
	 * @param part the SentencePart which is to be added to primarySceneModel.
	 * @param scenePart the ScenePart which this part has.
	 * @param primarySceneModel the primarySceneModel which the part is to be added to.
	 * @return the new ScenePart created based on the input part or the ScenePart which primarySceneModel has had before.
	 */
	private SceneElement addToPrimarySceneModel(SentencePart part, ScenePart scenePart, SceneModel primarySceneModel){
		
		if(part == null || scenePart == null || scenePart == ScenePart.UNKNOWN || primarySceneModel == null){
			MyError.error("null input parameter!");
			return null;
		}								
		
		if(scenePart == ScenePart.ROLE){
			if(!primarySceneModel.hasRole(part._wsd)){				
				Role role = new Role(part._name, part._wsd);				
				primarySceneModel.addRole(role);
				return role;
			}
			else
				return primarySceneModel.getRole(part._wsd);
		}
		else if(scenePart == ScenePart.DYNAMIC_OBJECT){		
			if(!primarySceneModel.hasDynamic_object(part._wsd)){				
				DynamicObject dynObj = new DynamicObject(part._name, part._wsd);
				primarySceneModel.addDynamic_object(dynObj);				
				return dynObj;
			}
			else
				return primarySceneModel.getDynamic_object(part._wsd);
		}
		else if(scenePart == ScenePart.STATIC_OBJECT){
			if(!primarySceneModel.hasStatic_object(part._wsd)){				
				StaticObject staObj = new StaticObject(part._name, part._wsd);
				primarySceneModel.addStatic_object(staObj);
				return staObj;
			}
			else
				return primarySceneModel.getStatic_object(part._wsd);
		}
		else if(scenePart == ScenePart.LOCATION){//TODO: check what else shall I do for this case!
			if(primarySceneModel.getLocation() != null)
				MyError.error("the primarySceneModel has had a location before!" + primarySceneModel.getLocation());

			Location location = new Location(part._name, part._wsd);
			primarySceneModel.setLocation(location);
			return location;
		}
		else if(scenePart == ScenePart.TIME){//TODO: check what else shall I do for this case!
			if(primarySceneModel.getTime() != null)
				MyError.error("the primarySceneModel has had a time before!" + primarySceneModel.getTime());

			Time time = new Time(part._name, part._wsd);
			primarySceneModel.setTime(time);
			return time;
		}
		return null;
	}
	
//	/**
//	 * this method based on the ScenePart of the subject(s) of the sentenceModel adds RoleAction(s) or ObjectAction(s) to primarySceneModel. 
//	 * It is important to note that when this method is called _wsd parameter of  
//	 * all subject(s) of this sentenceModel has been allocated ! 
//	 * 
//	 * @param verbRelation the relation indicating the action of this verb. 
//	 * @param primarySceneModel guaranteed not to be null.
//	 */
//	private void addVerbToPrimarySceneModel(PlausibleStatement verbRelation, SceneModel primarySceneModel){
//		if(verbRelation == null)
//			MyError.error("verb parameter of this method should not be null!");
//		
//		Node sbj = verbRelation.argument;				
//										
//		ScenePart sbjSp = _ttsEngine.whichScenePart(sbj, DependencyRelationType.SBJ);
//		
//		if(sbjSp == null || sbjSp == ScenePart.UNKNOWN){
//			MyError.error("this subject \"" + sbj + "\" has no ScenePart");
//			return;
//		}
//		
//		if(sbjSp == ScenePart.ROLE){			
//			
//			Role role = primarySceneModel.getRole(sbj);
//			if(role == null){
//				MyError.error(primarySceneModel + " SceneModel has not such a " + sbj + " Role.");
//				return;
//			}					
//			
//			RoleAction role_action = new RoleAction(verbRelation.getName(), verbRelation);				
//			role.addRole_action(role_action);
//		}
//		else if(sbjSp == ScenePart.DYNAMIC_OBJECT){
//			
//			DynamicObject dyn_obj = primarySceneModel.getDynamic_object(sbj);
//			if(dyn_obj == null){
//				MyError.error(primarySceneModel + " SceneModel has not such a " + sbj + " DynamicObject.");
//				return;
//			}
//			
//			ObjectAction obj_act = new ObjectAction(verbRelation.getName(), verbRelation);				
//			dyn_obj.addObejct_action(obj_act);
//		}			
//		
//	}
	
//	private void preprocessSubject(SentenceModel sentenceModel, SceneModel primarySceneModel){
//		
//		ArrayList<SentencePart> subjects = sentenceModel.getSubjects();
//			
//		print("\npreprocess subject: " + subjects);
//		
//		for(SentencePart sbj:subjects){
//		
//			if(sbj == null || !sbj.isSubject()){
//				MyError.error("bad subjct part " + sbj);
//				return;
//			}
//			
//			//_wsd of sbj is set to proper Node of KB.
//			allocate_wsd(sbj, false);	
//			
//			if(sbj._wsd != null)
//				addToPrimarySceneModel(sbj, primarySceneModel);			
//			else
//				MyError.error(sbj._wsd_name + " couldn't get allocated!");
//		}
//	}
//	
//	private void preprocessObject(SentenceModel sentenceModel, SceneModel primarySceneModel){
//		
//		ArrayList<SentencePart> objects = sentenceModel.getObjects();
//		
//		print("\npreprocess object: " + objects);
//		
//		for(SentencePart obj:objects){
//			
//			if(obj != null && !obj.isObject()){
//				MyError.error("bad obejct part " + obj);
//				return;
//			}	
//			else if(obj != null && obj.isObject()){
//			
//				//_wsd of obj is set to proper Node of KB.
//				allocate_wsd(obj, false);
//				
//				if(obj._wsd != null)			
//					addToPrimarySceneModel(obj, primarySceneModel);
//				else
//					MyError.error(obj._wsd_name + " couldn't get allocated!");
//			}
//		}
//	}		
//	
//	
//	private void preprocessAdverb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
//		
//		ArrayList<SentencePart> adverbs = sentenceModel.getAdverbs();
//		
//		print("\npreprocess adverb: " + adverbs);
//		
//		for(SentencePart adv:adverbs){
//			if(adv != null && !adv.isAdverb()){
//				MyError.error("bad adverb part " + adv);
//				return;
//			}
//			else if(adv != null && adv.isAdverb()){
//				
//				//_wsd of adv is set to proper Node of KB.
//				allocate_wsd(adv, false);
//				
//				if(adv._wsd != null)			
//					addToPrimarySceneModel(adv, primarySceneModel);
//				else
//					MyError.error(adv._wsd_name + " couldn't get allocated!");
//			}
//		}		
//	}
//	/**
//	 * TODO:
//	 * 1- allocate_wsd verb 									   				--> done
//	 * 2- adding proper RoleActoin or ObjectAction to sceneModel.  				--> done
//	 * 3- create relation of verb!		   						   				--> done
//	 * 
//	 * 4- defining verb capacities in INJUREDPIGEON kb.							--> done as test
//	 * 5- loading these capacities from kb for SynSetof verbs.					--> done
//	 * 6- preparing values for these capacities --> in SceneReasoner,next phase.
//	 *   															  
//	 * @param sentenceModel guaranteed not to be null.
//	 * @param primarySceneModel guaranteed not to be null.
//	 */
//	private void preprocessVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
//		
//		SentencePart verb = sentenceModel.getVerb();
//						
//		if(verb == null || !verb.isVerb()){
//			MyError.error("bad verb part " + verb);
//			return;
//		}		
//				
//		Node pure_verb = _ttsEngine.getPureNode(verb._wsd);
//		
//		if(pure_verb == null){
//			MyError.error("the pure version of " + verb._wsd + " could not be found");
//			return;
//		}
//		
//		//load verb capacities from kb.
//		ArrayList<PlausibleStatement> verb_cxs = loadVerbCapacities(pure_verb);
//		
//		print("loaded contexts " + verb_cxs + "\n");
//
//		here _wsd of subject(s) and object(s) of this sentence has been allocated!
//		ArrayList<PlausibleStatement> verbRelations = defineVerbRelation(sentenceModel, primarySceneModel);
//		
//		for(PlausibleStatement verbRel: verbRelations){
//			
////			addVerbToPrimarySceneModel(verbRel, primarySceneModel);	
//		
//			setLocationContext(verbRel, primarySceneModel.getLocation(), verb_cxs);
//			
//			setTimeContext(verbRel, primarySceneModel.getTime(), verb_cxs);				
//		}
//	}	

	/**
	 * this method defines the proper relation resulted from the verb of this sentence.
	 * It is important to note that when this method is called _wsd parameter of  
	 * all subject(s) and object(s) of this sentence has been allocated! 
	 * 
	 * @param verb the verb its relation are to be defined. guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private ArrayList<PlausibleStatement> defineVerbRelation(SentenceModel sentenceModel){
		
		ArrayList<PlausibleStatement> verbRelations = new ArrayList<PlausibleStatement>();
		
		SentencePart verb = sentenceModel.getVerb();
		
		//here _wsd of subject(s), object(s), and adverb(s) of this sentence has been allocated!								
		ArrayList<SentencePart> subjects = sentenceModel.getSubjects();
		
		if(verb == null || subjects == null || subjects.size() == 0){
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
						print("verb relation added ------------- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");														
					}				
			
			if(!transitive_verb){
				//TODO check if using "KnowledgeBase.HPR_ANY" as referent is correct?!
				//adding the relation of this sentence to kb. 
				PlausibleStatement rel = _kb.addRelation(sbj._wsd, KnowledgeBase.HPR_ANY, verb._wsd, SourceType.TTS);
				verbRelations.add(rel);
				print("verb relation added ------------- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");				
			}
		}
		return verbRelations;
	}

	private  ArrayList<Node>  loadVerbSemanticArguments(SentenceModel sentence) {
		SentencePart verbPart = sentence.getVerb();
		
		Node pure_verb = _ttsEngine.getPureNode(verbPart._wsd);
		
		if(verbPart == null || verbPart._wsd == null || pure_verb == null){
			MyError.error("verb or its wsd or its pure version should not be null!" + verbPart);
			return null;
		}
		
		Node verb_synSet = pure_verb.getSynSet();
						
		if(verb_synSet == null){
			MyError.error("this verb '" + pure_verb + "' has no SynSet!");
			return null;
		}
		
		Node mainSemanticArg = _kb.addConcept(MainSemanticArgumet_name);
		
		ArrayList<PlausibleAnswer> semArgsAnswers = _ttsEngine.writeAnswersTo(mainSemanticArg, verb_synSet, null);	
		
		ArrayList<Node> semArgs = new ArrayList<Node>();			
		
		for(PlausibleAnswer arg:semArgsAnswers)
			if(arg != null && arg.answer != null)
				semArgs.add(arg.answer);
		
		return semArgs;		
	}

	
//	/**
//	 * load verb capacities from kb.
//	 * 
//	 * @param pure_verb guaranteed not to be null.
//	 */
//	private ArrayList<PlausibleStatement> loadVerbCapacities(Node pure_verb) {	
//		
//		ArrayList<PlausibleStatement> cxs = new ArrayList<PlausibleStatement>();
//		
//		Node synSet = pure_verb.getSynSet();
//		
//		print("\nSynSet of " + pure_verb + " is " + synSet);
//		
//		if(synSet == null)
//			MyError.error("the verb " + pure_verb + " has no Synset!");
//		else
//			cxs = synSet.loadCXs();
//		
//		if(default_contexts == null){			
//			Node verb_root = _ttsEngine.verb_root;
//			default_contexts = verb_root.loadCXs();
//		}
//		
//		cxs.addAll(default_contexts);	
//		
//		return cxs;
//	}
//	
//	private void setLocationContext(PlausibleStatement verbRelation, Location location, ArrayList<PlausibleStatement> CXs){
//		if(verbRelation == null || location == null || CXs == null)
//			return;
//		
//		for(PlausibleStatement cx : CXs){		
//			
//			if(cx.relationType == null){
//				MyError.error("this " + cx + " has no relationType!");
//				return;
//			}			
//			
//			String cxName = cx.relationType.getContextName();			
//			
//			if(CONTEXT.fromString(cxName) == CONTEXT.LOCATION){				
//				PlausibleStatement locCx = _kb.addRelation(verbRelation, location._node, cx.relationType);
//				print("" + locCx + " (" + verbRelation.getName() + ")= " + location._node + "\n");			
//			}
//		}
//	}
//	
//	
//	private void setTimeContext(PlausibleStatement verbRelation, Time time, ArrayList<PlausibleStatement> CXs) {
//		if(verbRelation == null || time == null || CXs == null)
//			return;
//		
//		for(PlausibleStatement cx : CXs){		
//			
//			if(cx.relationType == null){
//				MyError.error("this " + cx + " has no relationType!");
//				return;
//			}			
//			
//			String cxName = cx.relationType.getContextName();			
//			
//			if(CONTEXT.fromString(cxName) == CONTEXT.TIME){				
//				PlausibleStatement timeCx = _kb.addRelation(verbRelation, time._node, cx.relationType);
//				print("" + timeCx + " (" + verbRelation.getName() + ")= " + time._node + "\n");			
//			}
//		}
//		
//	}
}

