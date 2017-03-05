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
	
	private ArrayList<RoleIntent> role_intents = new ArrayList<RoleIntent>();

	private ArrayList<RoleEmotion> role_emotions = new ArrayList<RoleEmotion>();
	
	private ArrayList<RoleState> role_states = new ArrayList<RoleState>();

	public Role(SceneModel scene, String name, Node node) {
		super(scene, name, ScenePart.ROLE, node);
		this.role_actions = new ArrayList<RoleAction>();
		this.role_intents = new ArrayList<RoleIntent>();
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

	private RoleIntent getRole_intent(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_intents))
			for(RoleIntent rg:role_intents)
				if(rg._node.equalsRelaxed(node))
					return rg;
		return null;
	}
	
	public ArrayList<RoleIntent> getRole_intents() {		
		return role_intents;
	}
	
	private RoleState getRole_state(Node node) {
		if(node == null)
			return null;
		
		if(!Common.isEmpty(role_states))
			for(RoleState rm:role_states)
				if(rm._node.equalsRelaxed(node))
					return rm;
		return null;
	}

	
	public ArrayList<RoleState> getRole_states(){
		return this.role_states;
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
	
	public boolean hasRole_intents(RoleIntent role_intent){
		if(this.role_intents != null)
			for(RoleIntent rg:role_intents)
				if(rg.equals(role_intent))
					return true;
		return false;
	}	
	
	public boolean hasRole_state(RoleState role_mode){
		if(this.role_states != null)
			for(RoleState rm:role_states)
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
	 * @param role_intent
	 * @return
	 */
	public RoleIntent addRole_intent(RoleIntent role_intent) {
		if(role_intent != null){
			if(this.role_intents == null)
				this.role_intents = new ArrayList<RoleIntent>();
			
			if(!hasRole_intents(role_intent)){
				this.role_intents.add(role_intent);
				System.out.println("RoleGoal " + role_intent + " added to " + this._name);
				return role_intent;
			}
			else{
				System.out.println(this._name + " role has had " + role_intent + " before!");
				RoleIntent exist = getRole_intent(role_intent._node);
				if(exist != null)
					exist.mergeWith(role_intent);
				return exist;
			}
		}
		return null;
	}

	/**
	 * adds role_mood as RoleMood to this Role or 
	 * merges it with the existing equivalent RoleMood of this Role.
	 * 
	 * @param role_state to be added to this Role.
	 * @return the added RoleMood.
	 */
	public RoleState addRole_state(RoleState role_state){
		if(role_state != null){
			if(this.role_states == null)
				this.role_states = new ArrayList<RoleState>();
			
			if(!hasRole_state(role_state)){
				this.role_states.add(role_state);
				System.out.println("RoleMood " + role_state + " added to " + this._name);
				return role_state;
			}
			else{
				System.out.println(this._name + " role has had " + role_state + " before!");
				RoleState exist = getRole_state(role_state._node);
				if(exist != null)
					exist.mergeWith(role_state);
				return exist;
			}
		}
		return null;			
	}
	
	

	@Override
	public String toString() {
		return  "[" + _node + "= " + _name + 
				" role_actions=  " + role_actions + 
				" role_goals=    " + role_intents + 
				" role_emotions= " + role_emotions + 
				" role_moods=    " + role_states + "]";
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
				
		for(RoleIntent rg:role.getRole_intents())
			this.addRole_intent(rg);

		for(RoleEmotion re:role.getRole_emotions())
			this.addRole_emotion(re);	
		
		for(RoleState rm:role.getRole_states())
			this.addRole_state(rm);
	}

}
