package sceneElement;

import java.util.ArrayList;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;

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
	
	
	public ObjectState getObject_state(Node node){
		if(node == null)
			return null;
		
		if(!Common.isEmpty(object_states))
			for(ObjectState objStat:object_states)
				if(objStat._node.equalsRelaxed(node))
					return objStat;
		return null;	
	}
		
	public boolean hasObject_state(ObjectState objState){
		if(objState != null && object_states.contains(objState))
			return true;
		return false;
	}

	/**
	 * sets the current_state of this SceneObject and 
	 * adds this current_state to object_states of this SceneObject via addObjectState. 
	 * 
	 * @param current_state
	 * @return
	 */
	public ObjectState setCurrent_state(ObjectState current_state) {
		if(object_states != null){
//			TODO: temporarily ignore the object_states of a SceneObject for setting current_state!
//			if(object_states.contains(current_state)){				
				this.current_state = current_state;
				System.out.println("current ObjectState of " + this._name + " set to " + current_state);
				return addObject_state(current_state);
				
		}
//			}
//			else
//				MyError.error(current_state + " is not in the object_states of " + this._name);
		return null;
	}	
	
	/**
	 * adds state as ObjectState to this SceneObject or 
	 * merges it with the existing equivalent ObjectState of this SceneObject.
	 * 
	 * @param state
	 * @return
	 */
	public ObjectState addObject_state(ObjectState state){
		if(this.object_states == null)
			this.object_states = new ArrayList<ObjectState>();
		
		if(state != null)
			if(!hasObject_state(state)){
				this.object_states.add(state);
				System.out.println("ObjectState " + state + " added to " + this._name);
				return state;
			}
			else{
				System.out.println(this._name + " Objec has had " + state + " before!");
				ObjectState exist = getObject_state(state._node);
				if(exist != null)
					exist.mergeWith(state);
				return exist;
			}
		return null;
	}
	
	public void mergeWith(SceneObject scenObj) {		
		if(scenObj == null)
			return;
		
		if(this._name == null || this._name.equals(""))
			if(scenObj._name != null && !scenObj._name.equals(""))
				this._name = scenObj._name;
		
		if(this._node == null && scenObj._node != null)
			this._node = scenObj._node;
		
		for(ObjectState stat:scenObj.object_states)
			this.addObject_state(stat);
		
		if(this.current_state == null && scenObj.current_state != null)
			this.current_state = scenObj.current_state;
	}
	
	
	
}
