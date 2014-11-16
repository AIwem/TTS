package userInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.StoryModel;
import sceneReasoner.TTSEngine;

public class UI {
	
	private String inputStoryFilePath = "inputStory/inputStrory5.txt";
	private String mainKbFilePath = "kb/farsnet--19.txt";
	private String myKbFilePath = "kb/injuredPigeon5.txt";
	//private String myKbFilePath = "kb/injuredPigeon_simple.txt";
	//private String myKbFilePath = "kb/injuredPigeon_SS.txt";
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
		
		for(String line:inputs){
			
			if(line.equals("«داستان جدید»")){				
				storyModel = new StoryModel("stroy" + ++story_num);
				tts = new TTSEngine(mainKbFilePath, myKbFilePath);
				continue;
			}
			else if(line.equals("«صحنه جدید»")){
				
				//Then give the sentences of a scene to TTSEngine to enrich. 
			 	tts.TextToScene(current_scene_lines, storyModel, false);		 	
				
				current_scene_lines = new ArrayList<String>();
				continue;
			}			
			current_scene_lines.add(line);			
		}
		//sentences of the last scene will be sent to TTSEngine to enrich. 
	 	tts.TextToScene(current_scene_lines, storyModel, true);	 	
	 	
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
		
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
		
//		Node arg = ui.tts._TTSKb.addConcept("چشم افتادن#v");
//		Node desNode =  ui.tts._TTSKb.addConcept("*چشم افتادن#v (1) (1)");
//		PlausibleTerm desc = (PlausibleTerm)desNode; 
//		desc.relationType = ui.tts._TTSKb.addConcept("چشم افتادن#v (1)");
//		
//		Node ref = ui.tts._TTSKb.addConcept("کبوتر#n1 (1)");
//				
//		ui.tts.checkSemanticReasoner1(arg, desc, ref);
//		
//		//ui.tts.checkSemanticReasoner1();
//		//ui.tts.checkSemanticReasoner2(psm.getDynamic_objects().get(0)._node);

		System.out.println("الحمدلله");
	}
}

