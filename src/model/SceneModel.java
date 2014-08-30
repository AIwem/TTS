package model;

import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;
import java.util.Locale;

import sceneElement.DynamicObject;
import sceneElement.Goal;
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
	
	private StoryModel story;
	
	private ArrayList<SentenceModel> sentences = new ArrayList<SentenceModel>();
	
	private ArrayList<Node> scene_nodes = new ArrayList<Node>();
	
	private ArrayList<Role> roles = new ArrayList<Role>();
	
	private ArrayList<StaticObject> static_objs = new ArrayList<StaticObject>();
	
	private ArrayList<DynamicObject> dynamic_objs = new ArrayList<DynamicObject>();
	
	private Location location;
	
	private Time time;
	
	private ArrayList<SceneGoal> scene_goals = new ArrayList<SceneGoal>();
		
	private ArrayList<SceneEmotion> scene_emotions = new ArrayList<SceneEmotion>();
	
	
	public StoryModel getStory() {
		return story;
	}

	public void setStory(StoryModel story) {
		this.story = story;
	}

	public ArrayList<SentenceModel> getSentences() {
		return sentences;
	}

	public void addSentences(ArrayList<SentenceModel> sentences) {
		if(this.sentences != null)
			this.sentences = new ArrayList<SentenceModel>();
		if(sentences != null && sentences.size()>0)
			this.sentences.addAll(sentences);
	}
	
	public void addSentence(SentenceModel sentence) {
		if(this.sentences != null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentence != null)
			this.sentences.add(sentence);
	}

	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void addRoles(ArrayList<Role> roles) {
		if(this.roles != null)
			this.roles = new ArrayList<Role>();
		
		if(roles != null && roles.size()>0)
			this.roles.addAll(roles);
	}
	
	public void addRole(Role role) {
		if(this.roles != null)
			this.roles = new ArrayList<Role>();
		
		if(role != null)
			this.roles.add(role);
	}

	public ArrayList<StaticObject> getStatic_obj() {
		return static_objs;
	}
	
	public void addStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(this.static_objs != null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_objects != null && static_objects.size()>0)
			this.static_objs.addAll(static_objects);
	}
	
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objs != null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_object != null)
			this.static_objs.add(static_object);
	}

	public ArrayList<DynamicObject> getDynamic_obj() {
		return dynamic_objs;
	}

	public void addDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {
		if(this.dynamic_objs != null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_objects != null && dynamic_objects.size()>0)
			this.dynamic_objs.addAll(dynamic_objects);
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objs != null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			this.dynamic_objs.add(dynamic_object);
	}
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
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

	public void addScene_goals(ArrayList<SceneGoal> scene_goals) {
		if(this.scene_goals != null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goals != null && scene_goals.size()>0)
			this.scene_goals.addAll(scene_goals);
	}
	
	public void addScene_goal(SceneGoal scene_goal) {
		if(this.scene_goals != null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goal != null)
			this.scene_goals.add(scene_goal);
	}

	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
	}

	public void addScene_emotions(ArrayList<SceneEmotion> scene_emotions) {
		if(this.scene_emotions != null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotions != null && scene_emotions.size()>0)
			this.scene_emotions.addAll(scene_emotions);
	}
	
	public void addScene_emotion(SceneEmotion scene_emotion) {
		if(this.scene_emotions != null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotion != null)
			this.scene_emotions.add(scene_emotion);
	}
	
	public Node findNode(String name){
		for (Node node:scene_nodes){
			if(node.getName().equals(name))
				return node;
		}
		return null;
	}

}
