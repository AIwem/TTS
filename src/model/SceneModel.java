package model;

import java.util.ArrayList;
import java.util.Locale;

import sceneElement.DynamicObject;
import sceneElement.Goal;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.SceneEmotion;
import sceneElement.StaticObject;
import sceneElement.Time;

/**
 * 
 * @author hashemi
 *
 */
public class SceneModel {
	
	private StoryModel story;
	
	private ArrayList<SentenceModel> sentences;
	
	private ArrayList<Role> roles;
	
	private ArrayList<StaticObject> static_obj;
	
	private ArrayList<DynamicObject> dynamic_obj;
	
	private Location location;
	
	private Time time;
	
	private ArrayList<Goal> scene_goal;
		
	private ArrayList<SceneEmotion> emotions;

}
