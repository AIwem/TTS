package sceneReasoner;

import java.util.ArrayList;

import sceneElement.DynamicObject;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.MyError;

/**
 * SceneReasoner is a reasoning engine which reasons information of scenes based on 
 * input description (primary SceneModel) of that scene. It produces a rich scene model
 * as output which contains enough information to produce animation of the scene.
 *   
 * @author hashemi
 *
 */
public class SceneReasoner {
	
	private KnowledgeBase _kb;	
	private SemanticReasoner _re;
	//private TTSEngine _ttsEngine = null;
		
	
	public SceneReasoner(KnowledgeBase kb, SemanticReasoner re, TTSEngine ttsEngine){
		this._kb = kb;
		this._re = re;
		//this._ttsEngine = ttsEngine;
	}
	
	
	public void print(String toPrint){
		System.out.println(toPrint);
	}
	
	public void printPlausibleAnswers(ArrayList<PlausibleAnswer> pas){
		if (pas.size() > 0)
		{
			PlausibleQuestion pq = pas.get(0).question;
			
			if (pq != null)
			{
				print(pq.toString());
				print("");
				
				print("---------->>>>>>>>>>>> lastPlausibleQuestion = " + pq.toString());
			}			
			
		}
		
		int i = 0;
		for (PlausibleAnswer pa: pas)
		{
			i++;
			
			print("");
			print("-------------------------------");
			print("");
			
			print(i + ": " + pa.toString());
			print(pa.certaintyToString());
			
	//		print("---------->>>>>>>>>>>> lastPlausibleQuestion = " + pa.question.toString());
		}
		
		print("");
		print("~~~~~~~~~~~~~~~~~~~~~~~~~~");
		print("");
		
		print("تعداد کل استنباط های انجام شده: " + _re.totalCalls);
		print("زمان کل استدلال: " + _re.reasoningTime/1000 + " ثانیه");
	}


	
	public ArrayList<PlausibleAnswer> testSemanticReasoner(){
		
		PlausibleQuestion pq = new PlausibleQuestion();
		
		pq.argument = _kb.findConcept("انسان");		
		pq.descriptor = _kb.findConcept("ISA");
		pq.referent = _kb.findConcept("");
		
		ArrayList<PlausibleAnswer> pas = _re.answerQuestion(pq);
		return pas;
		
	}
	
	/**
	 * this methods merges the sentences, roles, dynamic_objects, static_objects, scene_goals, and scene_emotions of
	 * SceneModel of each sentences with each-other.  
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged.
	 * @return
	 */
	public SceneModel mergeScenesOfSentences(ArrayList<SceneModel> sentencesPrimaryScenes) {
		if(sentencesPrimaryScenes == null || sentencesPrimaryScenes.size() == 0)
			return null;
		SceneModel merged_primary_scene = new SceneModel();
		
		mergeSentences(sentencesPrimaryScenes, merged_primary_scene);
				
		mergeRoles(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeDynamicObjects(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeLocations(sentencesPrimaryScenes, merged_primary_scene);
		
			
			
			//------------
//			this.sentences = new ArrayList<SentenceModel>();		
//			this.roles = new ArrayList<Role>();
//			this.static_objs = new ArrayList<StaticObject>();
//			this.dynamic_objs = new ArrayList<DynamicObject>();
//			this.scene_goals = new ArrayList<SceneGoal>();
//			this.scene_emotions = new ArrayList<SceneEmotion>();
		
			//------------
			
		
		return merged_primary_scene;
	}
	
	/**
	 * this methods merges the Sentences of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged roles. granted not be null!
	 */
	private void mergeSentences(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_sentence:sentencesPrimaryScenes){
			
			ArrayList<SentenceModel> current_sentences = current_sentence.getSentences();
			
			for(SentenceModel sentence:current_sentences)
				
				if(!merged_primary_scene.hasSentence(sentence))
					merged_primary_scene.addSentence(sentence);
		}		
	}
	
	/**
	 * this methods merges the roles of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged roles. granted not be null!
	 */
	private void mergeRoles(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			
			ArrayList<Role> current_roles = current_scene.getRoles();
			
			for(Role role:current_roles)
				
				if(!merged_primary_scene.hasRole(role.getNode()))
					merged_primary_scene.addRole(role);
		}		
	}
	
	/**
	 * this methods merges the DynamicObjects of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged DynamicObjects. granted not be null!
	 */
	private void mergeDynamicObjects(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			ArrayList<DynamicObject> current_dynamicObjects = current_scene.getDynamic_objects();
			
			for(DynamicObject dynObj:current_dynamicObjects)
				
				if(!merged_primary_scene.hasDynamic_object(dynObj.getNode()))
					merged_primary_scene.addDynamic_object(dynObj);
		}
	}
	
	/**
	 * this methods merges the Location of sentencesPrimaryScenes with each-other.
	 * TODO: design a better policy!
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged Location. granted not be null!
	 */
	private void mergeLocations(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			Location current_location = current_scene.getLocation();
				
			if(merged_primary_scene.getLocation() != null)
				MyError.error("this SceneModel previouly has a location " + merged_primary_scene.getLocation());
			merged_primary_scene.setLocation(current_location);
		}
	}
	
//	public void setLocation(Location location) {
//		if(this.location != null)
//			
//		this.location = location;
//	}
	
	public void mergeScenes(StoryModel storyModel) {		
		
	}

	public void enrichSceneModel(StoryModel storyModel) {
	
		
	}
}
