package model;

public enum SyntaxTag {
	SUBJECT,
	SUBJECT_PART,
	
	OBJECT,
	OBJECT_PART,
	
	ADVERB,
	ADVERB_PART,
		
	VERB,
	VERB_PART,
	
	UNKNOWN;
	
	public SyntaxTag getPartVersion(){
		if(this == SUBJECT)
			return SUBJECT_PART;
		
		if(this == OBJECT)
			return OBJECT_PART;
		
		if(this == ADVERB)
			return ADVERB_PART;
		
		if(this == VERB)
			return VERB_PART;
		
		return UNKNOWN;
	}
	
}
