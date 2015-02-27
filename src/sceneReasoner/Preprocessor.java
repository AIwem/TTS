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
import model.SemanticTag;
import model.SentencePart;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;
import model.SubSemanticTag;
import model.VerbType;

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
	private String mainSemanticArgumet_name = "MainSemArg";
	private String zamir_enekasi = "خود#n3";
	
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */
//	private String sentenceInfosFileName = "inputStory/sentenceInfos2_simple.txt";
//	private String sentenceInfosFileName = "inputStory/sentenceInfos_SS.txt";
//	private String sentenceInfosFileName = "inputStory/SentenceInfos12.txt";
//	private String sentenceInfosFileName = "inputStory/SentenceInfos2-1.txt";
	private String sentenceInfosFileName = "inputStory/SentenceInfos3.txt";

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
	 * name:کبوتر را	POS:NOUN	SYN:OBJ		SRC:	SEM:ARG1_THEME		WSD:کبوتر#n1	sub_part:2,3	num:
	 * name:کبوتر	POS:NOUN	SYN:PREDEP	SRC:3	SEM:ARG1_THEME_P	WSD:کبوتر#n1	sub_part:-		num:2
	 * name:را	POS:UNKNOWN	SYN:OBJ		SRC:4	SEM:ARG1_THEME_P	WSD:-		sub_part:-		num:3
	 * 
	 * @param partStr partStr has all information about current Part.
	 * @return equivalent Part Object.
	 */
	private SentencePart createPart(String partStr, SentenceModel senteceModel){
	
		String[] parts = partStr.split("(\t)+");
		
		if(parts.length != 8){			
			MyError.error("Bad sentence information format " + partStr + " parts-num " + parts.length);
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
				
		//print(newPart.getStr());
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
//				print(currentPart._wsd_name + " couldn't get allocated!");
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

	/**
	 * preprocessScene preprocesses input sentenceModel and converts it to the primarySceneModel.    
	 * 
	 * @param sentenceModel the SenetenceModel to be converted. guaranteed not to be null.
	 * @param primarySceneModel the SceneModel which this sentenceModel is to be added to. guaranteed not to be null.
	 * @param storyModel the StoryModel which the returned sceneModel is to be added to. guaranteed not to be null.
	 * @return SceneModel containing input sentenceModel 
	 */	 
	public void preprocessScene(SentenceModel sentenceModel, SceneModel primarySceneModel, StoryModel storyModel){		
		
		if(sentenceModel == null || primarySceneModel == null || storyModel == null){
			MyError.error("None of senetecenModel, sceneModel, and storyModel should be null! " + sentenceModel);
			return; //null;
		}		
		
		prepareNullSemanticTags(sentenceModel, primarySceneModel, storyModel);
		
		checkAllSemanticTagsWithUser(sentenceModel, primarySceneModel);
		
		if(sentenceModel.getArg0() != null)
			preprocessSemanticArg(sentenceModel.getArg0().convertToSemanticTag(), sentenceModel, primarySceneModel);
		
		if(sentenceModel.getArg1() != null)
			preprocessSemanticArg(sentenceModel.getArg1().convertToSemanticTag(), sentenceModel, primarySceneModel);
	
		if(sentenceModel.getArg2() != null)
			preprocessSemanticArg(sentenceModel.getArg2().convertToSemanticTag(), sentenceModel, primarySceneModel);

		preprocessVerbArg(sentenceModel, primarySceneModel);
		
		SceneElement arg3SceneElem = null;
		
		SceneElement arg4SceneElem = null;
		
		if(sentenceModel.getArg3() != null)
			arg3SceneElem = preprocessSemanticArg(sentenceModel.getArg3().convertToSemanticTag(), sentenceModel, primarySceneModel);
				
		if(sentenceModel.getArg4() != null)
			arg4SceneElem = preprocessSemanticArg(sentenceModel.getArg4().convertToSemanticTag(), sentenceModel, primarySceneModel);
		
		if(sentenceModel.getArg5() != null)
			preprocessSemanticArg(sentenceModel.getArg5().convertToSemanticTag(), sentenceModel, primarySceneModel);
	
		processSubSemanticArgs(sentenceModel, primarySceneModel);
		
		processLocationOfScene(sentenceModel, arg3SceneElem, arg4SceneElem, primarySceneModel);
		
		processVisualTags(sentenceModel, primarySceneModel);
				
		print("\nprimarySceneModel\n" + primarySceneModel);
//		return primarySceneModel;
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
		
		print("\n=============== in   prepareNullSemanticTags =======================");
		
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
		
		if(!Common.isEmpty(missingMainArgs))
			for(SceneModel scene:storyModel.getScenes())
				if(!scene.equals(primarySceneModel))
					prepareNullSemanticTagsForAScene(sentenceModel, missingMainArgs, scene);
				
		print("=============== end of prepareNullSemanticTags =====================");
	}	
	
	
	private void checkAllSemanticTagsWithUser(SentenceModel sentenceModel, SceneModel primarySceneModel){
		//TODO check with user!		
	}
	
	/**
	 * This method preprocesses the SemanticTag of sentenceModel, both MainSemanticTags and SubSemanticTags.
	 * it creates proper SceneElement based on the SenetncePart of that semanticTag and its related ScenePart.
	 * then adds this SceneElement to the primarytSceneModel.
	 * 	<li> if ScenePart of SceneElement is ROLE 
	 * 		<ul> then adds it to roles of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is DYNAMIC_OBJECT 
	 * 		<ul> then adds it to dynamic_objects of primarySceneModel. </ul> 
	 * </li>
	 * <li> if ScenePart of SceneElement is STATIC_OBJECT
	 * 		<ul> then adds it to static_objects of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is SCENE_EMOTION
	 * 		<ul> then adds it to scene_emotions of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is SCENE_GOAL
	 * 		<ul> then adds it to scene_goals of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is LOCATION
	 * 		<ul> then adds it to alternativeLocations of primarySceneModel, not Location of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is TIME
	 * 		<ul> then adds it to alternativeTime of primarySceneModel, not Time of primarySceneModel. </ul>
	 * </li>
	 * <li> if ScenePart of SceneElement is ROLE_MOOD or OBJECT_STATE 
	 * 		<ul> do nothing.</ul>
	 * </li>
	 * 
	 * This method preprocesses the dependents (adjectives and mozaf_elaihs) of semanticTags too. 
	 * 
	 * @param semanticTag may be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 * @return the SceneElement created based on semanticTag
	 */
	private SceneElement preprocessSemanticArg(SemanticTag semanticTag, SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		if(semanticTag == null)
			return null;
		
		if(sentenceModel.hasSemanticArg(semanticTag)){
						
			print("\n=============== in     preprocess " + semanticTag + " =======================");
			
			
			SentencePart semArgPart = sentenceModel.getSentencePart(semanticTag);
			
			if(semArgPart == null){				
				MyError.error("the sentenceModel has " + semanticTag + " but it didn't find!" + sentenceModel);
				return null;
			}
			
			//It means that it is verb (infinitive) so the processing must perform on it and its dependents too!
			if(semArgPart.isInfinitive()){
				//TODO: complete this part later!
				print("It is an infinitive so, the process should got performed for it again!");
				
				return null;
			}
			else{
						
				//reasoning ScenePart from KB, ROLE,DYNAMIC_OBJECT, STATIC_OBJECT, ....
				ScenePart scenePart = _ttsEngine.whichScenePart(semArgPart);
				
				if(scenePart == null || scenePart == ScenePart.UNKNOWN){
					MyError.error("the " + semanticTag +": " + semArgPart + " ScenePart was not found!");
					return null;
				}
				
				SceneElement inputSceneElement = createSceneElement(semArgPart, scenePart);
				
				if(inputSceneElement == null){
					MyError.error("the " + semArgPart + " could not convert to a SceneElement!");
					return null;
				}
				
				boolean isRedundantPart = primarySceneModel.hasSceneElement(inputSceneElement);
				
				SceneElement sceneElem = null;
				
				//It means that the primarySceneModel has had this ScenePart before, so we will merge the information of this part with that one
				if(isRedundantPart){
					
					print(inputSceneElement._name + " is redundant!");
					
					sceneElem = primarySceneModel.getSceneElement(inputSceneElement);
					
					if(sceneElem == null){
						MyError.error("primarySceneModel has " + semArgPart + " but it could not be found!");
						return null;
					}
					sceneElem.mergeWith(inputSceneElement);	
				}
				//It means that this semArgPart is a newly seen ScenePart which is to be added to primarySceneModel.
				else{
					//creates a new ScenePart based on semArgPart and adds it to the primarySceneModel or return null if it was redundant!
					primarySceneModel.addToPrimarySceneModel(inputSceneElement);
					
					sceneElem = inputSceneElement;				
				}
				
				//------------------- pre-processing dependents of semArgPart --------------------
				
				preprocessDependentsOfSemanticArg(semArgPart, sceneElem);
								
				print("=============== end of preprocess " + semanticTag + " =======================");
				
				return sceneElem;
			}
		}
		return null;
	}
		
	/**
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessVerbArg(SentenceModel sentenceModel, SceneModel primarySceneModel) {
				
		print("\n--------------- in   verb preprocess -------------------------------");
		
		//TODO: check the correct place of this statement!
		defineVerbRelation(sentenceModel);
		
		SentencePart verb = sentenceModel.getVerb();
		
		if(verb == null){
			MyError.error("The SentenceModel has no verb as a mistake!");
			return;
		}
			
		VerbType verbType = verb.getVerbType();
		
		print(verb + " vebType is: " + verbType);
		
		switch(verbType.name()){
			case("MORAKAB"):
				preprocessingActionVerb(sentenceModel, primarySceneModel);
				break;
			case("BASIT"):
				preprocessingActionVerb(sentenceModel, primarySceneModel);			
				break;			
			case("BASIT_RABTI"):
				//TODO: check this part!
				preprocessingRabtiVerb(verb, sentenceModel, primarySceneModel);
				break;				
			case("BASIT_NAMAFOLI"):
				//TODO: check this part!
				preprocessingNamafoliVerb(verb, sentenceModel, primarySceneModel);
				break;			
			default:
				print("Unknown verb type!");
			
		}		
		print("--------------- end of verb preprocess -----------------------------");
	}
	
	/**
	 * This method processes the SubSemanticTags of sentenceModel.
	 * The SubSemanticTags are as follow:
	 * <li> if sentenceModel has Arg-LOC	کجا رویداد(فعل) اتفاق می‌افتد؟مکان فیزکی و انتزاعی 
	 * 		<ul> if isLocation, setLocation of primarySceneModel.</ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-DIR	مسیر حرکت 
	 * 		<ul> this Arg will be process in processLocationOfScene method.</ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-TMP	زمان
	 * 		<ul> if isTime, setTime of primarySceneModel.</ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-MNR	چگونه؟
	 * 		<ul> if Arg0 of sentenceModel is Role, then adds RoleMood to it.</ul>
	 * 		<ul> if Arg0 of sentenceModel is Dynamic_object, then adds Object_state to it.</ul> 
	 *  
	 * </li>
	 * <li>  if sentenceModel has Arg-PRP	منظور، هدف و انگیزه رویداد
	 * 		<ul> adds to scene_goals of primarySceneModel via preprocessSemanticArg.</ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-GOL	غایت فعل + برای کس یا چیز دیگر(مقصد افعال حرکتی،سودبرنده افعال دیگر)
	 * 		<ul> if isHuman, adds to roles of primarySceneModel via preprocessSemanticArg.</ul>
	 * 		<ul> if isAnimal, adds to dynamic_objects of primarySceneModel via preprocessSemanticArg.</ul>
	 * 		<ul> if isLocation, adds to alternativeLocations of primarySceneModel via preprocessSemanticArg. </ul>
	 * 		<ul> otherwise, adds to static_objects of primarySceneModel via preprocessSemanticArg. </ul>  
	 * </li>
	 * <li>  if sentenceModel has Arg-CAU	هدف انجام عمل، چرا؟ 
	 * 		<ul> adds to scene_goals of primarySceneModel via preprocessSemanticArg.</ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-COM	همراه با چه کسی/نهادی (انسان یا سازمان، شیء نمیشود)ه 
	 * 		<ul> if isHuman, adds to roles of primarySceneModel via preprocessSemanticArg.</ul>
	 * 		<ul> if isAnimal, adds to dynamic_objects of primarySceneModel via preprocessSemanticArg.</ul>		
	 * </li>
	 * <li>  if sentenceModel has Arg-INS	ابزار یا شیء انجام رویداد
	 * 		<ul> if isAnimal, adds to dynamic_objects of primarySceneModel via preprocessSemanticArg.</ul>
	 * 		<ul> else adds to static_objects of primarySceneModel via preprocessSemanticArg.</ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-EXT	میزان تغییر حاصل از فعل
	 * 		<ul> no processing yet! </ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-REC	ضمایر انعکاسی و دوطرفه (خود، یکدیگر)
	 * 		 <ul> no processing yet! </ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-MOD	فعل وجهی(باید، ممکن‌است، قادراست، شاید، احتمالا) 
	 * 		<ul> no processing yet! </ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-NEG	عدم وقوع رویداد
	 * 	 	<ul> no processing yet! </ul> 
	 * </li>
	 * <li>  if sentenceModel has Arg-CND	تحلیل جملات شرطی 
	 * 	 	<ul> no processing yet! </ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-DIS	علایم گفتمان (بنابراین، ازاین رو) 
	 * 	 	<ul> no processing yet! </ul>
	 * </li>
	 * <li>  if sentenceModel has Arg-ADV	هر قید دیگری که در بالا نگنجد.
	 * 	 	<ul> no processing yet! </ul> 
	 * </li>
		
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void processSubSemanticArgs(SentenceModel sentenceModel, SceneModel primarySceneModel) {
				
		print("\n=============== in processSubSemArgs ===============================");
		
		//processing ArgM_LOC, if any.
		SentencePart locPart = sentenceModel.getSentencePart(SubSemanticTag.LOC);
		if(locPart != null){
			print("SceneModel has " + locPart + " ArgM_LOC");
			
			//It can only return Location SceneElement. 
			SceneElement locElem = preprocessSemanticArg(SubSemanticTag.LOC.convertToSemanticTag(), sentenceModel, primarySceneModel);
			
			if(locElem.scenePart == ScenePart.LOCATION)
				primarySceneModel.setLocation((Location) locElem);
		}
		
		//processing ArgM_TMP, if any.
		SentencePart tmpPart = sentenceModel.getSentencePart(SubSemanticTag.TMP);
		if(tmpPart != null){
			print("SceneModel has " + tmpPart + " ArgM_TMP");

			//It can only return Time SceneElement.
			SceneElement tmpElem = preprocessSemanticArg(SubSemanticTag.TMP.convertToSemanticTag(), sentenceModel, primarySceneModel);
			
			if(tmpElem != null && tmpElem.scenePart == ScenePart.TIME)
				primarySceneModel.setTime((Time) tmpElem);
		}
		
		//processing ArgM_MNR, if any.
		SentencePart mnrPart = sentenceModel.getSentencePart(SubSemanticTag.MNR);
		if(mnrPart != null){
			print("SceneModel has " + mnrPart + " ArgM_MNR");
			
			SentencePart arg0Part = sentenceModel.getArg0SentencePart();
			
			if(arg0Part != null){
				SceneElement arg0Elem = primarySceneModel.getSceneElement(arg0Part._wsd);
				
				if(arg0Elem != null){
					//It can be added as RoleMood or ObjectState to Arg0
					SceneElement mnrElem = arg0Elem.addDependent(mnrPart, "manner");
					
					if(mnrElem != null)
						preprocessDependentsOfSemanticArg(mnrPart, mnrElem);
				}
				else
					print("no SceneElement for Arg0 found in primarySceneModel, in order to " + mnrPart + " to be added to it!");
			}
			else
				print("no Arg0Part found in sentnceModel, in order to " + mnrPart + " to be added to it!");			
		}

		//processing ArgM_PRP, if any.
		SentencePart prpPart = sentenceModel.getSentencePart(SubSemanticTag.PRP);
		if(prpPart != null){
			print("SceneModel has " + prpPart + " ArgM_PRP");
			
			//It can return only Scene_goal SceneElement. 
			preprocessSemanticArg(SubSemanticTag.PRP.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
		
		//processing ArgM_GOL, if any.
		SentencePart golPart = sentenceModel.getSentencePart(SubSemanticTag.GOL);
		if(golPart != null){
			print("SceneModel has " + golPart + " ArgM_GOL");
			
			//It can return Role, Dynamic_object, Static_object, or Location SceneElement. 
			preprocessSemanticArg(SubSemanticTag.GOL.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}

		//processing ArgM_CAU, if any.
		SentencePart cauPart = sentenceModel.getSentencePart(SubSemanticTag.CAU);
		if(cauPart != null){
			print("SceneModel has " + cauPart + " ArgM_CAU");
			
			//It can return only Scene_goal SceneElement. 
			preprocessSemanticArg(SubSemanticTag.CAU.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
		
		//processing ArgM_COM, if any.
		SentencePart comPart = sentenceModel.getSentencePart(SubSemanticTag.COM);
		if(comPart != null){
			print("SceneModel has " + comPart + " ArgM_COM");
			
			//It can return only Role or Dynamic_object SceneElement. 
			preprocessSemanticArg(SubSemanticTag.COM.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
	
		//processing ArgM_INS, if any.
		SentencePart insPart = sentenceModel.getSentencePart(SubSemanticTag.INS);
		if(insPart != null){
			print("SceneModel has " + insPart + " ArgM_INS");
			
			//It can return only Dynamic_object or Static_object SceneElement. 
			preprocessSemanticArg(SubSemanticTag.INS.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
		
		print("=============== end of processSubSemArgs ===========================");
	}
	
	/**
	 * 
	 * @param sentenceModel guaranteed not to be null.
	 * @param arg3SceneElement
	 * @param arg4SceneElement
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void processLocationOfScene(SentenceModel sentenceModel, SceneElement arg3SceneElement, SceneElement arg4SceneElement, SceneModel primarySceneModel) {
		
		if(primarySceneModel.getLocation() != null)
			return;
		
		print("\n=============== in processLocation =================================");
		
		SentencePart arg3Part = sentenceModel.getArg3SentencePart();
		
		SentencePart arg4Part = sentenceModel.getArg4SentencePart();
		
		if(arg3Part != null && arg3Part._semanticTag != null && arg3Part._semanticTag.isMainSemanticTag()){
		
			MainSemanticTag arg3SemArg = arg3Part._semanticTag.convertToMainSemanticTag();
			
			//Sentence has ARG3_SOURCE_STARTPOINT
			if(arg3SemArg == MainSemanticTag.ARG3_SOURCE_STARTPOINT && arg3SceneElement != null && arg3SceneElement.scenePart == ScenePart.LOCATION){
			
				primarySceneModel.setLocation((Location)arg3SceneElement);
				
				//Sentence has ARG4_ENDPOINT
				if(arg4Part != null && arg4SceneElement != null)			 
					primarySceneModel.addAlternativeLocation((Location)arg4SceneElement);				
				
				//Sentence hasn't ARG4_ENDPOINT
				else{
					
					SentencePart arg_dirPart = sentenceModel.getSentencePart(SubSemanticTag.DIR);
					
					//Sentence has ARG_DIR
					if(arg_dirPart != null){
						
						SceneElement arg_dirSceneElem = preprocessSemanticArg(SubSemanticTag.DIR.convertToSemanticTag(), sentenceModel, primarySceneModel) ;							
						
						if(arg_dirSceneElem == null){
							MyError.error("the " + arg_dirPart + " could not convert to a SceneElement!");
							return;
						}							

						primarySceneModel.addAlternativeLocation((Location)arg_dirSceneElem);
					}										
				}				
			}
			return;		 			
		}
		//Sentence hasn't ARG3_SOURCE_STARTPOINT but maybe have arg4Part or arg_dirPart
		
		//Sentence has ARG4_ENDPOINT
		if(arg4Part != null)			
				primarySceneModel.addAlternativeLocation((Location)arg4SceneElement);				
		
		//Sentence hasn't ARG4_ENDPOINT
		else{
			
			SentencePart arg_dirPart = sentenceModel.getSentencePart(SubSemanticTag.DIR);
			
			//Sentence has ARG_DIR
			if(arg_dirPart != null){
				
				SceneElement arg_dirSceneElem = preprocessSemanticArg(SubSemanticTag.DIR.convertToSemanticTag(), sentenceModel, primarySceneModel);
				
				if(arg_dirSceneElem == null){
					MyError.error("the " + arg_dirPart + " could not convert to a SceneElement!");
					return;
				}
				
				primarySceneModel.addAlternativeLocation((Location)arg_dirSceneElem);
			}								
		}
		
		print("=============== end of processLocation =============================");		
	}

	private void processVisualTags(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		// TODO Auto-generated method stub
		
	}	

	/**
	 * 
	 * @param sentenceModel guaranteed not to be null.
	 * @param missingMainArgs guaranteed not to be null.
	 * @param sceneModel guaranteed not to be null.
	 */
	private void prepareNullSemanticTagsForAScene(SentenceModel sentenceModel, ArrayList<MainSemanticTag> missingMainArgs, SceneModel sceneModel){		
		
		if(!Common.isEmpty(missingMainArgs)){
			
			ArrayList<MainSemanticTag> preparedMainArgs = new ArrayList<MainSemanticTag>();
			
			ArrayList<SentenceModel> allSentences = sceneModel.getSentences();
			
			if(!Common.isEmpty(allSentences)){		
				
				for(MainSemanticTag miss:missingMainArgs){
					
					if(miss != null && (miss.isArg0() || miss.isArg1())){
					
						for(SentenceModel sent:allSentences){
							if(sent.equals(sentenceModel))
								continue;
							
	//						print("In sentence: " + sent.getOriginalSentence());
							
							if(miss.isArg0() && sent.hasArg0()){							
								SentencePart arg0SentencePart = sent.getArg0SentencePart();
								
								arg0SentencePart.set_semanticTag(miss.name());
								preparedMainArgs.add(arg0SentencePart._semanticTag.convertToMainSemanticTag());							
								sentenceModel.setPrerparedSentencePart(arg0SentencePart);						
								
								print("prepared " + arg0SentencePart._semanticTag);
								break;
							}
							else if(miss.isArg1() && sent.hasArg1()){
								SentencePart arg1SentencePart = sent.getArg1SentencePart();
								
								arg1SentencePart.set_semanticTag(miss.name());
								preparedMainArgs.add(arg1SentencePart._semanticTag.convertToMainSemanticTag());
								sentenceModel.setPrerparedSentencePart(arg1SentencePart);
								
								print("prepared " + arg1SentencePart._semanticTag);
								break;
							}
						}
					}					
				}
				for(MainSemanticTag prepared:preparedMainArgs)
					missingMainArgs.remove(prepared);
			}			
		}			
	}
	
	/**
	 * this method process the dependents (adjectives and mozaf_elaihs) of semArgPart (if any).
	 * it converts each adjective and mozaf_elaih of semArgPart to :
	 * <li> if scenePart of sceneElement is ROLE:
	 * 		<ul> it adds a RoleMood to sceneElement. </ul>	
	 * </li>	  			
	 * <li> if scenePart of sceneElement is DYNAMIC_OBJECT or STATIC_OBJECT:
	 *  	<ul> it adds an ObjectState to sceneElement. </ul> 		
	 * </li> 
	 * <li> if scenePart of sceneElement is LOCATION, TIME, SCENE_EMOTION, SCENE_GOAL:
	 * 		<ul> it dose nothing yet! </ul>
	 * <li> 
	 * 
	 * @param semArgPart guaranteed not to be null.
	 * @param sceneElement guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessDependentsOfSemanticArg(SentencePart semArgPart, SceneElement sceneElement){
		
		//It means that this sentencePart has some adjectives
		if(semArgPart.hasAnyAdjectives()){
			
			print(semArgPart + " " + semArgPart._semanticTag + " has some adjectives:" + semArgPart.getAdjectives());
			
			ArrayList<SentencePart> adjectives = semArgPart.getAdjectives();
			
			for(SentencePart adj:adjectives)
				sceneElement.addDependent(adj, "adjective");			
		}
		//It means that this sentencePart has some mozad_elaih
		if(semArgPart.hasAnyMozaf_elaihs()){				
			
			print(semArgPart  + " " + semArgPart._semanticTag + " has mozaf_elaih: " + semArgPart.getMozaf_elaih());
			
			ArrayList<SentencePart> mozafs = semArgPart.getMozaf_elaih();
			
			for(SentencePart moz:mozafs)
				
				if(moz._wsd != null && !moz._wsd.getName().contains(zamir_enekasi))
					sceneElement.addDependent(moz, "mozaf_elaih");
		}			
	}

	/**
	 * 
	 * @param verb guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessingNamafoliVerb(SentencePart verb, SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		SentencePart arg1Part = sentenceModel.getArg1SentencePart();
		
		if(arg1Part == null){
			print(verb + " is namafoli but ARG1 not found as a mistake!");
			return;
		}		
		
		SceneElement arg1Elem = primarySceneModel.getSceneElement(arg1Part._wsd);
		
		if(arg1Elem == null){
			print(arg1Part + " can not be found in primarySceneModel!");
			return;
		}		
		
		arg1Elem.addDependent(verb, "adjective");
	}

	/**
	 * 
	 * @param verb guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessingRabtiVerb(SentencePart verb, SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		SentencePart arg1Part = sentenceModel.getArg1SentencePart();
		
		SentencePart arg2Part = sentenceModel.getArg2SentencePart();
		
		if(arg1Part == null || arg2Part == null){
			print(verb + " is rabti but ARG1 or ARG2 not found as a mistake!");
			return;
		}		
		
//		if(arg2Part.isAdjective()){
		
			SceneElement arg1Elem = primarySceneModel.getSceneElement(arg1Part._wsd);
			
			if(arg1Elem == null){
				print(arg1Part + " can not be found in primarySceneModel!");
				return;
			}		
			
			arg1Elem.addDependent(arg2Part, "adjective");			
//		}
//		else
//			print(verb + " is rabti but Arg2Part " + arg2Part + " is not adjective!");
	}

	private void preprocessingActionVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		SentencePart verb = sentenceModel.getVerb();
		
		if(verb == null){
			MyError.error("The SentenceModel has no verb as a mistake!");
			return;
		}
		
		SentencePart arg0Part = sentenceModel.getArg0SentencePart();
		
		if(arg0Part == null){
			MyError.error("The " + verb + " is BASIT, but has no Arg0Part as a mistake!");
			return;
		}
		
		SceneElement arg0Elem = primarySceneModel.getSceneElement(arg0Part._wsd);
		
		if(arg0Elem == null){
			print(arg0Part + " can not be found in primarySceneModel!");
			return;
		}
		
		arg0Elem.addDependent(verb, "action");	
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
							descriptor = _kb.addConcept(relation_name, false);
						}			    		
			    	}
			    }	
			}
			
			
			if(descriptor != null){//it means that findRelation has not found it and it is newly fetched from kb.			
				
				wsd = _kb.addRelation(argument, referent, descriptor, SourceType.TTS);
				
				print("wsdRel added ---- : " + wsd.argument.getName() + " --> " + wsd.getName() + " --> " + wsd.referent.getName() + "\n");
				
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
						descriptor = _kb.addConcept(relation_name, false);						
						
						wsd = _kb.addRelation(argument, referent, descriptor, SourceType.TTS);
						
						print("wsdRel added ---- : " + wsd.argument.getName() + " -- " + wsd.getName() + " -- " + wsd.referent.getName() + "\n");
						
						_ttsEngine.addRelationInstance(relation_name, wsd);
					}
				}
				else
					descriptor = wsd;
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
	
	private SceneElement createSceneElement(SentencePart part, ScenePart scenePart){
		
		if(part == null || scenePart == null || scenePart == ScenePart.UNKNOWN){
			MyError.error("null input parameter for createSceneElement !");
			return null;
		}								
		
		if(scenePart == ScenePart.ROLE)			
				return new Role(part._name, part._wsd);
		else if(scenePart == ScenePart.ROLE_ACTION)
			return new RoleAction(part._name, part._wsd);
		else if(scenePart == ScenePart.DYNAMIC_OBJECT)
			return new DynamicObject(part._name, part._wsd);
		else if(scenePart == ScenePart.OBJECT_ACTION)
			return new ObjectAction(part._name, part._wsd);
		else if(scenePart == ScenePart.STATIC_OBJECT)
			return new StaticObject(part._name, part._wsd);				
		else if(scenePart == ScenePart.LOCATION)
			return new Location(part._name, part._wsd);			
		else if(scenePart == ScenePart.TIME)
			return new Time(part._name, part._wsd);
		else if(scenePart == ScenePart.SCENE_EMOTION)
			return new SceneEmotion(part._name, part._wsd);
		else if(scenePart == ScenePart.SCENE_GOAL)
			return new SceneGoal(part._name, part._wsd);
		return null;
	}

	
	@SuppressWarnings("unused")
	private void add_adjective_mozaf(SentenceModel sentence, SentencePart mainPart, Node descriptor, Node referent) {
		
		if(sentence == null || mainPart == null || descriptor == null || referent == null){
			MyError.error("null input parameter for add_adjective_mozaf!");
			if(mainPart == null)
				MyError.error("no mainPart could be found!");
			return;
		}
		
		if(descriptor.getPos() == POS.ADJECTIVE){ //it means that descriptor is describing an adjective.
		
			SentencePart adjPart = new SentencePart(referent.getName(), POS.ADJECTIVE, DependencyRelationType.NPOSTMOD, null, referent, null, sentence);
			
			//1:added, 0:merged, -1:Nop
			int added = mainPart.addAdjective(adjPart);
		}
		else if(descriptor.getPos() == POS.NOUN){ //it means that descriptor is describing a mozaf_alaih.
			
			SentencePart mozPart = new SentencePart(referent.getName(), POS.NOUN, DependencyRelationType.MOZ, null, referent, null, sentence);
			
			//1:added, 0:merged, -1:Nop
			int added = mainPart.addMozaf_elaih(mozPart);
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
						print("verbRel added ---- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");														
					}				
			
			if(!transitive_verb){
				//TODO check if using "KnowledgeBase.HPR_ANY" as referent is correct?!
				//adding the relation of this sentence to kb. 
				PlausibleStatement rel = _kb.addRelation(sbj._wsd, KnowledgeBase.HPR_ANY, verb._wsd, SourceType.TTS);
				verbRelations.add(rel);
				print("verbRel added ---- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");				
			}
		}
		return verbRelations;
	}

	private  ArrayList<Node>  loadVerbSemanticArguments(SentenceModel sentence) {
		SentencePart verbPart = sentence.getVerb();
		if(verbPart == null)
			return null;
		
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
		
		Node mainSemanticArg = _kb.addConcept(mainSemanticArgumet_name, false);
		
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


	