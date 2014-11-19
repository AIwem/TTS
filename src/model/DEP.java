package model;

public enum DEP {
	MAIN,
	PRE,
	POST,
	UNKOWN;
	
	public static DEP fromString(String depname){
		if (depname != null)
			for (DEP d : DEP.values()) 
				if (depname.equalsIgnoreCase(d.name())) 
					return d;
       return null;
	}
	
	
}
