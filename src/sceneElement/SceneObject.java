package sceneElement;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;


public class SceneObject extends SceneElement{
	
	private ArrayList<ObjectState> object_states = new ArrayList<ObjectState>();
	
	private ObjectState current_state = null;
	
	public SceneObject() {
		super();
	}

	public SceneObject(String _name, Node _node) {
		super(_name, _node);
	}

	public ObjectState getCurrent_state() {
		return current_state;
	}
	
	public boolean hasThisState(ObjectState objState){
		if(objState != null && object_states.contains(objState))
			return true;
		return false;
	}

	public void setCurrent_state(ObjectState current_state) {
		if(object_states != null && object_states.contains(current_state))
			this.current_state = current_state;
		else
			MyError.error(current_state + " is not in the object_states of " + this);
	}
	
	
	
}
