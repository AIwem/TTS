package sceneElement;

import java.util.ArrayList;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;


public class SceneObject extends SceneElement{
	
	protected ArrayList<ObjectState> object_states = new ArrayList<ObjectState>();
	
	protected ObjectState current_state = null;
	
	public SceneObject() {
		super();
	}

	public SceneObject(String name, Node node) {
		super(name, ScenePart.SCENE_OBJECT, node);
	}

	public ObjectState getCurrent_state() {
		return current_state;
	}
	
	public boolean hasThisState(ObjectState objState){
		if(objState != null && object_states.contains(objState))
			return true;
		return false;
	}

	public boolean setCurrent_state(ObjectState current_state) {
		if(object_states != null && object_states.contains(current_state)){
			this.current_state = current_state;
			return true;
		}
		else
			MyError.error(current_state + " is not in the object_states of " + this);
		return false;
	}
	
	
	
}
