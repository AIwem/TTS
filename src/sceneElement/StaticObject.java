package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class StaticObject extends SceneObject {

		
	public StaticObject(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.STATIC_OBJECT;
	}

	@Override
	public String toString() {
		return "[" + _node + "=  " + _name +				
				" object_states=  " + object_states + "]";
	}
	
	public void mergeWith(StaticObject statObj) {
		if(statObj == null)
			return;
		
		super.mergeWith(statObj);			
	}

}
