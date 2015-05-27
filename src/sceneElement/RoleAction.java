package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleAction extends Action {
	
	private Role actor = null;

	public RoleAction(SceneModel scene, String name, Node node) {
		super(scene, name, node);		
	}

	public Role getActor() {
		return actor;
	}

	public void setActor(Role actor) {
		this.actor = actor;
	}

}
