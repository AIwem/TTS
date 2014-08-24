package model;

import java.util.ArrayList;

/**
 * 
 * This class contains information of an input sentence.
 * It has different parts of speech of the sentence and the original sentence in natural language.
 * 
 * @author hashemi
 *
 */
public class SentenceInfo {
		
	/**
	 * the scene which this sentence belongs to. 
	 */
	private SceneModel scene;
	
	/**
	 * original sentence in "natural language". 
	 */
	private String originalSentence;
	
	/**
	 * subject(s) of this sentence.
	 */
	private ArrayList<Part> subjects;
	
	/**
	 * The only verb of this sentence.
	 */
	private Part verb;
	
	/**
	 * object(s) of this sentence, if any.
	 */
	private ArrayList<Part> objects;
	
	/**
	 * adverb(s) of this sentence, if any.
	 */
	private ArrayList<Part> adverbs;
	
	public SentenceInfo(String originalSentence){
		this.originalSentence = originalSentence;			
	}
	
	public SentenceInfo(String originalSentence, SceneModel sceneModel){
		this.originalSentence = originalSentence;
		this.scene = sceneModel;		
	}
	
	public SceneModel getScene() {
		return scene;
	}

	public void setScene(SceneModel scene) {
		this.scene = scene;
	}
		
	public ArrayList<Part> getSubjects() {
		return subjects;
	}
	
	public ArrayList<String> getSubjectsString() {
		ArrayList<String> sbjStr = new ArrayList<String>(); 
		for(Part sbj:subjects)
			sbjStr.add(sbj._name);			
		return sbjStr;
	}
	
	public void setSubjects(ArrayList<Part> subjects) {
		this.subjects = subjects;
	}
	
	/**
	 * The setter method which converts each string argument to a Part object 
	 * with _srl "SBJ" and _name argument content.  
	 * @param subjects list of subjects of this sentence.
	 */
	public void setSubjects(String... subjects) {
		this.subjects = new ArrayList<Part>();
		for(String sbj:subjects)
			if(sbj!= null)
				this.subjects.add(new Part(sbj,SRL.SBJ));			
				
	}
	
	public Part getVerb() {
		return verb;
	}
	
	public String getVerbString() {
		return verb._name;
	}

	public void setVerb(Part verb) {
		this.verb = verb;
	}
	
	/** The setter method which converts string argument to a Part object
	 * with _srl "VERB" and _name argument content.
	 * @param verb verb of this sentence.
	 */
	public void setVerb(String verb) {
		this.verb = new Part(verb,SRL.VERB);
	}

	public ArrayList<Part> getObjects() {
		return objects;
	}
	
	public ArrayList<String> getObjectsString() {
		ArrayList<String> objStr = new ArrayList<String>(); 
		for(Part obj:objects)
			objStr.add(obj._name);			
		return objStr;
	}

	public void setObjects(ArrayList<Part> objects) {
		this.objects = objects;
	}
	
	/**
	 * The setter method which converts each string argument to a Part object 
	 * with _srl "OBJ" and _name argument content.  
	 * @param objects list of objects of this sentence.
	 */
	public void setObjects(String... objects) {
		this.objects = new ArrayList<Part>();
		for(String obj:objects)
			if(obj!= null)
				this.objects.add(new Part(obj,SRL.OBJ));		
	}

	public ArrayList<Part> getAdverbs() {
		return adverbs;
	}
	
	public ArrayList<String> getAdverbsString() {
		ArrayList<String> advStr = new ArrayList<String>(); 
		for(Part adv:adverbs)
			advStr.add(adv._name);			
		return advStr;
	}
	
	public void setAdverbs(ArrayList<Part> adverbs) {
		this.adverbs = adverbs;
	}

	/**
	 * The setter method which converts each string argument to a Part object
	 * with _srl "ADV" and _name argument content.  
	 * @param adverbs list of adverbs of this sentence.
	 */
	public void setAdverbs(String... adverbs) {
		this.adverbs = new ArrayList<Part>();
		for(String adv:adverbs)
			if(adv!= null)
				this.adverbs.add(new Part(adv,SRL.ADV));
	}
	
	
	public static void main(String[] args){
		SentenceInfo sen1 = new SentenceInfo("");
		sen1.setSubjects("pesarak","ali");
		
		ArrayList<Part> sbjs = sen1.getSubjects();
				
		System.out.println(sbjs);
		
	}
	
}
