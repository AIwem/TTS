package sceneElement;



import sceneReasoner.TTSEngine;
import model.SceneModel;
import model.ScenePart;
import model.Word;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

public class SceneElement {
	
	public SceneModel _scene;
	
	public String _name;
	
	public ScenePart scenePart;
	
	public Node _node;	
	
	
	public SceneElement(SceneModel scene){
		super();
		this._scene = scene;
	}
	
	public SceneElement(SceneModel scene, String name, ScenePart scenePart, Node node) {
		super();
		this._scene = scene;
		this._name = name;
		this.scenePart = scenePart;
		this._node = node;		
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
	
	public SceneElement addDependent(Word dependent, String elementType, TTSEngine ttsEngine){
		
		if(dependent == null || elementType == null){
			MyError.error("null input parameter for addDependent");
			return null;
		}		
		if(dependent._wsd == null)
			return null;
		
		switch(elementType)
		{
			case("adjective"):
			case("mozaf_elaih"):
			case("manner"):
			{		
				if(this.scenePart == ScenePart.ROLE)
					 return this.addRoleMoodToRole(dependent._wordName, dependent._wsd);
					
				else if(this.scenePart == ScenePart.DYNAMIC_OBJECT || this.scenePart == ScenePart.STATIC_OBJECT)
					return this.addStateToSceneObject(dependent._wordName, dependent._wsd);
		
				else if(this.scenePart == ScenePart.LOCATION){
					
					if(elementType.equals("mozaf_elaih") && ttsEngine != null){
					
						print("\nobject in Location for " + elementType + " of LOCATION. \n");
					
						boolean isDyn = ttsEngine.isWordDynamicObject(dependent);
						
						boolean isRole = ttsEngine.isWordRole(dependent);
						
						if(isDyn){
							DynamicObject dynObj = new DynamicObject(this._scene, dependent._wordName, dependent._wsd); 
							this._scene.addDynamic_object(dynObj);
							return dynObj;
						}
						else if(isRole){
							Role role = new Role(this._scene, dependent._wordName, dependent._wsd); 
							this._scene.addRole(role);
							return role;
						}
						else{
							StaticObject staObj = new StaticObject(this._scene, dependent._wordName, dependent._wsd);
							this._scene.addStatic_object(staObj);
							return staObj;
						}
					}
				}		
				else if(this.scenePart == ScenePart.SCENE_GOAL){
					print("what to do for " + elementType + " of SCENE_GOAL ?!");//TODO
					return null;
				}
				else if(this.scenePart == ScenePart.TIME){
					print("what to do for " + elementType + " of TIME ?!");//TODO
					return null;
				}
				else if(this.scenePart == ScenePart.SCENE_EMOTION){
					print("what to do for " + elementType + " of SCENE_EMOTION ?!");//TODO
					return null;
				}
				break;
			}
			case("action"):{
				if(this.scenePart == ScenePart.ROLE)
					return this.addRoleActionToRole(dependent._wordName, dependent._wsd);
				
				else if(this.scenePart == ScenePart.DYNAMIC_OBJECT)
					return this.addObjectActionToDynmicAction(dependent._wordName, dependent._wsd);
				break;
			}
			default:
				MyError.error("Unknown elementType: " + elementType);				
		}
		return null;
	}
	
	/**
	 * creates a RoleAction and calls addRole_action for this Role. 
	 *  
	 * @param name
	 * @param node
	 * @return
	 */
	private RoleAction addRoleActionToRole(String name, Node node){
		
		try{
			Role role = (Role)this;
			RoleAction roleAct = new RoleAction(this._scene, name, node); 
			return role.addRole_action(roleAct);			
		}
		catch(Exception e){
			print("" + e);
			return null;
		}
	}
	
	/**
	 * creates a ObjectAction and calls addObject_action for this ObjectAction.
	 * 
	 * @param name
	 * @param node
	 */
	
	private ObjectAction addObjectActionToDynmicAction(String name, Node node){		
		try{
			DynamicObject dynObj = (DynamicObject)this;
			ObjectAction objAct = new ObjectAction(this._scene, name, node); 
			return dynObj.addObejct_action(objAct);
		}
		catch(Exception e){
			print("" + e);
			return null;
		}
	}
	
	/**
	 * creates a RoleMood and calls addRole_mood for this Role.
	 * 
	 * @param name
	 * @param node
	 * @return
	 */
	private RoleMood addRoleMoodToRole(String name, Node node){		
		try{
			Role role = (Role)this;
			RoleMood rm = new RoleMood(this._scene, name, node);
			return role.addRole_mood(rm);
		}
		catch(Exception e){			
			print("" + e);
			return null;
		}
	}
	
	/**
	 * creates a SceneObject and calls setCurrent_state for this SceneObject. 
	 * 
	 * @param name
	 * @param node
	 * @return
	 */
	private ObjectState addStateToSceneObject(String name, Node node) {
		try{
			SceneObject sceObj = (SceneObject)this;			
			ObjectState objState = new ObjectState(this._scene, name, node);	
			return sceObj.setCurrent_state(objState);				
		}
		catch(Exception e){			
			print("" + e);
			return null;
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
		System.out.println(toPrint);
		
	}

}
