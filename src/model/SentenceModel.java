package model;

import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

/**
 * 
 * This class contains information of an input sentence.
 * It has different SentenceParts of speech of the sentence and the original sentence in natural language.
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
	private ArrayList<SentencePart> subjects = new ArrayList<SentencePart>();
	
	/**
	 * The only verb of this sentence.
	 */
	private SentencePart verb;
	
	/**
	 * object(s) of this sentence, if any.
	 */
	private ArrayList<SentencePart> objects = new ArrayList<SentencePart>();
	
	/**
	 * adverb(s) of this sentence, if any.
	 */
	private ArrayList<SentencePart> adverbs = new ArrayList<SentencePart>();
	
	public SentenceModel(String originalSentence){
		this.originalSentence = originalSentence;			
		this.subjects = new ArrayList<SentencePart>();
		this.objects = new ArrayList<SentencePart>();
		this.adverbs = new ArrayList<SentencePart>();
	}
	
	public SentenceModel(String originalSentence, SceneModel sceneModel){
		this.originalSentence = originalSentence;
		this.scene = sceneModel;				
		this.subjects = new ArrayList<SentencePart>();
		this.objects = new ArrayList<SentencePart>();
		this.adverbs = new ArrayList<SentencePart>();
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
	 * @return the first subject.
	 */
	public SentencePart getFirstSubject(){
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
	
	/**
	 * this methods adds subject at the end of subjects list of this SentenceModel.
	 * 
	 * @param subject
	 */
	public void addSingleSubject(SentencePart subject) {
		if(subject == null){
			MyError.error("added subject could not be null");
			return;
		}
		if(this.subjects == null)
			this.subjects = new ArrayList<SentencePart>();
		this.subjects.add(subject);		
	}
		
	
	/**
	 * this method adds the input subjects at the end of subjects list of this SentenceModel.
	 * it converts each string argument to a SentencePart object 
	 * with _srl "SBJ" and _name argument content.
	 *   
	 * @param subjects list of subjects of this sentence.
	 */
	public void addSubjects(String... subjects) {
		if(this.subjects == null)
			this.subjects = new ArrayList<SentencePart>();
		
		for(String sbj:subjects)
			if(sbj!= null)
				this.subjects.add(new SentencePart(sbj,SRL.SUBJECT));			
				
	}
	
	public SentencePart getVerb() {
		return verb;
	}
	
	public String getVerbString() {
		if(verb != null)
			return verb._name;
		return "";
	}

	public void setVerb(SentencePart verb) {
		this.verb = verb;
	}
	
	/** The setter method which converts string argument to a SentencePart object
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
	 * @return the first object.
	 */
	public SentencePart getFirstObject(){
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
	
	/**
	 * this methods adds object at the end of objects list of this SentenceModel.
	 * 
	 * @param object
	 */
	public void addSingleObject(SentencePart object) {
		if(object == null){
			MyError.error("added object could not be null");
			return;
		}
		if(this.objects == null)
			this.objects = new ArrayList<SentencePart>();
		this.objects.add(object);		
	}
		
	
	/**
	 * this method adds the input objects at the end of objects list of this SentenceModel.
	 * it converts each string argument to a SentencePart object 
	 * with _srl "OBJ" and _name argument content.
	 *   
	 * @param objects list of objects of this sentence.
	 */
	public void addObjects(String... objects) {
		if(this.objects == null)
			this.objects = new ArrayList<SentencePart>();
		for(String obj:objects)
			if(obj!= null)
				this.objects.add(new SentencePart(obj,SRL.OBJECT));		
	}

	public ArrayList<SentencePart> getAdverbs() {
		return adverbs;
	}
	
	/** 
	 * @return the first adverb.
	 */
	public SentencePart getFirstAdverb(){	
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
	
	/**
	 * this methods adds adverb at the end of adverbs list of this SentenceModel.
	 * 
	 * @param adverb
	 */	
	public void addSingleAdverb(SentencePart adverb) {
		if(adverb == null){
			MyError.error("added adverb could not be null");
			return;
		}
		if(this.adverbs == null)
			this.adverbs = new ArrayList<SentencePart>();
		this.adverbs.add(adverb);		
	}
		

	/**
	 * this method adds the input adverbs at the end of adverbs list of this SentenceModel.
	 * it converts each string argument to a SentencePart object
	 * with _srl "ADV" and _name argument content.
	 *   
	 * @param adverbs list of adverbs of this sentence.
	 */
	public void addAdverbs(String... adverbs) {
		if(this.adverbs == null)
			this.adverbs = new ArrayList<SentencePart>();
		for(String adv:adverbs)
			if(adv!= null)
				this.adverbs.add(new SentencePart(adv,SRL.ADVERB));
	}
	
	/**
	 * This method gets sentence object and its different Parts, 
	 * then places each SentencePart object in its propel SRL term.
	 * we have temporarily assumed that each sentence has single subject, object, and adverb. 
	 * //TODO: it must be generalized to multi-subjects, multi-objects, and multi-adverbs. 
	 * 
	 * @param sentence sentence object which is to be completed.
	 * @param senParts different SentencePart object of this sentence.
	 */
	public void arrageSentenceParts(String NLSentence, ArrayList<SentencePart> senParts) {
		
		for(SentencePart part:senParts){
			if(part == null)
				continue;			
			if(part.isSubject()){
				this.addSingleSubject(part);
				continue;
			}
			if(part.isVerb()){
				this.setVerb(part);
				continue;
			}
			if(part.isObject()){
				this.addSingleObject(part);
				continue;
			}
			if(part.isSubject()){
				this.addSingleSubject(part);
				continue;
			}
			if(part.isAdverb()){
				this.addSingleAdverb(part);				
			}			
		}		
	}

	@Override
	public String toString() {
		String rs = "subjects:";
		ArrayList<SentencePart> subjects = getSubjects();
		for(SentencePart sbj:subjects)
			rs += sbj.toString() + ", ";
		
		rs += "\nverb:";
		SentencePart verb = getVerb();
		if(verb != null)
			rs += verb.toString();
		
		rs += "\nobjects:";
		ArrayList<SentencePart> objects = getObjects();
		for(SentencePart obj:objects)
			rs += obj.toString() + ", ";
		
		rs += "\nadverbs";
		ArrayList<SentencePart> adverbs = getAdverbs();
		for(SentencePart adv:adverbs)			
			rs += adv.toString() + ", ";

//		String rs = "";
//		rs = getSubjectsString() + "\t" + getVerbString() + "\t" + getObjectsString() + "\t" + getAdverbsString();
		
		String wsd = "\nWSD:\n";
		wsd += "subjects:";		
		for(SentencePart sbj:subjects)
			if(sbj._wsd != null)
				wsd += sbj._wsd.toString() + ", ";
			else
				wsd += "null, ";
		
		wsd += "\nverb:";		
		if(verb != null && verb._wsd != null)
			wsd += verb._wsd.toString();	
		else
			wsd += "null";
	
		wsd += "\nobjects:";		
		for(SentencePart obj:objects)
			if(obj._wsd != null)
				wsd += obj._wsd.toString() + ", ";
			else
				wsd += "null, ";
		
		wsd += "\nadverbs";		
		for(SentencePart adv:adverbs)			
			if(adv._wsd != null)
				wsd += adv._wsd.toString() + ", ";
			else
				wsd += "null, ";
		
		return rs + "\n" + wsd;
	}

	public String getOriginalSentence() {
		return originalSentence;
	}

	
//	public static void main(String[] args){
//		SentenceModel sen1 = new SentenceModel("");
//		sen1.addSubjects("pesarak","ali", "aa");
//		
//		System.out.println(sen1);		
//	}
	
}
