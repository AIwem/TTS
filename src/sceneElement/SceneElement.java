package sceneElement;



import ir.ac.itrc.qqa.semantic.kb.Node;

public class SceneElement {

	public Node _node;
	
	public String _name;
	
	public SceneElement(){
		super();
	}
	
	public SceneElement(String _name, Node _node) {
		super();
		this._name = _name;
		this._node = _node;		
	}

		
	public Node getNode() {
		return _node;
	}

	public void setNode(Node _node) {
		this._node = _node;
	}

	public String getName() {
		return _name;
	}

	public void setName(String _name) {
		this._name = _name;
	}

	
	@Override
	public String toString() {
		return  _node + "=  " + _name;
	}

}
