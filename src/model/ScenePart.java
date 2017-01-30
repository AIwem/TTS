package model;

public enum ScenePart {
	ROLE,
	
	SCENE_OBJECT,
	DYNAMIC_OBJECT,
	STATIC_OBJECT,
	
	LOCATION,
	
	TIME,
	
	ACTION,
	DYNAMIC_OBJECT_ACTION,
	ROLE_ACTION,
	
	EMOTION,
	ROLE_EMOTION,
	SCENE_EMOTION,
	
	GOAL,
	ROLE_INTENT,
	SCENE_GOAL,	
	
	ROLE_STATE,
	STATIC_OBJECT_STATE,
	DYNAMIC_OBJECT_STATE,	
		
	UNKNOWN;
	
	public static ScenePart fromString(String classTag){
		if (classTag != null){
			
			for (ScenePart sp : ScenePart.values()) 
				if (classTag.equalsIgnoreCase(sp.name())) 
					return sp;
		}
       return UNKNOWN;
	}
}