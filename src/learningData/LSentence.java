package learningData;

import java.util.ArrayList;

import model.Phrase;




public class LSentence {

	/**
	 * original sentence in "natural language". 
	 */
	private String NLSentence;
	
	/**
	 * Words of this sentence.
	 */
	private ArrayList<LWord> _words = new ArrayList<LWord>();
	
	/**
	 * Phrases of this sentence.
	 */
	private ArrayList<Phrase> _phrases = new ArrayList<Phrase>();
	
}
