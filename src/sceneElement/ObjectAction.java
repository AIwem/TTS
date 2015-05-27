package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class ObjectAction extends Action {

	private DynamicObject actor = null;
	
	public ObjectAction(SceneModel scene, String name, Node node) {
		super(scene, name, node);		
	}

	public DynamicObject getActor() {
		return actor;
	}

	public void setActor(DynamicObject actor) {
		this.actor = actor;
	}

}
