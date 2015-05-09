package model;



import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

/**
 * Some Word instances together make a Phrase and some Phrase instances together make a Sentence.
 * 
 * Each Word object holds different information about itself.
 * information like its _number, _word, _lem, _pos, _synTAg, _semTag, ... 
 *  
 * @author hashemi
 *
 */
public class Word {
	
	/**
	 * TODO: check to delete or not!
	 * senteceModel which this Word belongs to.
	 */
	public SentenceModel _senteceModel;
	
	/**
	 * Phrase which this Word belongs to.
	 */
	public Phrase _phrase;
	
	/**
	 * number of this Word object in sentence.
	 */
	public int _number = -1;
	
	/**
	 * word String of this Word object.
	 */
	public String _word;
	
	/**
	 * lem of this Word object.
	 */
	public String _lem;
	
	/**
	 * great Part-Of-Speech of this Word object.
	 * we assume that it is the main POS that we have used before!
	 */
	public POS	_gPOS = null;
	
	/**
	 * properties of this Word object.
	 */
	public String _props = null;
	
	/**
	 * this Word SyntaxTag 
	 */
	public DependencyRelationType _syntaxTag = null;
	
	/**
	 * the number of the source Word of this Word's _syntaxTag
	 */
	public int _srcOfSynTag_number = -1;	
	
	/**
	 * this Word Semantic-Role-Label.
	 */
	public SemanticTag _semanticTag = null;
	
	/**
	 * Word_Sense_Disambiguation of this Word object.. 
	 * It means mapping of this Word to KB concepts.
	 */
	public Node _wsd = null;
	
	/**
	 * the String name of Word_Sense_Disambiguation of this Word object. 
	 */
	public String _wsd_name;
	
	/**
	 * This constructor gets an input string in the format 
	 * "1		پسرک		پسرک		N		N	number=SING|senID=001	11					SBJ			_	_"
	 * these parts are respectively:
	 * 
	 * number	name	lem		gPOS	_	properties				_srcOfSynTag_number syntaxTag	_	_
	 * @param wStr the String definition of this word as parsed by Syntax Parser.
	 */
	public Word(String wStr, SentenceModel sentence) {
//		print(wStr);
				
		String[] parts = wStr.split("(\t)+");
		
		if(parts.length != 10){			
			MyError.error("Bad sentence information format " + wStr + " parts-num " + parts.length);
			return;
		}
		this._senteceModel = sentence;
		
		this.set_number(parts[0].trim());
		
		this._word = parts[1].trim();
		
		this._lem = parts[2].trim();
		
		this.set_gPOS(parts[3]);
		
		this._props = parts[5];
		
		this.set_srcOfSynTag_number(parts[6]);
		
		this.set_syntaxTag(parts[7]);
		
		//discarding parts[4], [8] and [9]
		
		print(getStr2());
	}
	
	@Override
	//public String getStr() {
	public String toString() {
		return _word;
	}
	
