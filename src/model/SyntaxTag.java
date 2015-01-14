package model;

public enum SyntaxTag {
	SUBJECT,
	SUBJECT_PART,
	
	OBJECT,
	OBJECT_PART,
	
	ADVERB,
	ADVERB_PART,
	
	COMPLEMENT,
	COMPLEMENT_PART,
		
	VERB,
	VERB_PART,
	
	UNKNOWN;
	
	public SyntaxTag getPartVersion(){
		switch(this){
			case SUBJECT: return SUBJECT_PART;
			
			case OBJECT: return OBJECT_PART;
			
			case ADVERB: return ADVERB_PART;
					
			case COMPLEMENT: return COMPLEMENT_PART;
			
			case VERB: return VERB_PART;
			
			default: return UNKNOWN;
		}
	}
}
