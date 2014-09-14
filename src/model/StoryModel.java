package model;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;
import java.util.Hashtable;



public class StoryModel {
	
	public String _name;
	
	private ArrayList<SceneModel> scenes = new ArrayList<SceneModel>();
	

	/**
	 * this map holds the Node object seen yet in this StoryModel object. 
	 * It maps the Node pure name as key to the ArrayList<Node> of seen Nodes with this name in this SceneModel object.
	 * for each key, the first Node in ArrayList contains the pure Node object fetched from kb.
	 * the others are different instances of that pure Node.
	 */	
	private Hashtable<String, ArrayList<Node>> story_nodes = new Hashtable<String,ArrayList<Node>>();
	
	/**
	 * this map holds the ScenePart of Node object seen yet in this StoryModel object. 
	 * It maps the pure name of Node (as in kb) as key and the ScenePart of the Node with this name as value. 
	 * 
	 */	
	private Hashtable<String, ScenePart> story_sceneParts = new Hashtable<String, ScenePart>();
	
	
	
	public StoryModel(String name){
		this._name = name;
		this.scenes = new ArrayList<SceneModel>();	
		
		this.story_nodes = new Hashtable<String, ArrayList<Node>>();
		this.story_sceneParts = new Hashtable<String, ScenePart>();
		
	}

	public ArrayList<SceneModel> getScenes() {
		return scenes;
	}
	
	/**
	 * adding input scene to this storyModel.
	 *  it sets the storyModel parameter of input sceneModel too.
	 * @param scene the input scene to be added to this storyModel.
	 */
	public void addScene(SceneModel scene) {
		if(scene == null){
			MyError.error("the Scene of a stroy can not be null " + scene);
			return;
		}
		if(this.scenes == null)
			this.scenes = new ArrayList<SceneModel>();
		
		this.scenes.add(scene);	
	}	
	
	public boolean isSeenNode(String name){
		return story_nodes.containsKey(name);		
	}
	
	public ArrayList<Node> getSeenNode(String pure_name){
		return story_nodes.get(pure_name);
	}
	
	public boolean isSeenScenePart(String name){
		return story_sceneParts.containsKey(name);		
	}
	
	public ScenePart getSeenScenePart(String pure_name){
		return story_sceneParts.get(pure_name);
	}

	/**
	 * this method adds a row to story_nodes with pure_name as key and instances arrayList as values.
	 * 
	 * @param pure_name the name of original node fetched from FarsNet.
	 * @param instances the list of instances of this original node in this sceneModel. 
	 */
	public void addTo_story_nodes(String pure_name, ArrayList<Node> instances){
		if(pure_name == null || instances == null) 
			return;
		
		if(story_nodes.containsKey(pure_name))
			MyError.error("story_nodes_ previosly contains this pure name " + story_nodes.get(pure_name));
		
		story_nodes.put(pure_name, instances);
			
	}
	
	/**
	 * this method adds a row to the story_sceneParts with pure_name as key and the scenePart as value.
	 * this is done in order to preventing re-querying the kb to detect ScenePart of every instance of a original node in this sceneModel. 
	 * 
	 * @param pure_name the name of original node fetched from FarsNet.
	 * @param scenePart if scenePart equals to "ScenePart.UNKNOWN" nothing will be added to scene_parts.
	 */
	public void addTo_story_sceneParts(String pure_name, ScenePart scenePart) {
		if(pure_name == null || pure_name.equals("") || scenePart == null || scenePart == ScenePart.UNKNOWN)
			return;
		
		if(story_sceneParts.containsKey(pure_name))
			MyError.error("story_sceneParts previosly contained such a \"" + pure_name + "\" with the value: " + story_sceneParts.get(pure_name));
		story_sceneParts.put(pure_name, scenePart); 
	}
	
	public void printDictionary(){
		print("\n story_nodes");
		for(String key:story_nodes.keySet()){
			print(key +": " + story_nodes.get(key));
		}
			
		print("\n story_sceneParts");
		for(String key:story_sceneParts.keySet()){
			print(key +": " + story_sceneParts.get(key));
		}
		print("\n");
	}

	private void print(String toPrint) {
		System.out.println(toPrint);		
	}
}
