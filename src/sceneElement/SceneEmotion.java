package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class SceneEmotion  extends Emotion{

	public SceneEmotion(String name, Node node) {
		super(name, node);
		this.scenePart = ScenePart.SCENE_EMOTION;
	}
}
