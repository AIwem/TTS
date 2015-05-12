package model;

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
	
	//-------------------- getter part --------------------------
	
	public String getNLSentence() {
		return NLSentence;
	}	

	public Word getVerb(){
		if(_words != null)
			for(Word wrd:_words)
				if(wrd != null && wrd.isVerb())
					return wrd;
		
		MyError.error("This sentence has no verb!: " + this);
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
				System.out.println("first here in makePhrase!");
				phraseHead.addAdjective(ph_w);
			}
			else if(ph_w.isMozaf_elaih()){
				System.out.println("first here in makePhrase!");
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
