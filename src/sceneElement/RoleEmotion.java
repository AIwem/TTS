package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class RoleEmotion extends Emotion {

	public RoleEmotion(String name, Node node) {
		super(name, node);
		this.scenePart = ScenePart.ROLE_EMOTION;
	}

}
