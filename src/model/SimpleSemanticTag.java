package model;

public enum SimpleSemanticTag {
	// Main arguments
	Arg0,
	Arg1,
	Arg2,
	Arg3,
	Arg4,
	Arg5,
//	ARG0_AGENT,				// Agent of event
//	
//	ARG0_CAUSER, 			// Causer of event
//	
//	ARG0_EXPERIENCER, 		//Experiencer of event
//	
//	ARG0_INITIATOR, 		//Initiator of event
//
//	
//	ARG1_PATIENT,			// Patient of event
//	
//	ARG1_THEME,				//Theme of event
//	
//	
//		
//	ARG2_EXTEND,			//Extend of event
//	
//	ARG2_OBJ2,				//Obj2 of event
//	
//	ARG2_BENEFICIARY,		//Beneficiary of event
//	
//	ARG2_INSTRUMENT,		//Instrument of event
//	
//	ARG2_ATTRIBUTE,			//Attribute of event
//	
//	ARG2_GOAL_ENDSTATE,		//Goal-Endstate of event
//	
//	
//	
//	ARG3_SOURCE_STARTPOINT,	//Source-Startpoint
//	
//	ARG3_BENEFICIARY,		//Beneficiary of event
//	
//	ARG3_INSTRUMENT,		//Instrument of event
//	
//	ARG3_ATTRIBUTE,			//Attribute of event
//	
//	
//	ARG4_ENDPOINT,					// Endpoint of event
//		
//	
//	ARG5,	// verb-specific
	
	
	// Modifiers
	COM,	// Commutative
	
	LOC,	// Locative: They walk [around the countryside],
	
	DIR,	// Directional: They [walk along the road], No one wants the U.S. to pick up its marbles and go [home].
	
	GOL,	// Goal
	
	MNR,	// Manner: He works [well] with others
	
	TMP,	// Temporal: He was born [in 1980]
	
	EXT,	// Extent (amount of change): He raised prices [more than she did], I like her [a lot], they raised the prices [by 150%]!
	
	REC,	// Reciprocal: himself, themselves, itself, together, each, other, jointly, both, etc.: If it were a good idea he would do it [himself]
	
	PRP,	// Purpose
	
	CAU,	// Cause
	
	DIS,	// Discourse Markers: also, however, too, as well, but, and, as we've seen,	before, instead, on the other hand, for instance, etc.:
	
	MOD,	// Modals: will, may, can, must, shall, might, should, could, would, going (to), have (to) and used (to)
	
	NEG,	// Negation: not, n't, never, no longer, etc.
	
	ADV,	// Adverbials (modify entire sentence): As opposed to ArgM-MNR, which modify the verb, ARGM-ADVs usually modify the	entire sentence.
	
	CND,	// Condition as [if you send me the money] I will pay you back as soon as possible.
	
	INS;	// Instrument, He was killed with a sledge hammer.
	
	
		
	public static SimpleSemanticTag fromString(String semanticTagName){
		if (semanticTagName != null){
			
			if(semanticTagName.startsWith("ArgM-"))
				semanticTagName = semanticTagName.substring("ArgM-".length());
			
			for (SimpleSemanticTag st : SimpleSemanticTag.values()) 
				if (semanticTagName.equalsIgnoreCase(st.name())) 
					return st;
		}
       return null;
	}

//	public boolean isMainSemanticTag(){		
//		for(MainSemanticTag mainSemArg:MainSemanticTag.values())
//			if(mainSemArg.name().contains(this.name()))						
//				return true;
//		return false;
//	}
//	
//	public boolean isSubSemanticTag(){		
//		for(SubSemanticTag subSemArg:SubSemanticTag.values())
//			if(subSemArg.name().contains(this.name()))						
//				return true;
//		return false;
//	}
	
//	public MainSemanticTag convertToMainSemanticTag(){
//		try{
//			if(this.isMainSemanticTag())
//				return MainSemanticTag.valueOf(this.name());
//			throw new Exception(this + " is not a MainSemanticTag!");
//		}
//		catch (Exception e) {
//			return null;
//		}
//	}
//	
//	public SubSemanticTag convertToSubSemanticTag(){
//		try{
//			if(this.isSubSemanticTag())
//				return SubSemanticTag.valueOf(this.name());
//			throw new Exception(this + " is not a SubSemanticTag!");
//		}
//		catch (Exception e) {
//			return null;
//		}
//	}
	
	public boolean isArg0(){
		if(this.name().startsWith("Arg0"))
			return true;
		return false;	
	}
	
	public boolean isArg1(){
		if(this.name().startsWith("Arg1"))
			return true;
		return false;	
	}
	
	public boolean isArg2(){
		if(this.name().startsWith("Arg2"))
			return true;
		return false;	
	}
	
	public boolean isArg3(){
		if(this.name().startsWith("Arg3"))
			return true;
		return false;	
	}
	
	public boolean isArg4(){
		if(this.name().startsWith("Arg4"))
			return true;
		return false;	
	}
	
	public boolean isArg5(){
		if(this.name().startsWith("Arg5"))
			return true;
		return false;	
	}	
}
