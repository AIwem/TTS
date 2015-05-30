package userInterface;

import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleTerm;

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

public class UI {
	

	private String inputStoryFilePath = "inputStory/inputStrory8.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory2-1.txt";
//	private String inputStoryFilePath = "inputStory/inputStrory3.txt";
	private String mainKbFilePath = "kb/farsnet--23.txt";
//	private String mainKbFilePath = "kb/farsnet.txt";
	private String myKbFilePath = "kb/injuredPigeon6.txt";
	//private String myKbFilePath = "kb/injuredPigeon_simple.txt";
	//private String myKbFilePath = "kb/injuredPigeon_SS.txt";
	private String verbCapacitiesPath = "kb/verbCapacities.txt";
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
				tts = new TTSEngine(mainKbFilePath, myKbFilePath, verbCapacitiesPath);
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

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله");
		
		UI ui = new UI();
		StoryModel sm = ui.TTS();		

		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ الحمدلله");
		
		Node arg = ui.tts._TTSKb.addConcept("دیدن#v5");
		Node desNode =  ui.tts._TTSKb.addConcept("*دیدن#v5 (1) (1)");
		PlausibleTerm desc = (PlausibleTerm)desNode; 
		desc.relationType = ui.tts._TTSKb.addConcept("دیدن#v5 (1)");
		
		Node ref = ui.tts._TTSKb.addConcept("کبوتر#n1 (1)");
				
		ui.tts.checkSemanticReasoner1(arg, desc, ref);
		
		//ui.tts.checkSemanticReasoner1();
		//ui.tts.checkSemanticReasoner2(psm.getDynamic_objects().get(0)._node);	
	}
}

