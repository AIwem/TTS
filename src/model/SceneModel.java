package model;


import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import sceneElement.DynamicObject;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.SceneElement;
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import sceneElement.Time;

/**
 * 
 * @author hashemi
 *
 */
public class SceneModel {
	
	private StoryModel storyModel;
	
	private ArrayList<SentenceModel> sentences = new ArrayList<SentenceModel>();
			
	private ArrayList<Role> roles = new ArrayList<Role>();	
	
	private ArrayList<StaticObject> static_objects = new ArrayList<StaticObject>();
	
	private ArrayList<DynamicObject> dynamic_objescts = new ArrayList<DynamicObject>();
	
	private Location location;
	
	private Time time;
	
	private ArrayList<SceneGoal> scene_goals = new ArrayList<SceneGoal>();
		
	private ArrayList<SceneEmotion> scene_emotions = new ArrayList<SceneEmotion>();
	
	
	public SceneModel(StoryModel storyModel) {		
		this.storyModel = storyModel;
		this.sentences = new ArrayList<SentenceModel>();		
		this.roles = new ArrayList<Role>();
		this.static_objects = new ArrayList<StaticObject>();
		this.dynamic_objescts = new ArrayList<DynamicObject>();
		this.scene_goals = new ArrayList<SceneGoal>();
		this.scene_emotions = new ArrayList<SceneEmotion>();
	}
	
	public SceneModel(){
		this.sentences = new ArrayList<SentenceModel>();		
		this.roles = new ArrayList<Role>();
		this.static_objects = new ArrayList<StaticObject>();
		this.dynamic_objescts = new ArrayList<DynamicObject>();
		this.scene_goals = new ArrayList<SceneGoal>();
		this.scene_emotions = new ArrayList<SceneEmotion>();
	}
	//------------------ setter part -------------------
	
	public void setStory(StoryModel story) {
		this.storyModel = story;
	}

	public void setLocation(Location location) {		
		this.location = location;
	}

	public void setTime(Time time) {
		this.time = time;
	}
	
	//------------------ getter part -------------------

	public StoryModel getStory() {
		return storyModel;
	}

	public ArrayList<SentenceModel> getSentences() {
		return sentences;
	}

	public ArrayList<Role> getRoles() {
		return roles;
	}
	
	public Role getRole(Node role_node) {
		if(role_node == null)
			return null;
		
		for(Role role:this.roles)			
			if(role._node == role_node)
				return role;
		
		MyError.error("this SceneModel has no such a " + role_node + " Role.");
		return null;
	}
	
	public ArrayList<StaticObject> getStatic_objects() {
		return static_objects;
	}
	
	public StaticObject getStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return null;
		
		for(StaticObject staObj: this.static_objects)
			if(staObj._node == static_object_node)
				return staObj;
		
