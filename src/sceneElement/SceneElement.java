package sceneElement;



import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

public class SceneElement {

	public String _name;
	
	public ScenePart scenePart;
	
	public Node _node;	
	
	
	public SceneElement(){
		super();
	}
	
	public SceneElement(String _name, ScenePart scenePart, Node _node) {
		super();
		this._name = _name;
		this.scenePart = scenePart;
		this._node = _node;		
	}

	public void setName(String _name) {
		this._name = _name;
	}
	
	public void setNode(Node _node) {
		this._node = _node;
	}
	
	public String getName() {
		return _name;
	}
	
	public Node getNode() {
		return _node;
	}
	
	public void addRoleActionToRole(String name, Node node){
		
		try{
			Role role = (Role)this;
			RoleAction roleAct = new RoleAction(name, node); 
			if(role.addRole_action(roleAct))
				System.out.println("RoleAction " + roleAct + " action added to the " + role);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void addObjectActionToDynmicAction(String name, Node node){		
		try{
			DynamicObject dynObj = (DynamicObject)this;
			ObjectAction objAct = new ObjectAction(name, node); 
			if(dynObj.addObejct_action(objAct))
				System.out.println("RoleAction " + objAct + " action added to the " + dynObj);
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public void addRoleMoodToRole(String name, Node node){		
		try{
			Role role = (Role)this;
			RoleMood rm = new RoleMood(name, node);
			if(role.addRole_mood(rm))
				 System.out.println("RoleMood " + rm + " added to " + role._name);		 
			 
		}
		catch(Exception e){			
			System.out.println(e);		
		}
	}
	
	public void addStateToDynamicObject(String name, Node node) {
		try{
			DynamicObject dynobj = (DynamicObject)this;			
			ObjectState objState = new ObjectState(name, node);	
			if(dynobj.setCurrent_state(objState))
				System.out.println("ObjectState " + objState + " added to " + dynobj._name);
		}
		catch(Exception e){			
			System.out.println(e);		
		}	
	}

	public void addStateToStaticObject(String name, Node node) {
		try{
			StaticObject staobj = (StaticObject)this;			
			ObjectState objState = new ObjectState(name, node);	
			if(staobj.setCurrent_state(objState))
				System.out.println("ObjectState " + objState + " added to " + staobj._name);
		}
		catch(Exception e){			
			System.out.println(e);		
		}
		
	}
	
	@Override
	public String toString() {
		return  _node + "=  " + _name;
	}

	public boolean equals(SceneElement sceneElement) {
		if(this._node != null && sceneElement != null && this._node == sceneElement._node)
			return true;
		return false;
	}
	
	public void mergeWith(SceneElement element){
		if(element == null){
			MyError.error("can not merge null with SceneElement!");
			return;
		}
										
		if(this.scenePart == ScenePart.ROLE && element.scenePart == ScenePart.ROLE){
			Role role = (Role) this;
			role.mergeWith((Role)element);	
		}
		else if(this.scenePart == ScenePart.DYNAMIC_OBJECT && element.scenePart == ScenePart.DYNAMIC_OBJECT){			
			DynamicObject dynObj = (DynamicObject) this;
			dynObj.mergeWith((DynamicObject)element);			
		}
		else if(this.scenePart == ScenePart.STATIC_OBJECT && element.scenePart == ScenePart.STATIC_OBJECT){
			StaticObject staObj = (StaticObject)this;
			staObj.mergeWith((StaticObject)element);				
		}
		else if(this.scenePart == ScenePart.LOCATION && element.scenePart == ScenePart.LOCATION){
			Location location = (Location) this;
			location.mergeWith((Location)location);			
		}
		else if(this.scenePart == ScenePart.TIME && element.scenePart == ScenePart.TIME){
			Time time = (Time)this;
			time.mergWith((Time)element);
		}
	}
}
