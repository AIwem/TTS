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
public class SentenceModel{
		
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
	private ArrayList<SentencePart> subjects;
	
	/**
	 * The only verb of this sentence.
	 */
	private SentencePart verb;
	
	/**
	 * object(s) of this sentence, if any.
	 */
	private ArrayList<SentencePart> objects;
	
	/**
	 * adverb(s) of this sentence, if any.
	 */
	private ArrayList<SentencePart> adverbs;
	
	public SentenceModel(String originalSentence){
		this.originalSentence = originalSentence;			
	}
	
	public SentenceModel(String originalSentence, SceneModel sceneModel){
		this.originalSentence = originalSentence;
		this.scene = sceneModel;		
	}
	
	public SceneModel getScene() {
		return scene;
	}

	public void setScene(SceneModel scene) {
		this.scene = scene;
	}
		
	public ArrayList<SentencePart> getSubjects() {
		return subjects;
	}
	
	/** 
	 * @return the single (probably the first) subject.
	 */
	public SentencePart getSingleSubject(){
		if(subjects != null && subjects.size() > 0)
			return subjects.get(0);
		return null;	
	}
	
	public ArrayList<String> getSubjectsString() {
		ArrayList<String> sbjStr = new ArrayList<String>(); 
		for(SentencePart sbj:subjects)
			sbjStr.add(sbj._name);			
		return sbjStr;
	}
	
	public void setSubjects(ArrayList<SentencePart> subjects) {
		this.subjects = subjects;
	}
	
	public void setSingleSubject(SentencePart subject) {
		if(subject != null){
			ArrayList<SentencePart> sbjs = new ArrayList<SentencePart>();
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
		this.subjects = new ArrayList<SentencePart>();
		for(String sbj:subjects)
			if(sbj!= null)
				this.subjects.add(new SentencePart(sbj,SRL.SUBJECT));			
				
	}
	
	public SentencePart getVerb() {
		return verb;
	}
	
	public String getVerbString() {
		return verb._name;
	}

	public void setVerb(SentencePart verb) {
		this.verb = verb;
	}
	
	/** The setter method which converts string argument to a Part object
	 * with _srl "VERB" and _name argument content.
	 * @param verb verb of this sentence.
	 */
	public void setVerb(String verb) {
		this.verb = new SentencePart(verb,SRL.VERB);
	}

	public ArrayList<SentencePart> getObjects() {
		return objects;
	}
	
	/** 
	 * @return the single (probably the first) object.
	 */
	public SentencePart getSingleObject(){
		if(objects != null && objects.size() > 0)
			return objects.get(0);
		return null;		
	}
	
	public ArrayList<String> getObjectsString() {
		ArrayList<String> objStr = new ArrayList<String>(); 
		for(SentencePart obj:objects)
			objStr.add(obj._name);			
		return objStr;
	}

	public void setObjects(ArrayList<SentencePart> objects) {
		this.objects = objects;
	}
	
	public void setSingleObject(SentencePart object) {
		if(object != null){
			ArrayList<SentencePart> objs = new ArrayList<SentencePart>();
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
		this.objects = new ArrayList<SentencePart>();
		for(String obj:objects)
			if(obj!= null)
				this.objects.add(new SentencePart(obj,SRL.OBJECT));		
	}

	public ArrayList<SentencePart> getAdverbs() {
		return adverbs;
	}
	
	/** 
	 * @return the single (probably the first) adverb.
	 */
	public SentencePart getSingleAdverb(){	
		if(adverbs != null && adverbs.size() > 0)
			return adverbs.get(0);
		return null;	
		
		
	}
	
	public ArrayList<String> getAdverbsString() {
		ArrayList<String> advStr = new ArrayList<String>(); 
		for(SentencePart adv:adverbs)
			advStr.add(adv._name);			
		return advStr;
	}
	
	public void setAdverbs(ArrayList<SentencePart> adverbs) {
		this.adverbs = adverbs;
	}
	
	public void setSingleAdverb(SentencePart adverb) {
		if(adverb != null){
			ArrayList<SentencePart> advs = new ArrayList<SentencePart>();
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
		this.adverbs = new ArrayList<SentencePart>();
		for(String adv:adverbs)
			if(adv!= null)
				this.adverbs.add(new SentencePart(adv,SRL.ADVERB));
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
	public void arrageSentenceParts(String NLSentence, ArrayList<SentencePart> senParts) {
		
		for(SentencePart part:senParts){
			if(part == null)
				continue;			
			if(part.isSubject()){
				this.setSingleSubject(part);
				continue;
			}
			if(part.isVerb()){
				this.setVerb(part);
				continue;
			}
			if(part.isObject()){
				this.setSingleObject(part);
				continue;
			}
			if(part.isSubject()){
				this.setSingleSubject(part);
				continue;
			}
			if(part.isAdverb()){
				this.setSingleAdverb(part);				
			}			
		}		
	}

	@Override
	public String toString() {
		String rs = "";
		SentencePart sbj = getSingleSubject();
		if(sbj != null)
			rs += sbj.toString();
		SentencePart verb = getVerb();
		if(verb != null)
			rs += "\t"+ verb.toString();
		SentencePart obj = getSingleObject();
		if(obj != null)
			rs += "\t" +obj.toString();
		SentencePart adv = getSingleAdverb();
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

	public String getOriginalSentence() {
		return originalSentence;
	}

	/*
	public static void main(String[] args){
		SentenceModel sen1 = new SentenceModel("");
		sen1.setSubjects("pesarak","ali");
		
		ArrayList<Part> sbjs = sen1.getSubjects();
				
		System.out.println(sbjs);		
	}
	*/
}
