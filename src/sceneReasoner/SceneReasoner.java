package sceneReasoner;

import java.util.ArrayList;

import model.SceneModel;
import ir.ac.itrc.qqa.semantic.enums.ExecutionMode;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;

/**
 * SceneReasoner is a reasoning engine which reasons information of scenes based on 
 * input description (primary SceneModel) of that scene. It produces a rich scene model
 * as output which contains enough information to produce animation of the scene.
 *   
 * @author hashemi
 *
 */
public class SceneReasoner {
	
	private KnowledgeBase _kb;	
	private SemanticReasoner _re;
		
	
	public SceneReasoner(KnowledgeBase kb){
		this._kb = kb;
		_re = new SemanticReasoner(_kb, ExecutionMode.RELEASE);
		_re.setMaxReasoningDepth(10);
		_re.setMaximumAnswers(10);
		
	}
	
	public SceneModel enrichSceneModel(SceneModel primarySceneModel){
		return null;
	}
	
	
	
public void print(String toPrint){
	System.out.println(toPrint);
}

public void printPlausibleAnswers(ArrayList<PlausibleAnswer> pas){
	if (pas.size() > 0)
	{
		PlausibleQuestion pq = pas.get(0).question;
		
		if (pq != null)
		{
			print(pq.toString());
			print("");
			
			print("---------->>>>>>>>>>>> lastPlausibleQuestion = " + pq.toString());
		}			
		
	}
	
	int i = 0;
	for (PlausibleAnswer pa: pas)
	{
		i++;
		
		print("");
		print("-------------------------------");
		print("");
		
		print(i + ": " + pa.toString());
		print(pa.certaintyToString());
		
//		print("---------->>>>>>>>>>>> lastPlausibleQuestion = " + pa.question.toString());
	}
	
	print("");
	print("~~~~~~~~~~~~~~~~~~~~~~~~~~");
	print("");
	
	print("تعداد کل استنباط های انجام شده: " + _re.totalCalls);
	print("زمان کل استدلال: " + _re.reasoningTime/1000 + " ثانیه");
}


	
	public ArrayList<PlausibleAnswer> testSemanticReasoner(){
		
		PlausibleQuestion pq = new PlausibleQuestion();
		
		pq.argument = _kb.findConcept("انسان");		
		pq.descriptor = _kb.findConcept("ISA");
		pq.referent = _kb.findConcept("");
		
		ArrayList<PlausibleAnswer> pas = _re.answerQuestion(pq);
		return pas;
		
	}
}
