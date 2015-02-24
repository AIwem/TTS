package model;

import java.util.ArrayList;

import ir.ac.itrc.qqa.semantic.enums.DependencyRelationType;
import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.util.Common;
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
	 * this part Part-Of-Speech.
	 */
	public POS	_pos = null;
	
	/**
	 * this part SyntaxTag 
	 */
	public DependencyRelationType _syntaxTag = null;
	
	/**
	 * the number of the source SentencePart of this part's _syntaxTag
	 */
	private int _sourceOfSynNum = -1;	
	
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
	private ArrayList<SentencePart> sub_parts;
	
	/**
	 * The adjectives of this SentencePart. 
	 */
	private ArrayList<SentencePart> adjectives;
	
	/**
	 * The mozaf_elaih of this SentencePart. 
	 */
	private ArrayList<SentencePart> mozaf_elaih;
	
//	/**
//	 * this part dependency in noun-phrase or verb-phrase.
//	 */
//	public DEP _dep = null;
	
	

	/**
	 * this part number
	 */
	private int _number = -1;
	
	

	/**
	 * capacities of this SentencePart, it can be verb, noun, adjective or ...
	 */
	public ArrayList<Node> capacities = null;
	

	public SentencePart(String _name, String number, SentenceModel senteceModel) {
		this._name = _name;		
		this.set_number(number);
		this._senteceModel = senteceModel;
	}
	
	/**
	 * @param _name
	 * @param sentenceModel
	 */
	public SentencePart(String number, SentenceModel sentenceModel) {
		this.set_number(number);;
		this._senteceModel = sentenceModel;
	}
		
	/**
	 * @param _name
	 * @param _synTag
	 */
	public SentencePart(String _name, DependencyRelationType _synTag, SentenceModel sentenceModel) {
		this._name = _name;
		this._syntaxTag = _synTag;
		this._senteceModel = sentenceModel;
	}

	/**
	 * @param _name
	 * @param _pos
	 * @param _synTag
	 * @param _semanticTag
	 * @param wsd 
	 * @param sub_parts
	 * @param sentenceModel 
	 */
	public SentencePart(String _name, POS _pos, DependencyRelationType _synTag, SemanticTag semanticTag, Node wsd, ArrayList<SentencePart> sub_parts, SentenceModel sentenceModel) {
		this._name = _name;		
		this._pos = _pos;
		this._syntaxTag = _synTag;
		this._semanticTag = semanticTag;
		this._wsd = wsd;
		this.sub_parts = sub_parts;
		this._senteceModel = sentenceModel;
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
	
	public boolean isAdjective(){
		if(_pos == POS.ADJECTIVE || _syntaxTag == DependencyRelationType.NPREMOD || _syntaxTag == DependencyRelationType.NPOSTMOD)
			return true;
		return false;
	}
	
	public boolean isMozaf_elaih(){
		if(_syntaxTag == DependencyRelationType.MOZ)
			return true;
		return false;
	}
	
	public boolean isInfinitive() {
		//TODO: correct this part!
		if(_name.contains("دویدن"))
			return true;
		return false;
	}
	
	public boolean hasSub_parts(){
		return !Common.isEmpty(sub_parts);		
	}
	
	public boolean hasAnyAdjectives(){
		return !Common.isEmpty(adjectives);
	}
	
	public boolean hasAdjective(Node adj_node){
		if(!Common.isEmpty(adjectives))
			for(SentencePart adj:adjectives)
				if(adj._wsd != null && adj._wsd.equalsRelaxed(adj_node))
					return true;
		return false;		
	}
	
	public boolean hasAnyMozaf_elaihs(){
		return !Common.isEmpty(mozaf_elaih);
	}
	
	public boolean hasMozaf_elaih(Node moz_node){
		if(!Common.isEmpty(mozaf_elaih))
			for(SentencePart moz:mozaf_elaih)
				if(moz._wsd != null && moz._wsd.equalsRelaxed(moz_node))
					return true;
		return false;		
	}
	
//	public boolean isPreSub_part(){
//		if(_dep == DEP.PRE)
//			return true;
//		return false;
//	}
//	
//	public boolean isPostSub_part(){
//		if(_dep == DEP.POST)
//			return true;
//		return false;
//	}
//	
//	public boolean isMainSub_part(){
//		if(_dep == DEP.MAIN)
//			return true;
//		return false;
//	}
	
	/**
	 * return the one of its sub_parts which its number is input number
	 * @param number the number of returned sub_part
	 * @return
	 */
	public SentencePart getSub_part(int number){
		if(!hasSub_parts()){
			if(this._number == number)
				return this;
			MyError.error(this + " SentencePart hasn't any sub_part");
			return null;
		}
		
		for(SentencePart p:sub_parts)
			if(p._number == number)
				return p;
		MyError.error(this + " SentencePart hasn't such a sub_part with number " + number);
		return null;
	}
	
	public ArrayList<SentencePart> getSub_parts() {
		return sub_parts;
	}
	
	public int get_sourceOfSynNum() {
		return _sourceOfSynNum;
	}
	
	public ArrayList<SentencePart> getAdjectives() {
		return adjectives;
	}
	
	public SentencePart getAdjective(Node adj_node){
		if(!Common.isEmpty(adjectives))
			for(SentencePart adj:adjectives)
				if(adj._wsd.equalsRelaxed(adj_node))
					return adj;
		return null;		
	}
	
	public ArrayList<SentencePart> getMozaf_elaih() {
		return mozaf_elaih;
	}
	
	public SentencePart getMozaf_elaih(Node moz_node){
		if(!Common.isEmpty(mozaf_elaih))
			for(SentencePart moz:mozaf_elaih)
				if(moz._wsd.equalsRelaxed(moz_node))
					return moz;
		return null;		
	}
	
//	public SentencePart getPreSub_part() {
//		if(!hasSub_parts())
//			return null;
//		
//		for(SentencePart p:sub_parts)
//			if(p._dep == DEP.PRE)
//				return p;
//		MyError.error("sub_parts has no PRE part!" + this.getStr());
//		return null;
//	}	
//	
//	public SentencePart getPostSub_part() {
//		if(!hasSub_parts())
//			return null;
//		
//		for(SentencePart p:sub_parts)
//			if(p._dep == DEP.POST)
//				return p;
//		MyError.error("sub_parts has no POST part!" + this.getStr());
//		return null;
//	}
	
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
	
	public SentencePart getRootPart() {
		if(_senteceModel == null)
			return null;
					
		SentencePart verb = this._senteceModel.getVerb();
		
		if(verb == null)
			return null;
		
		for(SentencePart sPart:this.sub_parts)
			if(sPart._sourceOfSynNum == verb._number)
				return sPart;
		
		return null;
	}
	
	public SentencePart getInnerPart() {
		if(_senteceModel == null)
			return null;
					
		SentencePart verb = this._senteceModel.getVerb();
		
		if(verb == null)
			return null;
				
		SentencePart rootPart = getRootPart();
		
		if(rootPart == null)
			return null;
		
		SentencePart found = rootPart; 
		
//		int flag = -1;
		
		while(found != null){
			boolean flag = false;			
			for(SentencePart sPart:this.sub_parts){				
				if(sPart._sourceOfSynNum == found._number){
					found = sPart;
					flag = true;
					break;
				}
			}			
			if(flag == false)
				return found;
		}
		
		return null;
	}
	
	public VerbType getVerbType(){
		if(_wsd != null && isVerb()){			
			if(_wsd.getName().contains("افتاد"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("برداشت"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("کرد"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("دوید"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("وارد شد"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("خارج شد"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("داد"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("شکست"))
				return VerbType.BASIT_NAMAFOLI;
			if(_wsd.getName().contains("دور انداخت"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("بودن"))
				return VerbType.BASIT_RABTI;
			if(_wsd.getName().contains("دیدن"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("پرسید"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("بغض کردن"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("گذاشت"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("خیره شد"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("به خواب رفت"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("برگشت"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("گفت"))
				return VerbType.BASIT;
					
		}
		return VerbType.UNKNOWN;
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
//		else rs += "-";
//		rs += " dep=";
//		if(_dep != null) rs += _dep;
		else rs += "-";
		rs += "\n";
		return rs;
	}	


	@Override
	//public String getStr() {
	public String toString() {
		return _name;
	}
	
	public void set_number(String number) {
		if(number == null || number.equals("") || number.equals("-"))
			this._number = -1;
		else
			this._number = Integer.parseInt(number);
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
	
	public void set_sourceOfSynNum(String sourceOfSynNum) {
		if(sourceOfSynNum == null || sourceOfSynNum.equals("") || sourceOfSynNum.equals("-"))
			this._sourceOfSynNum = -1;
		else
			this._sourceOfSynNum = Integer.parseInt(sourceOfSynNum);
	}
	
	public void set_semanticTag(String semTag) {
		if(semTag != null && !semTag.equals("") && !semTag.equals("-"))
			_semanticTag = SemanticTag.fromString(semTag);
		
		if(_semanticTag == null)
			MyError.error("bad semantcTag name " + semTag + " for " + this._name);	
	}
	
	public void set_wsd_name(String wsd_name) {
		if(wsd_name != null && !wsd_name.equals("") && !wsd_name.equals("-"))
			this._wsd_name = wsd_name;
	}
	
	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}
	
//	public void set_dep(String dep) {
//		if(dep != null && !dep.equals("") && !dep.equals("-"))
//			_dep = DEP.fromString(dep);		
////		if(_dep == null)
////			MyError.error("bad dep name " + dep);			
//	}
		
	public void setCapacities(ArrayList<Node> capacities) {
		this.capacities = capacities;
	}

	public void setSub_parts(ArrayList<SentencePart> sub_parts) {
		
		this.sub_parts = sub_parts;
		
		boolean hasMoz = false;
		boolean hasAdj = false;
		
		if(adjectives == null)
			adjectives = new ArrayList<SentencePart>();
		else
			hasAdj = true;
		
		if(mozaf_elaih == null)
			mozaf_elaih = new ArrayList<SentencePart>();
		else
			hasMoz = true;
		
		if(sub_parts != null)			
			for(SentencePart subPart:sub_parts)
				if(subPart.isMozaf_elaih()){
					System.out.println("first here in setSub_parts!");
					hasMoz = true;
					addMozaf_elaih(subPart);					
				}
				else if(subPart.isAdjective()){
					System.out.println("first here in setSub_parts!");
					hasAdj = true;
					addAdjective(subPart);							
				}
		
		
		if(!hasAdj)
			adjectives = null;
		
		if(!hasMoz)
			mozaf_elaih = null;
	}
		
	
	public void setAdjectives(ArrayList<SentencePart> adjectives) {
		this.adjectives = adjectives;
	}
	
	public void setMozaf_elaih(ArrayList<SentencePart> mozaf_elaih) {
		this.mozaf_elaih = mozaf_elaih;
	}
	
	/**
	 * 
	 * @param adj
	 * @return an integer, 1 means adj added, 0 means the SentencePart own adjective has merged with adj, and -1 means nothing happened! 
	 */
	public int addAdjective(SentencePart adj){
		if(adj == null)
			return -1;
		
		if(adjectives == null)
			adjectives = new ArrayList<SentencePart>();
		
		if(!hasAdjective(adj._wsd)){
			System.out.println(adj._wsd + " adj added to " + this._name + "\n");
			adjectives.add(adj);
			return 1;
		}
		else{
			System.out.println(this._name + " has this " + adj._wsd + " adj before! so they will merge \n");
			
			SentencePart oldAdj = getAdjective(adj._wsd);
			
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
	 * @return  an integer, 1 means moz added, 0 means the SentencePart own mozaf_elaih has merged with moz, and -1 means nothing happened!
	 */
	public int addMozaf_elaih(SentencePart moz){
		if(moz == null)
			return -1;
		
		if(mozaf_elaih == null)
			mozaf_elaih = new ArrayList<SentencePart>();
		
		if(!hasMozaf_elaih(moz._wsd)){
			System.out.println(moz._wsd + " mozaf added to " + this._name + "\n");
			mozaf_elaih.add(moz);
			return 1;
		}
		else{
			System.out.println(this._name + " has this " + moz._wsd + " mozaf before! so they will merge \n");
			
			SentencePart oldMoz = getMozaf_elaih(moz._wsd);
			
			if(oldMoz != null){
				oldMoz.mergeWith(moz);			
				return 0;
			}					
			return -1;
		}
	}
	
	/**
	 * this method merges called SentencePart with the input SentencePart, newPart.
	 * in merging the called SentencePart is main, it means that only when a parameter in 
	 * called SentencePart is null it is replaced with the newPart SentencePart.
	 * @param newPart the SentencePart is to be merged with this SentencePart.
	 */
	public void mergeWith(SentencePart newPart){
		if(newPart == null)
			return;
		
		if(_senteceModel == null)
			if(newPart._senteceModel != null)
				_senteceModel = newPart._senteceModel;
		
		if(_name == null || _name.equals(""))
			if(newPart._name != null && !newPart._name.equals(""))
				_name = newPart._name;
		
		if(_pos == null || _pos == POS.ANY)
			if(newPart._pos == null && newPart._pos != POS.ANY)
				_pos = newPart._pos;
		
		if(_syntaxTag == null || _syntaxTag == DependencyRelationType.ANY)
			if(newPart._syntaxTag != null && newPart._syntaxTag != DependencyRelationType.ANY)
				_syntaxTag = newPart._syntaxTag;
		
		if(_sourceOfSynNum < 0)
			if(newPart._sourceOfSynNum >= 0)
				_sourceOfSynNum = newPart._sourceOfSynNum;
			
		if(_semanticTag == null)
			if(newPart._semanticTag != null)
				_semanticTag = newPart._semanticTag;

		if(_wsd == null)
			if(newPart._wsd != null)
				_wsd = newPart._wsd;
		
		if(_wsd_name == null || _wsd_name.equals(""))
			if(newPart._wsd_name != null && !newPart.equals(""))
				_wsd_name = newPart._wsd_name;
		
		if(Common.isEmpty(sub_parts)){
			if(!Common.isEmpty(newPart.sub_parts))
				sub_parts = newPart.sub_parts;
		}
		else if(!Common.isEmpty(newPart.sub_parts))
			for(SentencePart sp:sub_parts){
				SentencePart relSubPart = newPart.getSub_part(sp._number);				
				sp.mergeWith(relSubPart);
			}
		
		if(Common.isEmpty(adjectives)){
			if(!Common.isEmpty(newPart.adjectives))
				adjectives = newPart.adjectives;
		}
		else if(!Common.isEmpty(newPart.adjectives))
			for(SentencePart adj:adjectives){
				SentencePart relAdj = newPart.getAdjective(adj._wsd);				
				adj.mergeWith(relAdj);
			}				
		
		if(Common.isEmpty(mozaf_elaih)){
			if(!Common.isEmpty(newPart.mozaf_elaih))
				mozaf_elaih = newPart.mozaf_elaih;
		}
		else if(!Common.isEmpty(newPart.mozaf_elaih))
			for(SentencePart moz:mozaf_elaih){
				SentencePart relMoz = newPart.getMozaf_elaih(moz._wsd);				
				moz.mergeWith(relMoz);
			}				
		
		if(_number < 0)
			if(newPart._number >= 0)
				_number = newPart._number;
		
		if(Common.isEmpty(capacities)){
			if(!Common.isEmpty(newPart.capacities))
				capacities = newPart.capacities;
		}
	}	
	
}
