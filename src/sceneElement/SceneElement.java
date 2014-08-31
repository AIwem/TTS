package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

public class SceneElement {

	Node _node;
	
	String _name;
	
	public SceneElement(){
		super();
	}	
	
	public SceneElement(String _name, Node _node) {
		super();
		this._name = _name;
		this._node = _node;		
	}

}
