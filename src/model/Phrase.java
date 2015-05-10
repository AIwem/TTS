package model;

import java.util.ArrayList;

public class Phrase {
	
	private Word _headWord = null;
	
	private ArrayList<Word> _words = new ArrayList<Word>();
	
//	private ArrayList<Word> _word_srcs = new ArrayList<Word>();
	
	public Phrase(Word headWord, ArrayList<Word> words){
		this.set_headWord(headWord);
		this.set_words(words);
	}
	
	//-------------------- setter part --------------------------
	/**
	 * @param _headWord the _headWord to set
	 */
	public void set_headWord(Word _headWord) {
		this._headWord = _headWord;
	}
	
	/**
	 * @param _words the _words to set
	 */
	public void set_words(ArrayList<Word> words) {
		if(words != null)
			_words = words;		
	}

	//-------------------- getter part --------------------------
	
	/**
	 * @return the _headWord
	 */
	public Word get_headWord() {
		return _headWord;
	}

	
	public ArrayList<Word> get_words() {
		return _words;
	}

	@Override
	public String toString() {
		String str = "Phrase with head: " + _headWord + " \n";
		for(Word w:_words)
			str += w + "  ";
		return str;
	}
	
}

