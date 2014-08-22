package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.Node;

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
	 * @param _name
	 */
	public Part(String _name) {
		this._name = _name;
	}
	
	/**
	 * sub_part of this part. for example "Å”—ò" has sub_parts of "Å”—", "ò".
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
	
	public String getStr() {
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
		rs += " sub_parts=";
		if(sub_parts != null && sub_parts.size() > 0) rs += "" + sub_parts;
		else rs += "-";			
		return rs;
	}	


	@Override
	public String toString() {
		return _name;
	}	
	
	
}
