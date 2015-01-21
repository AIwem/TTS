package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

/**
 * some SentencePart objects together make a sentence. one or multiple SentencePart objects for sentence subject.
 * only one SentencePart object for sentence verb.
 * one or multiple SentencePart objects for sentence object.
 * one or multiple SentencePart objects for sentence adverb.
 * 
 * each SentencePart object holds different information about the single noun/verb or noun-phrase/verb-phrase.
 * information like its name, 
 * its Part-Of-Speech (e.g. NOUN, ADJECTIVE, VERB, ...)
 * its Semantic-Role-Label (e.g. SUBJECT, VERB, OBJECT, ...)
 *  
 * @author hashemi
 *
 */
public class SentencePart {
	
	/**
	 * senteceModel which this part belongs to.
	 */
	public SentenceModel _senteceModel;
	
	/**
	 * name of this part
	 */
	public String _name;
	
	/**
	 * name of this part in the sentence
	 */
	public String _name_in_sentence;
	
	/**
	 * this part Part-Of-Speech.
	 */
	public POS	_pos = null;
	
	/**
	 * this part SyntaxTag 
	 */
	public DependencyRelationType _syntaxTag = null;
	
	/**
	 * this part Semantic-Role-Label.
	 */
	public SemanticTag _semanticTag = null;
	
	/**
	 * this part Word_Sense_Disambiguation. 
	 * It means mapping of this part to KB concepts.
	 */
	public Node _wsd = null;
	
	/**
	 * this part Word_Sense_Disambiguation name. 
	 */
	public String _wsd_name;
	
	/**
	 * sub_part of this part. for example "کبوتر زخمی" has sub_parts of "کبوتر" and "زخمی".
	 * we have assumed that sub_parts has depth of 1. It means each sub_part has no sub_part in itself.
	 */
	public ArrayList<SentencePart> sub_parts;
	
	/**
	 * this part dependency in noun-phrase or verb-phrase.
	 */
	public DEP _dep = null;
	
	/**
	 * this part number
	 */
	public String _number;
	
	/**
	 * capacities of this SentencePart, it can be verb, noun, adjective or ...
	 */
	public ArrayList<Node> capacities = null;
	

	public SentencePart(String _name, String _name_in_sentence, String number, SentenceModel senteceModel) {
		this._name = _name;
		this._name_in_sentence = _name_in_sentence;
		this._number = number;
		this._senteceModel = senteceModel;
	}
	
	/**
	 * @param _name
	 * @param sentenceModel
	 */
	public SentencePart(String _name, SentenceModel sentenceModel) {
		this._name = _name;
		this._senteceModel = sentenceModel;
	}
		
	/**
	 * @param _name
	 * @param _synTag
	 */
	public SentencePart(String _name, DependencyRelationType _synTag) {
		this._name = _name;
		this._syntaxTag = _synTag;
	}

	/**
	 * @param _name
	 * @param sub_parts
	 * @param _pos
	 * @param _synTag
	 * @param wSD
	 */
	public SentencePart(String _name, String _name_in_sentence, POS _pos, DependencyRelationType _synTag, SemanticTag semanticTag, Node wsd, ArrayList<SentencePart> sub_parts) {
		this._name = _name;		
		this._name_in_sentence = _name_in_sentence;
		this._pos = _pos;
		this._syntaxTag = _synTag;
		this._semanticTag = semanticTag;
		_wsd = wsd;
		this.sub_parts = sub_parts;
	}
	
