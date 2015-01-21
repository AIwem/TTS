package model;

public enum MainSemanticTag {
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
	ARG4_ATTRIBUTE,			//Attribute of event
	ARG4_ATTRIBUTE_P,		
	
	
	ARG4_ENDPOINT,					// Endpoint of event
	ARG4_ENDPOINT_P,
	
	
	ARG5,	// verb-specific
	ARG5_P;

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

}
