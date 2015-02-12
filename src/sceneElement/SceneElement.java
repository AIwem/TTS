package sceneElement;



import model.ScenePart;
import model.SentencePart;
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
	
	public void addDependent(SentencePart dependent, String elementType){
		
		if(dependent == null){
			MyError.error("null input parameter for addDependent");
			return;
		}		
		
		if(this.scenePart == ScenePart.ROLE)
			 this.addRoleMoodToRole(dependent._name, dependent._wsd);
			
		else if(this.scenePart == ScenePart.DYNAMIC_OBJECT || this.scenePart == ScenePart.STATIC_OBJECT)
			this.addStateToSceneObject(dependent._name, dependent._wsd);

		else if(this.scenePart == ScenePart.LOCATION)
			print("what to do for adj of LOCATION ?!");//TODO

		else if(this.scenePart == ScenePart.SCENE_GOAL)
			print("what to do for adj of SCENE_GOAL ?!");//TODO
	
		else if(this.scenePart == ScenePart.TIME)
			print("what to do for adj of TIME ?!");//TODO
	
		else if(this.scenePart == ScenePart.SCENE_EMOTION)
			print("what to do for adj of SCENE_GOAL ?!");//TODO

		
	}
	
	public void addRoleActionToRole(String name, Node node){
		
		try{
			Role role = (Role)this;
			RoleAction roleAct = new RoleAction(name, node); 
			role.addRole_action(roleAct);			
		}
		catch(Exception e){
			print("" + e);
		}
	}
	
	public void addObjectActionToDynmicAction(String name, Node node){		
		try{
			DynamicObject dynObj = (DynamicObject)this;
			ObjectAction objAct = new ObjectAction(name, node); 
			dynObj.addObejct_action(objAct);			
		}
		catch(Exception e){
			print("" + e);
		}
	}
	
	public void addRoleMoodToRole(String name, Node node){		
		try{
			Role role = (Role)this;
			RoleMood rm = new RoleMood(name, node);
			role.addRole_mood(rm);
		}
		catch(Exception e){			
			print("" + e);		
		}
	}
	
	public void addStateToSceneObject(String name, Node node) {
		try{
			SceneObject sceObj = (SceneObject)this;			
			ObjectState objState = new ObjectState(name, node);	
			sceObj.setCurrent_state(objState);				
		}
		catch(Exception e){			
			print("" + e);		
		}	
	}

	@Override
	public String toString() {
		return  _node + "=  " + _name;
	}

	public boolean equals(SceneElement sceneElement) {
		if(this._node != null && sceneElement != null && this._node.equalsRelaxed(sceneElement._node))
			return true;
		return false;
	}
	
	/**
	 * this method merges the SceneElement which is called on with its parameter, element.
	 * in merging the SceneElement which is called on is prior to input parameter, element.
	 * It means that only when a parameter in prior SceneElement is null it is replaced with the element parameter value.
	 * @param element
	 */
	public void mergeWith(SceneElement element){
		if(element == null){
			MyError.error("can not merge null with SceneElement!");
			return;
		}
		
		if(this._name == null || this._name.equals(""))
			if(element._name != null && !element._name.equals(""))
				this._name = element._name;
		
		if(this._node == null && element._node != null)
			this._node = element._node;
										
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
			time.mergeWith((Time)element);
		}
	}
	
	private void print(String toPrint){
		print(toPrint);
		
	}

}
