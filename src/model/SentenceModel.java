package model;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
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
	private String NLSentence;
	
	/**
	 * subject(s) of this sentence.
	 */
	private ArrayList<Word> subjects = new ArrayList<Word>();
	
	/**
	 * The only verb of this sentence.
	 */
	private Word verb;
	
	/**
	 * object(s) of this sentence, if any.
	 */
	private ArrayList<Word> objects = new ArrayList<Word>();
	
	/**
	 * adverb(s) of this sentence, if any.
	 */
	private ArrayList<Word> adverbs = new ArrayList<Word>();
	
	public SentenceModel(String originalSentence){
		this.NLSentence = originalSentence;			
		this.subjects = new ArrayList<Word>();
		this.objects = new ArrayList<Word>();
		this.adverbs = new ArrayList<Word>();
	}
	
	public SentenceModel(String originalSentence, SceneModel sceneModel){
		this.NLSentence = originalSentence;
		this.scene = sceneModel;				
		this.subjects = new ArrayList<Word>();
		this.objects = new ArrayList<Word>();
		this.adverbs = new ArrayList<Word>();
	}
	
	public void setScene(SceneModel scene) {
		this.scene = scene;
	}
	
	public void setSubjects(ArrayList<Word> subjects) {
		this.subjects = subjects;
	}
	
	public void setVerb(Word verb) {
		this.verb = verb;
	}
	
	/** The setter method which converts string argument to a SentencePart object
	 * with _syntaxTag "VERB" and _name argument content.
	 * @param verb verb of this sentence.
	 */
	public void setVerb(String verb) {
		this.verb = new Word(verb, DependencyRelationType.ROOT, this);
	}
	
	public void setObjects(ArrayList<Word> objects) {
		this.objects = objects;
	}
	
	public void setAdverbs(ArrayList<Word> adverbs) {
		this.adverbs = adverbs;
	}
	
	public void setPrerparedSentencePart(Word newSentencePart){		
		if(newSentencePart == null)
			return;	
		
		if(newSentencePart.isSubject())
			this.addSingleSubject(newSentencePart);
			
		if(newSentencePart.isVerb())
			this.setVerb(newSentencePart);
			
		if(newSentencePart.isObject())
			this.addSingleObject(newSentencePart);
					
		if(newSentencePart.isAdverb())
			this.addSingleAdverb(newSentencePart);
		
	}
	
	
	public boolean hasArg0(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG0"))
				return true;
		return false;
	}
	
	
	
	public boolean hasArg1(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG1"))
				return true;
		return false;
	}	
	
	
	
	public boolean hasArg2(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG2"))
				return true;
		return false;
	}
	
	public boolean hasArg3(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG3"))
				return true;
		return false;
	}

	public boolean hasArg4(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG4"))
				return true;
		return false;
	}
	
	public boolean hasArg5(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG5"))
				return true;
		return false;
	}	
	

	public boolean hasSemanticArg(SemanticTag semanticArg) {
		if(semanticArg == null)
			return false;
		
		if(semanticArg.isMainSemanticTag())
			return hasMainSemanticArg(semanticArg.convertToMainSemanticTag());
		
		else if(semanticArg.isSubSemanticTag())
			return hasSubSemanticArg(semanticArg.convertToSubSemanticTag());
					
		return false;
	}	
	
	public boolean hasMainSemanticArg(MainSemanticTag mainSemanticArg) {
		if(mainSemanticArg == null)
			return false;
		
		if(mainSemanticArg.isArg0())
			return hasArg0();
		
		if(mainSemanticArg.isArg1())
			return hasArg1();
		
		if(mainSemanticArg.isArg2())
			return hasArg2();
		
		if(mainSemanticArg.isArg3())
			return hasArg3();
		
		if(mainSemanticArg.isArg4())
			return hasArg4();
		
		if(mainSemanticArg.isArg5())
			return hasArg5();			
			
		return false;
	}	
	
	public boolean hasSubSemanticArg(SubSemanticTag subSemanticArg) {
		if(subSemanticArg == null)
			return false;
		
		ArrayList<SubSemanticTag> existingSubArgs = this.getExistingSubSematicArgs();
		
		for(SubSemanticTag exist:existingSubArgs)
			if(exist == subSemanticArg)
				return true;
		
		return false;
	}
	
	public MainSemanticTag getArg0(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG0"))
				return exist;
		return null;
	}	
	
	public Word getArg0SentencePart(){
		MainSemanticTag arg0 = getArg0();
		if(arg0 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg0.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg0.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg0.name()))
					return adv;
		}	
		return null;
	}
	
	public MainSemanticTag getArg1(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG1"))
				return exist;
		return null;
	}
	
	public Word getArg1SentencePart(){
		MainSemanticTag arg1 = getArg1();
		if(arg1 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg1.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg1.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg1.name()))
					return adv;
		}	
		return null;
	}
	
	public MainSemanticTag getArg2(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG2"))
				return exist;
		return null;
	}
	
	public Word getArg2SentencePart() {
		MainSemanticTag arg2 = getArg2();
		if(arg2 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg2.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg2.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg2.name()))
					return adv;
		}	
		return null;
	}

	
	public MainSemanticTag getArg3(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG3"))
				return exist;
		return null;
	}
	
	public Word getArg3SentencePart() {		
		MainSemanticTag arg3 = getArg3();
		if(arg3 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg3.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg3.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg3.name()))
					return adv;
		}	
		return null;
	}
	
	public MainSemanticTag getArg4(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG4"))
				return exist;
		return null;
	}
	
	public Word getArg4SentencePart() {		
		MainSemanticTag arg4 = getArg4();
		if(arg4 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg4.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg4.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg4.name()))
					return adv;
		}	
		return null;
	}
	
	public MainSemanticTag getArg5(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.name().startsWith("ARG5"))
				return exist;
		return null;
	}
	
	public Word getArg5SentencePart() {
		MainSemanticTag arg5 = getArg5();
		if(arg5 != null){
			for(Word sbj:subjects)
				if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(arg5.name()))
					return sbj;
			
			for(Word obj:objects)
				if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(arg5.name()))
					return obj;
			
			for(Word adv:adverbs)
				if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(arg5.name()))
					return adv;
		}	
		return null;}

	
	public SceneModel getScene() {
		return scene;
	}
		
	public ArrayList<Word> getSubjects() {
		return subjects;
	}
	
	/** 
	 * @return the first subject.
	 */
	public Word getFirstSubject(){
		if(subjects != null && subjects.size() > 0)
			return subjects.get(0);
		return null;	
	}
	
	public ArrayList<String> getSubjectsString() {
		ArrayList<String> sbjStr = new ArrayList<String>(); 
		for(Word sbj:subjects)
			sbjStr.add(sbj._word);			
		return sbjStr;
	}
	
	public Word getVerb() {
		return verb;
	}
	
	public String getVerbString() {
		if(verb != null)
			return verb._word;
		return "";
	}

	public ArrayList<Word> getObjects() {
		return objects;
	}
	
	/** 
	 * @return the first object.
	 */
	public Word getFirstObject(){
		if(objects != null && objects.size() > 0)
			return objects.get(0);
		return null;		
	}
	
	public ArrayList<String> getObjectsString() {
		ArrayList<String> objStr = new ArrayList<String>(); 
		for(Word obj:objects)
			objStr.add(obj._word);			
		return objStr;
	}


	public ArrayList<Word> getAdverbs() {
		return adverbs;
	}
	
	/** 
	 * @return the first adverb.
	 */
	public Word getFirstAdverb(){	
		if(adverbs != null && adverbs.size() > 0)
			return adverbs.get(0);
		return null;	
		
		
	}
	
	public ArrayList<String> getAdverbsString() {
		ArrayList<String> advStr = new ArrayList<String>(); 
		for(Word adv:adverbs)
			advStr.add(adv._word);			
		return advStr;
	}
	
	public String getOriginalSentence() {
		return NLSentence;
	}
	
	public ArrayList<Word> getSentencePartsWithSourceNumber(int number) {
		ArrayList<Word> allPart = new ArrayList<Word>();
		
		for(Word sbj:subjects)
			if(sbj != null && sbj.get_sourceOfSynNum() == number)
				allPart.add(sbj);
		
		for(Word obj:objects)
			if(obj != null && obj.get_sourceOfSynNum() == number)
				allPart.add(obj);
		
		for(Word adv:adverbs)
			if(adv != null && adv.get_sourceOfSynNum() == number)
				allPart.add(adv);
		
		return allPart;				
	}
	
	/**
	 * 
	 * @return returns the SemanticTags of all parts of this sentence. guaranteed not to be null.
	 */
	public ArrayList<SemanticTag> getExistingSematicArgs(){
		
		ArrayList<SemanticTag> allSemanticTags = new ArrayList<SemanticTag>();
		
		for(Word sbj:subjects)
			if(sbj != null && sbj._semanticTag != null)
				allSemanticTags.add(sbj._semanticTag);
		
		for(Word obj:objects)
			if(obj != null && obj._semanticTag != null)
				allSemanticTags.add(obj._semanticTag);
		
		for(Word adv:adverbs)
			if(adv != null && adv._semanticTag != null)
				allSemanticTags.add(adv._semanticTag);
		
		return allSemanticTags;
	}
	
	/**
	 * 
	 * @return returns the MainSemanticTags of all parts of this sentence. guaranteed not to be null.
	 */
	public ArrayList<MainSemanticTag> getExistingMainSematicArgs(){
		
		ArrayList<MainSemanticTag> existingMainSemanticTags = new ArrayList<MainSemanticTag>();
		
		for(Word sbj:subjects)
			if(sbj != null && sbj._semanticTag != null)
				if(sbj._semanticTag.isMainSemanticTag()){
					MainSemanticTag converted = sbj._semanticTag.convertToMainSemanticTag();
					if(converted != null)				
						existingMainSemanticTags.add(converted);
				}
		
		for(Word obj:objects)
			if(obj != null && obj._semanticTag != null)
				if(obj._semanticTag.isMainSemanticTag()){
					MainSemanticTag converted = obj._semanticTag.convertToMainSemanticTag();
					if(converted != null)				
						existingMainSemanticTags.add(converted);
				}
		
		for(Word adv:adverbs)
			if(adv != null && adv._semanticTag != null)
				if(adv._semanticTag.isMainSemanticTag()){
					MainSemanticTag converted = adv._semanticTag.convertToMainSemanticTag();
					if(converted != null)				
						existingMainSemanticTags.add(converted);				
				}
					
		
		return existingMainSemanticTags;
	}
	
	/**
	 * 
	 * @return returns the SubSemanticTags of all parts of this sentence. guaranteed not to be null.
	 */
	public ArrayList<SubSemanticTag> getExistingSubSematicArgs(){
		
		ArrayList<SubSemanticTag> allSubSemanticTags = new ArrayList<SubSemanticTag>();
		
		for(Word sbj:subjects)
			if(sbj != null && sbj._semanticTag != null)
				if(sbj._semanticTag.isSubSemanticTag()){
					SubSemanticTag converted = sbj._semanticTag.convertToSubSemanticTag();
					if(converted != null)				
						allSubSemanticTags.add(converted);				
				}
		
		for(Word obj:objects)
			if(obj != null && obj._semanticTag != null)
				if(obj._semanticTag.isSubSemanticTag()){
					SubSemanticTag converted = obj._semanticTag.convertToSubSemanticTag();
					if(converted != null)				
						allSubSemanticTags.add(converted);
				}
		
		for(Word adv:adverbs)
			if(adv != null && adv._semanticTag != null)
				if(adv._semanticTag.isSubSemanticTag()){
					SubSemanticTag converted = adv._semanticTag.convertToSubSemanticTag();
					if(converted != null)				
						allSubSemanticTags.add(converted);
				}
		
		return allSubSemanticTags;
	}
	
	
	
	
	
	
	/**
	 * this methods adds subject at the end of subjects list of this SentenceModel.
	 * 
	 * @param subject
	 */
	public void addSingleSubject(Word subject) {
		if(subject == null){
			MyError.error("added subject could not be null");
			return;
		}
		if(this.subjects == null)
			this.subjects = new ArrayList<Word>();
		this.subjects.add(subject);		
	}
		
	
	/**
	 * this method adds the input subjects at the end of subjects list of this SentenceModel.
	 * it converts each string argument to a SentencePart object 
	 * with _synTag "SBJ" and _name argument content.
	 *   
	 * @param subjects list of subjects of this sentence.
	 */
	public void addSubjects(String... subjects) {
		if(this.subjects == null)
			this.subjects = new ArrayList<Word>();
		
		for(String sbj:subjects)
			if(sbj != null)
				this.subjects.add(new Word(sbj, DependencyRelationType.SBJ, this));			
				
	}
	
	/**
	 * this methods adds object at the end of objects list of this SentenceModel.
	 * 
	 * @param object
	 */
	public void addSingleObject(Word object) {
		if(object == null){
			MyError.error("added object could not be null");
			return;
		}
		if(this.objects == null)
			this.objects = new ArrayList<Word>();
		this.objects.add(object);		
	}
		
	
	/**
	 * this method adds the input objects at the end of objects list of this SentenceModel.
	 * it converts each string argument to a SentencePart object 
	 * with _syntaxTag "OBJ" and _name argument content.
	 *   
	 * @param objects list of objects of this sentence.
	 */
	public void addObjects(String... objects) {
		if(this.objects == null)
			this.objects = new ArrayList<Word>();
		for(String obj:objects)
			if(obj!= null)
				this.objects.add(new Word(obj, DependencyRelationType.OBJ, this));		
	}

	
	
	
	
	
	
	/**
	 * this methods adds adverb at the end of adverbs list of this SentenceModel.
	 * 
	 * @param adverb
	 */	
	public void addSingleAdverb(Word adverb) {
		if(adverb == null){
			MyError.error("added adverb could not be null");
			return;
		}
		if(this.adverbs == null)
			this.adverbs = new ArrayList<Word>();
		this.adverbs.add(adverb);		
	}
		

	/**
	 * this method adds the input adverbs at the end of adverbs list of this SentenceModel.
	 * it converts each string argument to a SentencePart object
	 * with _syntaxTag "ADV" and _name argument content.
	 *   
	 * @param adverbs list of adverbs of this sentence.
	 */
	public void addAdverbs(String... adverbs) {
		if(this.adverbs == null)
			this.adverbs = new ArrayList<Word>();
		
		for(String adv:adverbs)
			if(adv!= null)
				this.adverbs.add(new Word(adv, DependencyRelationType.ADVRB, this));
	}
	
	

	
	
	
	
	//TODO: check what shall I do, when some part of sentence is infinitive?!
	public ArrayList<MainSemanticTag> getNecessarySematicArgs(){
		
		ArrayList<MainSemanticTag> necessarySemArg = new ArrayList<MainSemanticTag>();		
		
		if(!Common.isEmpty(verb.capacities))
			for(Node cap:verb.capacities)
				necessarySemArg.add(MainSemanticTag.fromString(cap.getName()));
			
		return necessarySemArg; 
	}

	public Word getSentencePart(Node partNode) {		
		
		for(Word sbj:subjects)
			if(sbj != null && sbj._wsd.equalsRelaxed(partNode))
				return sbj;				
		
		for(Word obj:objects)
			if(obj != null && obj._wsd.equalsRelaxed(partNode))
				return obj;
		
		for(Word adv:adverbs)
			if(adv != null && adv._wsd.equalsRelaxed(partNode))
				return adv;
		
		if(verb != null && verb._wsd.equalsRelaxed(partNode))
			return verb;
				
		return null;
	}
	
	public Word getSentencePart(SemanticTag semanticTag){
		if(semanticTag == null)
			return null;
		
		if(semanticTag.isMainSemanticTag())
			return getSentencePart(semanticTag.convertToMainSemanticTag());
		
		else if(semanticTag.isSubSemanticTag())
			return getSentencePart(semanticTag.convertToSubSemanticTag());
					
		return null;	
	}
	
	public Word getSentencePart(MainSemanticTag mainSemanticTag) {
		if(mainSemanticTag == null)
			return null;
		
		if(mainSemanticTag.isArg0())
			return getArg0SentencePart();

		if(mainSemanticTag.isArg1())
			return getArg1SentencePart();

		if(mainSemanticTag.isArg2())
			return getArg2SentencePart();

		if(mainSemanticTag.isArg3())
			return getArg3SentencePart();

		if(mainSemanticTag.isArg4())
			return getArg4SentencePart();
		
		if(mainSemanticTag.isArg5())
			return getArg5SentencePart();
		
		return null;
	}
	
	
	public Word getSentencePart(SubSemanticTag subSemanticArg) {	
		if(subSemanticArg == null)
			return null;
			
		for(Word sbj:subjects)
			if(sbj != null && sbj._semanticTag != null && sbj._semanticTag.name().equals(subSemanticArg.name()))
				return sbj;
		
		for(Word obj:objects)
			if(obj != null && obj._semanticTag != null && obj._semanticTag.name().equals(subSemanticArg.name()))
				return obj;
		
		for(Word adv:adverbs)
			if(adv != null && adv._semanticTag != null && adv._semanticTag.name().equals(subSemanticArg.name()))
				return adv;
		
		return null;
	}

	
	/**
	 * This method gets sentence object and its different Parts, 
	 * then places each SentencePart object in its proper syntaxTag term.
	 * we have temporarily assumed that each sentence has single subject, object, and adverb. 
	 * //TODO: check if its logic is general enough for every type of sentences! 
	 * 
	 * @param sentence sentence object which is to be completed.
	 * @param senParts different SentencePart object of this sentence. guaranteed not to be null.
	 */
	public void arrageSentenceParts(String NLSentence, ArrayList<Word> senParts) {
		
		for(Word part:senParts){
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
			if(part.isAdverb()){
				this.addSingleAdverb(part);				
			}			
		}		
	}
	
	
	
	@Override
	public String toString() {
//		String rs = "subjects:";
//		ArrayList<SentencePart> subjects = getSubjects();
//		for(SentencePart sbj:subjects)
//			rs += sbj.toString() + ", ";
//		
//		rs += "\nverb:";
//		SentencePart verb = getVerb();
//		if(verb != null)
//			rs += verb.toString();
//		
//		rs += "\nobjects:";
//		ArrayList<SentencePart> objects = getObjects();
//		for(SentencePart obj:objects)
//			rs += obj.toString() + ", ";
//		
//		rs += "\nadverbs";
//		ArrayList<SentencePart> adverbs = getAdverbs();
//		for(SentencePart adv:adverbs)			
//			rs += adv.toString() + ", ";

//		String rs = "";
//		rs = getSubjectsString() + "\t" + getVerbString() + "\t" + getObjectsString() + "\t" + getAdverbsString();
		
		
		String wsd = "subjects:";		
		for(Word sbj:subjects)
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
		for(Word obj:objects)
			if(obj._wsd != null)
				wsd += obj._wsd.toString() + ", ";
			else
				wsd += "null, ";
		
		wsd += "\nadverbs";		
		for(Word adv:adverbs)			
			if(adv._wsd != null)
				wsd += adv._wsd.toString() + ", ";
			else
				wsd += "null, ";
		
		return wsd; //rs + "\n" + wsd;
	}

	

	

	
//	public static void main(String[] args){
//		SentenceModel sen1 = new SentenceModel("");
//		sen1.addSubjects("pesarak","ali", "aa");
//		
//		System.out.println(sen1);		
//	}
	
}
