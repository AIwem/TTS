package model;



import java.util.ArrayList;

import org.omg.CORBA._PolicyStub;

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
	 * dose this word belongs to a phrase or it is independent. 
	 */
	public boolean _isDepended = false;
	
	/**
	 * number of this Word object in sentence.
	 */
	public int _number = -1;
	
	/**
	 * word String of this Word object.
	 */
	public String _wordName;
	
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
	 * this Word Semantic-Role-Label in the simple format (without sub parts).
	 */
	public SimpleSemanticTag _simpleSemanticTag = null;
	
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
	 * the String name of super Node of Word_Sense_Disambiguation of this Word object. 
	 */
	public String _wsd_superNode_name;
	
	/**
	 * the class tag of this word, it is multi-class. 
	 */
	public ScenePart _multiClassTag; 
	
	public String _dataSetRecord;
	
	/**
	 *  
	 * The adjectives of this Word. 
	 */
	private ArrayList<Word> adjectives;
	
	/**
	 *  
	 * The mozaf_elaih of this Word. 
	 */	
	private ArrayList<Word> mozaf_elaih;
	
	/**
	 *  
	 * semantic capacities of this Word, it can be verb, noun, adjective or ...
	 */
	private ArrayList<Node> semantic_capacities = null;
	
	/**
	 *  
	 * visual capacities of this Word, it can be verb, noun, adjective or ...
	 */
	private ArrayList<Node> visual_capacities = null;
	
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
		
		if(parts != null && parts.length != 10){			
			MyError.error("Bad sentence information format " + wStr + " parts-num " + parts.length);
			return;
		}
		this._senteceModel = sentence;
		
		this.set_number(parts[0].trim());
		
		this._wordName = parts[1].trim();
		
		this._lem = parts[2].trim();
		
		this.set_gPOS(parts[3]);
		
		this._props = parts[5];
		
		this.set_srcOfSynTag_number(parts[6]);
		
		this.set_syntaxTag(parts[7]);
		
		//discarding parts[4], [8] and [9]
		
		print(getStr2());
	}
	
	/**
	 * 
	 * @param wStr 
	 * @param sentence
	 * @param isFull is data for this word full or not?
	 */
	public Word(String wStr, SentenceModel sentence, boolean isFull) {
		
		this._dataSetRecord = wStr;
		
		if(isFull){// it is full record of a word
			//sample format:
			//2	یوسف		N		MOZ		1	یوسف§n-23957	نفر§n-13075		role	_	_		_		_	_
			//8	می‌خواست	V		PRD		7	خواستن§v-9670	رخداد§n-13136		no		Y	خواستن.360	Arg1	_	_	_		
					
			String[] parts = wStr.split("(\t)+");
			
			if(parts.length > 7){
		
				this._senteceModel = sentence;
				
				this.set_number(parts[0].trim());
				
				this._wordName = parts[1].trim();
				
				this.set_gPOS(parts[2]);
				
				this.set_syntaxTag(parts[3]);
				
				this.set_srcOfSynTag_number(parts[4]);
				
				this.set_wsd_name(parts[5]);
				
				this.set_wsd_superNode_name(parts[6]);
						
				this.set_multiClassTag(parts[7]);
			}
			
			for(int i = 8; i < parts.length; i++)
				if(parts[i] != null && !parts[i].matches("_"))
					this.set_simpleSemanticTag(parts[i]);				
		}
		else{//it lacks some elements of information such as wsd_name, wsd_superNode_name, multiClassTag.
			//sample format: 7	کوچکتر	ADJ	MOS	8	_	_	Arg2	
		
			String[] parts = wStr.split("(\t)+");
			
			if(parts.length > 4){
				
				this._senteceModel = sentence;
								
				this.set_number(parts[0].trim());
				
				this._wordName = parts[1].trim();
				
				this.set_gPOS(parts[2]);
				
				this.set_syntaxTag(parts[3]);
				
				this.set_srcOfSynTag_number(parts[4]);
			}		
			
			for(int i = 5; i < parts.length; i++)
				if(parts[i] != null && !parts[i].matches("_"))
					this.set_simpleSemanticTag(parts[i]);			
		
		}
//		print(getStr2());
	}
	
	/**
	 * @param senteceModel
	 * @param phrase
	 * @param number
	 * @param wordName
	 * @param lem
	 * @param gPOS
	 * @param props
	 * @param syntaxTag
	 * @param srcOfSynTag_number
	 * @param semanticTag
	 * @param wsd
	 * @param wsd_name
	 */
	public Word(SentenceModel senteceModel, Phrase phrase, int number, String wordName, String lem, POS gPOS, String props, 
			DependencyRelationType syntaxTag, int srcOfSynTag_number, SemanticTag semanticTag, Node wsd, String wsd_name) {
		super();
		this._senteceModel = senteceModel;
		this._phrase = phrase;
		this._number = number;
		this._wordName = wordName;
		this._lem = lem;
		this._gPOS = gPOS;
		this._props = props;
		this._syntaxTag = syntaxTag;
		this._srcOfSynTag_number = srcOfSynTag_number;
		this._semanticTag = semanticTag;
		this._wsd = wsd;
		this._wsd_name = wsd_name;
	}
	
	/**
	 * @param senteceModel
	 * @param phrase
	 * @param number
	 * @param wordName
	 * @param gPOS
	 * @param syntaxTag
	 * @param srcOfSynTag_number
	 * @param semanticTag
	 * @param wsd
	 */
	public Word(SentenceModel senteceModel, Phrase phrase, int number, String wordName, POS gPOS, 
			DependencyRelationType syntaxTag, int srcOfSynTag_number, SemanticTag semanticTag, Node wsd) {
		super();
		this._senteceModel = senteceModel;
		this._phrase = phrase;
		this._number = number;
		this._wordName = wordName;		
		this._gPOS = gPOS;		
		this._syntaxTag = syntaxTag;
		this._srcOfSynTag_number = srcOfSynTag_number;
		this._semanticTag = semanticTag;
		this._wsd = wsd;		
	}
	
	
	public Word makeDeepCopy(SentenceModel copy_sentence, Phrase copy_phrase){
		
		if(copy_sentence == null)
			copy_sentence = this._senteceModel;
		
		if(copy_phrase == null)
			copy_phrase = this._phrase;
		
		Word cpWord = new Word(copy_sentence, copy_phrase, -1, this._wordName, this._lem, this._gPOS,
				this._props, this._syntaxTag, -1, this._semanticTag, this._wsd, this._wsd_name);
				
		if(!Common.isEmpty(adjectives)){
			cpWord.adjectives = new ArrayList<Word>();
			for(Word adj:adjectives)
				cpWord.adjectives.add(adj.makeDeepCopy(copy_sentence, copy_phrase));
		}
		
		if(!Common.isEmpty(mozaf_elaih)){
			cpWord.mozaf_elaih = new ArrayList<Word>();
			for(Word moz:mozaf_elaih)
				cpWord.mozaf_elaih.add(moz.makeDeepCopy(copy_sentence, copy_phrase));
		}
		
		if(!Common.isEmpty(semantic_capacities)){
			cpWord.semantic_capacities = new ArrayList<Node>();		
			for(Node cap:semantic_capacities)
				cpWord.semantic_capacities.add(cap.makeCopy(cap.getName()));
		}
		
		if(!Common.isEmpty(visual_capacities)){
			cpWord.visual_capacities = new ArrayList<Node>();		
			for(Node cap:visual_capacities)
				cpWord.visual_capacities.add(cap.makeCopy(cap.getName()));
		}
		
		return cpWord;
	}
	
	@Override
	//public String getStr() {
	public String toString() {
		return _wordName;
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
	 * checks weather this Word object _syntaxTag is MOS or not?
	 * @return
	 */
	public boolean isMosnad(){
		if(_syntaxTag == DependencyRelationType.MOS)
			return true;
		return false;		
	}
	
	/**
	 * checks weather this Word object _syntaxTag is VERB or not?
	 * @return
	 */
	public boolean isVerb(){
		if(_syntaxTag == DependencyRelationType.ROOT || _srcOfSynTag_number == 0)
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
	
	public boolean isJunk(){
		if(_wordName == null)
			return false;
		
		_wordName = _wordName.trim();
		
		//TODO: complete list of stop words.
		String[] junks = {"و", "به", "از", "با", "را", "در", "تا"};
		
		for(String junk:junks)
			if(_wordName.equalsIgnoreCase(junk))
				return true;
		
		POS[] junkPOS = {POS.PUNC, POS.CONJ, POS.NUM}; 
		for(POS junk:junkPOS)
			if(_gPOS == junk)
				return true;
		return false;
	}
	
	public boolean isInfinitive() {
		//TODO: correct this Word!
		if(_wordName.contains("دویدن"))
			return true;
		if(_wordName.contains("گفتن"))
			return true;
		
		return false;
	}
	
 	//-------------------- setter part --------------------------
 	
	public void set_number(String number) {
//		print(this._wordName);
		if(number == null || number.equals("") || number.equals("-"))
			this._number = -1;
		else{
//			print(this._dataSetRecord);
			this._number = Integer.parseInt(number);
		}
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
		
//		if(_syntaxTag == null)
//			MyError.error("bad syntaxTag name " + synTag);	
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
//		
//		if(_semanticTag == null)
//			MyError.error("bad semantcTag name " + semTag + " for " + this._wordName);	
	}
	
	public void set_simpleSemanticTag(String semTagName) {
		if(semTagName != null && !semTagName.equals("") && !semTagName.equals("-"))
			_simpleSemanticTag = SimpleSemanticTag.fromString(semTagName);
	}
	
	public void set_wsd_name(String wsd_name) {
		if(wsd_name != null && !wsd_name.equals("") && !wsd_name.equals("-"))
			this._wsd_name = wsd_name;
	}
	
	public void set_wsd_superNode_name(String wsd_superNode_name) {
		if(wsd_superNode_name != null && !wsd_superNode_name.equals("") && !wsd_superNode_name.equals("-"))
			this._wsd_superNode_name = wsd_superNode_name;
	}
	
	public void set_multiClassTag(String multiClassTag) {
		if(multiClassTag != null && !multiClassTag.equals("") && !multiClassTag.equals("-"))			
			this._multiClassTag = ScenePart.fromString(multiClassTag);
	}
	
	public void set_wsd(Node wsd) {
		this._wsd = wsd;
	}
	
	public void setSemantic_capacities(ArrayList<Node> semantic_capacities) {
		this.semantic_capacities = semantic_capacities;
	}
	
	public void setVisual_capacities(ArrayList<Node> visual_capacities) {
		this.visual_capacities = visual_capacities;
	}
	
	public void addSemantic_capacity(Node semantic_capacity){
		if(semantic_capacity == null)
			return;
		
		if(semantic_capacities == null)
			semantic_capacities = new ArrayList<Node>();
		
		if(!semantic_capacities.contains(semantic_capacity))
			semantic_capacities.add(semantic_capacity);
	}
	
	public void addVisual_capacity(Node visual_capacity){
		if(visual_capacity == null)
			return;
		
		if(visual_capacities == null)
			visual_capacities = new ArrayList<Node>();
		
		if(!visual_capacities.contains(visual_capacity))
			visual_capacities.add(visual_capacity);
	}
		
	public void addSemantic_capacities(ArrayList<Node> semantic_capacities){
		if(Common.isEmpty(semantic_capacities))
			return;
		
		if(this.semantic_capacities == null)
			this.semantic_capacities = new ArrayList<Node>();
		
		for(Node cap:semantic_capacities)
			addSemantic_capacity(cap);
	}
	
	public void addVisual_capacities(ArrayList<Node> visual_capacities){
		if(Common.isEmpty(visual_capacities))
			return;
		
		if(this.visual_capacities == null)
			this.visual_capacities = new ArrayList<Node>();
		
		for(Node cap:visual_capacities)
			addVisual_capacity(cap);
	}
	
	
	public void setAdjectives(ArrayList<Word> adjectives) {
		this.adjectives = adjectives;
	}
	
	public void setMozaf_elaih(ArrayList<Word> mozaf_elaih) {
		this.mozaf_elaih = mozaf_elaih;
	}

	//-------------------- getter part --------------------------
	
	public ArrayList<Node> getSemantic_capacities() {
		return semantic_capacities;
	}
	
	public ArrayList<Node> getVisual_capacities() {
		return visual_capacities;
	}
	
	public Node getSemantic_capacity(String semantic_capacity_name) {		
		if(semantic_capacity_name == null || semantic_capacity_name.equals(""))
			return null; 
			
		if(!Common.isEmpty(semantic_capacities))
			for(Node cap:semantic_capacities)
				if(semantic_capacity_name.equals(cap.getName()))
					return cap;
		return null;
	}
	
	public Node getVisual_capacity(String visual_capacity_name) {		
		if(visual_capacity_name == null || visual_capacity_name.equals(""))
			return null; 
			
		if(!Common.isEmpty(visual_capacities))
			for(Node cap:visual_capacities)
				if(visual_capacity_name.equals(cap.getName()))
					return cap;
		return null;
	}
	
	//public String toString() {
 	public String getStr() {
 		String rs = "number=" + _number;		
		rs += " name=";
		if(_wordName != null) rs += "" + _wordName; 
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
 			if(_wordName != null) rs += "" + _wordName; 
 			else rs += "-";
 			
 			int len = rs.length();
 			if(len < 6)
 				rs += "\t\t\t";
 			else
 				rs += "\t\t"; 			
 			 			
 			if(_gPOS != null) rs += "" + _gPOS;
 			else rs += "NULL";
 			
 			if(_gPOS.toString().length() < 3)
 				rs += "\t\t\t\t";
 			else
 				rs += "\t\t\t";
 			
 			if(_syntaxTag != null) rs += "" + _syntaxTag;
 			else rs += "NULL"; 			
 			
 			len = rs.length();
 			if(len < 22)
 				rs += "\t\t\t";
 			else
 				rs += "\t\t";
 			
 			rs+=  _srcOfSynTag_number;
 			rs += "\n";
 			return rs;
 	}
 	
 	public String getFullStr() {
 		
 			String rs = "" + _number;		
			rs += "\t";
			if(_wordName != null) rs += "" + _wordName; 
			else rs += "-";
			
			int len = rs.length();
			if(len < 6)
				rs += "\t\t";
			else
				rs += "\t"; 			
			 			
			if(_gPOS != null) rs += "" + _gPOS;
			else rs += "NULL";
			
			if(_gPOS != null && _gPOS.toString().length() < 3)
				rs += "\t\t";
			else
				rs += "\t";
			
			if(_syntaxTag != null) rs += "" + _syntaxTag;
			else rs += "NULL"; 			
			
			len = rs.length();
			if(len < 22)
				rs += "\t\t";
			else
				rs += "\t";
			
			if(_simpleSemanticTag != null) rs += "" + _simpleSemanticTag;
			else rs += "null"; 			
			
			len = rs.length();
			if(len < 52)
				rs += "\t\t";
			else
				rs += "\t";
			
			if(_wsd_name != null) rs += "" + _wsd_name;
			else rs += "NULL"; 			
			
			len = rs.length();
			if(len < 72)
				rs += "\t\t";
			else
				rs += "\t";
			
			if(_wsd_superNode_name != null) rs += "" + _wsd_superNode_name;
			else rs += "NULL"; 			
			
			len = rs.length();
			if(len < 92)
				rs += "\t\t";
			else
				rs += "\t";
			
			if(_multiClassTag != null) rs += "" + _multiClassTag;
			else rs += "NULL"; 			
			
			len = rs.length();
			if(len < 112)
				rs += "\t\t";
			else
				rs += "\t";
			
			rs+=  _srcOfSynTag_number;
			rs += "\n";
			return rs;
	}
	/**
	 * sample format:
	 * N,SBJ,ARG0_EXPERIENCER,نفر§n-13075,NO,not_role_state
	 * @param verbNum
	 * @return
	 */
 	public String getStr4DataSet(int verbNum) {	 		
			String rs = "";
			if(_gPOS != null) rs += "" + _gPOS + ", ";
			else rs += "UNKNOWN, ";
			
			if(_syntaxTag != null) rs += "" + _syntaxTag + ", ";
			else rs += "UNKNOWN, ";
			
			if(_simpleSemanticTag != null) rs += "" + _simpleSemanticTag + ", ";
			else rs += "null, ";
			
			if(_wsd_superNode_name != null) rs += "" + _wsd_superNode_name + ", ";
			else rs += "null, ";
			
			if(_srcOfSynTag_number == 0 || _srcOfSynTag_number == verbNum)
				rs += "NO, ";
			else rs += "YES, ";
						
			if(_multiClassTag != null) rs += "" + _multiClassTag.toString().toLowerCase();
			else rs += "UNKNOWN";
			
			return rs;
	}
 	
 	/**
 	 * sample format: 
 	 * 9	شخصیتی	N	MOS	11	_	_	_	Arg2	_		
 	 * @return
 	 */
 	public String getStr4ManualDataSet() {
 		
 		String rs = "";
 		
 		if(_dataSetRecord != null)
 			rs = _dataSetRecord;
 		
 		else//_dataSetRecord == null{
 			MyError.exit("for word " + this._wordName + "_dataSetRecord must not be null !!!");
 		 	
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
 	
	public VerbType getVerbType(){
		
		//TODO: correct this method later !!!
		
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
			if(_wsd.getName().contains("شد"))
				return VerbType.BASIT_RABTI;
			if(_wsd.getName().contains("به خواب رفت"))
				return VerbType.MORAKAB;
			if(_wsd.getName().contains("برگشت"))
				return VerbType.BASIT;
			if(_wsd.getName().contains("گفت"))
				return VerbType.BASIT;
					
		}
		return VerbType.BASIT;
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
// 			System.out.println(adj._wsd + " adj added to " + this._wordName + "\n");
 			adjectives.add(adj);
 			return 1;
 		}
 		else{
// 			System.out.println(this._wordName + " has this " + adj._wsd + " adj before! so they will merge \n");
 			
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
// 			System.out.println(moz._wsd + " mozaf added to " + this._wordName + "\n");
 			mozaf_elaih.add(moz);
 			return 1;
 		}
 		else{
// 			System.out.println(this._wordName + " has this " + moz._wsd + " mozaf before! so they will merge \n");
 			
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
		
		if(_phrase == null)
			if(newWord._phrase != null)
				_phrase = newWord._phrase;
		
		if(_number < 0)
			if(newWord._number >= 0)
				_number = newWord._number;
		
		if(_wordName == null || _wordName.equals(""))
			if(newWord._wordName != null && !newWord._wordName.equals(""))
				_wordName = newWord._wordName;
		
		if(_lem == null || _lem.equals(""))
			if(newWord._lem != null && !newWord._lem.equals(""))
				_lem = newWord._lem;
		
		if(_gPOS == null || _gPOS == POS.UNKNOWN)
			if(newWord._gPOS == null && newWord._gPOS != POS.UNKNOWN)
				_gPOS = newWord._gPOS;
		
		if(_props == null || _props.equals(""))
			if(newWord._props != null && !newWord._props.equals(""))
				_props = newWord._props;
				
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
		
		if(Common.isEmpty(semantic_capacities)){
			if(!Common.isEmpty(newWord.semantic_capacities))
				semantic_capacities = newWord.semantic_capacities;
		}
		
		if(Common.isEmpty(visual_capacities)){
			if(!Common.isEmpty(newWord.visual_capacities))
				visual_capacities = newWord.visual_capacities;
		}
	}	
	
	
	private void print(String s){
		System.out.println(s);
	}
		
}
