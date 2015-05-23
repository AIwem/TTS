package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleMood extends SceneElement {
	
	public RoleMood(SceneModel scene, String name, Node node) {
		super(scene);
		this._name = name;
		this._node = node;		
	}
}
