package sceneElement;

import java.util.ArrayList;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;

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
	
	public boolean hasObject_state(ObjectState objState){
		if(objState != null && object_states.contains(objState))
			return true;
		return false;
	}

	public boolean setCurrent_state(ObjectState current_state) {
		if(object_states != null){
//			TODO: temporarily ignore the object_states of a SceneObject for setting current_state!
//			if(object_states.contains(current_state)){				
				this.current_state = current_state;
				System.out.println("current ObjectState of " + this._name + " set to " + current_state);
				addObject_state(current_state);
				return true;
		}
//			}
//			else
//				MyError.error(current_state + " is not in the object_states of " + this._name);
		return false;
	}	
	
	public boolean addObject_state(ObjectState state){
		if(this.object_states == null)
			this.object_states = new ArrayList<ObjectState>();
		
		if(state != null)
			if(!hasObject_state(state)){
				this.object_states.add(state);
				System.out.println("ObjectState " + state + " added to " + this._name);
				return true;
			}
			else
				System.out.println(this._name + " Objec has had " + state + " before!");
		return false;
	}
	
	public void mergeWith(SceneObject scenObj) {
		
		if(scenObj == null)
			return;
		
		for(ObjectState stat:scenObj.object_states)
			this.addObject_state(stat);
		
		if(this.current_state == null && scenObj.current_state != null)
			this.current_state = scenObj.current_state;
	}
	
	
	
}
