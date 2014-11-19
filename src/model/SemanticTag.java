package model;

public enum SemanticTag {
	
	// Main arguments
	ARG0,	// Proto-Agent
	ARG0_P,	
	ARG1,	// Proto-Patient
	ARG1_p,
	ARG2,	// verb-specific
	ARG2_p,
	ARG3,	// verb-specific
	ARG3_p,
	ARG4,	// verb-specific
	ARG4_p,
	ARG5,	// verb-specific
	ARG5_p,
	
	// Modifiers
	COM,	// Commutative
	COM_p,
	LOC,	// Locative: They walk [around the countryside],
	LOC_p,
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
		case ARG0:return ARG0_P;
		default:
			return null;
		}
	}
	
	public static SemanticTag fromString(String semanticTag){
		if (semanticTag != null){
			
			if(semanticTag.startsWith("ArgM-"))
				semanticTag = semanticTag.substring("ArgM-".length());
			
			for (SemanticTag st : SemanticTag.values()) 
				if (semanticTag.equalsIgnoreCase(st.name())) 
					return st;
		}
       return null;
	}

}
