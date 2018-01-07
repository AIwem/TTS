package sceneReasoner;

import ir.ac.itrc.qqa.nlp.PersianTools;
import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
//import ir.ac.itrc.qqa.semantic.enums.POS;
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
import model.POS;
import model.Phrase;
import model.ScenePart;
import model.SemanticTag;
import model.SentenceModel;
import model.Word;
import model.SceneModel;
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
	
	private PersianTools persianTool = null;
	
	private KnowledgeBase _kb;
//	private SemanticReasoner _re;
	private TTSEngine _ttsEngine = null;
		
//	private ArrayList<PlausibleStatement> default_contexts = null;
	private String mainSemanticArgumet_name = "MainSemArg";
	private String visualCapacity_name = "VC";
	private String default_verb_name = "verb#v";
	private String zamir_enekasi = "خود#n3";
	private String fake_word_name = "fake_Word";
	
	
	/**
	 * We have no NLP module to process input text and convert it to related part,
	 * so temporarily we aught to read these processed information from a file named  SentenceInfosFileName. 
	 */

//	private String sentenceInfosFileName = "inputStory/SentenceInfos1-3.txt";
//	private String sentenceInfosFileName = "inputStory/SentenceInfos2-3.txt";
	private String sentenceInfosFileName = "inputStory/SentenceInfos3-1.txt";

	public Preprocessor(KnowledgeBase kb, SemanticReasoner re, TTSEngine ttsEngine) {
		this._kb = kb;
//		this._re = re;
		this._ttsEngine = ttsEngine;
		
		persianTool = new PersianTools();
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
	 * This method gets extraWordInfo and completes that Word instance in the sentenceModel.
	 * extraWordInfo has a format like this:
	 * 
	 * name:پسرک			SEM:ARG0_EXPERIENCER	WSD:پسر#n2_وضعیت سنی#a_خردسال#a1	num:1
	 * name:کبوتر			SEM:ARG1_THEME			WSD:کبوتر#n1					num:7
	 * 
	 * @param extraWordInfo has information about Semantic Argument and WSD of current Word.
	 * @return completed Word instance in the sentenceModel.
	 */
	private Word completeWordInfo(String extraWordInfo, SentenceModel senteceModel){
	
		String[] parts = extraWordInfo.split("(\t)+");
		
		if(parts.length != 4){			
			MyError.error("Bad Word information format " + extraWordInfo + " parts-num " + parts.length);
			return null;
		}
					
		for(int i = 0; i < parts.length; i++)
			parts[i] = parts[i].substring(parts[i].indexOf(":")+1);				
		
		Word sentenceWord = senteceModel.getWord(parts[0]);
		
		if(sentenceWord != null){		
					
			if(parts[1] != null && !parts[1].equals("-"))
				sentenceWord.set_semanticTag(parts[1]);
			
			sentenceWord.set_wsd_name(parts[2]);
		}
		else{
			print("************************** here in phrase Word " + parts[0]);
			
			String phraseName = parts[0];

			//It means that this record of input is defining a phrase so
			//we must generate a fake Word with this wsd_name in order to just adding its relation to KB.
			if(phraseName.indexOf("_Phrase") != -1){
				String ph_headStr = phraseName.substring(0, phraseName.indexOf("_Phrase"));
															
				Word fakeWord = new Word(senteceModel, senteceModel.get_phrase_with_head(ph_headStr), -1, fake_word_name , "", POS.UNKNOWN, ""
						, DependencyRelationType.ANY, -1, null, null, parts[2]);

				sentenceWord = fakeWord;				
			}			
		}		
		
		return sentenceWord;		
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
		
		String parsedSen = persianTool.parser(NLsentence);
		
		String[] word_strs = parsedSen.split("\n");
		
		SentenceModel sentence = new SentenceModel(NLsentence, word_strs);

		print("^^^^^^^^^^^^^^^^^");

		
		//this array has information of all parts of this sentence.
		ArrayList<String> extraWordInfos = findSentenceInfos(NLsentence);
		
		if(extraWordInfos == null)
			return null;
		
		for(int i = 0; i < extraWordInfos.size(); i++){
			
			String currentWordInfo = extraWordInfos.get(i);
			
			Word currentWord = completeWordInfo(currentWordInfo, sentence);
			
			if(currentWord == null){
				MyError.error("The word with \"" + currentWordInfo + "\" couldn't be completed!" );
				continue;
				//return sentence;
			}
			
			if(currentWord._number < 0 && currentWord._wordName != null && currentWord._wordName.equals(fake_word_name))
				print("^^^^^^^^^^^^^^^^^^^^^^^^^^^ in fake_word");
			
			//setting _wsd of currentPart to the proper Node of KB.			
			if(currentWord.isVerb())
				//isnewNode parameter is true, because every verb is new a one!
				allocate_wsd(sentence ,currentWord, true);
			else
				allocate_wsd(sentence, currentWord, false);
			
			if(currentWord._wsd == null)
				print(currentWord._wsd_name + " couldn't get allocated for word: " + currentWord);
//				MyError.error(currentWord._wsd_name + " couldn't get allocated!");			
		}

		//adding verb relation to KB.
		//delayed to preprocessScene after preparing nullSemanticTags
		/*ArrayList<PlausibleStatement> verbRelations = */ //defineVerbRelation(sentence);
						
		Word verb = sentence.getVerb();
		
		if(verb != null){
			
			sentence.make_nested_sentences();
			
			//loading verb SemanticArguments.
			ArrayList<Node> semArgs = loadVerbSemanticCapacities(sentence);
			verb.setSemantic_capacities(semArgs);
			
			//loading verb VisualCapacities.
			ArrayList<Node> visualCaps = loadVerbVisualCapacities(sentence);
			verb.setVisual_capacities(visualCaps);
			
			ArrayList<SentenceModel> nested_sents = sentence.get_nested_sentences();
			
			if(!Common.isEmpty(nested_sents)){
				for(SentenceModel nest:nested_sents){
					Word vb = nest.getVerb();
					
					if(vb != null){
						ArrayList<Node> semArgsNest = loadVerbSemanticCapacities(nest);
						vb.setSemantic_capacities(semArgsNest);
						
						ArrayList<Node> visCapsNest = loadVerbVisualCapacities(nest);
						vb.setVisual_capacities(visCapsNest);
					}
				}
			}		
		}
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
				if(!Common.isEmpty(missingMainArgs))
					if(!scene.equals(primarySceneModel))
						prepareNullSemanticTagsForAScene(sentenceModel, missingMainArgs, scene);
					
		print("=============== end of prepareNullSemanticTags =====================");
	}	
	
	
	private void checkAllSemanticTagsWithUser(SentenceModel sentenceModel, SceneModel primarySceneModel){
		
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
			
			
			Word semArgWord = sentenceModel.getWord(semanticTag);
			
			if(semArgWord == null){				
				MyError.error("the sentenceModel has " + semanticTag + " but it didn't find!" + sentenceModel);
				return null;
			}
			
			//It means that it is verb (infinitive) so the processing must perform on it and its dependents too!
			if(semArgWord.isInfinitive()){
				//TODO: complete this part later!
				print("It is an infinitive so, the process should got performed for it again!");
				
				return null;
			}
			else{
						
				//reasoning ScenePart from KB, ROLE,DYNAMIC_OBJECT, STATIC_OBJECT, ....
				ScenePart scenePart = _ttsEngine.whichScenePart(semArgWord);
								
				if(scenePart == null || scenePart == ScenePart.NO){
					MyError.error("the " + semanticTag +": " + semArgWord + " ScenePart was not found!");
					return null;
				}
			
				SceneElement inputSceneElement = primarySceneModel.createSceneElement(semArgWord, scenePart);
				
				if(inputSceneElement == null){
					MyError.error("the " + semArgWord + " could not convert to a SceneElement!");
					return null;
				}
				
				boolean isRedundantPart = primarySceneModel.hasSceneElement(inputSceneElement);
				
				SceneElement sceneElem = null;
				
				//It means that the primarySceneModel has had this ScenePart before, so we will merge the information of this part with that one
				if(isRedundantPart){
					
					print(inputSceneElement._name + " is redundant!");
					
					sceneElem = primarySceneModel.getSceneElement(inputSceneElement);
					
					if(sceneElem == null){
						MyError.error("primarySceneModel has " + semArgWord + " but it could not be found!");
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
				
				preprocessDependentsOfSemanticArg(semArgWord, sceneElem);
								
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
		
		Word verb = sentenceModel.getVerb();
		
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
		Word locWord = sentenceModel.getWord(SubSemanticTag.LOC);
		if(locWord != null){
			print("SceneModel has " + locWord + " ArgM_LOC");
			
			//It can only return Location SceneElement. 
			SceneElement locElem = preprocessSemanticArg(SubSemanticTag.LOC.convertToSemanticTag(), sentenceModel, primarySceneModel);
			
			if(locElem.scenePart == ScenePart.LOCATION)
				primarySceneModel.setLocation((Location) locElem);
		}
		
		//processing ArgM_TMP, if any.
		Word tmpWord = sentenceModel.getWord(SubSemanticTag.TMP);
		if(tmpWord != null){
			print("SceneModel has " + tmpWord + " ArgM_TMP");

			//It can only return Time SceneElement.
			SceneElement tmpElem = preprocessSemanticArg(SubSemanticTag.TMP.convertToSemanticTag(), sentenceModel, primarySceneModel);
			
			if(tmpElem != null && tmpElem.scenePart == ScenePart.TIME)
				primarySceneModel.setTime((Time) tmpElem);
		}
		
		//processing ArgM_MNR, if any.
		Word mnrWord = sentenceModel.getWord(SubSemanticTag.MNR);
		if(mnrWord != null){
			print("SceneModel has " + mnrWord + " ArgM_MNR");
			
			Word arg0Word = sentenceModel.getArg0Word();
			
			if(arg0Word != null){
				SceneElement arg0Elem = primarySceneModel.getSceneElement(arg0Word._wsd);
				
				if(arg0Elem != null){
					//It can be added as RoleMood or ObjectState to Arg0
					SceneElement mnrElem = arg0Elem.addDependent(mnrWord, "manner", _ttsEngine);
					
					if(mnrElem != null)
						preprocessDependentsOfSemanticArg(mnrWord, mnrElem);
				}
				else
					print("no SceneElement for Arg0 found in primarySceneModel, in order to " + mnrWord + " to be added to it!");
			}
			else
				print("no Arg0Part found in sentnceModel, in order to " + mnrWord + " to be added to it!");			
		}

		//processing ArgM_PRP, if any.
		Word prpWord = sentenceModel.getWord(SubSemanticTag.PRP);
		if(prpWord != null){
			print("SceneModel has " + prpWord + " ArgM_PRP");
			
			//It can return only Scene_goal SceneElement. 
			preprocessSemanticArg(SubSemanticTag.PRP.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
		
		//processing ArgM_GOL, if any.
		Word golWord = sentenceModel.getWord(SubSemanticTag.GOL);
		if(golWord != null){
			print("SceneModel has " + golWord + " ArgM_GOL");
			
			//It can return Role, Dynamic_object, Static_object, or Location SceneElement. 
			preprocessSemanticArg(SubSemanticTag.GOL.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}

		//processing ArgM_CAU, if any.
		Word cauWord = sentenceModel.getWord(SubSemanticTag.CAU);
		if(cauWord != null){
			print("SceneModel has " + cauWord + " ArgM_CAU");
			
			//It can return only Scene_goal SceneElement. 
			preprocessSemanticArg(SubSemanticTag.CAU.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
		
		//processing ArgM_COM, if any.
		Word comWord = sentenceModel.getWord(SubSemanticTag.COM);
		if(comWord != null){
			print("SceneModel has " + comWord + " ArgM_COM");
			
			//It can return only Role or Dynamic_object SceneElement. 
			preprocessSemanticArg(SubSemanticTag.COM.convertToSemanticTag(), sentenceModel, primarySceneModel);
		}
	
		//processing ArgM_INS, if any.
		Word insWord = sentenceModel.getWord(SubSemanticTag.INS);
		if(insWord != null){
			print("SceneModel has " + insWord + " ArgM_INS");
			
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
		
		Word arg3Word = sentenceModel.getArg3Word();
		
		Word arg4Word = sentenceModel.getArg4Word();
		
		if(arg3Word != null && arg3Word._semanticTag != null && arg3Word._semanticTag.isMainSemanticTag()){
		
			MainSemanticTag arg3SemArg = arg3Word._semanticTag.convertToMainSemanticTag();
			
			//Sentence has ARG3_SOURCE_STARTPOINT
			if(arg3SemArg == MainSemanticTag.ARG3_SOURCE_STARTPOINT && arg3SceneElement != null && arg3SceneElement.scenePart == ScenePart.LOCATION){
			
				primarySceneModel.setLocation((Location)arg3SceneElement);
				
				//Sentence has ARG4_ENDPOINT
				if(arg4Word != null && arg4SceneElement != null)			 
					primarySceneModel.addAlternativeLocation((Location)arg4SceneElement);				
				
				//Sentence hasn't ARG4_ENDPOINT
				else{
					
					Word arg_dirWord = sentenceModel.getWord(SubSemanticTag.DIR);
					
					//Sentence has ARG_DIR
					if(arg_dirWord != null){
						
						SceneElement arg_dirSceneElem = preprocessSemanticArg(SubSemanticTag.DIR.convertToSemanticTag(), sentenceModel, primarySceneModel) ;							
						
						if(arg_dirSceneElem == null){
							MyError.error("the " + arg_dirWord + " could not convert to a SceneElement!");
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
		if(arg4Word != null)			
				primarySceneModel.addAlternativeLocation((Location)arg4SceneElement);				
		
		//Sentence hasn't ARG4_ENDPOINT
		else{
			
			Word arg_dirWord = sentenceModel.getWord(SubSemanticTag.DIR);
			
			//Sentence has ARG_DIR
			if(arg_dirWord != null){
				
				SceneElement arg_dirSceneElem = preprocessSemanticArg(SubSemanticTag.DIR.convertToSemanticTag(), sentenceModel, primarySceneModel);
				
				if(arg_dirSceneElem == null){
					MyError.error("the " + arg_dirWord + " could not convert to a SceneElement!");
					return;
				}
				
				primarySceneModel.addAlternativeLocation((Location)arg_dirSceneElem);
			}								
		}
		
		print("=============== end of processLocation =============================");		
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

						//for(SentenceModel sent:allSentences){						
						for(int index = allSentences.size() - 1; index >= 0; index--){
							SentenceModel sent = allSentences.get(index);

							if(sent.equals(sentenceModel))
								continue;
							
//							print("In sentence: " + sent.getOriginalSentence());
							
							if(miss.isArg0() && sent.hasArg0()){							
								Word arg0Word = sent.getArg0Word();
								
								if(arg0Word != null){									
									Word copyArg0Word = arg0Word.makeDeepCopy(sentenceModel, null);  
									copyArg0Word.set_semanticTag(miss.name());
									preparedMainArgs.add(copyArg0Word._semanticTag.convertToMainSemanticTag());							
									sentenceModel.setPrerparedWord(copyArg0Word);						
									
									print("prepared " + copyArg0Word._semanticTag);
									break;
								}
							}
							else if(miss.isArg1() && sent.hasArg1()){
								Word arg1Word = sent.getArg1Word();
								
								if(arg1Word != null){
									Word copyArg1Word = arg1Word.makeDeepCopy(sentenceModel, null);
									copyArg1Word.set_semanticTag(miss.name());
									
									if(!copyArg1Word.isObject()){
										Phrase arg1_ph = arg1Word._phrase;
										if(arg1_ph != null && arg1_ph.get_headWord() != null && arg1_ph.get_headWord().isObject())
											copyArg1Word._syntaxTag = DependencyRelationType.OBJ;										
									}
									
									preparedMainArgs.add(copyArg1Word._semanticTag.convertToMainSemanticTag());
									sentenceModel.setPrerparedWord(copyArg1Word);
									
									print("prepared " + copyArg1Word._semanticTag);
									break;
								}
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
	 * @param semArgWord guaranteed not to be null.
	 * @param sceneElement guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessDependentsOfSemanticArg(Word semArgWord, SceneElement sceneElement){
		
		//It means that this sentencePart has some adjectives
		if(semArgWord.hasAnyAdjectives()){
			
			print(semArgWord + " " + semArgWord._semanticTag + " has some adjectives:" + semArgWord.getAdjectives());
			
			ArrayList<Word> adjectives = semArgWord.getAdjectives();
			
			for(Word adj:adjectives)
				sceneElement.addDependent(adj, "adjective", _ttsEngine);			
		}
		//It means that this sentencePart has some mozad_elaih
		if(semArgWord.hasAnyMozaf_elaihs()){				
			
			print(semArgWord  + " " + semArgWord._semanticTag + " has mozaf_elaih: " + semArgWord.getMozaf_elaih());
			
			ArrayList<Word> mozafs = semArgWord.getMozaf_elaih();
			
			for(Word moz:mozafs)
				
				if(moz._wsd != null && !moz._wsd.getName().contains(zamir_enekasi))
					sceneElement.addDependent(moz, "mozaf_elaih", _ttsEngine);
		}			
	}

	/**
	 * 
	 * @param verb guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessingNamafoliVerb(Word verb, SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		Word arg1Word = sentenceModel.getArg1Word();
		
		if(arg1Word == null){
			print(verb + " is namafoli but ARG1 not found as a mistake!");
			return;
		}		
		
		SceneElement arg1Elem = primarySceneModel.getSceneElement(arg1Word._wsd);
		
		if(arg1Elem == null){
			print(arg1Word + " can not be found in primarySceneModel!");
			return;
		}		
		
		arg1Elem.addDependent(verb, "adjective", _ttsEngine);
		
		//------
		Word arg0Word = sentenceModel.getArg0Word();
		
		if(arg0Word != null){
			
			SceneElement arg0Elem = primarySceneModel.getSceneElement(arg0Word._wsd);
			
			if(arg0Elem == null){
				print(arg0Word + " can not be found in primarySceneModel!");
				return;
			}
			
			arg0Elem.addDependent(verb, "action", _ttsEngine);			
		}			
	}

	/**
	 * 
	 * @param verb guaranteed not to be null.
	 * @param sentenceModel guaranteed not to be null.
	 * @param primarySceneModel guaranteed not to be null.
	 */
	private void preprocessingRabtiVerb(Word verb, SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		Word arg1Word = sentenceModel.getArg1Word();
		
		Word arg2Word = sentenceModel.getArg2Word();
		
		if(arg1Word == null || arg2Word == null){
			print(verb + " is rabti but ARG1 or ARG2 not found as a mistake!");
			return;
		}		
		
//		if(arg2Part.isAdjective()){
		
			SceneElement arg1Elem = primarySceneModel.getSceneElement(arg1Word._wsd);
			
			if(arg1Elem == null){
				print(arg1Word + " can not be found in primarySceneModel!");
				return;
			}		
			
			arg1Elem.addDependent(arg2Word, "adjective", _ttsEngine);			
//		}
//		else
//			print(verb + " is rabti but Arg2Part " + arg2Part + " is not adjective!");
	}

	private void preprocessingActionVerb(SentenceModel sentenceModel, SceneModel primarySceneModel) {
		
		Word verb = sentenceModel.getVerb();
		
		if(verb == null){
			MyError.error("The SentenceModel has no verb as a mistake!");
			return;
		}
		
		Word arg0Word = sentenceModel.getArg0Word();
		
		if(arg0Word == null){
			MyError.error("The " + verb + " is BASIT, but has no Arg0Word as a mistake!");
			return;
		}
		
		SceneElement arg0Elem = primarySceneModel.getSceneElement(arg0Word._wsd);
		
		if(arg0Elem == null){
			print(arg0Word + " can not be found in primarySceneModel!");
			return;
		}
		
		arg0Elem.addDependent(verb, "action", _ttsEngine);	
	}

	/**
	 * This method maps part's wsd parameter to a concept in _kb based on part's wsd_name parameter.
	 * if part's wsd_name is "-" no mapping occurs.
	 * if part's wsd_name has just one part, it is the main concept name, so it must directly maps to a node in _kb.
	 * if part's wsd_name has more than one part which includes one MAIN and probably a PRE or POST, so it must be mapped to a plausible statement in a _kb.
	 * 
	 * @param word the part its wsd parameter to be set.
	 * @param isNewNode is this part a new instance or is is the same as seen before.
	 * @param synTag the SyntaxTag of this node in the sentence.
	 */
	private void allocate_wsd(SentenceModel sentence, Word word, boolean isNewNode){
		if(word == null)
			return;
			
		String wsd_name = word._wsd_name;
		
		if(wsd_name == null || wsd_name.equals("-"))
			return;
	
		//it means that it is not just node but plausible statement for example: پسر#n2_وضعیت سنی#a_خردسال#a1 or 7_وضعیت سلامتی#a_8
		if(wsd_name.indexOf("_") != -1){
			
			String[] word_elements = wsd_name.split("_"); //1_وضعیت سنی#a_خردسال#a1 --> [word_num:1 --> وضعیت سلامتی --> خردسال#a1]
			
			if(word_elements.length != 3){
				MyError.error("wrong wsd_name" + wsd_name);
				return;
			}
			
			Node argument = null;
			Node descriptor = null;
			Node referent = null;			
					
			PlausibleStatement wsd = null;
			
			String relation_name = null;
			
			for(int i = 0; i < 3; i++){
				
				Node cur_word_node = null;
				
				try {
					int word_num = -1;
					
					//if it dosen't throw exception, it means that cur_word is a number
			        word_num = Integer.parseInt(word_elements[i]);
			        
			        Word cur_word = null;
			        
			        //it is a valid word_num. 
			        if(word_num != -1){
			        	cur_word = sentence.getWord(word_num);
			        	cur_word_node = cur_word._wsd;			        	
			        }
			        
			        if(i == 0 || i == 2)
			    		if(cur_word_node != null)
							if(argument == null)
								argument = cur_word_node;								
							else if(referent == null)
								referent = cur_word_node;						
			    	
			        
			    } catch (NumberFormatException e) {
			    	//if it has thrown an exception it means that word_elements[i] is the name of a concept.
			    	//it is argument or referent
			    	if(i == 0 || i == 2){
			    		cur_word_node = _ttsEngine.findorCreateInstance(word_elements[i], isNewNode);
			    		
			    		if(cur_word_node != null){
							
			    			if(argument == null)
								argument = cur_word_node;
							else if(referent == null)
								referent = cur_word_node;
							
						}
			    	}
			    	//it is descriptor
			    	else{
			    		relation_name = word_elements[1];
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
			word.set_wsd(argument);
			
			Word argWord = sentence.getWord(argument);
			
			if(argWord != null)
				add_adjective_mozaf(sentence, argWord, descriptor, referent);
			else
				add_adjective_mozaf(sentence, word, descriptor, referent);
			//return;
		}
		else{
			//it means this part wsd_name is just one concept name, so we find or add it in sceneModel.			
			Node wsd = _ttsEngine.findorCreateInstance(wsd_name, isNewNode);
			word.set_wsd(wsd);
		}		
	}
	
	@SuppressWarnings("unused")
	private void add_adjective_mozaf(SentenceModel sentence, Word mainPart, Node descriptor, Node referent) {
		
		if(sentence == null || mainPart == null || descriptor == null || referent == null){
			MyError.error("null input parameter for add_adjective_mozaf!");
			if(mainPart == null)
				MyError.error("no mainPart could be found!");
			return;
		}
		
		if(descriptor.getPos() == ir.ac.itrc.qqa.semantic.enums.POS.ADJECTIVE){ //it means that descriptor is describing an adjective.

			Word adjWord = new Word(sentence, null, -1, referent.getName(),POS.ADJ, DependencyRelationType.NPOSTMOD, mainPart._number, null, referent);
			
			//1:added, 0:merged, -1:Nop
			int added = mainPart.addAdjective(adjWord);
		}
		else if(descriptor.getPos() == ir.ac.itrc.qqa.semantic.enums.POS.NOUN){ //it means that descriptor is describing a mozaf_alaih.
			
			Word mozWord = new Word(sentence, null, -1, referent.getName(),POS.N, DependencyRelationType.MOZ, mainPart._number, null, referent);
		
			//1:added, 0:merged, -1:Nop
			int added = mainPart.addMozaf_elaih(mozWord);
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
					
		Word verb = sentenceModel.getVerb();
		
		//here _wsd of subject(s), object(s), verb and ... of this sentence has been allocated!
		
		if(verb == null || verb._wsd == null){
			MyError.error("sentence has no verb or its verb: " + verb + " _wsd is null!" + sentenceModel.getNLSentence());
			return verbRelations;
		}
		
		ArrayList<Word> sbj_words = sentenceModel.get_subjects();
		
		if(SentenceModel.is_list_or_wsd_empty(sbj_words)){
		
			ArrayList<Phrase> sbj_phrases = sentenceModel.get_subject_phrases();
			
			if(Common.isEmpty(sbj_phrases)){
				MyError.error("sentence with verb " + verb + " has no subject phrase! " + sentenceModel);
				return verbRelations;	
			}
			
			sbj_words = new ArrayList<Word>();
			
			for(Phrase sbj_ph:sbj_phrases)
				sbj_words.add(sbj_ph.get_headWord());			
		}
		
		if(!SentenceModel.is_list_or_wsd_empty(sbj_words)){
		
			for(Word sbj:sbj_words){
				
				if(sbj == null || sbj._wsd == null){
					MyError.error("evene subject: " + sbj + " or its _wsd is null! ");
					continue;
				}
	
				boolean transitive_verb = false;
				
				ArrayList<Word> obj_words = sentenceModel.get_objects();
				
				if(SentenceModel.is_list_or_wsd_empty(obj_words)){
					
					ArrayList<Phrase> obj_phrases = sentenceModel.get_object_phrases();
					
					if(!Common.isEmpty(obj_phrases)){
						
						obj_words = new ArrayList<Word>();
						
						for(Phrase obj_ph:obj_phrases){
							
							if(obj_ph != null){
								
								Word obj = obj_ph.get_wordWithSyntax(DependencyRelationType.PREDEP);
								
								if(obj == null || obj._wsd == null){
									MyError.error("This " + obj_ph + " pharse has no PREDEP Word or its _wsd is null!");								
									continue;
								}
								else{
									//it is a transitive verb.
									transitive_verb = true;
									obj_words.add(obj);
								}				
							}
						}
					}
				}
				else
					transitive_verb = true;
				
				if(!SentenceModel.is_list_or_wsd_empty(obj_words)){
				
					for(Word obj:obj_words){
						
						if(obj == null || obj._wsd == null){
							MyError.error("evene object: " + obj + " or its _wsd is null! ");
							continue;	
						}
						
						//adding the relation of this sentence to kb.
						PlausibleStatement rel = _kb.addRelation(sbj._wsd, obj._wsd, verb._wsd, SourceType.TTS);
						verbRelations.add(rel);
						print("verbRel added1 ---- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");
						
//						PlausibleStatement ps2 = _kb.addRelation(rel, _kb.addConcept("اکنون§n-12609"), KnowledgeBase.HPR_CXTIME, SourceType.TTS);
//						print("relation added ------------- : " + ps2.argument + " -- " + ps2 + " -- " + ps2.referent);	
					}				
				}
							
				if(!transitive_verb){
					
					ArrayList<Word> mos_words = sentenceModel.get_mosnads();
					
					if(SentenceModel.is_list_or_wsd_empty(mos_words)){
					
						ArrayList<Phrase> mos_phrases = sentenceModel.get_mosnad_phrases();				
						
						if(!Common.isEmpty(mos_phrases)){
							
							mos_words = new ArrayList<Word>();
							
							for(Phrase mos_ph:mos_phrases){
								
								if(mos_ph != null){
									
									Word mos = mos_ph.get_wordWithSyntax(DependencyRelationType.POSDEP);
									
									if(mos == null || mos._wsd == null){									
										MyError.error("This " + mos_ph + " pharse has no POSDEP Word or its _wsd is null!");										
										continue;									
									}									
									else{
										//it is a transitive verb.
										transitive_verb = true;
										mos_words.add(mos);
									}	
								}									
							}
						}
					}
					else
						transitive_verb = true;
					
					if(!SentenceModel.is_list_or_wsd_empty(mos_words)){
						
						for(Word mos:mos_words){
							
							if(mos == null || mos._wsd == null){
								MyError.error("evene mosnad: " + mos + " or its _wsd is null! ");
								continue;	
							}
						
							//adding the relation of this sentence to kb.
							PlausibleStatement rel = _kb.addRelation(sbj._wsd, mos._wsd, verb._wsd, SourceType.TTS);
							verbRelations.add(rel);
							print("verbRel added2 ---- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");						
						}					
					}
				}				
				
				if(!transitive_verb){
					//TODO check if using "KnowledgeBase.HPR_ANY" as referent is correct?!
					//adding the relation of this sentence to kb. 
					PlausibleStatement rel = _kb.addRelation(sbj._wsd, KnowledgeBase.HPR_ANY, verb._wsd, SourceType.TTS);
					verbRelations.add(rel);
					print("verbRel added3 ---- : " + rel.argument.getName() + " --> " + rel.getName() + " --> " + rel.referent.getName() + "\n");				
				}
			}
		}
		
		return verbRelations;
	}
	
	/**
	 * loads verb semantic arguments from Knowledge base. 
	 * @param sentence guaranteed not to be null!
	 * @return
	 */
	private  ArrayList<Node>  loadVerbSemanticCapacities(SentenceModel sentence) {
		Word verb = sentence.getVerb();
		if(verb == null)
			return null;
		
		Node pure_verb = _ttsEngine.getPureNode(verb._wsd);
		
		if(verb == null || verb._wsd == null || pure_verb == null){
			MyError.error("verb or its wsd or its pure version should not be null!" + verb);
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
	
	/**
	 * loads verb visual capacities from Knowledge base.
	 * 
	 * @param sentence guaranteed not to be null! 
	 * @return
	 */
	private ArrayList<Node> loadVerbVisualCapacities(SentenceModel sentence) {
		Word verb = sentence.getVerb();
		
		if(verb == null)
			return null;
		
		Node pure_verb = _ttsEngine.getPureNode(verb._wsd);
		
		if(verb == null || verb._wsd == null || pure_verb == null){
			MyError.error("verb or its wsd or its pure version should not be null!" + verb);
			return null;
		}
		
		Node verb_synSet = pure_verb.getSynSet();
						
		if(verb_synSet == null){
			MyError.error("this verb '" + pure_verb + "' has no SynSet!");
			return null;
		}
		
		Node visualCapNode = _kb.addConcept(visualCapacity_name, false);
		
		ArrayList<PlausibleAnswer> visualCapsAnswers = _ttsEngine.writeAnswersTo(visualCapNode, verb_synSet, null);	
		
		ArrayList<Node> visCaps = new ArrayList<Node>();			
		
		if(!Common.isEmpty(visualCapsAnswers))
			for(PlausibleAnswer cap:visualCapsAnswers)
				if(cap != null && cap.answer != null)
					visCaps.add(cap.answer);
		
		Node default_verb = _kb.addConcept(default_verb_name, false);
		
		if(default_verb == null){
			MyError.error("the dafault verb should not be null!");
			return visCaps;
		}
		
		visualCapsAnswers = null;
		
		visualCapsAnswers = _ttsEngine.writeAnswersTo(visualCapNode, default_verb, null);
		
		if(!Common.isEmpty(visualCapsAnswers))
			for(PlausibleAnswer cap:visualCapsAnswers)
				if(cap != null && cap.answer != null)
					visCaps.add(cap.answer);
		
		return visCaps;
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


	
