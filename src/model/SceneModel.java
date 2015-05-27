package model;


import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
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
	
	private ArrayList<Location> alternativeLocations = new ArrayList<Location>();
	
	private Time time;
	
	private ArrayList<Time> alternativeTimes = new ArrayList<Time>();
	
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
		if(location != null){
			if(this.location != null)
				print("the SceneModel previous Location: " + this.location + " replaced with " + location);
				
			print("Location " + location + " set for SceneModel.");
			
			this.location = location;
//			addAlternativeLocation(location);
		}
	}
	
	public void setTime(Time time) {
		if(time != null){
			if(this.time != null)
				print("the SceneModel previous Time: " + this.time + " replaced with " + time);
			
			print("Time " + time + " set for SceneModel.");
			this.time = time;
//			addAlternativeTime(time);
		}		
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
			if(role._node.equalsRelaxed(role_node))
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
			if(staObj._node.equalsRelaxed(static_object_node))
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
			if(dynObj._node.equalsRelaxed(dynamin_object_node))
				return dynObj;
		
		MyError.error("this SceneModel has no such a " + dynamin_object_node + " DynamicObject.");
		return null;
	}

	public Location getLocation() {
		return location;
	}

	public ArrayList<Location> getAlternativeLocations() {
		return alternativeLocations;
	}
	
	public Location getAlternativeLocation(Node alternative_location_node) {
		if(alternative_location_node == null)
			return null;
		
		for(Location altLoc: this.alternativeLocations)
			if(altLoc._node.equalsRelaxed(alternative_location_node))
				return altLoc;
		
		MyError.error("this SceneModel has no such a " + alternative_location_node + " Location as alternativeLocation.");
		return null;
	}	
	
	
	public Time getTime() {
		return time;
	}
	
	public ArrayList<Time> getAlternativeTimes(){
		return alternativeTimes;
	}
	
	public Time getAlternativeTime(Node alternative_time_node) {
		if(alternative_time_node == null)
			return null;
		
		for(Time altTime: this.alternativeTimes)
			if(altTime._node.equalsRelaxed(alternative_time_node))
				return altTime;
		
		MyError.error("this SceneModel has no such a " + alternative_time_node + " Time as alternativeTime.");
		return null;
	}	
	
	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
	}

	public SceneEmotion getScene_emotion(Node scene_emotion_node) {
		if(scene_emotion_node == null)
			return null;
		
		for(SceneEmotion sceEmo: this.scene_emotions)
			if(sceEmo._node.equalsRelaxed(scene_emotion_node))
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
			if(sceGoal._node.equalsRelaxed(scene_goal_node))
				return sceGoal;
		
		MyError.error("this SceneModel has no such a " + scene_goal_node + " SceneGoal.");
		return null;	
	}
	
	/**
	 * @param s
	 * @return the SceneElement (Role, DynamicObject, StaticObject, ...) which this SceneModel has based on Word._wsd
	 */
	public SceneElement getSceneElement(Node node) {		
		if(node == null){
			MyError.error("null input parameter for getSceneElement!");
			return null;
		}								
		
		if(!Common.isEmpty(roles))
			for(Role role:roles)
				if(role._node.equalsRelaxed(node))
					return role;
			
		if(!Common.isEmpty(dynamic_objescts))
			for(DynamicObject dynObj:dynamic_objescts)
				if(dynObj._node.equalsRelaxed(node))
					return dynObj;
		
		if(!Common.isEmpty(static_objects))
			for(StaticObject staObj:static_objects)
				if(staObj._node.equalsRelaxed(node))
					return staObj;
				
		if(location != null && location._node.equalsRelaxed(node))
			return location;
		
		if(!Common.isEmpty(alternativeLocations))
			for(Location loc:alternativeLocations)
				if(loc._node.equalsRelaxed(node))
					return loc;
			
		if(time != null && time._node.equalsRelaxed(node))
			return time;
		
		if(!Common.isEmpty(alternativeTimes))
			for(Time ti:alternativeTimes)
				if(ti._node.equalsRelaxed(node))
					return ti;
		
		if(!Common.isEmpty(scene_emotions))
			for(SceneEmotion emot:scene_emotions)
				if(emot._node.equalsRelaxed(node))
					return emot;
		
		if(!Common.isEmpty(scene_goals))
			for(SceneGoal goal:scene_goals)
				if(goal._node.equalsRelaxed(node))
					return goal;
			
		return null;
	}
	
	/**
	 * @param sceneElement
	 * @return the SceneElement (Role, DynamicObject, StaticObject, ...) which this SceneModel has based on Word._wsd
	 */
	public SceneElement getSceneElement(SceneElement sceneElement) {
		
		if(sceneElement == null || sceneElement.scenePart == null || sceneElement.scenePart == ScenePart.UNKNOWN){
			MyError.error("null input parameter for getSceneElement!");
			return null;
		}								
		
		ScenePart scenePart = sceneElement.scenePart;
		
		if(scenePart == ScenePart.ROLE)			
			if(hasRole(sceneElement._node))
				return getRole(sceneElement._node);		
		
		if(scenePart == ScenePart.DYNAMIC_OBJECT)		
			if(hasDynamic_object(sceneElement._node))
				return getDynamic_object(sceneElement._node);		
		
		if(scenePart == ScenePart.STATIC_OBJECT)
			if(hasStatic_object(sceneElement._node))
				return getStatic_object(sceneElement._node);		
		
		if(scenePart == ScenePart.LOCATION)
			if(hasAlternativeLocation(sceneElement._node))			
				return getAlternativeLocation(sceneElement._node);
		
		if(scenePart == ScenePart.TIME)
			if(hasAlternativeTime(sceneElement._node))
				return getAlternativeTime(sceneElement._node);
		
		if(scenePart == ScenePart.SCENE_EMOTION)
			if(hasScene_emotion(sceneElement._node))
				return getScene_emotion(sceneElement._node);
		
		if(scenePart == ScenePart.SCENE_GOAL)
			if(hasScene_goal(sceneElement._node))
				return getScene_goal(sceneElement._node);
		
		return null;
	}
	
	public Word findWordOfSceneElement(SceneElement sceneElement){
		if(sceneElement == null)
			return null;
		
		for(SentenceModel sentence:this.sentences){
			Word wrd = sentence.getWord(sceneElement._node);
			if(wrd != null)
				return wrd;
		}
		return null;		
	}
	
	//------------------ add   part -------------------
	
	public void addSentence(SentenceModel sentence) {
		if(this.sentences == null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentence != null)
			if(!hasSentence(sentence))
				this.sentences.add(sentence);
	}
	
	public void addAllSentences(ArrayList<SentenceModel> sentences) {
		if(!Common.isEmpty(sentences))
			for(SentenceModel sen:sentences)
				addSentence(sen);
	}

	public void addRole(Role role) {
		if(this.roles == null)
			this.roles = new ArrayList<Role>();
		
		if(role != null)
			if(!hasRole(role)){
				this.roles.add(role);
				print(role._name + " added to SceneModel as Role.");
			}
			else
				print("SceneModel has this " + role + " Role before!");
	}
	
	public void addAllRoles(ArrayList<Role> roles) {
		if(!Common.isEmpty(roles))
			for(Role rl:roles)
				addRole(rl);			
	}
		
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objects == null)
			this.static_objects = new ArrayList<StaticObject>();
		
		
		if(static_object != null)
			if(!hasStatic_object(static_object)){
				this.static_objects.add(static_object);
				print(static_object._name + " added to SceneModel as Static_object.");
			}
			else
				print("SceneModel has this " + static_object + " Static_object before!");
	}
	
	public void addAllStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(!Common.isEmpty(static_objects))
			for(StaticObject stObj:static_objects)
				addStatic_object(stObj);		
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objescts == null)
			this.dynamic_objescts = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			if(!hasDynamic_object(dynamic_object)){
				this.dynamic_objescts.add(dynamic_object);
				print(dynamic_object._name + " added to SceneModel as Dynamic_object.");
			}
			else
				print("SceneModel has this " + dynamic_object + " Dynamic_object before!");
	}

	public void addAllDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {		
		if(!Common.isEmpty(dynamic_objects))
			for(DynamicObject dynObj:dynamic_objects)
				addDynamic_object(dynObj);
	}
	
	public void addScene_goal(SceneGoal scene_goal) {
		if(this.scene_goals == null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goal != null)
			if(!hasScene_goal(scene_goal)){
				this.scene_goals.add(scene_goal);
				print(scene_goal._name + " added to SceneModel as Scene_goal.");
			}
			else
				print("SceneModel has this " + scene_goal + " Scene_goal before!");
	}
		
	public void addAllScene_goals(ArrayList<SceneGoal> scene_goals) {
		if(!Common.isEmpty(scene_goals))
			for(SceneGoal sceGoal:scene_goals)
				addScene_goal(sceGoal);		
	}
	
	public void addScene_emotion(SceneEmotion scene_emotion) {
		if(this.scene_emotions == null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotion != null)
			if(!hasScene_emotion(scene_emotion)){
				this.scene_emotions.add(scene_emotion);
				print(scene_emotion._name + " added to SceneModel as Scene_emotion.");
			}
			else
				print("SceneModel has this " + scene_emotion + " Scene_emotion before!");
	}
	
	public void addAllScene_emotions(ArrayList<SceneEmotion> scene_emotions) {
		if(!Common.isEmpty(scene_emotions))
			for(SceneEmotion sceEmo:scene_emotions)
				addScene_emotion(sceEmo);
	}
	
	public void addAlternativeLocation(Location location) {		
		if(location != null){
			if(alternativeLocations == null)
				alternativeLocations = new ArrayList<Location>();
			
			if(!hasAlternativeLocation(location)){
				alternativeLocations.add(location);
				print("alternativeLocation " + location + " added to SceneModel.");
			}
			else
				print("SceneModel has this " + location + " AlternativeLocation before!");			
		}		
	}


	
	public void addAlternativeTime(Time time) {
		if(time != null){
			if(alternativeTimes == null)
				alternativeTimes = new ArrayList<Time>();
			
			if(!hasAlternativeTime(time)){
				alternativeTimes.add(time);
				print("alternativeTime " + time + " added to SceneModel.");
			}
			else
				print("SceneModel has this " + time + " AlternativeTime before!");
		}			
	}
	
		/**
	 * this method based on the ScenePart of the part adds a Role, DynamicObject, StaticObject, or ... to primarySceneModel.
	 * TODO: we have assumed for simplicity which every scene has a unique Role, DyanamicObject, and StaticObject with a one name.
	 * for example all «پسرک» refer to just one Role.
	 * <li> if ScenePart of SceneElement is ROLE 
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
	 * @param sceneElement the new  SceneElement which is to be added to this SceneModel.  
	 */
	public void addToPrimarySceneModel(SceneElement sceneElement){
		if(sceneElement == null || sceneElement.scenePart == null || sceneElement.scenePart == ScenePart.UNKNOWN){		
			MyError.error("null or UNKNOWN input parameter for addToPrimarySceneModel !");
			return;
		}								
		
		if(sceneElement.scenePart == ScenePart.ROLE){
			Role role = (Role) sceneElement;
			addRole(role);			
		}
		else if(sceneElement.scenePart == ScenePart.DYNAMIC_OBJECT){
			DynamicObject dynObj = (DynamicObject) sceneElement;			
			addDynamic_object(dynObj);
		}
		else if(sceneElement.scenePart == ScenePart.STATIC_OBJECT){
			StaticObject staObj = (StaticObject) sceneElement;			
			addStatic_object(staObj);
		}
		else if(sceneElement.scenePart == ScenePart.LOCATION){
			Location altLoc = (Location) sceneElement;
			addAlternativeLocation(altLoc);			
		}
		else if(sceneElement.scenePart == ScenePart.TIME){
			Time altTime = (Time) sceneElement;
			addAlternativeTime(altTime);
		}
		else if(sceneElement.scenePart == ScenePart.SCENE_EMOTION){
			SceneEmotion sceEmo = (SceneEmotion) sceneElement;			
			addScene_emotion(sceEmo);
		}
		else if(sceneElement.scenePart == ScenePart.SCENE_GOAL){
			SceneGoal sceGoal = (SceneGoal) sceneElement;			
			addScene_goal(sceGoal);						
		}		
	}
	
	//------------------ has   part -------------------
	
	public boolean hasRole(Node role_node) {
		if(role_node == null)
			return false;
		
		for(Role role:this.roles)
			if(role._node.equalsRelaxed(role_node))
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
			if(dynObj._node.equalsRelaxed(dynamin_object_node))
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
			if(staObj._node.equalsRelaxed(static_object_node))
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
			if(sceEmo._node.equalsRelaxed(scene_emotion_node))
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
			if(sceGoal._node.equalsRelaxed(scene_goal_node))
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
	
	public boolean hasAlternativeLocation(Node locatin_node) {
		if(locatin_node == null)
			return false;
		
		for(Location loc: this.alternativeLocations)
			if(loc._node.equalsRelaxed(locatin_node))
				return true;
		return false;
	}
	
	public boolean hasAlternativeLocation(Location location) {
		if(location == null)
			return false;
		
		for(Location loc: this.alternativeLocations)
			if(loc.equals(location))
				return true;	
		return false;
	}
	
	public boolean hasAlternativeTime (Node time_node) {
		if(time_node == null)
			return false;
		
		for(Time t: this.alternativeTimes)
			if(t._node.equalsRelaxed(time_node))
				return true;	
		return false;
	}	
	
	
	public boolean hasAlternativeTime (Time time) {
		if(time == null)
			return false;
		
		for(Time t: this.alternativeTimes)
			if(t.equals(time))
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
		
		if(scenePart == ScenePart.DYNAMIC_OBJECT)		
			if(hasDynamic_object(sceneElement._node))				
				return true;
			else
				return false;
		
		if(scenePart == ScenePart.STATIC_OBJECT)
			if(hasStatic_object(sceneElement._node))				
				return true;			
			else
				return false;
		
		if(scenePart == ScenePart.LOCATION)
			if(getLocation() != null)
				if(getLocation()._node.equalsRelaxed(sceneElement._node))
					return true;
				else if(hasAlternativeLocation(sceneElement._node))
					return true;
				else
					return false;
		
		if(scenePart == ScenePart.TIME)
			if(getTime() != null)
				if(getTime()._node.equalsRelaxed(sceneElement._node))
					return true;
				else if(hasAlternativeTime(sceneElement._node))
					return true;
				else
					return false;						
		
		if(scenePart == ScenePart.SCENE_EMOTION)
			if(hasScene_emotion(sceneElement._node))				
				return true;
			else
				return false;
		
		if(scenePart == ScenePart.SCENE_GOAL)
			if(hasScene_goal(sceneElement._node))				
				return true;
			else
				return false;
		
		return false;
	}	
		
	private void print(String toPrint){
		System.out.println(toPrint);
		
	}

	@Override
	public String toString() {
		String st = "roles= ["; 
		for (Role r : this.roles)
			st += "\n" + "\t" + r;
		st += "]";
		
		st+= "\ndynamic_objs= ";
		for(DynamicObject dOb:this.dynamic_objescts)
			st += dOb; //"\n" + dOb;
		
		st += "\nstatic_objs= ";
		for(StaticObject stOb:this.static_objects)
			st += stOb; //"\n" + stOb;
		
		st += "\nlocation= " + location +
		"\nalternativeLocations= " + alternativeLocations +
		"\ntime= " + time +
		"\nalternativeTimes= " + alternativeTimes +
		"\nscene_goals= " + scene_goals + 
		"\nscene_emotions= " + scene_emotions + "]\n";
		
		return st;
	}

//	public void mergeElementWith(SceneModel mergee) {
//		if(mergee == null){
//			MyError.error("SceneModel can not merge with null!");
//			return;
//		}
//		
//		for(Role role:this.roles)
//			if(mergee.hasRole(role))
//				role.mergeWith(role);
//		
//	}
	
}
			
		
	