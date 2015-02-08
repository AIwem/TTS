/**
 * 
 */
package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;

import model.ScenePart;


/**
 * @author hashemi
 *
 */
public class Role extends SceneElement{	
		
	private ArrayList<RoleAction> role_actions = new ArrayList<RoleAction>();
	
	private ArrayList<RoleGoal> role_goals = new ArrayList<RoleGoal>();

	private ArrayList<RoleEmotion> role_emotions = new ArrayList<RoleEmotion>();
	
	private ArrayList<RoleMood> role_moods = new ArrayList<RoleMood>();

	public Role(String name, Node node) {
		super(name, ScenePart.ROLE, node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_goals = new ArrayList<RoleGoal>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
		
	public ArrayList<RoleAction> getRole_actions() {
		return role_actions;
	}
	
	public ArrayList<RoleEmotion> getRole_emotions() {
		return role_emotions;
	}

	public ArrayList<RoleGoal> getRole_goals() {		
		return role_goals;
	}
	
	public ArrayList<RoleMood> getRole_moods(){
		return this.role_moods;
	}
		
	public boolean hasRole_action(RoleAction role_action){
		if(this.role_actions != null)
			for(RoleAction ra:role_actions)
				if(ra.equals(role_action))
					return true;
		return false;
	}
	
	public boolean hasRole_emotion(RoleEmotion role_emotion){
		if(this.role_emotions != null)
			for(RoleEmotion re:role_emotions)
				if(re.equals(role_emotion))
					return true;
		return false;
	}
	
	public boolean hasRole_goals(RoleGoal role_goal){
		if(this.role_goals != null)
			for(RoleGoal rg:role_goals)
				if(rg.equals(role_goal))
					return true;
		return false;
	}	
	
	public boolean hasRole_mood(RoleMood role_mode){
		if(this.role_moods != null)
			for(RoleMood rm:role_moods)
				if(rm.equals(role_mode))
					return true;
		return false;
	}
	
	public boolean addRole_action(RoleAction role_action) {
		if(role_action != null){
			if(this.role_actions == null)
				this.role_actions = new ArrayList<RoleAction>();
		
			if(!hasRole_action(role_action)){
				this.role_actions.add(role_action);
				System.out.println("RoleAction " + role_action + " added to " + this._name);
				return true;
			}
			else
				System.out.println(this._name + " role has had " + role_action + " before!");
		}
		return false;
	}

	public boolean addRole_emotion(RoleEmotion role_emotion) {
		if(role_emotion != null){
			if(this.role_emotions == null)
				this.role_emotions = new ArrayList<RoleEmotion>();
		
			if(!hasRole_emotion(role_emotion)){
				this.role_emotions.add(role_emotion);
				System.out.println("RoleEmotion " + role_emotion + " added to " + this._name);
				return true;
			}
			else
				System.out.println(this._name + " role has had " + role_emotion + " before!");
		}
		return false;
	}
	
	public boolean addRole_goal(RoleGoal role_goal) {
		if(role_goal != null){
			if(this.role_goals == null)
				this.role_goals = new ArrayList<RoleGoal>();
			
			if(!hasRole_goals(role_goal)){
				this.role_goals.add(role_goal);
				System.out.println("RoleGoal " + role_goal + " added to " + this._name);
				return true;
			}
			else
				System.out.println(this._name + " role has had " + role_goal + " before!");
		}
		return false;
	}

	public boolean addRole_mood(RoleMood role_mode){
		if(role_mode != null){
			if(this.role_moods == null)
				this.role_moods = new ArrayList<RoleMood>();
			
			if(!hasRole_mood(role_mode)){
				this.role_moods.add(role_mode);
				System.out.println("RoleMood " + role_mode + " added to " + this._name);
				return true;
			}
			else
				System.out.println(this._name + " role has had " + role_mode + " before!");
		}
		return false;			
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
