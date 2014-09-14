package model;


import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import sceneElement.DynamicObject;
import sceneElement.Location;
import sceneElement.Role;
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
	
	private ArrayList<StaticObject> static_objs = new ArrayList<StaticObject>();
	
	private ArrayList<DynamicObject> dynamic_objs = new ArrayList<DynamicObject>();
	
	private Location location;
	
	private Time time;
	
	private ArrayList<SceneGoal> scene_goals = new ArrayList<SceneGoal>();
		
	private ArrayList<SceneEmotion> scene_emotions = new ArrayList<SceneEmotion>();
	
		
	public SceneModel() {
		this.sentences = new ArrayList<SentenceModel>();		
		this.roles = new ArrayList<Role>();
		this.static_objs = new ArrayList<StaticObject>();
		this.dynamic_objs = new ArrayList<DynamicObject>();
		this.scene_goals = new ArrayList<SceneGoal>();
		this.scene_emotions = new ArrayList<SceneEmotion>();
	
	}
		
		
	public StoryModel getStory() {
		return storyModel;
	}

	public void setStory(StoryModel story) {
		this.storyModel = story;
	}

	public ArrayList<SentenceModel> getSentences() {
		return sentences;
	}

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

	public ArrayList<Role> getRoles() {
		return roles;
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

	public ArrayList<StaticObject> getStatic_objects() {
		return static_objs;
	}
	
	public void addAllStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(this.static_objs == null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_objects != null && static_objects.size()>0)
			this.static_objs.addAll(static_objects);
	}
	
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objs == null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_object != null)
			this.static_objs.add(static_object);
	}

	public ArrayList<DynamicObject> getDynamic_objects() {
		return dynamic_objs;
	}

	public void addAllDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {
		if(this.dynamic_objs == null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_objects != null && dynamic_objects.size()>0)
			this.dynamic_objs.addAll(dynamic_objects);
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objs == null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			this.dynamic_objs.add(dynamic_object);
	}
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if(this.location != null)
			MyError.error("this SceneModel previouly has a location " + this.location);
		this.location = location;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public ArrayList<SceneGoal> getScene_goals() {
		return scene_goals;
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

	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
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
		for(DynamicObject dOb:this.dynamic_objs)
			st += "\n" + dOb;
		
		st += "\nstatic_objs= ";
		for(StaticObject stOb:this.static_objs)
			st += "\n" + stOb;
		
		st += "\nlocation= " + location + 
		"\ntime= " + time +
		"\nscene_goals= " + scene_goals + 
		"\nscene_emotions= " + scene_emotions + "]\n";
		
		return st;
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

	public boolean hasRole(Node role_node) {
		if(role_node == null)
			return false;
		
		for(Role role:this.roles)
			if(role._node == role_node)
				return true;
		return false;
	}
	
	
	public DynamicObject getDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return null;
		
		for(DynamicObject dynObj: this.dynamic_objs)
			if(dynObj._node == dynamin_object_node)
				return dynObj;
		
		MyError.error("this SceneModel has no such a " + dynamin_object_node + " DynamicObject.");
		return null;
	}
	
	public boolean hasDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return false;
		
		for(DynamicObject dynObj: this.dynamic_objs)
			if(dynObj._node == dynamin_object_node)
				return true;
		return false;
	}

	public StaticObject getStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return null;
		
		for(StaticObject staObj: this.static_objs)
			if(staObj._node == static_object_node)
				return staObj;
		
		MyError.error("this SceneModel has no such a " + static_object_node + " StaticObject.");
		return null;
	}

	public boolean hasStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return false;
		
		for(StaticObject staObj: this.static_objs)
			if(staObj._node == static_object_node)
				return true;
		return false;
	}




	}
			
		
	