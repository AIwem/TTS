package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import model.SceneModel;
import model.ScenePart;



public class Emotion extends SceneElement{

	public Emotion(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.EMOTION, node);
	}

}
