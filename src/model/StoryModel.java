package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;



public class StoryModel {
	
	public String _name;
	
	private ArrayList<SceneModel> scenes = new ArrayList<SceneModel>();
	
	
	public StoryModel(String name){
		this._name = name;
		this.scenes = new ArrayList<SceneModel>();		
	}

	public ArrayList<SceneModel> getScenes() {
		return scenes;
	}
	
	public void addScene(SceneModel scene) {
		if(scene == null){
			MyError.error("the Scene of a stroy can not be null " + scene);
			return;
		}
		if(this.scenes == null)
			this.scenes = new ArrayList<SceneModel>();
		
		this.scenes.add(scene);
	}	
	
}