	/**
	 * checks weather this Word object _syntaxTag is SBJ or not?
	 * @return
	 */
	public boolean isSubject(){
		if(_syntaxTag == DependencyRelationType.SBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this Word object _syntaxTag is OBJ or not?
	 * @return
	 */
	public boolean isObject(){
		if(_syntaxTag == DependencyRelationType.OBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this Word object _syntaxTag is VERB or not?
	 * @return
	 */
	public boolean isVerb(){
		if(_syntaxTag == DependencyRelationType.ROOT)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this Word object _syntaxTag  is ADVERB or not?
	 * ADVRB: to constraint verbs, nouns, adjectives or ...
	 * NADV: prepositional complements of nouns
	 * ADVC: adverbial complements of verbs
	 * AVCONJ: adverbs with equal positions
	 * @return
	 */
	public boolean isAdverb(){
		if(_syntaxTag == DependencyRelationType.ADVRB || _syntaxTag == DependencyRelationType.NADV ||
				_syntaxTag == DependencyRelationType.ADVC || _syntaxTag == DependencyRelationType.AVCONJ) // || maybe DependenctRelationType.VPP
			return true;
		return false;		
	}
	
	public boolean isAdjective(){
		if(_gPOS == POS.ADJ || _syntaxTag == DependencyRelationType.NPREMOD || _syntaxTag == DependencyRelationType.NPOSTMOD)
			return true;
		return false;
	}
	
	public boolean isMozaf_elaih(){
		if(_syntaxTag == DependencyRelationType.MOZ)
			return true;
		return false;
	}
	
	public boolean isInfinitive() {
		//TODO: correct this Word!
		if(_word.contains("دویدن"))
			return true;
		return false;
	}
	
	//public String toString() {
 	public String getStr() {
 		String rs = "number=" + _number;		
		rs += " name=";
		if(_word != null) rs += "" + _word; 
		else rs += "-";
		rs += " POS=";
		if(_gPOS != null) rs += "" + _gPOS;
		else rs += "-";
		rs += " Syn=";
		if(_syntaxTag != null) rs += "" + _syntaxTag;
		else rs += "-";
		rs += " SynSrc=" + _srcOfSynTag_number;
		rs += " Sem=";
		if(_semanticTag != null) rs += "" + _semanticTag;
		else rs += "-";
		rs += " wsd=";
		if(_wsd != null) rs += _wsd;
		else rs += "-";
		rs += " Wsd_name=";
		if(_wsd_name != null) rs += _wsd_name;
		else rs += "-";
		rs += "\n";
		return rs;
	}	

 	//public String toString() {
 	 public String getStr2() {
 	 		String rs = "" + _number;		
 			rs += "\t";
 			if(_word != null) rs += "" + _word; 
 			else rs += "-";
 			rs += "\t";
 			if(_gPOS != null) rs += "" + _gPOS;
 			else rs += "-";
 			rs += "\t";
 			if(_syntaxTag != null) rs += "" + _syntaxTag;
 			else rs += "-";
 			rs += "\t" + _srcOfSynTag_number;
 			rs += "\n";
 			return rs;
 		}
 	
	public void set_number(String number) {
		if(number == null || number.equals("") || number.equals("-"))
			this._number = -1;
		else
			this._number = Integer.parseInt(number);
	}
	
	public void set_gPOS(String gPos) {
		if(gPos != null && !gPos.equals("") && !gPos.equals("-"))
			this._gPOS = POS.fromString(gPos);
		
		if(_gPOS == null)
			MyError.error("bad gPOS name " + gPos);	
	}

	public void set_propoerties(String props) {
		if(props != null && !props.equals("") && !props.equals("-"))
			this._props = props;
	}	
	
	public void set_syntaxTag(String synTag) {
		if(synTag != null && !synTag.equals("") && !synTag.equals("-"))
			this._syntaxTag = DependencyRelationType.fromString(synTag);
		
		if(_syntaxTag == null)
			MyError.error("bad syntaxTag name " + synTag);	
	}	
	
	public void set_srcOfSynTag_number(String srcOfSynTagNum) {
		if(srcOfSynTagNum == null || srcOfSynTagNum.equals("") || srcOfSynTagNum.equals("-"))
			this._srcOfSynTag_number = -1;
		else
			this._srcOfSynTag_number = Integer.parseInt(srcOfSynTagNum);
	}
	
	public void set_semanticTag(String semTag) {
		if(semTag != null && !semTag.equals("") && !semTag.equals("-"))
			_semanticTag = SemanticTag.fromString(semTag);
		
		if(_semanticTag == null)
			MyError.error("bad semantcTag name " + semTag + " for " + this._word);	
	}
	
	public void set_wsd_name(String wsd_name) {
		if(wsd_name != null && !wsd_name.equals("") && !wsd_name.equals("-"))
			this._wsd_name = wsd_name;
	}
	
	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}
	
	private void print(String s){
		System.out.println(s);
	}
		
}
