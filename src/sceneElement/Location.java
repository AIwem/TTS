package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;


public class Location extends SceneElement{
	
	public Location(String _name, Node _wsd) {
		super(_name, _wsd);
	}

	@Override
	public String toString() {
		return  "[" + _node + "=  " + _name + "]";
	}
}
