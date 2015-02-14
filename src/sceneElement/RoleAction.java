package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleAction extends Action {
	
	private Role actor = null;

	public RoleAction(String _name, Node _node) {
		super(_name, _node);		
	}

	public Role getActor() {
		return actor;
	}

	public void setActor(Role actor) {
		this.actor = actor;
	}

}
