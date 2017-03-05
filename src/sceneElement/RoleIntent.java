package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class RoleIntent extends Goal {

	public RoleIntent(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.ROLE_INTENT;
	}

}
