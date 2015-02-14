package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class SceneGoal extends Goal {

	public SceneGoal(String name, Node node) {
		super(name, node);
		this.scenePart = ScenePart.SCENE_GOAL;
	}
}
