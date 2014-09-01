package model;

import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

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
	
	private StoryModel story;
	
	private ArrayList<SentenceModel> sentences = new ArrayList<SentenceModel>();
	
	/**
	 * this map holds the Node object seen yet in this SceneModel object. 
	 * It maps the Node pure name as key to the ArrayList<Node> of seen Nodes with this name in this SceneModel object.
	 * for each key, the first Node in ArrayList contains the pure Node object fetched from kb.
	 * the others are different clone of that pure Node.
	 */	
	public Hashtable<String, ArrayList<Node>> scene_nodes_dic = new Hashtable<String,ArrayList<Node>>();
		
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
	/**
	 * findorAddNode searches this sceneModel scene_nodes to find a Node named "name".
	 * if it didn't find it, then load it from _kb and adds it to the scene_nodes.
	 * 
	 * TODO: we have temporarily assumed that every redundant input concept refers to the old seen one, not the new,
	 * for example all "پسرک" in the story refers to "*پسرک )1("
	 * for the new concept of "پسرک" the newNode parameter must be set to true.
	 * 
	 * @param name name of Node to be searched in sceneModel scene_nodes or _kb.
	 * @return Node object named "name".
	 */	
	public Node findorAddNode(String name, boolean newNode, KnowledgeBase kb){
		if(name == null || name.equals("-"))
			return null;		
		
		//a cloned Node with name "name" exists in scene_nodes_dic.
		if(scene_nodes_dic.containsKey(name)){
			
			ArrayList<Node> seenNodes = scene_nodes_dic.get(name);
			 
			 if(seenNodes == null)
				 seenNodes = new ArrayList<Node>();
			 if(seenNodes.size() == 0)
				 MyError.error(name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
			 
			 if(!newNode){//It is not a new Node, so return the seenNode
				 if(seenNodes.size() == 2)//the first Node is the pure Node fetched from kb, and the second is the only cloned node.
					 return seenNodes.get(1);
				 else//TODO: I must select the really desired one, but temporarily I return the last one!
					 return seenNodes.get(seenNodes.size()-1);
			 }
			 else{//It is a new Node, so a new cloned instance must be added to scene_nodes_dic.
				 if(seenNodes.size() > 0){
					 Node newlyCloned = new Node(seenNodes.get(0));//clone a node from its pure version fetched from kb.
					 seenNodes.add(newlyCloned);
					 return newlyCloned;
				 }
				 else{
					 MyError.error(name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
					 Node n = kb.addConcept(name);
					 if(n!= null){
						 Node cloned = cloneNode(n, kb);//clone a node from its pure version fetched from kb.
						 seenNodes.add(cloned);
						 return cloned;
					 }
					 else{
						 MyError.error("the node named " + name + " could not be found or even added to the knowledgebase!");
						 return null;
					 }
				 }
			 }
		}
		else{//scene_nodes_dic dose not contain this name, so it is not seen yet.			
			Node n = kb.addConcept(name);
			if(n!= null){
				ArrayList<Node> newlySeen = new ArrayList<Node>();
				newlySeen.add(n);
				Node cloned = cloneNode(n,kb);//clone a node from its pure version fetched from kb.
				newlySeen.add(cloned);
				scene_nodes_dic.put(name, newlySeen);
				return cloned;
			 }
			 else{
				 MyError.error("the node named " + name + " could not be found or even added to the knowledgebase!");
				 return null;
			 }
		}
	}
	
	/**
	 * addRelationWithoutClone searches the scene_nodes_dic for pure_name, 
	 * if it dosen't find it add pure_name as key and an ArrayList of Node with pure relation as first element and its cloned version as second element.
	 * if it finds pure_name, adds its cloned version to the end of the list of pure_name mapped ArrayList<Nodes>.
	 *  
	 * @param pure_name the name of PlausibleStatement to be added.
	 * @param clonedRelation the cloned PlausibleStatement to be added.
	 */
	public void addRelationWithoutClone(String pure_name, PlausibleStatement clonedRelation){
		if(pure_name == null || pure_name.equals("") || clonedRelation == null)
			return;
		
		//it is the first time this relation is seen.
		if(!scene_nodes_dic.containsKey(pure_name)){
			ArrayList<Node> seenRelation = new ArrayList<Node>();
			seenRelation.add(clonedRelation.relationType);
			seenRelation.add(clonedRelation);
			scene_nodes_dic.put(pure_name, seenRelation);
		}
		else{// we have seen this relation before
			ArrayList<Node> seenRelation = scene_nodes_dic.get(pure_name);
			
			if(seenRelation == null)
				seenRelation = new ArrayList<Node>();
			
			if(seenRelation.size() == 0){
				MyError.error(pure_name + " key exists in scene_nodes_dic but no relation exists! probably the map is corrupted!");
				seenRelation.add(clonedRelation.relationType);
			}
			seenRelation.add(clonedRelation);			
		}
	}
	
	/**
	 * findRelation searches the scene_nodes_dic to find the previous seen plausibleStatement with name "relation_name", if any.
	 * if found returns it, if scene_nodes_dic dosen't have such a relation_name as key, it return null.
	 * 
	 * @param relation_name the name of PlausibleStatement to be search
	 * @param kb 
	 * @return the PlausibleStatement object previously seen, it is cloned PlausibleStatement.
	 */
	public PlausibleStatement findRelation(String relation_name, KnowledgeBase kb){
		if(relation_name == null || relation_name.equals("-"))
			return null;		
		try{
			if(scene_nodes_dic.containsKey(relation_name)){
				ArrayList<Node> seenRelations = scene_nodes_dic.get(relation_name);
				if(seenRelations == null || seenRelations.size() == 0)
					return null;
				if(seenRelations.size() == 2)//the first Relation is the pure Node fetched from kb, and the second is the only cloned Relation.
					return (PlausibleStatement)seenRelations.get(1);
				else//TODO: I must select the really desired one, but temporarily I return the last one!
					return (PlausibleStatement)seenRelations.get(seenRelations.size()-1);
			}
			return null;
		}
		catch(Exception e){
			MyError.error(e.getMessage());
			return null;
		}			
	}
	
	private Node cloneNode(Node node, KnowledgeBase kb){
		if(node == null)
			return null;
		
		Node cloneNode = new Node(node);
		Node fromKB = kb.addConcept(cloneNode.getName());//TODO: %%%%%%%%%%%%%%%%%%%% check it		
		
		kb.addRelation(fromKB, node, KnowledgeBase.HPR_SIM);
		return fromKB;		
	}
	
	public void printDictionary(){
		System.out.println("\n scene_node_dic");
		for(String key:scene_nodes_dic.keySet()){
			System.out.println(key +":\n " + scene_nodes_dic.get(key));
		}
			
	}
}
