package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class Goal  extends SceneElement{

	public Goal(String name, Node node) {
		super(name, ScenePart.GOAL, node);		
	}
}
