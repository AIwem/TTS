package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;


public class RoleGoal extends Goal {

	public RoleGoal(String name, Node node) {
		super(name, node);
		this.scenePart = ScenePart.ROLE_GOAL;
	}

}