		MyError.error("this SceneModel has no such a " + static_object_node + " StaticObject.");
		return null;
	}
	
	public ArrayList<DynamicObject> getDynamic_objects() {
		return dynamic_objescts;
	}
	
	public DynamicObject getDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return null;
		
		for(DynamicObject dynObj: this.dynamic_objescts)
			if(dynObj._node == dynamin_object_node)
				return dynObj;
		
		MyError.error("this SceneModel has no such a " + dynamin_object_node + " DynamicObject.");
		return null;
	}

	public Location getLocation() {
		return location;
	}

	public Time getTime() {
		return time;
	}
	
	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
	}

	public SceneEmotion getScene_emotion(Node scene_emotion_node) {
		if(scene_emotion_node == null)
			return null;
		
		for(SceneEmotion sceEmo: this.scene_emotions)
			if(sceEmo._node == scene_emotion_node)
				return sceEmo;
		
		MyError.error("this SceneModel has no such a " + scene_emotion_node + " SceneEmotion.");
		return null;
	}
	
	public ArrayList<SceneGoal> getScene_goals() {
		return scene_goals;
	}
	
	
	public SceneGoal getScene_goal(Node scene_goal_node) {
		if(scene_goal_node == null)
			return null;
		
		for(SceneGoal sceGoal: this.scene_goals)
			if(sceGoal._node == scene_goal_node)
				return sceGoal;
		
		MyError.error("this SceneModel has no such a " + scene_goal_node + " SceneGoal.");
		return null;	
	}
	
	/**
	 * @param s
	 * @return the SceneElement (Role, DynamicObject, StaticObject, ...) which this SceneModel has based on sentencePart._wsd
	 */
	public SceneElement getSceneElement(Node node) {		
		if(node == null){
			MyError.error("null input parameter for getSceneElement!");
			return null;
		}								
		
		for(Role role:roles)
			if(role._node == node)
				return role;
		
		for(DynamicObject dynObj:dynamic_objescts)
			if(dynObj._node == node)
				return dynObj;
		
		for(StaticObject staObj:static_objects)
			if(staObj._node == node)
				return staObj;
		
		if(location != null && location._node == node)
			return location;
		
		if(time != null && time._node == node)
			return time;
		
		for(SceneEmotion emot:scene_emotions)
			if(emot._node == node)
				return emot;
		
		for(SceneGoal goal:scene_goals)
			if(goal._node == node)
				return goal;
		
		return null;
	}
	
	/**
	 * @param sceneElement
	 * @return the SceneElement (Role, DynamicObject, StaticObject, ...) which this SceneModel has based on part._wsd
	 */
	public SceneElement getSceneElement(SceneElement sceneElement) {
		
		if(sceneElement == null || sceneElement.scenePart == null || sceneElement.scenePart == ScenePart.UNKNOWN){
			MyError.error("null input parameter for getSceneElement!");
			return null;
		}								
		
		ScenePart scenePart = sceneElement.scenePart;
		
		if(scenePart == ScenePart.ROLE){			
			if(hasRole(sceneElement._node))
				return getRole(sceneElement._node);
		}
		else if(scenePart == ScenePart.DYNAMIC_OBJECT){		
			if(hasDynamic_object(sceneElement._node))
				return getDynamic_object(sceneElement._node);
		}				
		else if(scenePart == ScenePart.STATIC_OBJECT){
			if(hasStatic_object(sceneElement._node))
				return getStatic_object(sceneElement._node);
		}		
		else if(scenePart == ScenePart.LOCATION){
			if(getLocation() != null)
				return getLocation();			
		}		
		else if(scenePart == ScenePart.TIME){
			if(getTime() != null)
				return getTime();
		}
		else if(scenePart == ScenePart.SCENE_EMOTION){
			if(hasScene_emotion(sceneElement._node))
				return getScene_emotion(sceneElement._node);
		}
		else if(scenePart == ScenePart.SCENE_GOAL)
			if(hasScene_goal(sceneElement._node))
				return getScene_goal(sceneElement._node);
		
		return null;
	}
	//------------------ add   part -------------------
	
	public void addAllSentences(ArrayList<SentenceModel> sentences) {
		if(this.sentences == null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentences != null && sentences.size()>0)
			this.sentences.addAll(sentences);
	}
	
	public void addSentence(SentenceModel sentence) {
		if(this.sentences == null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentence != null)
			this.sentences.add(sentence);
	}

	public void addAllRoles(ArrayList<Role> roles) {
		if(this.roles == null)
			this.roles = new ArrayList<Role>();
		
		if(roles != null && roles.size()>0)
			this.roles.addAll(roles);
	}
	
	public void addRole(Role role) {
		if(this.roles == null)
			this.roles = new ArrayList<Role>();
		
		if(role != null)
			this.roles.add(role);
	}
	
	public void addAllStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(this.static_objects == null)
			this.static_objects = new ArrayList<StaticObject>();
		
		if(static_objects != null && static_objects.size()>0)
			this.static_objects.addAll(static_objects);
	}
	
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objects == null)
			this.static_objects = new ArrayList<StaticObject>();
		
		if(static_object != null)
			this.static_objects.add(static_object);
	}

	public void addAllDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {
		if(this.dynamic_objescts == null)
			this.dynamic_objescts = new ArrayList<DynamicObject>();
		
		if(dynamic_objects != null && dynamic_objects.size()>0)
			this.dynamic_objescts.addAll(dynamic_objects);
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objescts == null)
			this.dynamic_objescts = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			this.dynamic_objescts.add(dynamic_object);
	}
		
	public void addAllScene_goals(ArrayList<SceneGoal> scene_goals) {
		if(this.scene_goals == null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goals != null && scene_goals.size()>0)
			this.scene_goals.addAll(scene_goals);
	}
	
	public void addScene_goal(SceneGoal scene_goal) {
		if(this.scene_goals == null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goal != null)
			this.scene_goals.add(scene_goal);
	}
	

	public void addAllScene_emotions(ArrayList<SceneEmotion> scene_emotions) {
		if(this.scene_emotions == null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotions != null && scene_emotions.size()>0)
			this.scene_emotions.addAll(scene_emotions);
	}
	
	public void addScene_emotion(SceneEmotion scene_emotion) {
		if(this.scene_emotions == null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotion != null)
			this.scene_emotions.add(scene_emotion);
	}
	
	/**
	 * this method based on the ScenePart of the part adds a Role, DynamicObject, StaticObject, or ... to primarySceneModel.
	 * TODO: we have assumed for simplicity which every scene has a unique Role, DyanamicObject, and StaticObject with a one name.
	 * for example all «پسرک» refer to just one Role. 
	 *  
	 * @param sceneElement the new  SceneElement which is to be added to this SceneModel. 
	 */
	public void addToPrimarySceneModel(SceneElement sceneElement){
		if(sceneElement == null || sceneElement.scenePart == null || sceneElement.scenePart == ScenePart.UNKNOWN){		
			MyError.error("null input parameter for addToPrimarySceneModel !");
			return;
		}								
		
		if(sceneElement.scenePart == ScenePart.ROLE){
			Role role = (Role) sceneElement;
			if(!hasRole(role)){
				addRole(role);				
			}
		}
		else if(sceneElement.scenePart == ScenePart.DYNAMIC_OBJECT){
			DynamicObject dynObj = (DynamicObject) sceneElement;
			if(!hasDynamic_object(dynObj)){
				addDynamic_object(dynObj);
			}
			
		}
		else if(sceneElement.scenePart == ScenePart.STATIC_OBJECT){
			StaticObject staObj = (StaticObject) sceneElement;
			if(!hasStatic_object(staObj)){
				addStatic_object(staObj);				
			}			
		}
		else if(sceneElement.scenePart == ScenePart.LOCATION){//TODO: check what else shall I do for this case!			
			if(getLocation() != null){
				MyError.error("the primarySceneModel has had a location before!" + getLocation());
				return;
			}

			Location location = (Location) sceneElement;
			setLocation(location);
			
		}
		else if(sceneElement.scenePart == ScenePart.TIME){//TODO: check what else shall I do for this case!
			if(getTime() != null){
				MyError.error("the primarySceneModel has had a time before!" + getTime());
				return;
			}

			Time time = (Time) sceneElement;
			setTime(time);			
		}		
	}
	
	//------------------ has   part -------------------
	
	public boolean hasRole(Node role_node) {
		if(role_node == null)
			return false;
		
		for(Role role:this.roles)
			if(role._node == role_node)
				return true;
		return false;
	}
	
	public boolean hasRole(Role role) {
		if(role == null)
			return false;
		
		for(Role rl:this.roles)
			if(rl.equals(role))
				return true;
		return false;
	}
	
	public boolean hasDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return false;
		
		for(DynamicObject dynObj: this.dynamic_objescts)
			if(dynObj._node == dynamin_object_node)
				return true;
		return false;
	}
	
	public boolean hasDynamic_object(DynamicObject dynamin_object) {
		if(dynamin_object == null)
			return false;
		
		for(DynamicObject dynObj: this.dynamic_objescts)
			if(dynObj.equals(dynamin_object))
				return true;
		return false;
	}

	

	public boolean hasStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return false;
		
		for(StaticObject staObj: this.static_objects)
			if(staObj._node == static_object_node)
				return true;
		return false;
	}
	
	public boolean hasStatic_object(StaticObject static_object) {
		if(static_object == null)
			return false;
		
		for(StaticObject staObj: this.static_objects)
			if(staObj.equals(static_object))
				return true;
		return false;
	}


	public boolean hasSentence(SentenceModel sentence) {
		if(sentence == null)
			return false;
		
		for(SentenceModel sen:this.sentences)
			if(sen == sentence)
				return true;
		return false;
	}

	public boolean hasScene_emotion(Node scene_emotion_node) {
		if(scene_emotion_node == null)
			return false;
		
		for(SceneEmotion sceEmo: this.scene_emotions)
			if(sceEmo._node == scene_emotion_node)
				return true;
		return false;
	}
	
	public boolean hasScene_emotion(SceneEmotion scene_emotion) {
		if(scene_emotion == null)
			return false;
		
		for(SceneEmotion sceEmo: this.scene_emotions)
			if(sceEmo.equals(scene_emotion))
				return true;
		return false;
	}
	


	public boolean hasScene_goal(Node scene_goal_node) {
		if(scene_goal_node == null)
			return false;
		
		for(SceneGoal sceGoal: this.scene_goals)
			if(sceGoal._node == scene_goal_node)
				return true;	
			return false;
	}
	
	public boolean hasScene_goal(SceneGoal scene_goal) {
		if(scene_goal == null)
			return false;
		
		for(SceneGoal sceGoal: this.scene_goals)
			if(sceGoal.equals(scene_goal))
				return true;	
			return false;
	}	
	
	public boolean hasSceneElement(SceneElement sceneElement) {
		
		if(sceneElement == null || sceneElement.scenePart == null || sceneElement.scenePart == ScenePart.UNKNOWN){
			MyError.error("null input parameter for hasSceneElement!");
			return false;
		}								
		
		ScenePart scenePart = sceneElement.scenePart;
		
		if(scenePart == ScenePart.ROLE)
			if(hasRole(sceneElement._node))				
				return true;
			else
				return false;
		
		else if(scenePart == ScenePart.DYNAMIC_OBJECT)		
			if(hasDynamic_object(sceneElement._node))				
				return true;
			else
				return false;
		
		else if(scenePart == ScenePart.STATIC_OBJECT)
			if(hasStatic_object(sceneElement._node))				
				return true;			
			else
				return false;
		
		else if(scenePart == ScenePart.LOCATION)
			if(getLocation() != null)
				if(getLocation()._node == sceneElement._node)
					return true;
				else
					return false;

		
		else if(scenePart == ScenePart.TIME)
			if(getTime() != null)
				if(getTime()._node == sceneElement._node)
					return true;
				else
					return false;						
		
		return false;
	}	
		
	
	@SuppressWarnings("unused")
	private void print(String toPrint){
		System.out.println(toPrint);
		
	}

	@Override
	public String toString() {
		String st = "SceneModel [" + "\nroles= "; 
		for (Role r : this.roles)
			st += "\n" + r;
		
		st+= "\ndynamic_objs= ";
		for(DynamicObject dOb:this.dynamic_objescts)
			st += "\n" + dOb;
		
		st += "\nstatic_objs= ";
		for(StaticObject stOb:this.static_objects)
			st += "\n" + stOb;
		
		st += "\nlocation= " + location + 
		"\ntime= " + time +
		"\nscene_goals= " + scene_goals + 
		"\nscene_emotions= " + scene_emotions + "]\n";
		
		return st;
	}
	
}
			
		
	