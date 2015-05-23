package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class Action extends SceneElement{
	
	public Action(SceneModel scene, String name, Node node){
		super(scene, name, ScenePart.ACTION ,node);
	}

}
