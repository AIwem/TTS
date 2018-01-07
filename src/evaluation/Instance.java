package evaluation;

import model.ScenePart;


public class Instance {
	
	public String record;
	
	public ScenePart trueSceneElement;
	
	public ScenePart classifiedSceneElement;
	
	public Instance(String record, String trueSceneElement){
		this.record = record;
		this.trueSceneElement = ScenePart.valueOf(trueSceneElement); 
	}
	
	public Instance(String record, String trueSceneElement, String classifiedSceneElement){
		this.record = record;
		this.trueSceneElement = ScenePart.valueOf(trueSceneElement.toUpperCase()); 
		setClassifiedSceneElement(classifiedSceneElement);
	}
	
	public void setClassifiedSceneElement(String classifedSceneElement){
		this.classifiedSceneElement = ScenePart.valueOf(classifedSceneElement.toUpperCase());
	}

	public String toString(){
		return this.record;
	}
}
