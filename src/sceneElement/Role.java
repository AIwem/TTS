/**
 * 
 */
package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;

import model.Part;

/**
 * @author hashemi
 *
 */
public class Role extends SceneElement{	
	
	public Role(String _name, Node _node) {
		super(_name, _node);		
	}

	private ArrayList<RoleAction> role_actions;
	
	private ArrayList<RoleGoal> role_goals;

	private ArrayList<RoleEmotion> role_emotions;

}
