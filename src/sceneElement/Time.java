package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

public class Time  extends SceneElement{

	public Time(String _name, Node _wsd) {
		 super(_name, _wsd);
	}

	@Override
	public String toString() {
		return  "[" + _node + "=  " + _name + "]";
	}
	
	

}
