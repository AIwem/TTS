package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;


public class Location extends SceneElement{
	
	public Location(String name, Node node) {
		super(name, ScenePart.LOCATION, node);
	}

	@Override
	public String toString() {
		return  "[" + _node + "=  " + _name + "]";
	}
	
	public void mergeWith(Location location){
		if(location == null)
			return;
		//TODO: check what else shall I do for this case!
		MyError.error("I don't know how to merge two locations! 1:" + this + " and 2:" + location);
	}
}
