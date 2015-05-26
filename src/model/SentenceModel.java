package model;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
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
	 * describe whether this sentence instance is a nested one or not.
	 */
	@SuppressWarnings("unused")
	private boolean is_nested = false;
	
	/**
	 * nested sentences in this sentence, it can have depth of 1 only. means a nested sentence, itself can not have nested sentence.  
	 */
	private ArrayList<SentenceModel> _nested_sentences = new ArrayList<SentenceModel>();
	
	/**
	 * The father SentenceModel of this nested sentence, if any. 
	 */
	private SentenceModel father_sentence = null;

	public SentenceModel(SceneModel scene, SentenceModel father_sentence) {
		this.scene = scene;
		
		if(father_sentence != null)
			is_nested = true;
		
		this.father_sentence = father_sentence;	
	}
		
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
		
		//------------------ correct Parser Error --------------
		
			//------------------ story 1: injured pigeon --------------
		
		if(NLSentence.equals("پسرک امروز در راه خانه یک کبوتر زخمی را دید.")){
			Word emroz = getWord(2);
			emroz._srcOfSynTag_number = getVerb()._number;
		}
		if(NLSentence.equals("شروع به دويدن به سمت خانه کرد.")){
			_words.get(0)._syntaxTag = DependencyRelationType.NVE;
			_words.get(5)._syntaxTag = DependencyRelationType.MOZ;
			_words.get(5)._srcOfSynTag_number = 5;
		}
		else if(NLSentence.equals("سپس به طرف اتاقش دوید.")){			
			_words.get(1)._syntaxTag = DependencyRelationType.VPP;
		}
		else if(NLSentence.equals("شکست.")){
			_words.remove(0);
			_words.get(0)._wordName = "شکست";		
			_words.get(0)._number = 1;
			_words.get(1)._number = 2;
			_words.get(1)._srcOfSynTag_number = 1;
		}
		else if(NLSentence.equals("دور انداخت.")){
			_words.remove(2);		
			_words.get(0)._syntaxTag = DependencyRelationType.NVE;
			_words.get(1)._wordName = "انداخت";			
			_words.get(2)._number = 3;
		}
		
		//------------------ story 2: absence f Mohammad --------------
		
		else if(NLSentence.equals("معلم جای خالی او را دید.")){			
			_words.get(1)._syntaxTag = DependencyRelationType.PREDEP;
			_words.get(1)._srcOfSynTag_number = 5;				
		}
		else if(NLSentence.equals("بعد از چند لحظه برگشت.")){			
			_words.get(0)._syntaxTag = DependencyRelationType.ADVRB;
			_words.get(1)._syntaxTag = DependencyRelationType.MOZ;
			_words.get(1)._srcOfSynTag_number = 1;										
		}
		
		//------------------ story 3: Christmas night --------------
		
		else if(NLSentence.equals("دختری با یک جفت پای برهنه از روی تخت بیرون پرید.")){			
			_words.get(3)._syntaxTag = DependencyRelationType.MESU;
			_words.get(3)._srcOfSynTag_number = 5;
			_words.get(4)._syntaxTag = DependencyRelationType.POSDEP;			
			_words.get(4)._srcOfSynTag_number = 2;			
			_words.get(6)._syntaxTag = DependencyRelationType.VPP;
			_words.remove(9);
			_words.get(9)._wordName = "پرید";
			_words.get(9)._number = 10;
			_words.get(10)._number = 11;
			_words.get(0)._srcOfSynTag_number = 10;
			_words.get(1)._srcOfSynTag_number = 10;
			_words.get(6)._srcOfSynTag_number = 10;
			_words.get(8)._srcOfSynTag_number = 10;
			_words.get(10)._srcOfSynTag_number = 10;			
		}
		else if(NLSentence.equals("با یک صورت کوچولو، با موهای فرفری روی نوک پنجه پایش ایستاده.")){			
			_words.get(8)._syntaxTag = DependencyRelationType.ADVRB;
			_words.get(8)._srcOfSynTag_number = 12;
			_words.get(9)._syntaxTag = DependencyRelationType.MOZ;			
			_words.get(10)._syntaxTag = DependencyRelationType.MOZ;
			_words.get(10)._srcOfSynTag_number = 10;
		}
		else if(NLSentence.equals("نوک بینی اش را به شیشه سرد پنجره چسبانده بود.")){
			_words.get(0)._syntaxTag = DependencyRelationType.PREDEP;
			_words.get(0)._srcOfSynTag_number = 4;
			_words.get(2)._syntaxTag = DependencyRelationType.MOZ;			
			_words.get(2)._srcOfSynTag_number = 2;
			_words.get(7)._syntaxTag = DependencyRelationType.MOZ;
			_words.get(7)._srcOfSynTag_number = 6;			
		}
		else if(NLSentence.equals("بابا نوئل سوار بر سورتمه درازی از راه رسید.")){
			_words.get(1)._syntaxTag = DependencyRelationType.ADVRB;						
		}
		else if(NLSentence.equals("چند تا گوزن آن را میکشیدند.")){
			_words.get(0)._syntaxTag = DependencyRelationType.NPREMOD;			
			_words.get(0)._srcOfSynTag_number = 2;
			_words.get(1)._syntaxTag = DependencyRelationType.MESU;
			_words.get(1)._srcOfSynTag_number = 3;			
			_words.get(2)._syntaxTag = DependencyRelationType.SBJ;
			_words.get(2)._srcOfSynTag_number = 6;
			_words.get(3)._syntaxTag = DependencyRelationType.PREDEP;
			_words.get(3)._srcOfSynTag_number = 5;
		}
		
		//------------------ end of correct Parser Error --------------
		
		arrangeWords();
	}	

	//-------------------- setter part --------------------------
	/**
	 * @param scene the scene to set
	 */	
	public void setScene(SceneModel scene) {
		this.scene = scene;
		ArrayList<SentenceModel> nest_sents = get_nested_sentences();
		if(!Common.isEmpty(nest_sents))
			for(SentenceModel sent:nest_sents)
				sent.setScene(scene);
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

	public ArrayList<SentenceModel> get_nested_sentences() {
		return _nested_sentences;
	}
	
	public SentenceModel getFather_sentence() {
		return father_sentence;
	}
	
	public Word getVerb(){
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isVerb())
					return wrd;
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isVerb())
					return wrd;
		
		MyError.error("This sentence has no verb!: " + this);
		return null;
	}
	
	public ArrayList<Word> get_all_verb_in_wsd(){
		ArrayList<Word> verbWords = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words){				
				
				String wsd_name = null;
				
				if(wrd._wsd != null)
					wsd_name = wrd._wsd.getName();
				
				 if(wsd_name != null && wsd_name.contains("#v"))
					 verbWords.add(wrd);
			}
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words){				
				
				String wsd_name = null;
				
				if(wrd._wsd != null)
					wsd_name = wrd._wsd.getName();
				
				 if(wsd_name.contains("#v"))
					 verbWords.add(wrd);
			}
		
		return verbWords;
	}
	
	public ArrayList<Word> get_subjects() {
		
		ArrayList<Word> sbjs = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isSubject())
					sbjs.add(wrd);					
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isSubject())
					sbjs.add(wrd);
					
		if(sbjs.size() > 0)
			return sbjs;
		else
			return null;
	}
	
	public ArrayList<Phrase> get_subject_phrases() {
		
		ArrayList<Phrase> sbj_phrases = new ArrayList<Phrase>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isSubject()){
					Phrase sbj_ph = get_phrase_with_head(wrd);
					if(sbj_ph != null)
						sbj_phrases.add(sbj_ph);
					else{
						MyError.error("This Sentence has subject " + wrd + " in _words but it is not head of a phrase!");
					}
				}
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isSubject()){
					Phrase sbj_ph = wrd._phrase;// get_phrase_with_head(wrd);
					if(sbj_ph != null)
						sbj_phrases.add(sbj_ph);
					else{
						MyError.error("This Sentence has subject " + wrd + " in _prepared_words  but it is not head of a phrase!");
					}
				}
		
		if(sbj_phrases.size() > 0)
			return sbj_phrases;
		else
			return null;
	}
	
	public ArrayList<Word> get_objects() {
		
		ArrayList<Word> objs = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isObject())
					objs.add(wrd);					
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isObject())
					objs.add(wrd);
					
		if(objs.size() > 0)
			return objs;
		else
			return null;
	}
	
	public ArrayList<Phrase> get_object_phrases() {

		ArrayList<Phrase> obj_phrases = new ArrayList<Phrase>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isObject()){
					Phrase obj_ph = get_phrase_with_head(wrd);
					if(obj_ph != null)
						obj_phrases.add(obj_ph);
					else{
						MyError.error("This Sentence has object " + wrd + " in _words but it is not head of a phrase!");
					}
				}
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isObject()){
					Phrase obj_ph = wrd._phrase; //get_phrase_with_head(wrd);
					if(obj_ph != null)
						obj_phrases.add(obj_ph);
					else{
						MyError.error("This Sentence has object " + wrd + " in _prepared_words but it is not head of a phrase!");
					}
				}
		
		if(obj_phrases.size() > 0)
			return obj_phrases;
		else
			return null;
	}
	
	public ArrayList<Word> get_mosnads() {
		
		ArrayList<Word> moss = new ArrayList<Word>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isMosnad())
					moss.add(wrd);					
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isMosnad())
					moss.add(wrd);
					
		if(moss.size() > 0)
			return moss;
		else
			return null;
	}
	
	public ArrayList<Phrase> get_mosnad_phrases() {
		
		ArrayList<Phrase> mos_phrases = new ArrayList<Phrase>();
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)
				if(wrd != null && wrd.isMosnad()){
					Phrase mos_ph = get_phrase_with_head(wrd);
					if(mos_ph != null)
						mos_phrases.add(mos_ph);
					else{
						MyError.error("This Sentence has mosnad " + wrd + " in _words but it is not head of a phrase!");
					}
				}
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
				if(wrd != null && wrd.isMosnad()){
					Phrase mos_ph = wrd._phrase;// get_phrase_with_head(wrd);
					if(mos_ph != null)
						mos_phrases.add(mos_ph);
					else{
						MyError.error("This Sentence has mosnad " + wrd + " in _prepared_words but it is not head of a phrase!");
					}
				}
		
		if(mos_phrases.size() > 0)
			return mos_phrases;
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
	
	public Phrase get_phrase_with_word(Word phrase_word){
		if(phrase_word == null)
			return null;
		
		if(Common.isEmpty(_phrases))
			return null;
		
		for(Phrase ph:_phrases)
			if(ph.get_words() != null && ph.get_words().contains(phrase_word))
				return ph;
		return null;
	}
	
	/**
	 * 
	 * @param phrase_head
	 * @return the Phrase with head phrase_head.
	 */
	public Phrase get_phrase_with_head(Word phrase_head){
		if(phrase_head == null)
			return null;
		
		if(Common.isEmpty(_phrases))
			return null;
		
		for(Phrase ph:_phrases)
			if(ph.get_headWord() == phrase_head)
				return ph;
		return null;
	}
	
	public Phrase get_phrase_with_head(String phrase_head_name){
		if(phrase_head_name == null || phrase_head_name.equals(""))
			return null;
		
		Word ph_head = getWord(phrase_head_name);
		
		if(ph_head == null){
			MyError.error("This sentence hasn't such a phrase with head " + phrase_head_name);
			return null;
		}
		
		return get_phrase_with_head(ph_head);
	}
	
	/**
	 * @return the _words
	 */
	public ArrayList<Word> getWords() {
		return _words;
	}
	
	/**
	 * 
	 * @param word_name the Word of this SentenceModel with _wordName word_name.
	 * @return 
	 */
	public Word getWord(String word_name) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._wordName.equalsIgnoreCase(word_name))
					return w;
		//TODO: is it correct to search in _prepared_words too?!
		if(!Common.isEmpty(_prepared_words))
			for(Word w:_prepared_words)
				if(w._wordName.equalsIgnoreCase(word_name))
					return w;
		return null;
	}
	
	
	/**
	 * 
	 * @param word_number the Word of this SentenceModel with _number word_number.
	 * @return 
	 */
	public Word getWord(int word_number) {
		if(!Common.isEmpty(_words))
			for(Word w:_words)
				if(w._number == word_number)
					return w;
		return null;
	}
	
	public Word getWord(Node wordNode) {		
		
		if(!Common.isEmpty(_words))
			for(Word wrd:_words)					
				if(wrd != null && wrd._wsd != null && wrd._wsd.equalsRelaxed(wordNode))
					return wrd;
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)					
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
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)		
				if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(subSemanticArg.name()))
					return wrd;
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
		
		if(!Common.isEmpty(_prepared_words))		
			for(Word wrd:_prepared_words)
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
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
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
		
		if(!Common.isEmpty(_prepared_words))
			for(Word wrd:_prepared_words)
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
		if(arg0 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg0.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg0.name()))
						return wrd;
		}
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
		if(arg1 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg1.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg1.name()))
						return wrd;
		}
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
		if(arg2 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg2.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg2.name()))
						return wrd;
		}
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
		if(arg3 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg3.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg3.name()))
						return wrd;
		
		}
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
		if(arg4 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg4.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)			
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg4.name()))
						return wrd;			
			
		}
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
		if(arg5 != null){
			if(!Common.isEmpty(_words))
				for(Word wrd:_words)					
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg5.name()))
						return wrd;
			
			if(!Common.isEmpty(_prepared_words))
				for(Word wrd:_prepared_words)					
					if(wrd != null && wrd._semanticTag != null && wrd._semanticTag.name().equals(arg5.name()))
						return wrd;
		}
		return null;
	}
		
	
	//-------------------- end of getter part --------------------------
	
	private void add_nested_sentence(SentenceModel nested) {
		if(nested == null)
			return;
		
		if(_nested_sentences == null)
			_nested_sentences = new ArrayList<SentenceModel>();
		
		nested.is_nested = true;
		
		if(!_nested_sentences.contains(nested))
			_nested_sentences.add(nested);		
	}
	
	private void addWord(Word new_word){
		if(_words == null)
			_words = new ArrayList<Word>();
		
		if(new_word != null){
			_words.add(new_word);
			new_word._senteceModel = this;			
		}
	}
	
	private boolean removeWord(Word word){
		if(word == null)
			return false;
		
		if(_words == null){
			word._senteceModel = null;
			return true;
		}
		
		if(_words.contains(word)){
			word._senteceModel = null;
			_words.remove(word);
			return true;
		}
		return false;
	}
	
	private void addPhrase(Phrase new_phrase){
		if(_phrases == null)
			_phrases = new ArrayList<Phrase>();
		
		if(new_phrase != null){
			_phrases.add(new_phrase);
			new_phrase._senteceModel = this;
		}
	}	
	
	private boolean removePhrase(Phrase phrase){
		if(phrase == null)
			return false;
		
		if(_phrases == null){
			phrase._senteceModel = null;
			return true;
		}
		
		if(_phrases.contains(phrase)){
			phrase._senteceModel = null;
			_phrases.remove(phrase);
			return true;
		}
		return false;
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
		
		int root_num = verb._number;
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(root_num);
			
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
	
	public void make_nested_sentences(){
		
		print("*********************");
		
		Word verb = getVerb();
		
		if(verb == null)
			return;
		
		int root_num = verb._number;
		
		ArrayList<Word> allVerb = get_all_verb_in_wsd();
		
		if(allVerb != null && allVerb.size() > 1){
			for(Word vb:allVerb){
				if(vb._number == root_num)
					continue;				
				
				/*
				 * دویدن: 		 به 		دویدن به سمت خانه
				 * ست: 		او، کجا		او کجاست
				 * ندارد: 		خبر 		خبر ندارد
				 * 	گفتن:		ه- 		-
				 */

				//now vb is a verb in the sentence but not the ROOT verb of this SentnceModel.
				int vb_num = vb._number;
				
				SentenceModel nested= new SentenceModel(this.scene, this);
				
				this.add_nested_sentence(nested);
				
				this.removeWord(vb);
				
				vb._srcOfSynTag_number = 0;
				
				nested.addWord(vb);
				
				ArrayList<Word> startFromVerb = getWordsWithSourceNumber(vb_num);
				
				if(Common.isEmpty(startFromVerb))
					continue;
							
				//------------ detect and generate phrases in this nested sentence ------------
				
				for(Word ph_h:startFromVerb){
					
					ArrayList<Word> phraseWords = new ArrayList<Word>();
					
					makePhrases(ph_h, phraseWords);
					
					Phrase ph = new Phrase(ph_h, phraseWords);
					
					nested.addPhrase(ph);
						
					if(ph.get_words() != null){
						for(Word w:ph.get_words()){
							this.removeWord(w);
							Phrase old_ph = this.get_phrase_with_word(w);
							this.removePhrase(old_ph);
							nested.addWord(w);
						}									
					}					
				}
				
				print("\n nested: " + vb + "   " + nested);
				print(""+ nested.get_phrases());
			}
		}		
		print("\n this "  + verb);
		for(Phrase p:_phrases)
			print("" + p);
	}
	

	/**
	 * This method returns true only when even one of the words is not null and its _wsd is not null too.
	 * @param words
	 * @return
	 */
	public static boolean is_list_or_wsd_empty(ArrayList<Word> words){
		if(Common.isEmpty(words))
			return true;
		
		boolean empty = true;
		
		for(Word wd:words)
			if(wd != null && wd._wsd != null)
				empty = false;
		
		return empty;
	}
	
	@Override
	public String toString() {
		return NLSentence;
	}
	
	private void print(String s){
		System.out.println(s);
	}

	

}
