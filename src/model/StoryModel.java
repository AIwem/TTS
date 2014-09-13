package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;



public class StoryModel {
	
	public String _name;
	
	private ArrayList<SceneModel> scenes = new ArrayList<SceneModel>();
	
	public StoryModel(String name){
		this._name = name;
		scenes = new ArrayList<SceneModel>();
	}

	public ArrayList<SceneModel> getScenes() {
		return scenes;
	}

	public void setScenes(ArrayList<SceneModel> scenes) {
		if(scenes == null || scenes.size() == 0){
			MyError.error("the scenes of a stroy can not be null " + scenes);
			return;
		}
		if(this.scenes == null)
			this.scenes = new ArrayList<SceneModel>();
		
		this.scenes = scenes;
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
