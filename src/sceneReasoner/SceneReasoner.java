package sceneReasoner;

import java.util.ArrayList;

import sceneElement.Location;
import sceneElement.Time;
import model.MainSemanticTag;
import model.SceneModel;
import model.ScenePart;
import model.Word_old;
import model.StoryModel;
import model.SubSemanticTag;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.Common;


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
	
	
//	/**
//	 * this methods merges the sentences, roles, dynamic_objects, static_objects, scene_goals, and scene_emotions, locations and times of
//	 * SceneModel of each sentences with each-other.  
//	 * 
//	 * @param sentencesPrimaryScenes SceneModels of sentences which are to be merged.
//	 * @return the merged sceneModel which all parameter are set except storyModel parameter.
//	 */
//	public SceneModel mergeScenesOfSentences(ArrayList<SceneModel> sentencesPrimaryScenes) {
//		if(sentencesPrimaryScenes == null || sentencesPrimaryScenes.size() == 0)
//			return null;
//		
//		SceneModel merged_primary_scene = new SceneModel();
//		
//		mergeSentences(sentencesPrimaryScenes, merged_primary_scene);
//				
//		mergeRoles(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeDynamicObjects(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeStaticObjects(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeSceneGoals(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeSceneEmotions(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeLocations(sentencesPrimaryScenes, merged_primary_scene);
//		
//		mergeTimes(sentencesPrimaryScenes, merged_primary_scene);
//		
//		return merged_primary_scene;
//	}
//	
//	/**
//	 * this methods merges the Sentences of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their Sentences are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged sentences. granted not be null!
//	 */
//	private void mergeSentences(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
//	
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			
//			ArrayList<SentenceModel> current_sentences = current_scene.getSentences();
//			
//			for(SentenceModel sentence:current_sentences)
//				
//				if(!merged_primary_scene.hasSentence(sentence)){
//					merged_primary_scene.addSentence(sentence);
//					sentence.setScene(merged_primary_scene);
//				}
//		}		
//	}
//	
//	/**
//	 * this methods merges the roles of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their Roles are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged roles. granted not be null!
//	 */
//	private void mergeRoles(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
//	
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			
//			ArrayList<Role> current_roles = current_scene.getRoles();
//			
//			for(Role role:current_roles)
//				
//				if(!merged_primary_scene.hasRole(role.getNode()))
//					merged_primary_scene.addRole(role);
//			
//				else{//it has this role before
//					Role beforeRole = merged_primary_scene.getRole(role.getNode());
//					beforeRole.mergeWith(role);
//				}
//		}		
//	}
//	
//	/**
//	 * this methods merges the DynamicObjects of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their DynamicObjects are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged DynamicObjects. granted not be null!
//	 */
//	private void mergeDynamicObjects(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
//	
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			ArrayList<DynamicObject> current_dynamicObjects = current_scene.getDynamic_objects();
//			
//			for(DynamicObject dynObj:current_dynamicObjects)
//				
//				if(!merged_primary_scene.hasDynamic_object(dynObj.getNode()))
//					merged_primary_scene.addDynamic_object(dynObj);
//			
//				else{//it has this DynamicObject before
//					DynamicObject beforeDynObj = merged_primary_scene.getDynamic_object(dynObj.getNode());
//					beforeDynObj.mergeWith(dynObj);
//				}
//		}
//	}
//	
//	/**
//	 * this methods merges the StaticObjects of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their StaticObejcts are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged StaticObjects. granted not be null!
//	 */
//	private void mergeStaticObjects(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
//		
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			ArrayList<StaticObject> current_staticObjects = current_scene.getStatic_objects();
//			
//			for(StaticObject staObj:current_staticObjects)
//				
//				if(!merged_primary_scene.hasStatic_object(staObj.getNode()))
//					merged_primary_scene.addStatic_object(staObj);
//			
//				else{//it has this StaticObject before
//					StaticObject beforeStaObj = merged_primary_scene.getStatic_object(staObj.getNode());
//					beforeStaObj.mergeWith(staObj);
//				}
//		}
//	}
//	
//	/**
//	 * this methods merges the Location of sentencesPrimaryScenes with each-other.
//	 * TODO: design a better policy!
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their Location are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged Location. granted not be null!
//	 */
//	private void mergeLocations(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
//		
//		ArrayList<Location> scene_locations = new ArrayList<Location>();
//		
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			
//			Location current_location = current_scene.getLocation();
//			
//			if(current_location != null){				
//				
//				scene_locations.add(current_location);
//				
//				//temporarily set the first Location for merged_scene!
//				if(merged_primary_scene.getLocation() == null)
//					merged_primary_scene.setLocation(current_location);
//			}
//		}
//
//		//it means that there are more than one Location for this scene!
//		if(scene_locations.size() > 1){
//			MyError.error("this SceneModel has more than one Location");
//			print("scene_locations: " + scene_locations + "\n");				
//			
//			for(Location loc:scene_locations){
//				ArrayList<PlausibleStatement> loc_mozafs = loc._node.getMozaf(_ttsEngine.mozaf_root);
//				print(loc + " mozafs are= " + loc_mozafs);
//			}
//		}
//	}
//	
//	/**
//	 * this methods merges the Time of sentencesPrimaryScenes with each-other.
//	 * TODO: design a better policy!
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their Time are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged Time. granted not be null!
//	 */
//	private void mergeTimes(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene){
//		
//		ArrayList<Time> scene_times = new ArrayList<Time>();
//	
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			
//			Time current_time = current_scene.getTime();
//				
//			if(current_time != null){
//				
//				scene_times.add(current_time);
//
//				//temporarily set the first Time for merged_scene!
//				if(merged_primary_scene.getTime() == null)
//					merged_primary_scene.setTime(current_time);
//				
//			}
//		}
//		
//		//it means that there are more than one Location for this scene!			
//		if(scene_times.size() > 1){
//			MyError.error("this SceneModel has more than one Time");
//			print("scene_times: " + scene_times + "\n");
//			
//			for(Time ti:scene_times){
//				ArrayList<PlausibleStatement> time_mozafs = ti._node.getMozaf(_ttsEngine.mozaf_root);
//				print(ti + " mozafs are= " + time_mozafs);
//			}
//			
//		}
//		
//	}
//	
//	/**
//	 * this methods merges the SceneEmotions of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their SceneEmotions are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged SceneEmotions. granted not be null!
//	 */
//	private void mergeSceneEmotions(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
//		
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			ArrayList<SceneEmotion> current_scene_emotions = current_scene.getScene_emotions();
//			
//			for(SceneEmotion sceEmo:current_scene_emotions)
//				
//				if(!merged_primary_scene.hasScene_emotion(sceEmo.getNode()))
//					merged_primary_scene.addScene_emotion(sceEmo);
//			
//				else{//it has this SceneEmotion before
//					SceneEmotion beforeSceEmo = merged_primary_scene.getScene_emotion(sceEmo.getNode());
//					beforeSceEmo.mergeWith(sceEmo);
//				}
//		}
//		
//	}
//	
//	
//	/**
//	 * this methods merges the SceneGoals of sentencesPrimaryScenes with each-other.
//	 *  
//	 * @param sentencesPrimaryScenes SceneModels of sentences which their SceneGoals are to be merged. granted not be null!
//	 * @param merged_primary_scene SceneModel with merged SceneGoals. granted not be null!
//	 */
//	private void mergeSceneGoals(ArrayList<SceneModel> sentencesPrimaryScenes, SceneModel merged_primary_scene) {
//		
//		for(SceneModel current_scene:sentencesPrimaryScenes){
//			ArrayList<SceneGoal> current_scene_goals = current_scene.getScene_goals();
//			
//			for(SceneGoal sceGoal:current_scene_goals)
//				
//				if(!merged_primary_scene.hasScene_goal(sceGoal.getNode()))
//					merged_primary_scene.addScene_goal(sceGoal);
//			
//				else{//it has this SceneGoal before
//					SceneGoal beforeSceGoal = merged_primary_scene.getScene_goal(sceGoal.getNode());
//					beforeSceGoal.mergeWith(sceGoal);
//				}
//		}
//		
//	}
//
//		
//	public void mergeSceneElements(StoryModel storyModel) {
//		if(storyModel == null){
//			MyError.error("null storyModel for merging scenes!");
//			return;
//		}
//		
//		ArrayList<SceneModel> storyScenes = storyModel.getScenes();
//		int scene_num = -1;
//		if(storyScenes != null)
//			scene_num = storyScenes.size();
//		
//		for(int i = 0; i < scene_num; i++){
//			for(int j = 0; j < scene_num; j++){
//				if(j == i)
//					continue;
//				SceneModel merger = storyScenes.get(i);
//				SceneModel mergee = storyScenes.get(j);
//				
//				if(mergee != null && merger != null)
//					merger.mergeElementWith(mergee);
//			}
//		}
//			
//		
//	}

	/**
	 * This method postprocesses the Location of every SceneModels of stroyModel based on 
	 * location and alternativeLocations of them.
	 * 
	 * @param storyModel guaranteed not to be null.
	 */
	public void postprocessLocation(StoryModel storyModel) {
				
		print("=============== in postprocessLocation =============================");
		
		ArrayList<SceneModel> storyScenes = storyModel.getScenes();
		
		int scene_num = -1;
		if(storyScenes != null)
			scene_num = storyScenes.size();
		
		for(int i = 0; i < scene_num; i++){
			
			SceneModel currentScene = storyScenes.get(i);
			
			if(currentScene != null)
				if(currentScene.getLocation() == null){
					
					ArrayList<Location> altLocs = currentScene.getAlternativeLocations();
					
					if(!Common.isEmpty(altLocs)){
						if(altLocs.size() == 1)
							currentScene.setLocation(altLocs.get(0));
						else//TODO: design better logic for setLocation from alternativeLocations.
							currentScene.setLocation(altLocs.get(0));
					}
					else if(i >= 1){
						SceneModel lastScene = storyScenes.get(i-1);
						
						if(lastScene != null && lastScene.getAlternativeLocations() != null){
							
							ArrayList<Location> lastScene_altLocs = lastScene.getAlternativeLocations();
							
							if(!Common.isEmpty(lastScene_altLocs)){								
								
								for(int index = lastScene_altLocs.size() - 1; index >= 0; index--){
								
									Location candidLoc = lastScene_altLocs.get(index);
								
									if(candidLoc != null){							
										
										Word_old candidPart = lastScene.findSentencePartofSceneElement(candidLoc);
										
										//if it is ArgM_DIR, then set its core as Location of currentScene.
										if(candidPart._semanticTag == SubSemanticTag.DIR.convertToSemanticTag()){ 
											
											Word_old innerPart = candidPart.getInnerPart();
											
											if(innerPart != null){
												print("inner part of " + candidPart +" %%%%%%%%%%%%%%%%%%%%%%%%% " + innerPart);
												
												ScenePart scenePart = _ttsEngine.whichScenePart(innerPart);
												
												if(scenePart != null || scenePart != ScenePart.UNKNOWN){												
													currentScene.setLocation(new Location(innerPart._word, innerPart._wsd));
													print("the lastScene AergM_Dir innerPart" + currentScene.getLocation() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
													break;
												}
											}
										}
										//if it is ARG4, then set that as Location of currentScene.
										else if(candidPart._semanticTag == MainSemanticTag.ARG4_ENDPOINT.convertToSemanticTag()){
											ScenePart scenePart = _ttsEngine.whichScenePart(candidPart);
											
											if(scenePart == ScenePart.LOCATION){
												currentScene.setLocation(new Location(candidPart._word, candidPart._wsd));
												print("the lastScene Arg4 " + currentScene.getLocation() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
												break;
											}
										}
									}
								}
								//set the last Location of lastScene_altLocs for current_scene anyway!!!
								if(currentScene.getLocation() == null){									
									currentScene.setLocation(lastScene_altLocs.get(lastScene_altLocs.size() - 1));
									print("the last of lastScene_altLocs " + currentScene.getLocation() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
								}
							}
							
						}
					}
				}			
		}		
				
		print("=============== end of postprocessLocation =========================");
	}
	

	/**
	 * This method postprocesses the Time of every SceneModels of stroyModel based on 
	 * time and alternativeTimes of them.
	 * 
	 * @param storyModel guaranteed not to be null.
	 */
	public void postprocessTime(StoryModel storyModel) {
				
		print("=============== in postprocessTime =================================");
		
		ArrayList<SceneModel> storyScenes = storyModel.getScenes();
		
		int scene_num = -1;
		if(storyScenes != null)
			scene_num = storyScenes.size();
		
		for(int i = 0; i < scene_num; i++){
			
			SceneModel currentScene = storyScenes.get(i);			
			
			if(currentScene != null){
				
				Time currentTime = currentScene.getTime();
				
				ArrayList<Time> altTimes = currentScene.getAlternativeTimes();
				
				boolean flag = false;
				
				if((currentTime != null && !isValidSceneTime(currentTime)) || currentTime == null){					
					
					if(!Common.isEmpty(altTimes)){
						for(Time t:altTimes){							
							if(t != currentTime){
								if(isValidSceneTime(t)){
									currentScene.setTime(t);
									print("altTime " + t +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
									flag = true;
									break;
								}
							}
						}						
					}
					// altTime is empty or all of it has been fori or sepas!
					if(!flag && i >= 1){
						
						SceneModel lastScene = storyScenes.get(i-1);
						
						if(lastScene != null && lastScene.getAlternativeTimes() != null){
							
							ArrayList<Time> lastScene_altTimes = lastScene.getAlternativeTimes();
							
							if(!Common.isEmpty(lastScene_altTimes)){
								
								for(int index = lastScene_altTimes.size() - 1; index >= 0; index--){
									
									Time beforeTime = lastScene_altTimes.get(index);
									
									if(isValidSceneTime(beforeTime)){
										currentScene.setTime(beforeTime);
										print("lastScene altTime " + beforeTime +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
										flag = true;
										break;
									}									
								}
							}
							// lastScene_altTimes is empty or all of it has been fori or sepas!
							if(!flag){								
								//set the Time of the lastScene for current_scene anyway!!!
								if(lastScene.getTime() != null){
									currentScene.setTime(lastScene.getTime());
									print("lastScene Time " + lastScene.getTime() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
								}
								//set the last time of altTimes for current_scene anyway!!!
								else if(!Common.isEmpty(altTimes)){ 										
									currentScene.setTime(altTimes.get(altTimes.size() - 1));
									print("the last of altTimes " + currentScene.getTime() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
								}
								//set the last time of lastScene_altTimes for current_scene anyway!!!
								else if(!Common.isEmpty(lastScene_altTimes)){ 									
									currentScene.setTime(lastScene_altTimes.get(lastScene_altTimes.size() - 1));
									print("the last of lastScene_altTimes " + currentScene.getTime() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
								}
							}													
						}
					}
				}
			}
		}		
				
		print("=============== end of postprocessTime =============================");
	}

	private boolean isValidSceneTime(Time time) {
		if(time == null)
			return false;
		
		ArrayList<Node> invalidSceneTimes = new ArrayList<Node>();
		invalidSceneTimes.add(_ttsEngine._TTSKb.addConcept("سپس§r-21500", false));
		invalidSceneTimes.add(_ttsEngine._TTSKb.addConcept("فوری§r-15249", false));
		
		for(Node invalid:invalidSceneTimes){
		
			ArrayList<PlausibleAnswer> answers = _ttsEngine.writeAnswersTo(KnowledgeBase.HPR_ISA, time._node, invalid);
			
			for(PlausibleAnswer ans:answers)
				if(ans.answer == KnowledgeBase.HPR_YES)
					return false;
		}
		return true;
	}


	public void enrichSceneModel(StoryModel storyModel) {
	
		
	}

}
