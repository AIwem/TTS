package learningData;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;

import java.util.ArrayList;

import model.POS;
import model.Phrase;
import model.ScenePart;
import model.SemanticTag;


public class LWord {

	/**
	 * senteceModel which this Word belongs to.
	 */
	public LSentence _sentece;
	
	/**
	 * Phrase which this Word belongs to.
	 */
	public Phrase _phrase;
	
	/**
	 * The record of this word in dataset.
	 * format: 4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_	
	 */
	public String _wordRecord;
	
	/**
	 * number of this Word object in sentence.
	 */
	public int _number = -1;
	
	/**
	 * word String of this Word object.
	 */
	public String _wordName;
	
	/**
	 * great Part-Of-Speech of this Word object.
	 * we assume that it is the main POS that we have used before!
	 */
	public POS	_gPOS = null;
	
	/**
	 * this Word SyntaxTag 
	 */
	public DependencyRelationType _syntaxTag = null;
	
	/**
	 * the number of the source Word of this Word's _syntaxTag
	 */
	public int _srcOfSynTag_number = -1;	
	
	/**
	 * Word_Sense_Disambiguation of this Word object.
	 * It means mapping of this Word to KB concepts.
	 */
	public Node _wsd = null;
	
	/**
	 * the String name of Word_Sense_Disambiguation of this Word object. 
	 */
	public String _wsd_name;
	
	/**
	 * The super category of Word_Sense_Disambiguation of this Word object. 
	 * It means mapping of this Word to main categories of KB concepts.
	 */
	public Node _super_wsd = null;
	
	/**
	 * the String name of the super Word_Sense_Disambiguation of this Word object. 
	 */
	public String _super_wsd_name;
	
	/**
	 * The ScenePart which this word has.
	 */
	public ScenePart _scenePart;

	/**
	 * this Word Semantic-Role-Label.
	 */
	public ArrayList<SemanticTag> _semanticTags = null;
	
	/**
	 *  
	 * The adjectives of this Word. 
	 */
	private ArrayList<LWord> adjectives;
	
	/**
	 *  
	 * The mozaf_elaih of this Word. 
	 */	
	private ArrayList<LWord> mozaf_elaih;

	public LWord(String wordStr) {
		
		_wordRecord = wordStr;
		
		_wordRecord = _wordRecord.trim();
		
		//wordRecord format: 4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_	
		String[] parts = _wordRecord.split("(\t)+");
		
		if(parts == null)
			return;
		
		for(int i = 0; i < parts.length; i++)
			if(parts[i] != null)
				parts[i] = parts[i].trim();
			
		
		_number = Integer.parseInt(parts[0]);
		
		_wordName = parts[1];
		
		_gPOS = POS.fromString(parts[2]);
		
		_syntaxTag = DependencyRelationType.fromString(parts[3]);
		
		_srcOfSynTag_number = Integer.parseInt(parts[4]);
		
		_wsd_name = parts[5];
		
		_super_wsd_name = parts[6];
		
		_scenePart = ScenePart.fromString(parts[7]);
		
		_semanticTags = new ArrayList<SemanticTag>();
		
		for(int i = 10; i < parts.length; i++)
			if(parts[i] != null && !parts[i].equalsIgnoreCase("_"))
				_semanticTags.add(SemanticTag.fromString(parts[i]));
		
	}
	//	wordRecord format: 4	برادر	N	OBJ	5	برادر§n-14090	نفر§n-13075	role	_	_	Arg1	_	
	public String toString(){
		String str = "";
		
		str += _number + "\t" + _wordName + "\t" + _gPOS + "\t" + _syntaxTag + "\t" + _srcOfSynTag_number + "\t";
		str += _wsd_name + "\t" + _super_wsd_name + "\t" + _scenePart + "\t";
		for(SemanticTag st:_semanticTags)
			str += st + "\t";
		
		return str;
	}
	
	
}
