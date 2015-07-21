package userInterface;

import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import model.StoryModel;
import sceneReasoner.TTSEngine;


@SuppressWarnings("unused")
public class UI {
	

	private String inputStoryFilePath = "inputStory/inputStrory8.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory2-1.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory3.txt";
//	private String mainKbFilePath = "kb/farsnet--24.txt";
	private String mainKbFilePath = "kb/farsnet.txt";
	private String myKbFilePath = "kb/injuredPigeon8.txt";
	//private String myKbFilePath = "kb/injuredPigeon_simple.txt";
	//private String myKbFilePath = "kb/injuredPigeon_SS.txt";
	private String verbSemanticCapacitiesPath = "kb/verbSemanticCapacities.txt";
	private String verbVisualCapacitiesPath = "kb/verbVisualCapacities.txt";
	private TTSEngine tts;
	
	public UI(){		
		//tts = new TTSEngine(mainKbFilePath, myKbFilePath);
	}
	
	private void print(String s){
		System.out.println(s);
	}
	/**
	 * main cycle of reading input sentences from user input file, and giving sentence to TTSEngine,
	 * and showing its output to the user.
	 */
	public StoryModel TTS(){
		
		//It must read sentence from user input.
		//temporarily it reads all input story from file instead of getting from user!
		ArrayList<String> inputs = importInputTexts(inputStoryFilePath);

		ArrayList<String> current_scene_lines = new ArrayList<String>();
		
		int story_num = 0;		
		StoryModel storyModel = new StoryModel("story"+story_num);		
		
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("output\\sceneOutput.txt", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		for(String line:inputs){
			
			if(line.equals("«داستان جدید»")){				
				storyModel = new StoryModel("stroy" + ++story_num);
				tts = new TTSEngine(mainKbFilePath, myKbFilePath, verbSemanticCapacitiesPath, verbVisualCapacitiesPath);
				continue;
			}
			else if(line.equals("«صحنه جدید»")){
				
				//Then give the sentences of a scene to TTSEngine to enrich. 
			 	tts.TextToScene(current_scene_lines, storyModel, false, writer);		 	
				
				current_scene_lines = new ArrayList<String>();
				continue;
			}			
			current_scene_lines.add(line);			
		}
		//sentences of the last scene will be sent to TTSEngine to enrich. 
	 	tts.TextToScene(current_scene_lines, storyModel, true, writer);

		writer.close();
			
	 	return storyModel;
	}

	
	public ArrayList<String> importInputTexts(String filename)
	{
		ArrayList<String> inputs = new ArrayList<>();
		
		BufferedReader stream = null;
		
		try
		{
			stream = new BufferedReader(new InputStreamReader(new FileInputStream(filename), "utf-8"));
		}
		catch(Exception e)
		{
			print("Error opening `" + filename + "` for reading input natural language texts!");
			e.printStackTrace();
		}
		
		String line = "";
		
		while (line != null)
		{
			try
			{
				line = stream.readLine();
				
				if (line == null)
					break;
				
				line = line.trim();				
							
				if (line.startsWith("#")) // comment lines
					continue;
							
				inputs.add(line);

			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	
		try
		{
			stream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return inputs;
	}
	
	private void testKB(){
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~ testKB() ~~~~~~~~~~~~~~~~~~~~~");
		
		//query: مواجه شدن#v(f˸نفر§n-13075)={f˸حیوان#n1} ?				
		Node arg = tts._TTSKb.addConcept("نفر§n-13075");
		Node desNode =  tts._TTSKb.addConcept("مواجه شدن#v");
		Node ref = tts._TTSKb.addConcept("حیوان#n1");
		
//		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		//query: مواجه شدن#v(f˸پسر#n2 [1])={f˸حیوان#n1} ?				
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode =  tts._TTSKb.addConcept("مواجه شدن#v");
		ref = tts._TTSKb.addConcept("حیوان#n1");
		
		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		//query: ISA(f˸پسر#n2 [1])={f˸نفر§n-13075} ?				
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode = KnowledgeBase.HPR_ISA;
		ref = tts._TTSKb.addConcept("نفر§n-13075");
		
//		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		
		//rule: s˸مواجه شدن#v(f˸نفر§n-13075)={f˸حیوان#n1}	IMP	f˸حس§n-14738(f˸نفر§n-13075)={کلافه§a-118}
		//query: f˸حس§n-14738(s˸پسر#n2 [1])={?}
		
		arg = tts._TTSKb.addConcept("پسر#n2 [1]");
		desNode =  tts._TTSKb.addConcept("حس§n-14738");
//		PlausibleStatement desc = (PlausibleStatement)desNode; 
//		desc.relationType = tts._TTSKb.addConcept("دیدن#v5 [1]");
		ref = null; //tts._TTSKb.addConcept("حیوان#n1");
		
		tts.checkSemanticReasoner1(arg, desNode, ref);
		
		System.out.println("~~~~~~~~~~~~~~~~~~ end of testKB() ~~~~~~~~~~~~~~~~~~~~~");
	}

	private void testKb2() {


	     System.out.println(" ------------ TESTING IMP ------------ ");
	    

	     // adding the required statements

	     // 1: stating that 'the boy is a person'

	     Node ourBoy = tts._TTSKb.addConcept("پسر داستان ما");

	     Node person = tts._TTSKb.addConcept("نفر§n-13075");

	     tts._TTSKb.addRelation(ourBoy, person, KnowledgeBase.HPR_ISA);

	    

	     //2: stating that a pigeon is an animal

	     Node ourPigeon = tts._TTSKb.addConcept("کفتر داستان ما");

	     Node animal = tts._TTSKb.addConcept("حیوان#n1");

	     tts._TTSKb.addRelation(ourPigeon, animal, KnowledgeBase.HPR_ISA);

	    

	     //3: stating that our boy encountered the pigeon in our story

//	     Node encounters =  tts._TTSKb.addConcept("مواجه شدن#v");
//	     Node encounters =  tts._TTSKb.addConcept("دیدن#v5");	
	     Node encounters =  tts._TTSKb.addConcept("دیدن#v5 [1]");

	     tts._TTSKb.addRelation(ourBoy, ourPigeon, encounters);

	    

	     //4: asking what was the boy's emotion

	     Node sense =  tts._TTSKb.addConcept("حس§n-14738");

	    
	     tts.checkSemanticReasoner1(ourBoy, sense, null);

	     
	     System.out.println(" ------------ END OF TESTING IMP ------ ");
	     
	}
	
	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله ");
		
		UI ui = new UI();
		StoryModel sm = ui.TTS();		

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ الحمدلله");
		
		ui.testKB();
		
		ui.testKb2();
	}
}

