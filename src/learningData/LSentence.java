package learningData;

import java.util.ArrayList;


public class LSentence {

	/**
	 * original sentence in "natural language". 
	 */
	private String NLSentence = "";
	
	/**
	 * Words of this sentence.
	 */
	private ArrayList<LWord> _words = new ArrayList<LWord>();
	
	/**
	 * Phrases of this sentence.
	 */
//	private ArrayList<Phrase> _phrases = new ArrayList<Phrase>();

	public LSentence(){
		
	}
	
	public LSentence(ArrayList<LWord> words) {
		if(words == null)
			return;
		
		_words = words;
		String NLSent = "";
		for(LWord word:_words)
			NLSent += word._wordName + " ";
		
		NLSent = NLSent.trim();
		this.NLSentence = NLSent;
	}
	
	public ArrayList<LWord> getWords() {
		if(_words == null)
			_words = new ArrayList<LWord>();
		return _words;
	}
	
	public void makeNLSentence(){
		if(_words== null || _words.size() == 0)
			return;
		
		String NLSent = "";
		for(LWord word:_words)
			NLSent += word._wordName + " ";
		
		NLSent = NLSent.trim();
		this.NLSentence = NLSent;		
	}
	
	public ArrayList<Integer> getMissedWordIndices(){
		if(_words == null || _words.size() == 0)
			return null;
		
		ArrayList<Integer> missed = new ArrayList<Integer>();
		
		int lastIndex = 0;
		
		for(int i = 0; i < _words.size(); i++){
						
			int curIndex = _words.get(i)._number;
			
			if(curIndex != lastIndex+1){
				int misInterval =  curIndex - lastIndex - 1;
				
				for(int j = 1; j <= misInterval; j++)
					missed.add(lastIndex + j);
				
				lastIndex = curIndex;	
			}else
				lastIndex = curIndex;
		}
				
		return missed;		
	}
	
	public int[] getNumberofWords(){
		int[] numbers = new int[_words.size()];
				
		for(int i = 0; i < numbers.length;i++)
			numbers[i] = _words.get(i)._number;
		
		return numbers;
	}
	
	public boolean hasWordwithNumber(int word_number){
		int[] nums = getNumberofWords();
		
		for(int n:nums)
			if(n == word_number)
				return true;
				
		return false;
	}
	
	public LWord getWordwithNumber(int word_number){
				
		for(LWord wrd:_words)
			if(wrd._number == word_number)
				return wrd;
				
		return null;
	}
	
	public void addWord(LWord word){
		if(word == null)
			return;
		
		if(_words == null)
			_words = new ArrayList<LWord>();
		
		_words.add(word);
	}
	
	public void addAllWords(ArrayList<LWord> words){
		if(words == null || words.size() == 0)
			return;
		
		if(_words == null)
			_words = new ArrayList<LWord>();
		
		_words.addAll(words);
	}
	
	public String getCompleteStr(){
		if (_words == null || _words.size() == 0)
			return "";
		
		String comp = "";
		for(LWord wrd:_words)
			comp += wrd.getCorporaStr() + "\n";
//			comp += wrd.toString() + "\n";
		
		return comp;
	}
	
	public ArrayList<String> getDatasetStr(){
		if (_words == null || _words.size() == 0)
			return null;
		
		ArrayList<String> sentRecords = new ArrayList<String>();
		
		for(LWord wrd:_words)
			sentRecords.addAll(wrd.getDatasetRecord());
		
		return sentRecords;
	}
	
	public String toString(){
		return NLSentence;
	}
	
}
