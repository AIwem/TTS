package model;


/*
 *	1.	فعل بسیط
 *		1.1.	فعل ربطی
 *		1.2.	فعل وجهی
 *		1.3.	فعل نامفعولی
 *	2.	فعل مرکب
 *		2.1.	فعل پی بستی
 *	3.	گروه فعلی
 */

public enum VerbType {
	BASIT, // mean basit va na rabti ya vajhi ya namafoli
	BASIT_RABTI,
	BASIT_VAJHI,
	BASIT_NAMAFOLI,
	
	MORAKAB,
	MORAKAB_PEIBASTI,
	
	VARB_PHRASE, 
	
	UNKNOWN;
}
