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
	private KnowledgeBase _TTSKb;
	private SemanticReasoner _re;
	
	private String kbFilePath;
	private String storyFilePath;
	
	private Preprocessor _pp;
	private SceneReasoner _sr;
	
	public TTSEngine(String kbFilePath, String storyFilePath){
		this.kbFilePath = kbFilePath;
		this.storyFilePath = storyFilePath;
		
		this._TTSKb = new KnowledgeBase();
		this._re = new SemanticReasoner(_TTSKb, ExecutionMode.RELEASE);
		_re.setMaxReasoningDepth(10);
		_re.setMaximumAnswers(3);
		
		loadKb();
		
		_pp = new Preprocessor(_TTSKb, _re);
		_sr = new SceneReasoner(_TTSKb);
	}
	
	/**
	 * main cycle of converting text to scene.
	 * 
	 * @param inputNLSentence input natural language sentence
	 * @param command three possible commands: new story, new scene, or new sentence.
	 */
	public SceneModel TextToScene(String inputNLSentence, String command){
		if(!isKbInitialized)
			loadKb();
		
		if(command == "new story"){
			
		}
		if(command == "new scene"){
			
		}	
		
		System.out.println("natural   sentence: " + inputNLSentence);
		
		SentenceModel sen = _pp.preprocessSentence(inputNLSentence);
		
		//System.out.println("preproc sentence: \n" + sen);
		SceneModel primarySceneModel = new SceneModel();
		
		_pp.preprocessScene(sen, primarySceneModel);
						
		System.out.println("primary sceneModel: " + sen + "\n\n");		
					
		//SceneModel richSM = _sr.enrichSceneModel(primarySM);		 
		
		return null; //richSM;
	}
		
	public void checkSemanticReasoner()
	{
		SemanticReasoner _re = new SemanticReasoner(_TTSKb, ExecutionMode.RELEASE);
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = _TTSKb.addConcept("پسرک");
		pq.referent = _TTSKb.addConcept("انسان");		
		pq.descriptor = _TTSKb.addConcept("چشم افتادن");

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
	

	
	public int loadKb()
	{
		int loaded = 0;
		Long start = System.currentTimeMillis();
		
		loaded = _TTSKb.importKb(kbFilePath);		
		loaded = _TTSKb.importKb(storyFilePath);	
		
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
