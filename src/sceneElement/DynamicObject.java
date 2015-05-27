package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import model.SceneModel;
import model.ScenePart;

public class DynamicObject extends SceneObject{	
	
	private ArrayList<ObjectAction> object_actions = new ArrayList<ObjectAction>();
	
	public DynamicObject(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.DYNAMIC_OBJECT;
		this.object_actions = new ArrayList<ObjectAction>();
	}

	public ArrayList<ObjectAction> getObject_actions() {
		return object_actions;
	}
	
	public ObjectAction getObject_action(Node action_node) {
		if(action_node == null)
			return null; 
		
		for(ObjectAction act:this.object_actions)
			if(act._node.equalsRelaxed(action_node))
				return act;
		
		MyError.error(this + " DynamicObject has no such a " + action_node + " action.");
		return null;
	}
	
	public boolean hasObject_action(ObjectAction action){
		if(this.object_actions != null)
			for(ObjectAction act:object_actions)
				if(act.equals(action))
					return true;
		return false;
	}
	
	/**
	 * adds action as ObjectAction to this DynamicObject or 
	 * merges it with the existing equivalent ObjectAction of this DyanamicObject.
	 * 
	 * @param action
	 * @return
	 */
	public ObjectAction addObejct_action(ObjectAction action) {
		if(this.object_actions == null)
			this.object_actions = new ArrayList<ObjectAction>();
		
		if(action != null)
			if(!hasObject_action(action)){
				this.object_actions.add(action);
				System.out.println("ObjectAction " + action + " added to " + this._name);
				return action;
			}
			else{
				System.out.println(this._name + " DynamicObjec has had " + action + " before!");
				ObjectAction exist = getObject_action(action._node);
				if(exist != null)
					exist.mergeWith(action);
				return exist;
			}
		return null;
	}
	
	@Override
	public String toString() {
		return "[" + _node + "=  " + _name + 
				" object_actions=  " + object_actions +
				" object_states=  " + object_states + "]";
	}

	public void mergeWith(DynamicObject dynObj) {		
		if(dynObj == null)
			return;
		
		super.mergeWith(dynObj);
		
		for(ObjectAction act:dynObj.getObject_actions())
			this.addObejct_action(act);			
	}
	
}
