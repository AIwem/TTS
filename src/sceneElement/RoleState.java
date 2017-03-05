package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleState extends SceneElement {
	
	public RoleState(SceneModel scene, String name, Node node) {
		super(scene);
		this._name = name;
		this._node = node;		
	}
}
