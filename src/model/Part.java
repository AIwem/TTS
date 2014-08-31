package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.MyError;

public class Part {
	/**
	 * name of this part
	 */
	public String _name;
	
	/**
	 * this part Part-Of-Speech.
	 */
	public POS	_pos;
	
	/**
	 * this part Semantic-Role-Label.
	 */
	public SRL _srl;
	
	/**
	 * this part Word_Sense_Disambiguation. 
	 * It means mapping of this part to KB concepts.
	 */
	public Node _wsd;
	
	/**
	 * this part Word_Sense_Disambiguation name. 
	 */
	public String _wsd_name;
	/**
	 * this part dependency in noun-phrase or verb-phrase.
	 */
	public DEP _dep;
	
	/**
	 * @param _name
	 */
	public Part(String _name) {
		this._name = _name;
	}
	
	/**
	 * sub_part of this part. for example "کبوتر زخمی" has sub_parts of "کبوتر" and "زخمی".
	 * we have assumed that sub_parts has depth of 1. It means each sub_part has no sub_part in itself.
	 */
	public ArrayList<Part> sub_parts;
	
	/**
	 * @param _name
	 * @param _srl
	 */
	public Part(String _name, SRL _srl) {
		this._name = _name;
		this._srl = _srl;
	}

	/**
	 * @param _name
	 * @param sub_parts
	 * @param _pos
	 * @param _srl
	 * @param wSD
	 */
	public Part(String _name, POS _pos, SRL _srl, Node wSD, ArrayList<Part> sub_parts) {
		this._name = _name;		
		this._pos = _pos;
		this._srl = _srl;
		_wsd = wSD;
		this.sub_parts = sub_parts;
	}
	
	/**
	 * checks weather this part object _srl is SBJ or not?
	 * @return
	 */
	public boolean isSubject(){
		if(_srl == SRL.SBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _srl is OBJ or not?
	 * @return
	 */
	public boolean isObject(){
		if(_srl == SRL.OBJ)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _srl is VERB or not?
	 * @return
	 */
	public boolean isVerb(){
		if(_srl == SRL.VERB)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this part object _srl is ADVERB or not?
	 * @return
	 */
	public boolean isAdverb(){
		if(_srl == SRL.ADV)
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
	
	public Part getMainSub_part(){
		if(!hasSub_parts())
			return this;
		
		for(Part p:sub_parts)
			if(p._dep == DEP.MAIN)
				return p;
		MyError.error("sub_parts has no MAIN part!" + this.getStr());
		return null;
	}
	
	public Part getPreSub_part() {
		if(!hasSub_parts())
			return null;
		
		for(Part p:sub_parts)
			if(p._dep == DEP.PRE)
				return p;
		MyError.error("sub_parts has no PRE part!" + this.getStr());
		return null;
	}	
	
	public Part getPostSub_part() {
		if(!hasSub_parts())
			return null;
		
		for(Part p:sub_parts)
			if(p._dep == DEP.POST)
				return p;
		MyError.error("sub_parts has no POST part!" + this.getStr());
		return null;
	}
	
	public String toString() {
 	//public String getStr() {		
		String rs = "name=";
		if(_name != null) rs += "" + _name; 
		else rs += "-";
		rs += " POS=";
		if(_pos != null) rs += "" + _pos;
		else rs += "-";
		rs += " SRL=";
		if(_srl != null) rs += "" + _srl;
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


	//@Override
	public String getStr() {
	//public String toString() {
		return _name;
	}

	public void set_pos(String pos) {		
		switch(pos){
			case "NOUN": _pos = POS.NOUN; break;
			case "VERB": _pos = POS.VERB; break;
			case "ADJECTIVE": _pos = POS.ADJECTIVE; break;
			case "SETELLITE_ADJECTIVE": _pos = POS.SETELLITE_ADJECTIVE; break;
			case "ADVERB": _pos = POS.ADVERB; break;
			case "ANY": _pos = POS.ANY; break;
			case "UNKNOWN": _pos = POS.UNKNOWN; break;
			default: _pos = POS.UNKNOWN;
		}
	}

	public void set_srl(String srl) {
		switch(srl){
			case "SBJ": _srl = SRL.SBJ; break;
			case "SBJ_P": _srl = SRL.SBJ_P; break;
			case "VERB": _srl = SRL.VERB; break;
			case "VERB_P": _srl = SRL.VERB_P; break;
			case "OBJ": _srl = SRL.OBJ; break;
			case "OBJ_P": _srl = SRL.OBJ_P; break;
			case "ADV": _srl = SRL.ADV; break;
			case "ADV_P": _srl = SRL.ADV_P; break;
			default: _srl = SRL.UNKNOWN;
		}	
	}
	
	
	public void set_wsd_name(String wsd_name) {
		this._wsd_name = wsd_name;
	}
	
	public void set_dep(String dep) {
		switch(dep){
			case "MAIN": _dep = DEP.MAIN; break;
			case "PRE": _dep = DEP.PRE; break;
			case "POST": _dep = DEP.POST; break;
			default: _dep = DEP.UNKOWN;
		}	
	}

	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}	
	
}
