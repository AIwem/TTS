package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class Action extends SceneElement{
	
	public Action(String _name, Node _node){
		super(_name, ScenePart.ACTION ,_node);
	}

}
