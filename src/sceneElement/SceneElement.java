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
