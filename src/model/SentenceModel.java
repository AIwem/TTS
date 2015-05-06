package model;

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
	 * words of this sentence.
	 */
	private ArrayList<Word> _words = new ArrayList<Word>();
	
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
	}
	
	public String getOriginalSentence() {
		return NLSentence;
	}
	
	public ArrayList<Word> getWordsWithSourceNumber(int number) {
		ArrayList<Word> allStartingWords = new ArrayList<Word>();		
		
		for(Word wrd:_words)
			if(wrd != null && wrd._number == number)
				allStartingWords.add(wrd);
		
		return allStartingWords;
	}
			
	public void arrangeWords(){
		
	}

}
