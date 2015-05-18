package model;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

public class SentenceModel {
	
	/**
	 * the scene which this sentence belongs to. 
	 */
	private SceneModel scene;
	
	/**
	 * original sentence in "natural language". 
	 */
	private String NLSentence;
	
	/**
	 * Words of this sentence.
	 */
	private ArrayList<Word> _words = new ArrayList<Word>();
	
	/**
	 * prepared Words of this sentence.
	 */
	private ArrayList<Word> _prepared_words = new ArrayList<Word>();
	
	/**
	 * Phrases of this sentence.
	 */
	private ArrayList<Phrase> _phrases = new ArrayList<Phrase>();
	
	/**
	 * input parameter words contains String definition of each word of this SentenceModel.
	 * @param words
	 */
	public SentenceModel(String NLsentence, String[] words){
		this.NLSentence = NLsentence;
		
		if(_words == null)
			_words = new ArrayList<Word>();
		
		for(String wStr:words){
			Word wd = new Word(wStr, this);
			this._words.add(wd);
		}
		arrangeWords();
	}
	
	//-------------------- setter part --------------------------
	/**
	 * @param scene the scene to set
	 */	
	public void setScene(SceneModel scene) {
		this.scene = scene;
	}	
	
	/**
	 * @param phrases the _phrases to set
	 */
	public void set_phrases(ArrayList<Phrase> phrases) {
		this._phrases = phrases;
	}	

	/**
	 * @param words the words to set
	 */
	public void set_words(ArrayList<Word> words) {
		this._words = words;
	}
	
	public void setPrerparedWord(Word preparedWord){		
		if(preparedWord == null)
			return;	
		
		if(_prepared_words == null)
			_prepared_words = new ArrayList<Word>();
		
		_prepared_words.add(preparedWord);		
	}
	
	
	//-------------------- setter part --------------------------
	
