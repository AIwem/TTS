package model;

public enum MainSemanticTag {	
	// Main arguments
	ARG0_AGENT,				// Agent of event
	
	ARG0_CAUSER, 			// Causer of event
	
	ARG0_EXPERIENCER, 		//Experiencer of event
	
	ARG0_INITIATOR, 		//Initiator of event

	
	ARG1_PATIENT,			// Patient of event
	
	ARG1_THEME,				//Theme of event
	
	
		
	ARG2_EXTEND,			//Extend of event
	
	ARG2_OBJ2,				//Obj2 of event
	
	ARG2_BENEFICIARY,		//Beneficiary of event
	
	ARG2_INSTRUMENT,		//Instrument of event
	
	ARG2_ATTRIBUTE,			//Attribute of event
	
	ARG2_GOAL_ENDSTATE,		//Goal-Endstate of event
	
	
	
	ARG3_SOURCE_STARTPOINT,	//Source-Startpoint
	
	ARG3_BENEFICIARY,		//Beneficiary of event
	
	ARG3_INSTRUMENT,		//Instrument of event
	
	ARG3_ATTRIBUTE,			//Attribute of event
	
	
	ARG4_ENDPOINT,					// Endpoint of event
		
	
	ARG5;	// verb-specific
	
	public static MainSemanticTag fromString(String mainSemanticTag){
		if (mainSemanticTag != null){
			
			for (MainSemanticTag st : MainSemanticTag.values()) 
				if (mainSemanticTag.equalsIgnoreCase(st.name())) 
					return st;
		}
       return null;
	}
	
	public static boolean isMainSemanticTag(String mainSemanticTag){
		try{
			MainSemanticTag.valueOf(mainSemanticTag);
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
