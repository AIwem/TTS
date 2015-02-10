package model;

public enum SemanticTag {
	// Main arguments
	ARG0_AGENT,				// Agent of event
	ARG0_AGENT_P,	
	ARG0_CAUSER, 			// Causer of event
	ARG0_CAUSER_P,
	ARG0_EXPERIENCER, 		//Experiencer of event
	ARG0_EXPERIENCER_P,
	ARG0_INITIATOR, 		//Initiator of event
	ARG0_INITIATOR_P,
	
		
	ARG1_PATIENT,			// Patient of event
	ARG1_PATIENT_P,
	ARG1_THEME,				//Theme of event
	ARG1_THEME_P,
	
		
	ARG2_EXTEND,			//Extend of event
	ARG2_EXTEND_P,
	ARG2_OBJ2,				//Obj2 of event
	ARG2_OBJ2_P,
	ARG2_BENEFICIARY,		//Beneficiary of event
	ARG2_BENEFICIARY_P,
	ARG2_INSTRUMENT,		//Instrument of event
	ARG2_INSTRUMENT_P,
	ARG2_ATTRIBUTE,			//Attribute of event
	ARG2_ATTRIBUTE_P,
	ARG2_GOAL_ENDSTATE,		//Goal-Endstate of event
	ARG2_GOAL_ENDSTATE_P,
	
	
	ARG3_SOURCE_STARTPOINT,	//Source-Startpoint
	ARG3_SOURCE_STARTPOINT_P,
	ARG3_BENEFICIARY,		//Beneficiary of event
	ARG3_BENEFICIARY_P,
	ARG3_INSTRUMENT,		//Instrument of event
	ARG3_INSTRUMENT_P,
	ARG3_ATTRIBUTE,			//Attribute of event
	ARG3_ATTRIBUTE_P,		
	
	
	ARG4_ENDPOINT,					// Endpoint of event
	ARG4_ENDPOINT_P,
	
	
	ARG5,	// verb-specific
	ARG5_P,
	
	// Modifiers
	COM,	// Commutative
	COM_P,
	LOC,	// Locative: They walk [around the countryside],
	LOC_P,
	DIR,	// Directional: They [walk along the road], No one wants the U.S. to pick up its marbles and go [home].
	DIR_P,
	GOL,	// Goal
	GOL_P,
	MNR,	// Manner: He works [well] with others
	MNR_P,
	TMP,	// Temporal: He was born [in 1980]
	TMP_P,
	EXT,	// Extent (amount of change): He raised prices [more than she did], I like her [a lot], they raised the prices [by 150%]!
	EXT_P,
	REC,	// Reciprocal: himself, themselves, itself, together, each, other, jointly, both, etc.: If it were a good idea he would do it [himself]
	REC_P,
	PRP,	// Purpose
	PRP_P,
	CAU,	// Cause
	CAU_P,
	DIS,	// Discourse Markers: also, however, too, as well, but, and, as we've seen,	before, instead, on the other hand, for instance, etc.:
	DIS_P,
	MOD,	// Modals: will, may, can, must, shall, might, should, could, would, going (to), have (to) and used (to)
	MOD_P,
	NEG,	// Negation: not, n't, never, no longer, etc.
	NEG_P,
	ADV,	// Adverbials (modify entire sentence): As opposed to ArgM-MNR, which modify the verb, ARGM-ADVs usually modify the	entire sentence.
	ADV_P,
	CND,	// Condition as [if you send me the money] I will pay you back as soon as possible.
	CND_P,
	INS,	// Instrument, He was killed with a sledge hammer.
	INS_P;
	
	public SemanticTag getPartVersion(){		
		switch(this){
			case ARG0_AGENT: return ARG0_AGENT_P;
			case ARG0_CAUSER: return ARG0_CAUSER_P;
			case ARG0_EXPERIENCER: return	ARG0_EXPERIENCER_P;
			case ARG0_INITIATOR: return ARG0_INITIATOR_P;
			
			case ARG1_PATIENT: return ARG1_PATIENT_P;
			case ARG1_THEME: return ARG1_THEME_P;
			
			case ARG2_EXTEND: return ARG2_EXTEND_P;
			case ARG2_OBJ2: return ARG2_OBJ2_P;
			case ARG2_BENEFICIARY: return ARG2_BENEFICIARY_P;
			case ARG2_INSTRUMENT: return ARG2_INSTRUMENT_P;
			case ARG2_ATTRIBUTE: return ARG2_ATTRIBUTE_P;
			case ARG2_GOAL_ENDSTATE: return ARG2_GOAL_ENDSTATE_P;

			case ARG3_SOURCE_STARTPOINT: return ARG3_SOURCE_STARTPOINT_P;
			case ARG3_BENEFICIARY: return ARG3_BENEFICIARY_P;
			case ARG3_INSTRUMENT: return ARG3_INSTRUMENT_P;
			case ARG3_ATTRIBUTE: return ARG3_ATTRIBUTE_P;		
			
			
			case ARG4_ENDPOINT: return ARG4_ENDPOINT_P;
						
			case ARG5: return ARG5_P;
			
			case COM: return COM_P;
			case LOC: return LOC_P;
			case DIR: return DIR_P;
			case GOL: return GOL_P;
			case MNR: return MNR_P;
			case TMP: return TMP_P;			
			case EXT: return EXT_P;
			case REC: return REC_P;
			case PRP: return PRP_P;
			case CAU: return CAU_P;
			case DIS: return DIS_P;
			case MOD: return MOD_P;
			case NEG: return NEG_P;
			case ADV: return ADV_P;
			case CND: return CND_P;
			case INS: return INS_P;
			
			default: return null;
		}
	}
	
	public static SemanticTag fromString(String semanticTag){
		if (semanticTag != null){
			
			if(semanticTag.startsWith("ArgM_"))
				semanticTag = semanticTag.substring("ArgM_".length());
			
			for (SemanticTag st : SemanticTag.values()) 
				if (semanticTag.equalsIgnoreCase(st.name())) 
					return st;
		}
       return null;
	}

	public boolean isMainSemanticTag(){		
		for(MainSemanticTag mainSemArg:MainSemanticTag.values())
			if(mainSemArg.name().contains(this.name()))						
				return true;
		return false;
	}
	
	public boolean isSubSemanticTag(){		
		for(SubSemanticTag subSemArg:SubSemanticTag.values())
			if(subSemArg.name().contains(this.name()))						
				return true;
		return false;
	}
	
	public MainSemanticTag convertToMainSemanticTag(){
		try{
			if(this.isMainSemanticTag())
				return MainSemanticTag.valueOf(this.name());
			throw new Exception(this + " is not a MainSemanticTag!");
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public SubSemanticTag convertToSubSemanticTag(){
		try{
			if(this.isSubSemanticTag())
				return SubSemanticTag.valueOf(this.name());
			throw new Exception(this + " is not a SubSemanticTag!");
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public boolean isArg0(){
		if(this.name().startsWith("ARG0"))
			return true;
		return false;	
	}
	
	public boolean isArg1(){
		if(this.name().startsWith("ARG1"))
			return true;
		return false;	
	}
	
	public boolean isArg2(){
		if(this.name().startsWith("ARG2"))
			return true;
		return false;	
	}
	
	public boolean isArg3(){
		if(this.name().startsWith("ARG3"))
			return true;
		return false;	
	}
	
	public boolean isArg4(){
		if(this.name().startsWith("ARG4"))
			return true;
		return false;	
	}
	
	public boolean isArg5(){
		if(this.name().startsWith("ARG5"))
			return true;
		return false;	
	}

	
}
