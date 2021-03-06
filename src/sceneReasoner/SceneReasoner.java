package sceneReasoner;

import java.util.ArrayList;

import sceneElement.Location;
import sceneElement.Role;
import sceneElement.RoleAction;
import sceneElement.RoleEmotion;
import sceneElement.RoleMood;
import sceneElement.Time;
import model.MainSemanticTag;
import model.SceneModel;
import model.ScenePart;
import model.Word;
import model.StoryModel;
import model.SubSemanticTag;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.Common;
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
	//private SemanticReasoner _re;
	private TTSEngine _ttsEngine = null;
	
	private String general_visual_capacity_emotion = "f˸حس§n-14738";	
	private String general_visual_capacity_state = "f˸وضعیت§n-12745";
	
	public SceneReasoner(KnowledgeBase kb, SemanticReasoner re, TTSEngine ttsEngine){
		this._kb = kb;
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
										
										Word candidWord = lastScene.findWordOfSceneElement(candidLoc);
										
										if(candidWord != null){
										
											//if it is ArgM_DIR, then set its core as Location of currentScene.
											if(candidWord._semanticTag == SubSemanticTag.DIR.convertToSemanticTag()){ 

												ArrayList<Word> mozafs = candidWord.getMozaf_elaih();
												
												Word innerDirection = null;
												
												if(!Common.isEmpty(mozafs))
													 innerDirection = mozafs.get(0);
												
												if(innerDirection != null){
													print("inner direction of " + candidWord +" %%%%%%%%%%%%%%%%%%%%%%%%% " + innerDirection);
													
													ScenePart scenePart = _ttsEngine.whichScenePart(innerDirection);
													
													if(scenePart != null || scenePart != ScenePart.NO){												
														currentScene.setLocation(new Location(currentScene, innerDirection._wordName, innerDirection._wsd));
														print("the lastScene ArgM_Dir innerDirection " + currentScene.getLocation() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
														break;
													}
												}
											}
											//if it is ARG4, then set that as Location of currentScene.
											else if(candidWord._semanticTag == MainSemanticTag.ARG4_ENDPOINT.convertToSemanticTag()){
												ScenePart scenePart = _ttsEngine.whichScenePart(candidWord);
												
												if(scenePart == ScenePart.LOCATION){
													currentScene.setLocation(new Location(currentScene, candidWord._wordName, candidWord._wsd));
													print("the lastScene Arg4 " + currentScene.getLocation() +" %%%%%%%%%%%%%%%%%%%%%%%%% has been set!");
													break;
												}
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

	/**
	 * 
	 * @param storyModel guaranteed not to be null!
	 */
	public void enrichStoryModel(StoryModel storyModel) {
		ArrayList<SceneModel> story_scenes = storyModel.getScenes();
		
		if(!Common.isEmpty(story_scenes))
			for(SceneModel scene:story_scenes)
				if(scene != null)
					enrichSceneModel(storyModel, scene);
	}
	

	/**
	 * 
	 * @param storyModel guaranteed not to be null!
	 * @param sceneModel guaranteed not to be null!
	 */
	private void enrichSceneModel(StoryModel storyModel, SceneModel sceneModel){

		//-------------- phase 1 of enrichment: enriching Roles: --------------
		ArrayList<Role> scene_roles = sceneModel.getRoles();
		if(!Common.isEmpty(scene_roles))
			for(Role role:scene_roles)
				if(role != null)
					enrichRole(storyModel, sceneModel, role);
				
		//-------------- phase 2 of enrichment: enriching DynamicObjects: -----
		
		//-------------- phase 3 of enrichment: enriching StaticObejcts: ------
		
		//-------------- phase 4 of enrichment: enriching Location: -----------
		
		//-------------- phase 5 of enrichment: enriching Time: ---------------
		
		//-------------- phase 6 of enrichment: enriching Roles: --------------
		
		//-------------- phase 7 of enrichment: enriching SceneEmotions: ------
		
		//-------------- phase 8 of enrichment: enriching SceneGoals: ---------				

	}

	/**
	 * @param sceneModel guaranteed not to be null!
	 * @param role guaranteed not to be null!
	 */
	private void inferEmotion(SceneModel sceneModel, Role role){
				
		print("\n==================== infer emotion ==================");
		
		Node emotion = _kb.addConcept(general_visual_capacity_emotion, false);
		
		//-------------inferring emotion --------------------------------
		
		//TODO: comment for speeding up!!!
		// generate a query for emotion visual capacity of this Role to enrich it. 
		ArrayList<PlausibleAnswer> answers = _ttsEngine.inferRuleFromKB(emotion, role._node, null);
//		ArrayList<PlausibleAnswer> answers = new ArrayList<PlausibleAnswer>();
		
		print("Answers: " + answers.size());
	
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)			
				System.out.println("-------" + ++countJustification + "--------\n" + justification);
										
		}
		
		count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			ArrayList<RoleAction> role_actions = role.getRole_actions();
			
			if(!Common.isEmpty(role_actions)){
				
				for(RoleAction action:role_actions){
					
					Node action_node = action._node;
					
					boolean wasEffective = false;
					
					int countJustification = 0;
					for (String justification: justifications)
					{
						//It means that this action was effective in inferring this answer.
						if(justification.contains(action_node.getName())){
							
							System.out.println("\n" + action_node.getName() + "------- was effective in infering:" + answer.toString() + " justification: " + countJustification);
							
							RoleEmotion rolEmo = new RoleEmotion(sceneModel, answer.answer.getName(), answer.answer);
							
							role.addRole_emotion(rolEmo);
							
							action.setEmotion_in_action(rolEmo);
							
							wasEffective = true;
							
							break;
						}						
					}
					//It means that this action was not effective in inferring this answer.
					if(!wasEffective)
						System.out.println("\n" + action_node.getName() + "------- was notttttt effective in  infering:" + answer.toString() + " justification: " + countJustification);
				}
			}
		}
				
		print("\n==================== end of infer emotion ===========");
	}

	
	/**
	 * @param sceneModel guaranteed not to be null!
	 * @param role guaranteed not to be null!
	 */
	private void inferState(SceneModel sceneModel, Role role){
				
		print("\n==================== infer state ====================");
		
		Node state = _kb.addConcept(general_visual_capacity_state, false);
		
		//-------------inferring state --------------------------------
		
		//TODO: comment for speeding up!!!
		// generate a query for state visual capacity of this Role to enrich it. 
		ArrayList<PlausibleAnswer> answers = _ttsEngine.inferRuleFromKB(state, role._node, null);
//		ArrayList<PlausibleAnswer> answers = new ArrayList<PlausibleAnswer>();
		
		print("Answers: " + answers.size());
	
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)			
				System.out.println("-------" + ++countJustification + "--------\n" + justification);
										
		}
		
		count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			ArrayList<RoleAction> role_actions = role.getRole_actions();
			
			if(!Common.isEmpty(role_actions)){
				
				for(RoleAction action:role_actions){
					
					Node action_node = action._node;
					
					boolean wasEffective = false;
					
					int countJustification = 0;
					for (String justification: justifications)
					{
						//It means that this action was effective in inferring this answer.
						if(justification.contains(action_node.getName())){
							
							System.out.println("\n" + action_node.getName() + "------- was effective in infering:" + answer.toString() + " justification: " + countJustification);
							
							RoleMood rolstat = new RoleMood(sceneModel, answer.answer.getName(), answer.answer);
							
							role.addRole_mood(rolstat);
							
							action.setMood_in_action(rolstat);
							
							wasEffective = true;
							
							break;
						}						
					}
					//It means that this action was not effective in inferring this answer.
					if(!wasEffective)
						System.out.println("\n" + action_node.getName() + "------- was notttttt effective in infering:" + answer.toString() + " justification: " + countJustification);
				}
			}
		}
		print("\n==================== end of infer state =============");
	}

	/**
	 * 
	 * @param storyModel guaranteed not to be null!
	 * @param sceneModel guaranteed not to be null!
	 * @param role guaranteed not to be null!
	 */	
	private void enrichRole(StoryModel stroyModel, SceneModel sceneModel, Role role){
		
		print("\n#################### enrich Role ####################");
		
		inferEmotion(sceneModel, role);
	
		inferState(sceneModel, role);
		
		//-------------enriching visual capacities ----------------------
			
		ArrayList<RoleAction> role_actions = role.getRole_actions();
		
		if(!Common.isEmpty(role_actions)){
			
			for(RoleAction action:role_actions){	
				
				Node role_node = role._node;		
				Node action_node = action._node;				
				
				if(role_node == null || action_node == null){
					MyError.error("Node of Role '"+ role +"' nor RoleAction '" + action + "' should not be null!");
					continue;
				}
				
				Word action_word = sceneModel.getWord(action_node);
				
				if(action_word != null){
					
					ArrayList<Node> visual_capacities = action_word.getVisual_capacities();
					
					if(!Common.isEmpty(visual_capacities)){
									
						print(action_word + " visual capacities are: " + visual_capacities);
						
						for(Node capacity:visual_capacities){
							
							// generate a query for each of this Role's actions to enrich it.
							//TODO: create the correct query: (capacity, action, null) ???!!!
//							ArrayList<PlausibleAnswer> answers = _ttsEngine.inferRuleFromKB(capacity, action_node, null);
							ArrayList<PlausibleAnswer> answers = new ArrayList<PlausibleAnswer>();
							print("Answers: " + answers.size());
						
							int count = 0;
							for (PlausibleAnswer answer: answers)
							{
								System.out.println("\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
								
								ArrayList<String> justifications = answer.GetTechnicalJustifications();
								
								int countJustification = 0;
								for (String justification: justifications)			
									System.out.println("-------" + ++countJustification + "--------\n" + justification);
															
							}
				 
						}				
					}
				}	
			}
		}
		print("\n#################### end of enrich Role ############# ");
	}
}
