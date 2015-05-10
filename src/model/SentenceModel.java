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
		arrangeWords();
	}
	
	public String getOriginalSentence() {
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
	
	private ArrayList<Word> getWordsWithSourceNumber(int number) {
		
		ArrayList<Word> allStartingWords = new ArrayList<Word>();		
		
		if(_words != null)
			for(Word wrd:_words)
				if(wrd != null && wrd._srcOfSynTag_number == number)
					allStartingWords.add(wrd);
			
		return allStartingWords;
	}
	
	private void makePhrases(Word phraseHead, ArrayList<Word> phraseWords){
		if(phraseHead == null)
			return;		
		
		if(!phraseWords.contains(phraseHead))
			phraseWords.add(phraseHead);
		
		ArrayList<Word> fromThis = getWordsWithSourceNumber(phraseHead._number);
		
		if(Common.isEmpty(fromThis))
			return;
		
//		for(Word w:fromThis)
//			if(!phraseWords.contains(w))
//				phraseWords.add(w);
		
		for(Word ph_w:fromThis)					
			makePhrases(ph_w, phraseWords);			
	
	}
	
	private void arrangeWords(){
		Word verb = getVerb();
		
		if(verb == null)
			return;
		
		int vNum = verb._number;
		
		ArrayList<Word> phraseHeads = getWordsWithSourceNumber(vNum);
		
		print("verb num: " + vNum);
		
		for(Word w:phraseHeads)
			print(w._number + "\t" + w._word);
		
		if(Common.isEmpty(phraseHeads))
			return;
					
		for(Word ph_h:phraseHeads){
			
			ArrayList<Word> phraseWords = new ArrayList<Word>();
			
			makePhrases(ph_h, phraseWords);
			
			print("phrase with head " + ph_h);
			
			for(Word w:phraseWords)
				print(w._number + "\t" + w._word);
		}
		
//		print("phrase words: \n");
//		
//		for(Word ph_w:phraseWords)
//			print(ph_w._number + "\t" + ph_w._word);
//		 
	}
	
	private void print(String s){
		System.out.println(s);
	}
		

}
