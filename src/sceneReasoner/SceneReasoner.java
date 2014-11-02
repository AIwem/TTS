package sceneReasoner;

import java.util.ArrayList;

import sceneElement.DynamicObject;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import sceneElement.Time;
import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
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
	
	//private KnowledgeBase _kb;	
	//private SemanticReasoner _re;
	private TTSEngine _ttsEngine = null;
		
	
	public SceneReasoner(KnowledgeBase kb, SemanticReasoner re, TTSEngine ttsEngine){
		//this._kb = kb;
		//this._re = re;
		this._ttsEngine = ttsEngine;
	}
	
	
	public void print(String toPrint){
		System.out.println(toPrint);
	}
	
	
	/**
	 * this methods merges the sentences, roles, dynamic_objects, static_objects, scene_goals, and scene_emotions, locations and times of
	 * SceneModel of each sentences with each-other.  
	 * 
	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged.
	 * @return the merged sceneModel which all parameter are set except storyModel parameter.
	 */
	public SceneModel mergeScenesOfSentences(ArrayList<SceneModel> sentencesPrimaryScenes) {
		if(sentencesPrimaryScenes == null || sentencesPrimaryScenes.size() == 0)
			return null;
		
		SceneModel merged_primary_scene = new SceneModel();
		
		mergeSentences(sentencesPrimaryScenes, merged_primary_scene);
				
		mergeRoles(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeDynamicObjects(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeStaticObjects(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeSceneGoals(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeSceneEmotions(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeLocations(sentencesPrimaryScenes, merged_primary_scene);
		
		mergeTimes(sentencesPrimaryScenes, merged_primary_scene);
		
		return merged_primary_scene;
	}
	
	/**
	 * this methods merges the Sentences of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their Sentences are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged sentences. granted not be null!
	 */
	private void mergeSentences(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			
			ArrayList<SentenceModel> current_sentences = current_scene.getSentences();
			
			for(SentenceModel sentence:current_sentences)
				
				if(!merged_primary_scene.hasSentence(sentence)){
					merged_primary_scene.addSentence(sentence);
					sentence.setScene(merged_primary_scene);
				}
		}		
	}
	
	/**
	 * this methods merges the roles of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their Roles are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged roles. granted not be null!
	 */
	private void mergeRoles(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			
			ArrayList<Role> current_roles = current_scene.getRoles();
			
			for(Role role:current_roles)
				
				if(!merged_primary_scene.hasRole(role.getNode()))
					merged_primary_scene.addRole(role);
			
				else{//it has this role before
					Role beforeRole = merged_primary_scene.getRole(role.getNode());
					beforeRole.mergeWith(role);
				}
		}		
	}
	
	/**
	 * this methods merges the DynamicObjects of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their DynamicObjects are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged DynamicObjects. granted not be null!
	 */
	private void mergeDynamicObjects(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			ArrayList<DynamicObject> current_dynamicObjects = current_scene.getDynamic_objects();
			
			for(DynamicObject dynObj:current_dynamicObjects)
				
				if(!merged_primary_scene.hasDynamic_object(dynObj.getNode()))
					merged_primary_scene.addDynamic_object(dynObj);
			
				else{//it has this DynamicObject before
					DynamicObject beforeDynObj = merged_primary_scene.getDynamic_object(dynObj.getNode());
					beforeDynObj.mergeWith(dynObj);
				}
		}
	}
	
	/**
	 * this methods merges the StaticObjects of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their StaticObejcts are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged StaticObjects. granted not be null!
	 */
	private void mergeStaticObjects(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
		
		for(SceneModel current_scene:sentencesPrimaryScenes){
			ArrayList<StaticObject> current_staticObjects = current_scene.getStatic_objects();
			
			for(StaticObject staObj:current_staticObjects)
				
				if(!merged_primary_scene.hasStatic_object(staObj.getNode()))
					merged_primary_scene.addStatic_object(staObj);
			
				else{//it has this StaticObject before
					StaticObject beforeStaObj = merged_primary_scene.getStatic_object(staObj.getNode());
					beforeStaObj.mergeWith(staObj);
				}
		}
	}
	
	/**
	 * this methods merges the Location of sentencesPrimaryScenes with each-other.
	 * TODO: design a better policy!
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their Location are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged Location. granted not be null!
	 */
	private void mergeLocations(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			Location current_location = current_scene.getLocation();
			
			if(current_location != null){
				
				//it means that there are more than one Location for this scene!
				if(merged_primary_scene.getLocation() != null){
					MyError.error("this SceneModel previouly has a location " + merged_primary_scene.getLocation());
					//TODO: the logic of merging location
					print("first scene location: " + merged_primary_scene.getLocation());
					
					print("mozaf: " + merged_primary_scene.getLocation()._node.getMozaf(_ttsEngine.mozaf));
										
					print("current location: " + current_location);
				}
				merged_primary_scene.setLocation(current_location);
			}
		}
	}
	
	/**
	 * this methods merges the Time of sentencesPrimaryScenes with each-other.
	 * TODO: design a better policy!
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their Time are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged Time. granted not be null!
	 */
	private void mergeTimes(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
	
		for(SceneModel current_scene:sentencesPrimaryScenes){
			Time current_time = current_scene.getTime();
				
			if(current_time != null){
				
				if(merged_primary_scene.getTime() != null)
					MyError.error("this SceneModel previouly has a time " + merged_primary_scene.getTime());
				merged_primary_scene.setTime(current_time);
			}
		}
	}
	
	/**
	 * this methods merges the SceneEmotions of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their SceneEmotions are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged SceneEmotions. granted not be null!
	 */
	private void mergeSceneEmotions(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
		
		for(SceneModel current_scene:sentencesPrimaryScenes){
			ArrayList<SceneEmotion> current_scene_emotions = current_scene.getScene_emotions();
			
			for(SceneEmotion sceEmo:current_scene_emotions)
				
				if(!merged_primary_scene.hasScene_emotion(sceEmo.getNode()))
					merged_primary_scene.addScene_emotion(sceEmo);
			
				else{//it has this SceneEmotion before
					SceneEmotion beforeSceEmo = merged_primary_scene.getScene_emotion(sceEmo.getNode());
					beforeSceEmo.mergeWith(sceEmo);
				}
		}
		
	}
	
	
	/**
	 * this methods merges the SceneGoals of sentencesPrimaryScenes with each-other.
	 *  
	 * @param sentencesPrimaryScenes SceneModels of sentences which their SceneGoals are to be merged. granted not be null!
	 * @param merged_primary_scene SceneModel with merged SceneGoals. granted not be null!
	 */
	private void mergeSceneGoals(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
		
		for(SceneModel current_scene:sentencesPrimaryScenes){
			ArrayList<SceneGoal> current_scene_goals = current_scene.getScene_goals();
			
			for(SceneGoal sceGoal:current_scene_goals)
				
				if(!merged_primary_scene.hasScene_goal(sceGoal.getNode()))
					merged_primary_scene.addScene_goal(sceGoal);
			
				else{//it has this SceneGoal before
					SceneGoal beforeSceGoal = merged_primary_scene.getScene_goal(sceGoal.getNode());
					beforeSceGoal.mergeWith(sceGoal);
				}
		}
		
	}

		
	public void mergeScenes(StoryModel storyModel) {		
		
	}

	public void enrichSceneModel(StoryModel storyModel) {
	
		
	}
	
}
