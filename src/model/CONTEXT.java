package model;

public enum CONTEXT {
	
	TIME,
	LOCATION,
	MOOD,
	EMOTION,
	SPEED,
	DIRECTION;
	
	public static CONTEXT parse(String cxName){
		if (cxName != null) 
			for (CONTEXT cx : CONTEXT.values()) 
				if (cxName.equalsIgnoreCase(cx.name())) 
					return cx;
       return null;
	}
}