	public boolean hasArg0(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg0())
				return true;
		return false;
	}
	
	
	
	public boolean hasArg1(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg1())
				return true;
		return false;
	}	
	
	
	
	public boolean hasArg2(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg2())
				return true;
		return false;
	}
	
	public boolean hasArg3(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg3())
				return true;
		return false;
	}

	public boolean hasArg4(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg4())
				return true;
		return false;
	}
	
	public boolean hasArg5(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg5())
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
	
	//-------------------- getter part --------------------------
	
	public String getNLSentence() {
		return NLSentence;
	}	

	public Word getVerb(){
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isVerb())
					return wrd;
		
		MyError.error("This sentence has no verb!: " + this);
		return null;
	}
	
	public ArrayList<Phrase> get_subject_phrases() {
		
		ArrayList<Phrase> sbj_phrases = new ArrayList<Phrase>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isSubject()){
					Phrase sbj_ph = get_phrase(wrd);
					if(sbj_ph != null)
						sbj_phrases.add(sbj_ph);
					else{
						MyError.error("This Sentence has subject " + wrd + " but it is not head of a phrase!");
					}
				}
		if(sbj_phrases.size() > 0)
			return sbj_phrases;
		else
			return null;
	}
	
	public ArrayList<Phrase> get_object_phrases() {

		ArrayList<Phrase> obj_phrases = new ArrayList<Phrase>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isObject()){
					Phrase obj_ph = get_phrase(wrd);
					if(obj_ph != null)
						obj_phrases.add(obj_ph);
					else{
						MyError.error("This Sentence has object " + wrd + " but it is not head of a phrase!");
					}
				}
		if(obj_phrases.size() > 0)
			return obj_phrases;
		else
			return null;
	}
	
	/**
	 * @return the scene
	 */
	public SceneModel getScene() {
		return scene;
	}
	
	/**
	 * @return the _phrases
	 */
	public ArrayList<Phrase> get_phrases() {
		return _phrases;	
	}
	
	/**
	 * 
	 * @param phrase_head
	 * @return the Phrase with head phrase_head.
	 */
	public Phrase get_phrase(Word phrase_head){
		if(phrase_head == null)
			return null;
		
		if(Common.isEmpty(_phrases))
			return null;
		
		for(Phrase ph:_phrases)
			if(ph.get_headWord() == phrase_head)
				return ph;
		return null;
	}
	
	public Phrase get_phrase(String phrase_head_name){
		if(phrase_head_name == null || phrase_head_name.equals(""))
			return null;
		
		Word ph_head = get_word(phrase_head_name);
		
		if(ph_head == null){
			MyError.error("This sentence hasn't such a phrase with head " + phrase_head_name);
			return null;
		}
		
		return get_phrase(ph_head);
	}
	
	/**
	 * @return the _words
	 */
	public ArrayList<Word> get_words() {
		return _words;
	}
	
	/**
	 * 
	 * @param word_name the Word of this SentenceModel with _wordName word_name.
	 * @return 
	 */
	public Word get_word(String word_name) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._wordName.equalsIgnoreCase(word_name))
					return w;
		return null;
	}
	
	
	/**
	 * 
	 * @param word_number the Word of this SentenceModel with _number word_number.
	 * @return 
	 */
	public Word get_word(int word_number) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._number == word_number)
					return w;
		return null;
	}
	
	
	private ArrayList<Word> getWordsWithSourceNumber(int number) {
		
		ArrayList<Word> allStartingWords = new ArrayList<Word>();		
		
		if(_words != null)
			for(Word wrd:_words)
				if(wrd != null && wrd._srcOfSynTag_number == number)
					allStartingWords.add(wrd);
			
		return allStartingWords;
	}
		
	/**
	 * 
	 * @return returns the SemanticTags of all words of this sentence. guaranteed not to be null.
	 */
	public ArrayList<SemanticTag> getExistingSematicArgs(){
		
		ArrayList<SemanticTag> allSemanticTags = new ArrayList<SemanticTag>();
		
		if(!Common.isEmpty(_words))		
			for(Word wrd:_words)
				if(wrd != null && wrd._semanticTag != null)
					allSemanticTags.add(wrd._semanticTag);
		
		return allSemanticTags;
	}
	
	/**
	 * 
	 * @return returns the MainSemanticTags of all words of this sentence. guaranteed not to be null.
	 */
	public ArrayList<MainSemanticTag> getExistingMainSematicArgs(){
		
		ArrayList<MainSemanticTag> existingMainSemanticTags = new ArrayList<MainSemanticTag>();
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd._semanticTag != null)
					if(wrd._semanticTag.isMainSemanticTag()){
						MainSemanticTag converted = wrd._semanticTag.convertToMainSemanticTag();
						if(converted != null)				
							existingMainSemanticTags.add(converted);
					}
						
		return existingMainSemanticTags;
	}
	
	/**
	 * 
	 * @return returns the SubSemanticTags of all words of this sentence. guaranteed not to be null.
	 */
	public ArrayList<SubSemanticTag> getExistingSubSematicArgs(){
		
		ArrayList<SubSemanticTag> allSubSemanticTags = new ArrayList<SubSemanticTag>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd._semanticTag != null)
					if(wrd._semanticTag.isSubSemanticTag()){
						SubSemanticTag converted = wrd._semanticTag.convertToSubSemanticTag();
						if(converted != null)				
							allSubSemanticTags.add(converted);				
					}
			
		return allSubSemanticTags;
	}
	
	//TODO: check what shall I do, when some word of sentence is infinitive?!
	public ArrayList<MainSemanticTag> getNecessarySematicArgs(){
		
		ArrayList<MainSemanticTag> necessarySemArg = new ArrayList<MainSemanticTag>();		
		
		Word verb = getVerb();
		
		if(verb == null)
			return null;
		
		if(!Common.isEmpty(verb.getCapacities()))
			for(Node cap:verb.getCapacities())
				necessarySemArg.add(MainSemanticTag.fromString(cap.getName()));
			
		return necessarySemArg; 
	}
	
	
	public MainSemanticTag getArg0(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg0())
				return exist;
		return null;
	}	
	
	public Word getArg0Word(){
		MainSemanticTag arg0 = getArg0();
		if(arg0 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg0.name()))
						return wrd;
		return null;
	}
	
	public MainSemanticTag getArg1(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg1())
				return exist;
		return null;
	}
	
	public Word getArg1Word(){
		MainSemanticTag arg1 = getArg1();
		if(arg1 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg1.name()))
						return wrd;
		return null;
	}
	
	public MainSemanticTag getArg2(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg2())
				return exist;
		return null;
	}
	
	public Word getArg2Word() {
		MainSemanticTag arg2 = getArg2();
		if(arg2 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg2.name()))
						return wrd;		
		return null;
	}

	
	public MainSemanticTag getArg3(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg3())
				return exist;
		return null;
	}
	
	public Word getArg3Word() {		
		MainSemanticTag arg3 = getArg3();
		if(arg3 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg3.name()))
						return wrd;
		
		return null;
	}
	
	public MainSemanticTag getArg4(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg4())
				return exist;
		return null;
	}
	
	public Word getArg4Word() {		
		MainSemanticTag arg4 = getArg4();
		if(arg4 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg4.name()))
						return wrd;				
		return null;
	}
	
	public MainSemanticTag getArg5(){
		ArrayList<MainSemanticTag> existingMainArgs = this.getExistingMainSematicArgs();
		for(MainSemanticTag exist:existingMainArgs)
			if(exist.isArg5())
				return exist;
		return null;
	}
	
	public Word getArg5Word() {
		MainSemanticTag arg5 = getArg5();
		if(arg5 != null)
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)					
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg5.name()))
						return wrd;			
		return null;
	}

	
		
	public Word getWord(Node wordNode) {		
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)					
				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
					return wrd;
		return null;
	}
	
	public Word getWord(SemanticTag semanticTag){
		if(semanticTag == null)
			return null;
		
		if(semanticTag.isMainSemanticTag())
			return getWord(semanticTag.convertToMainSemanticTag());
		
		else if(semanticTag.isSubSemanticTag())
			return getWord(semanticTag.convertToSubSemanticTag());
					
		return null;	
	}
	
	public Word getWord(MainSemanticTag mainSemanticTag) {
		if(mainSemanticTag == null)
			return null;
		
		if(mainSemanticTag.isArg0())
			return getArg0Word();

		if(mainSemanticTag.isArg1())
			return getArg1Word();

		if(mainSemanticTag.isArg2())
			return getArg2Word();

		if(mainSemanticTag.isArg3())
			return getArg3Word();

		if(mainSemanticTag.isArg4())
			return getArg4Word();
		
		if(mainSemanticTag.isArg5())
			return getArg5Word();
		
		return null;
	}
	
	
	public Word getWord(SubSemanticTag subSemanticArg) {	
		if(subSemanticArg == null)
			return null;
			
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)		
				if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(subSemanticArg.name()))
					return wrd;
		return null;
	}
	
	//-------------------- end of getter part --------------------------
	
	public void addPhrase(Phrase new_phrase){
		if(_phrases == null)
			_phrases = new ArrayList<Phrase>();
		
		if(new_phrase != null){
			_phrases.add(new_phrase);
			new_phrase._senteceModel = this;
		}
	}
	
	private void makePhrases(Word phraseHead, ArrayList<Word> phraseWords){
		if(phraseHead == null)
			return;		
		
		if(!phraseWords.contains(phraseHead))
			phraseWords.add(phraseHead);
		
		ArrayList<Word> fromThis = getWordsWithSourceNumber(phraseHead._number);
		
		if(Common.isEmpty(fromThis))
			return;
		
		for(Word ph_w:fromThis){
			
			if(ph_w.isAdjective()){
				System.out.println("\nfirst here in makePhrase!");
				phraseHead.addAdjective(ph_w);
			}
			else if(ph_w.isMozaf_elaih()){
				System.out.println("\nfirst here in makePhrase!");
				phraseHead.addMozaf_elaih(ph_w);
			}
			
			makePhrases(ph_w, phraseWords);
		}
	}
	
	private void arrangeWords(){
		Word verb = getVerb();
		
		if(verb == null)
			return;
		
		int vNum = verb._number;
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(vNum);
			
		if(Common.isEmpty(phraseHeads))
			return;
					
		//------------ detect and generate phrases in this sentence ------------
		
		for(Word ph_h:phraseHeads){
			
			ArrayList<Word> phraseWords = new ArrayList<Word>();
			
			makePhrases(ph_h, phraseWords);
			
			Phrase ph = new Phrase(ph_h, phraseWords);
			
			this.addPhrase(ph);
			
			print(""+ ph);
		}		 
	}
	
	private void print(String s){
		System.out.println(s);
	}

}
