package sceneElement;

import model.SceneModel;
import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;



public class SceneEmotion  extends Emotion{

	public SceneEmotion(SceneModel scene, String name, Node node) {
		super(scene, name, node);
		this.scenePart = ScenePart.SCENE_EMOTION;
	}
}
