package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.Node;

public class Part {
	
	public String _name;
	public ArrayList<Part> sub_parts;
	public POS	_pos;
	public SRL _srl;
	public Node _wsd;
	
	/**
	 * @param _name
	 */
	public Part(String _name) {
		this._name = _name;
	}
	
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
	public Part(String _name, ArrayList<Part> sub_parts, POS _pos, SRL _srl, Node wSD) {
		this._name = _name;
		this.sub_parts = sub_parts;
		this._pos = _pos;
		this._srl = _srl;
		_wsd = wSD;
	}

	@Override
	public String toString() {
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
		if(sub_parts != null) rs += "" + sub_parts;
		else rs += "-";			
		return rs;
	}	
	
	
}
