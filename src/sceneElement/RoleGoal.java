package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class RoleGoal extends Goal {

	public RoleGoal(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.ROLE_INTENT;
	}

}
