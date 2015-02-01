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
		
	private ArrayList<RoleAction> role_actions = new ArrayList<RoleAction>();
	
	private ArrayList<RoleGoal> role_goals = new ArrayList<RoleGoal>();

	private ArrayList<RoleEmotion> role_emotions = new ArrayList<RoleEmotion>();
	
	private ArrayList<RoleMood> role_moods = new ArrayList<RoleMood>();

	public Role(String _name, Node _node) {
		super(_name, _node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_goals = new ArrayList<RoleGoal>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
		
	public ArrayList<RoleAction> getRole_actions() {
		return role_actions;
	}

	public void addRole_action(RoleAction role_action) {
		if(role_action != null)
			this.role_actions.add(role_action);
	}

	public ArrayList<RoleGoal> getRole_goals() {		
		return role_goals;
	}

	public void addRole_goal(RoleGoal role_goal) {
		if(role_goal != null)
			this.role_goals.add(role_goal);
	}

	public ArrayList<RoleEmotion> getRole_emotions() {
		return role_emotions;
	}

	public void addRole_emotion(RoleEmotion role_emotion) {
		if(role_emotion != null)
			this.role_emotions.add(role_emotion);
	}
	
	public void addRole_mood(RoleMood role_mode){
		if(role_mode != null)
			this.role_moods.add(role_mode);
		
	}
	
	public ArrayList<RoleMood> getRole_moods(){
		return this.role_moods;
	}
	@Override
	public String toString() {
		return  "[" + _node + "= " + _name + 
				" role_actions=  " + role_actions + 
				" role_goals=    " + role_goals + 
				" role_emotions= " + role_emotions + 
				" role_moods=    " + role_moods + "]";
	}
	
	/**
	 * this method merges its caller with role.
	 * 
	 * @param role the role to be merged with its caller.
	 */
	public void mergeWith(Role role){
		if(role == null)
			return;
	
		for(RoleAction ra:role.getRole_actions())
			this.addRole_action(ra);
				
		for(RoleGoal rg:role.getRole_goals())
			this.addRole_goal(rg);

		for(RoleEmotion re:role.getRole_emotions())
			this.addRole_emotion(re);	
		
		for(RoleMood rm:role.getRole_moods())
			this.addRole_mood(rm);
	}

}
