package model;

public enum SubSemanticTag {
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
		
	public static SubSemanticTag fromString(String subSemanticTag){
		if (subSemanticTag != null){
			
			if(subSemanticTag.startsWith("ArgM_"))
				subSemanticTag = subSemanticTag.substring("ArgM_".length());
			
			for (SubSemanticTag st : SubSemanticTag.values()) 
				if (subSemanticTag.equalsIgnoreCase(st.name())) 
					return st;
		}
       return null;
	}

	public static boolean isSubSemanticTag(String subSemanticTag){
		try{
			SubSemanticTag.valueOf(subSemanticTag);
			return true;
		}
		catch(Exception e){
			return false;	
		}
	}
	
	public SemanticTag convertToSemanticTag(){
		try{
			return SemanticTag.valueOf(this.name());			
		}
		catch (Exception e) {
			return null;
		}
	}

}
