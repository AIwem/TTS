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
		System.out.println("natural sentence: " + inputNLSentence);
		
		SentenceModel sen = _pp.preprocessSentence(inputNLSentence);
		
		System.out.println("preproc sentence: " + sen);	
		
		SceneModel primarySM = _pp.preprocessScene(sen);
		
		System.out.println("preprocess scene: " + sen + "\n\n");
		
		SceneModel richSM = _sr.enrichSceneModel(primarySM);		 
		
		return richSM;
	}
		
	public void checkSemanticReasoner()
	{
		SemanticReasoner _re = new SemanticReasoner(_kb, ExecutionMode.RELEASE);
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = _kb.addConcept("پسرک");
		pq.referent = _kb.addConcept("انسان");		
		pq.descriptor = _kb.addConcept("چشم افتادن");

//		pq.argument = kb.addConcept("پسر بچه");
//		pq.referent = kb.addConcept("بچه");
//		pq.descriptor = KnowledgeBase.HPR_ISA;
		
		System.out.print(pq.toString() + " ... ");
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		
		System.out.println("done");
		
		System.out.println("Answers:");
		
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println(++count + ". " + answer.toString());
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)
			{
				System.out.println("-------" + ++countJustification + "--------");
				System.out.println(justification);
			}
		}
		
		System.out.println("Summary:");
		System.out.println("\tInferences: " + _re.totalCalls);
		System.out.println("\tTime: " + _re.reasoningTime / 100 + " ms");
		System.out.println("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
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
	
	
	/*
	public static void main(String[] args) {
		TTSEngine tts = new TTSEngine();
		tts.checkSemanticReasoner();
	}
	*/
}
