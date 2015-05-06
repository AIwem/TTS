package model;

import java.util.ArrayList;

public class Phrase {
	
	private Word _headWord = null;
	
	private ArrayList<Word> _words = new ArrayList<Word>();
	
	private ArrayList<Word> _word_srcs = new ArrayList<Word>();
	
	
	
	public Phrase(Word headWord, int[] word_srcs, Word[] words){
		this.set_headWord(headWord);
		this.setWords(words);
	}
	
	//-------------------- setter parts --------------------------
	
	public void setWords(Word... words){
		if(_words == null)
			_words = new ArrayList<Word>();
		for(Word w:words)
			this._words.add(w);
	}

	/**
	 * @param _headWord the _headWord to set
	 */
	public void set_headWord(Word _headWord) {
		this._headWord = _headWord;
	}
	

	//-------------------- getter parts --------------------------
	
	/**
	 * @return the _headWord
	 */
	public Word get_headWord() {
		return _headWord;
	}

}

