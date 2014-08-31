package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;

public class DynamicObject extends SceneObject{	
	
	ArrayList<ObjectAction> actions;
	
	public DynamicObject(String _name, Node _node) {
		super(_name, _node);
	}
}
