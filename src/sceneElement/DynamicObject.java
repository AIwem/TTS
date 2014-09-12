package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class DynamicObject extends SceneObject{	
	
	private ArrayList<ObjectAction> object_actions = new ArrayList<ObjectAction>();
	
	public DynamicObject(String _name, Node _node) {
		super(_name, _node);
		this.object_actions = new ArrayList<ObjectAction>();
	}

	public ArrayList<ObjectAction> getObject_actions() {
		return object_actions;
	}
	
	public ObjectAction getObject_action(Node action_node) {
		if(action_node == null)
			return null; 
		
		for(ObjectAction act:this.object_actions)
			if(act._node == action_node)
				return act;
		
		MyError.error(this + " DynamicObject has no such a " + action_node + " action.");
		return null;
	}
	
	public void addObejct_action(ObjectAction action) {
		if(this.object_actions == null)
			this.object_actions = new ArrayList<ObjectAction>();
		
		if(action != null)
			this.object_actions.add(action);
	}
	
	public void addAllObject_actions(ArrayList<ObjectAction> actions) {
		if(this.object_actions == null)
			this.object_actions = new ArrayList<ObjectAction>();
		this.object_actions.addAll(actions);
	}
	

	@Override
	public String toString() {
		return "[" + _node + "=  " + _name + 
				" object_actions=  " + object_actions + "]";
	}
}
