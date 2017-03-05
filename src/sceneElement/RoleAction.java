package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleAction extends Action {
	
	private Role actor = null;
	
	private RoleEmotion emotion_in_action = null;
	
	private RoleState mood_in_action = null;

	public RoleAction(SceneModel scene, String name, Node node) {
		super(scene, name, node);		
	}
	
	public void setActor(Role actor) {
		this.actor = actor;
	}
	
	public void setEmotion_in_action(RoleEmotion emotion_in_action) {
		this.emotion_in_action = emotion_in_action;
	}
	
	public void setMood_in_action(RoleState mood_in_action) {
		this.mood_in_action = mood_in_action;
	}
	
	public Role getActor() {
		return actor;
	}
	
	public RoleEmotion getEmotion_in_action() {
		return emotion_in_action;
	}

	public RoleState getMood_in_action() {
		return mood_in_action;
	}
}
