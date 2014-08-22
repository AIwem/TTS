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
	
	/** 
	 * @return the single (probably the first) subject.
	 */
	public Part getSingleSubject(){
		if(subjects != null && subjects.size() > 0)
			return subjects.get(0);
		return null;	
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
	
	public void setSingleSubject(Part subject) {
		if(subject != null){
			ArrayList<Part> sbjs = new ArrayList<Part>();
			sbjs.add(subject);
			this.subjects = sbjs;
		}
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
	
	/** 
	 * @return the single (probably the first) object.
	 */
	public Part getSingleObject(){
		if(objects != null && objects.size() > 0)
			return objects.get(0);
		return null;		
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
	
	public void setSingleObject(Part object) {
		if(object != null){
			ArrayList<Part> objs = new ArrayList<Part>();
			objs.add(object);
			this.objects = objs;
		}
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
	
	/** 
	 * @return the single (probably the first) adverb.
	 */
	public Part getSingleAdverb(){	
		if(adverbs != null && adverbs.size() > 0)
			return adverbs.get(0);
		return null;	
		
		
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
	
	public void setSingleAdverb(Part adverb) {
		if(adverb != null){
			ArrayList<Part> advs = new ArrayList<Part>();
			advs.add(adverb);
			this.adverbs = advs;
		}
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
	
	/**
	 * This method gets sentence object and its different Parts, 
	 * then places each part object in its propel SRL term.
	 * we have temporarily assumed that each sentence has single subject, object, and adverb. 
	 * //TODO: it must be generalized to multi-subjects, multi-objects, and multi-adverbs. 
	 * 
	 * @param sentence sentence object which is to be completed.
	 * @param senParts different part object of this sentence.
	 */
	public static SentenceInfo createSentence(String NLSentence, ArrayList<Part> senParts) {
		SentenceInfo sentence = new SentenceInfo(NLSentence);
		for(Part part:senParts){
			if(part == null)
				continue;			
			if(part.isSubject()){
				sentence.setSingleSubject(part);
				continue;
			}
			if(part.isVerb()){
				sentence.setVerb(part);
				continue;
			}
			if(part.isObject()){
				sentence.setSingleObject(part);
				continue;
			}
			if(part.isSubject()){
				sentence.setSingleSubject(part);
				continue;
			}
			if(part.isAdverb()){
				sentence.setSingleAdverb(part);				
			}			
		}
		return sentence;
	}

	@Override
	public String toString() {
		String rs = "";
		Part sbj = getSingleSubject();
		if(sbj != null)
			rs += sbj.toString();
		Part verb = getVerb();
		if(verb != null)
			rs += "\t"+ verb.toString();
		Part obj = getSingleObject();
		if(obj != null)
			rs += "\t" +obj.toString();
		Part adv = getSingleAdverb();
		if(adv != null)
			rs += "\t" + adv.toString();		
		
		String wsd = "";		
		if(sbj != null && sbj._wsd != null)
			wsd += sbj._wsd + "\t";
		else
			wsd += "null\t";
		if(verb != null && verb._wsd != null)
			wsd += verb._wsd + "\t";
		else
			wsd += "null\t";
		if(obj != null && obj._wsd != null)
			wsd += obj._wsd + "\t";		
		else
			wsd += "null\t";
		if(adv != null && adv._wsd != null)
			wsd += adv._wsd + "\t";
		else
			wsd += "null";
		return rs + "\n" + wsd;
	}
	
	
	
	/*
	public static void main(String[] args){
		SentenceInfo sen1 = new SentenceInfo("");
		sen1.setSubjects("pesarak","ali");
		
		ArrayList<Part> sbjs = sen1.getSubjects();
				
		System.out.println(sbjs);		
	}
	*/
}
