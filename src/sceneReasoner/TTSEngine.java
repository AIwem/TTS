package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.ExecutionMode;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;

import java.util.ArrayList;

import model.SceneModel;
import model.SentenceModel;

/**
 * TTSEngine is an engine that converts natural language texts to Scene. 
 * As Scene it produces a rich SceneModel which contains all necessary information for producing animation of 
 * input natural language text. It must be mentioned that all but not spatial information about 
 * final scene to be generated is available in rich SceneModel. Ideally this rich SceneModel 
 * could get converted to animation through an automatic animation generation engine. 
 *     
 * @author hashemi
 *
 */
public class TTSEngine {
	private boolean isKbInitialized = false;	
	private KnowledgeBase _kb;
	
	private Preprocessor _pp;
	private SceneReasoner _sr;
	
	public TTSEngine(){
		_kb = new KnowledgeBase();
		loadKb("kb");
		_pp = new Preprocessor(_kb);
		_sr = new SceneReasoner(_kb);
	}
	
	/**
	 * main cycle of converting text to scene.
	 * 
	 * @param inputNLSentence input natural language sentence
	 * @param command three possible commands: new story, new scene, or new sentence.
	 */
	public SceneModel TextToScene(String inputNLSentence, String command){
		switch(command){
			case "new story": break;
			case "new scene": break;
			default: break;
		}
		SentenceModel sen = _pp.preprocessSentence(inputNLSentence);
		SceneModel primarySM = _pp.preprocessScene(sen);
		
		SceneModel richSM = _sr.enrichSceneModel(primarySM);		 
		
		return richSM;
	}
		
	
	public int loadKb(String path)
	{
		int loaded = 0;
		Long start = System.currentTimeMillis();
		
		loaded = _kb.importKb(path+"/farsnet--3.txt");		
		loaded = _kb.importKb(path+"/injuredPigeon.txt");	
		
		Long end = System.currentTimeMillis();
				
		System.out.println("در مدت زمان «" + (end - start)/100 + "» دهم ثانیه در حافظه بار شد.");
		
		isKbInitialized = true;
				
		return loaded;
	}
	
	
	

}
