package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class Goal  extends SceneElement{

	public Goal(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.GOAL, node);		
	}
}