	/**
	 * checks weather this part object _syntaxTag is SBJ or not?
	 * @return
	 */
	public boolean isSubject(){
		if(_syntaxTag == DependencyRelationType.SBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _syntaxTag is OBJ or not?
	 * @return
	 */
	public boolean isObject(){
		if(_syntaxTag == DependencyRelationType.OBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _syntaxTag is VERB or not?
	 * @return
	 */
	public boolean isVerb(){
		if(_syntaxTag == DependencyRelationType.ROOT)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _syntaxTag  is ADVERB or not?
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
	
	public boolean hasSub_parts(){
		if(sub_parts != null && sub_parts.size() > 0)
			return true;
		return false;
	}
	
	public boolean isPreSub_part(){
		if(_dep == DEP.PRE)
			return true;
		return false;
	}
	
	public boolean isPostSub_part(){
		if(_dep == DEP.POST)
			return true;
		return false;
	}
	
	public boolean isMainSub_part(){
		if(_dep == DEP.MAIN)
			return true;
		return false;
	}
	
	public SentencePart getMainSub_part(){
		if(!hasSub_parts())
			return this;
		
		for(SentencePart p:sub_parts)
			if(p._dep == DEP.MAIN)
				return p;
		MyError.error("sub_parts has no MAIN part!" + this.getStr());
		return null;
	}
	
	public SentencePart getPreSub_part() {
		if(!hasSub_parts())
			return null;
		
		for(SentencePart p:sub_parts)
			if(p._dep == DEP.PRE)
				return p;
		MyError.error("sub_parts has no PRE part!" + this.getStr());
		return null;
	}	
	
	public SentencePart getPostSub_part() {
		if(!hasSub_parts())
			return null;
		
		for(SentencePart p:sub_parts)
			if(p._dep == DEP.POST)
				return p;
		MyError.error("sub_parts has no POST part!" + this.getStr());
		return null;
	}
	
	public ArrayList<Node> getCapacities() {
		return capacities;
	}
	
	//public String toString() {
 	public String getStr() {		
		String rs = "name=";
		if(_name != null) rs += "" + _name; 
		else rs += "-";
		rs += " POS=";
		if(_pos != null) rs += "" + _pos;
		else rs += "-";
		rs += " SYN=";
		if(_syntaxTag != null) rs += "" + _syntaxTag;
		else rs += "-";
		rs += " SEM=";
		if(_semanticTag != null) rs += "" + _semanticTag;
		else rs += "-";
		rs += " WSD=";
		if(_wsd != null) rs += _wsd;
		else rs += "-";
		rs += " WSD_name=";
		if(_wsd_name != null) rs += _wsd_name;
		else rs += "-";
		rs += " sub_parts=";
		if(hasSub_parts()) rs += "\n" + sub_parts;
		else rs += "-";
		rs += " dep=";
		if(_dep != null) rs += _dep;
		else rs += "-";
		rs += "\n\n";
		return rs;
	}	


	@Override
	//public String getStr() {
	public String toString() {
		return _name;
	}

	public void set_pos(String pos) {
		if(pos != null && !pos.equals("") && !pos.equals("-"))
			this._pos = POS.fromString(pos);
		
		if(_pos == null)
			MyError.error("bad pos name " + pos);	
	}

	public void set_syntaxTag(String synTag) {
		if(synTag != null && !synTag.equals("") && !synTag.equals("-"))
			this._syntaxTag = DependencyRelationType.fromString(synTag);
		
		if(_syntaxTag == null)
			MyError.error("bad syntaxTag name " + synTag);	
	}
	
	public void set_semanticTag(String semTag) {
		if(semTag != null && !semTag.equals("") && !semTag.equals("-"))
			_semanticTag = SemanticTag.fromString(semTag);
		
		if(_semanticTag == null)
			MyError.error("bad semantcTag name " + semTag);	
	}
	
	public void set_wsd_name(String wsd_name) {
		if(wsd_name != null && !wsd_name.equals("") && !wsd_name.equals("-"))
			this._wsd_name = wsd_name;
	}
	
	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}
	
	public void set_dep(String dep) {
		if(dep != null && !dep.equals("") && !dep.equals("-"))
			_dep = DEP.fromString(dep);		
//		if(_dep == null)
//			MyError.error("bad dep name " + dep);			
	}
		
	public void setCapacities(ArrayList<Node> capacities) {
		this.capacities = capacities;
	}
		
	
}
