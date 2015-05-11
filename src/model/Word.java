package model;



import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
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
	 * TODO: check to be deleted or not!
	 * 
	 * The adjectives of this Word. 
	 */
	private ArrayList<Word> adjectives;
	
	/**
	 * TODO: check to be deleted or not!
	 * 
	 * The mozaf_elaih of this Word. 
	 */	
	private ArrayList<Word> mozaf_elaih;
	
	/**
	 * TODO: check to be deleted or not!
	 * 
	 * capacities of this Word, it can be verb, noun, adjective or ...
	 */
	private ArrayList<Node> capacities = null;
	
	
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
	
	//-------------------- is part --------------------------
	
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
	 * checks weather this Word object _syntaxTag is ADVERB or not?
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
	
 	//-------------------- setter part --------------------------
 	
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
	
	public void setCapacities(ArrayList<Node> capacities) {
		this.capacities = capacities;
	}
	
	public void setAdjectives(ArrayList<Word> adjectives) {
		this.adjectives = adjectives;
	}
	
	public void setMozaf_elaih(ArrayList<Word> mozaf_elaih) {
		this.mozaf_elaih = mozaf_elaih;
	}

	//-------------------- getter part --------------------------
	
	public ArrayList<Node> getCapacities() {
		return capacities;
	}
	
	public Node getCapacity(String capacity_name) {		
		if(capacity_name == null || capacity_name.equals(""))
			return null; 
			
		if(!Common.isEmpty(capacities))
			for(Node cap:capacities)
				if(capacity_name.equals(cap.getName()))
					return cap;
		return null;
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
 	 
 	public ArrayList<Word> getAdjectives() {
		return adjectives;
	}
 	 
 	public Word getAdjective(Node adj_node){
		if(!Common.isEmpty(adjectives))
			for(Word adj:adjectives)
				if(adj._wsd != null && adj._wsd.equalsRelaxed(adj_node))
					return adj;
		return null;		
	}
	
	public ArrayList<Word> getMozaf_elaih() {
		return mozaf_elaih;
	}
	
	public Word getMozaf_elaih(Node moz_node){
		if(!Common.isEmpty(mozaf_elaih))
			for(Word moz:mozaf_elaih)
				if(moz._wsd != null && moz._wsd.equalsRelaxed(moz_node))
					return moz;
		return null;		
	}
 	
  	//-------------------- add part -----------------------------
 	 
 	/**
 	 * 
 	 * @param adj
 	 * @return an integer, 1 means adj added, 0 means the Word own adjective has merged with adj, and -1 means nothing happened! 
 	 */
 	public int addAdjective(Word adj){
 		if(adj == null)
 			return -1;
 		
 		if(adjectives == null)
 			adjectives = new ArrayList<Word>();
 		
 		if(!hasAdjective(adj._wsd)){
 			System.out.println(adj._wsd + " adj added to " + this._word + "\n");
 			adjectives.add(adj);
 			return 1;
 		}
 		else{
 			System.out.println(this._word + " has this " + adj._wsd + " adj before! so they will merge \n");
 			
 			Word oldAdj = getAdjective(adj._wsd);
 			
 			if(oldAdj != null){
 				oldAdj.mergeWith(adj);			
 				return 0;
 			}
 			return -1;
 		}
 	}
 	/**
 	 * 
 	 * @param moz
 	 * @return  an integer, 1 means moz added, 0 means the Word own mozaf_elaih has merged with moz, and -1 means nothing happened!
 	 */
 	public int addMozaf_elaih(Word moz){
 		if(moz == null)
 			return -1;
 		
 		if(mozaf_elaih == null)
 			mozaf_elaih = new ArrayList<Word>();
 		
 		if(!hasMozaf_elaih(moz._wsd)){
 			System.out.println(moz._wsd + " mozaf added to " + this._word + "\n");
 			mozaf_elaih.add(moz);
 			return 1;
 		}
 		else{
 			System.out.println(this._word + " has this " + moz._wsd + " mozaf before! so they will merge \n");
 			
 			Word oldMoz = getMozaf_elaih(moz._wsd);
 			
 			if(oldMoz != null){
 				oldMoz.mergeWith(moz);			
 				return 0;
 			}					
 			return -1;
 		}
 	}
 	 
 	//-------------------- has part -----------------------------
 	
 	public boolean hasAnyAdjectives(){
		return !Common.isEmpty(adjectives);
	}
	
	public boolean hasAdjective(Node adj_node){
		if(!Common.isEmpty(adjectives))
			for(Word adj:adjectives)
				if(adj._wsd != null && adj._wsd.equalsRelaxed(adj_node))
					return true;
		return false;		
	}
	
	public boolean hasAnyMozaf_elaihs(){
		return !Common.isEmpty(mozaf_elaih);
	}
	
	public boolean hasMozaf_elaih(Node moz_node){
		if(!Common.isEmpty(mozaf_elaih))
			for(Word moz:mozaf_elaih)
				if(moz._wsd != null && moz._wsd.equalsRelaxed(moz_node))
					return true;
		return false;		
	}
	
 	//-------------------- end of part --------------------------
	
	/**
	 * this method merges called Word with the input Word, newWord.
	 * in merging the called Word is main, it means that only when a parameter in 
	 * called Word is null it is replaced with the newWord Word.
	 * @param newWord the Word is to be merged with this Word.
	 */
	public void mergeWith(Word newWord){
		if(newWord == null)
			return;
		
		if(_senteceModel == null)
			if(newWord._senteceModel != null)
				_senteceModel = newWord._senteceModel;
		
		if(_word == null || _word.equals(""))
			if(newWord._word != null && !newWord._word.equals(""))
				_word = newWord._word;
		
		if(_gPOS == null || _gPOS == POS.UNKNOWN)
			if(newWord._gPOS == null && newWord._gPOS != POS.UNKNOWN)
				_gPOS = newWord._gPOS;
		
		if(_syntaxTag == null || _syntaxTag == DependencyRelationType.ANY)
			if(newWord._syntaxTag != null && newWord._syntaxTag != DependencyRelationType.ANY)
				_syntaxTag = newWord._syntaxTag;
		
		if(_srcOfSynTag_number < 0)
			if(newWord._srcOfSynTag_number >= 0)
				_srcOfSynTag_number = newWord._srcOfSynTag_number;
			
		if(_semanticTag == null)
			if(newWord._semanticTag != null)
				_semanticTag = newWord._semanticTag;

		if(_wsd == null)
			if(newWord._wsd != null)
				_wsd = newWord._wsd;
		
		if(_wsd_name == null || _wsd_name.equals(""))
			if(newWord._wsd_name != null && !newWord.equals(""))
				_wsd_name = newWord._wsd_name;
		
//		if(Common.isEmpty(sub_parts)){
//			if(!Common.isEmpty(newWord.sub_parts))
//				sub_parts = newWord.sub_parts;
//		}
//		else if(!Common.isEmpty(newWord.sub_parts))
//			for(Word_old sp:sub_parts){
//				Word_old relSubPart = newWord.getSub_part(sp._number);				
//				sp.mergeWith(relSubPart);
//			}
		
		if(Common.isEmpty(adjectives)){
			if(!Common.isEmpty(newWord.adjectives))
				adjectives = newWord.adjectives;
		}
		else if(!Common.isEmpty(newWord.adjectives))
			for(Word adj:adjectives){
				Word relAdj = newWord.getAdjective(adj._wsd);				
				adj.mergeWith(relAdj);
			}				
		
		if(Common.isEmpty(mozaf_elaih)){
			if(!Common.isEmpty(newWord.mozaf_elaih))
				mozaf_elaih = newWord.mozaf_elaih;
		}
		else if(!Common.isEmpty(newWord.mozaf_elaih))
			for(Word moz:mozaf_elaih){
				Word relMoz = newWord.getMozaf_elaih(moz._wsd);				
				moz.mergeWith(relMoz);
			}				
		
		if(_number < 0)
			if(newWord._number >= 0)
				_number = newWord._number;
		
		if(Common.isEmpty(capacities)){
			if(!Common.isEmpty(newWord.capacities))
				capacities = newWord.capacities;
		}
	}	
	
	
	private void print(String s){
		System.out.println(s);
	}
		
}
