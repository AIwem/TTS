/**
 * 
 */
package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;


/**
 * @author hashemi
 *
 */
public class Role extends SceneElement{	
	
	public Role(String _name, Node _node) {
		super(_name, _node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_goals = new ArrayList<RoleGoal>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
	private ArrayList<RoleAction> role_actions = new ArrayList<RoleAction>();
	
	private ArrayList<RoleGoal> role_goals = new ArrayList<RoleGoal>();

	private ArrayList<RoleEmotion> role_emotions = new ArrayList<RoleEmotion>();

	public ArrayList<RoleAction> getRole_actions() {
		return role_actions;
	}

	public void addRole_action(RoleAction role_action) {
		this.role_actions.add(role_action);
	}

	public ArrayList<RoleGoal> getRole_goals() {
		return role_goals;
	}

	public void addRole_goal(RoleGoal role_goal) {
		this.role_goals.add(role_goal);
	}

	public ArrayList<RoleEmotion> getRole_emotions() {
		return role_emotions;
	}

	public void addRole_emotion(RoleEmotion role_emotion) {
		this.role_emotions.add(role_emotion);
	}
	
	@Override
	public String toString() {
		return  "[" + _node + "= " + _name + 
				" role_actions= " + role_actions + 
				" role_goals= " + role_goals + 
				" role_emotions= " + role_emotions + "]";
	}

}
