package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class SceneGoal extends Goal {

	public SceneGoal(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.SCENE_GOAL;
	}
}
