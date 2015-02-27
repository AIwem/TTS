package sceneElement;

import model.ScenePart;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

public class Time  extends SceneElement{

	public Time(String name, Node node) {
		 super(name, ScenePart.TIME, node);
	}

	@Override
	public String toString() {
		return  "[" + _node + "=  " + _name + "]";
	}	
	
	public void mergeWith(Time time){
		if(time == null)
			return;
		//TODO: check what else shall I do for this case!
		MyError.error("I don't know how to merge two times! 1:" + this + " and 2:" + time);
	}

}
