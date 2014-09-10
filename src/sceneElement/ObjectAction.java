package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

public class ObjectAction extends Action {

	private DynamicObject actor = null;
	
	public ObjectAction(String _name, Node _node) {
		super(_name, _node);		
	}

	public DynamicObject getActor() {
		return actor;
	}

	public void setActor(DynamicObject actor) {
		this.actor = actor;
	}

}
