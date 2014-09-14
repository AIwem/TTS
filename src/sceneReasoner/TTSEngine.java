package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.ExecutionMode;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;

import model.SceneModel;
import model.SentenceModel;
import model.StoryModel;

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
	public KnowledgeBase _TTSKb;
	public SemanticReasoner _re;
	
	private String mainKbFilePath;
	private String myKbFilePath;
	
	private Preprocessor _pp;
	private SceneReasoner _sr;
	
	public TTSEngine(String mainKbFilePath, String myKbFilePath){
		this.mainKbFilePath = mainKbFilePath;
		this.myKbFilePath = myKbFilePath;
		
		this._TTSKb = new KnowledgeBase();
		this._re = new SemanticReasoner(_TTSKb, ExecutionMode.RELEASE);
		_re.setMaxReasoningDepth(12);
		_re.setMaximumAnswers(1);
		
		loadKb();
		
		_pp = new Preprocessor(_TTSKb, _re);
		_sr = new SceneReasoner(_TTSKb);
	}
	
	/**
	 * main cycle of converting a text about a single scene to its enriched SceneModel.
	 * this method receives sentences of a scene (total sentences describing that scene)
	 * first convert each sentence to its equivalent ScntenceModel.
	 * then adds each of these sentences to a sceneModel called primarySceneModel through preprocessor.
	 * then SceneReasoner enriches this primarySceneModel to enrichedSceneModel, means filling its missing information through reasoning.  
	 * 
	 * @param scene_inputNL input natural language sentences describing a single scene.
	 * @param storyModel the stroyModel which this scene belongs to. 
	 * @return 
	 */
	public SceneModel TextToScene(ArrayList<String> scene_inputNL, StoryModel storyModel){
		if(scene_inputNL == null || scene_inputNL.size() == 0 || storyModel == null){
			MyError.error("bad inputs!");
			return null;
		}
					
		if(!isKbInitialized)
			loadKb();
		
		SceneModel primarySceneModel = new SceneModel(_TTSKb, _re, storyModel);
		
		storyModel.addScene(primarySceneModel);
		
				
		for(String NLsentence:scene_inputNL){
			
			print("natural sentence: " + NLsentence);
			
			SentenceModel sen = _pp.preprocessSentence(NLsentence);
			 
			_pp.preprocessScene(sen, primarySceneModel);
							
			System.out.println("sentenceModel after preprocess: \n" + sen + "\n\n");		
						
			_sr.enrichSceneModel(primarySceneModel);
		}
		return primarySceneModel;
	}
		
	public void checkSemanticReasoner1(Node argument, Node descriptor)	{
		

		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;
		pq.referent = null;		
		//pq.descriptor = descriptor;
		pq.descriptor = KnowledgeBase.HPR_ANY;

		
		System.out.print(pq.toString() + " ... ");
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		
		System.out.println("done");
		
		System.out.println("Answers:");
		
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n\n\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)
			{
				System.out.println("-------" + ++countJustification + "--------");
				System.out.println(justification);
			}
		}
		print("\tInferences: " + _re.totalCalls);
		print("\tTime: " + _re.reasoningTime / 100 + " ms");
//		print("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
	}
	
	
public void checkSemanticReasoner2(Node argument)	{
		

		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;
		pq.referent = _TTSKb.addConcept("جانور§n-12239");		
		pq.descriptor = KnowledgeBase.HPR_ISA;
		
		System.out.print(pq.toString() + " ... ");
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		
		System.out.println("done");
		
		System.out.println("Answers:");
		
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println("\n\n\n" + ++count + ". " + answer.toString() + ">>>>>>>>>>>>>>>>>>");
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)
			{
				System.out.println("-------" + ++countJustification + "--------");
				System.out.println(justification);
			}
		}
		print("\tInferences: " + _re.totalCalls);
		print("\tTime: " + _re.reasoningTime / 100 + " ms");
//		print("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
	}
	
	private void print(String toPrint) {
		System.out.println(toPrint);
		
	}

	public int loadKb()
	{
		int loaded = 0;
		Long start = System.currentTimeMillis();
		
		loaded = _TTSKb.importKb(mainKbFilePath);		
		loaded = _TTSKb.importKb(myKbFilePath);	
		
		Long end = System.currentTimeMillis();
				
		System.out.println("در مدت زمان «" + (end - start)/100 + "» دهم ثانیه در حافظه بار شد.");
		
		isKbInitialized = true;
				
		return loaded;
	}	
	
}
