package sceneElement;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class ObjectState extends SceneElement{
	
	public ObjectState(SceneModel scene, String _name, Node _node) {
		super(scene);
		this._name = _name;
		this._node = _node;		
	}
}
