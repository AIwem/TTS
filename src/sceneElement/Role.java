/**
 * 
 */
package sceneElement;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;

import java.util.ArrayList;

import model.SceneModel;
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

	public Role(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE, node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_goals = new ArrayList<RoleGoal>();
		this.role_emotions = new ArrayList<RoleEmotion>();
	}
	
	private RoleAction getRole_action(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_actions))
			for(RoleAction ra:role_actions)
				if(ra._node.equalsRelaxed(node))
					return ra;
		return null;
	}
		
	public ArrayList<RoleAction> getRole_actions() {
		return role_actions;
	}
	
	private RoleEmotion getRole_emotion(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_emotions))
			for(RoleEmotion re:role_emotions)
				if(re._node.equalsRelaxed(node))
					return re;
		return null;
	}
	
	public ArrayList<RoleEmotion> getRole_emotions() {
		return role_emotions;
	}

	private RoleGoal getRole_goal(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_goals))
			for(RoleGoal rg:role_goals)
				if(rg._node.equalsRelaxed(node))
					return rg;
		return null;
	}
	
	public ArrayList<RoleGoal> getRole_goals() {		
		return role_goals;
	}
	
	private RoleMood getRole_mood(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_moods))
			for(RoleMood rm:role_moods)
				if(rm._node.equalsRelaxed(node))
					return rm;
		return null;
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
	
	/**
	 * adds role_action as RoleActione to this Role or 
	 * merges it with the existing equivalent RoleAction of this Role.
	 * 
	 * @param role_action
	 * @return
	 */
	public RoleAction addRole_action(RoleAction role_action) {
		if(role_action != null){
			if(this.role_actions == null)
				this.role_actions = new ArrayList<RoleAction>();
		
			if(!hasRole_action(role_action)){
				this.role_actions.add(role_action);
				System.out.println("RoleAction " + role_action + " added to " + this._name);
				return role_action;
			}
			else{
				System.out.println(this._name + " role has had " + role_action + " before!");
				RoleAction exist = getRole_action(role_action._node);
				if(exist != null)
					exist.mergeWith(role_action);
				return exist;
			}
		}
		return null;
	}

	/**
	 * adds role_emotion as RoleEmotion to this Role or 
	 * merges it with the existing equivalent RoleEmotion of this Role.
	 * 
	 * @param role_emotion
	 * @return
	 */
	public RoleEmotion addRole_emotion(RoleEmotion role_emotion) {
		if(role_emotion != null){
			if(this.role_emotions == null)
				this.role_emotions = new ArrayList<RoleEmotion>();
		
			if(!hasRole_emotion(role_emotion)){
				this.role_emotions.add(role_emotion);
				System.out.println("RoleEmotion " + role_emotion + " added to " + this._name);
				return role_emotion;
			}
			else{
				System.out.println(this._name + " role has had " + role_emotion + " before!");
				RoleEmotion exist = getRole_emotion(role_emotion._node);
				if(exist != null)
					exist.mergeWith(role_emotion);
				return exist;
			}
		}
		return null;
	}
	
	/**
	 * adds role_goal as RoleGoal to this Role or 
	 * merges it with the existing equivalent RoleGoal of this Role.
	 * 
	 * @param role_goal
	 * @return
	 */
	public RoleGoal addRole_goal(RoleGoal role_goal) {
		if(role_goal != null){
			if(this.role_goals == null)
				this.role_goals = new ArrayList<RoleGoal>();
			
			if(!hasRole_goals(role_goal)){
				this.role_goals.add(role_goal);
				System.out.println("RoleGoal " + role_goal + " added to " + this._name);
				return role_goal;
			}
			else{
				System.out.println(this._name + " role has had " + role_goal + " before!");
				RoleGoal exist = getRole_goal(role_goal._node);
				if(exist != null)
					exist.mergeWith(role_goal);
				return exist;
			}
		}
		return null;
	}

	/**
	 * adds role_mood as RoleMood to this Role or 
	 * merges it with the existing equivalent RoleMood of this Role.
	 * 
	 * @param role_mood to be added to this Role.
	 * @return the added RoleMood.
	 */
	public RoleMood addRole_mood(RoleMood role_mood){
		if(role_mood != null){
			if(this.role_moods == null)
				this.role_moods = new ArrayList<RoleMood>();
			
			if(!hasRole_mood(role_mood)){
				this.role_moods.add(role_mood);
				System.out.println("RoleMood " + role_mood + " added to " + this._name);
				return role_mood;
			}
			else{
				System.out.println(this._name + " role has had " + role_mood + " before!");
				RoleMood exist = getRole_mood(role_mood._node);
				if(exist != null)
					exist.mergeWith(role_mood);
				return exist;
			}
		}
		return null;			
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
