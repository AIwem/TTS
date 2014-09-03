package userInterface;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import model.SceneModel;
import sceneReasoner.TTSEngine;

public class UI {
	
	private String inputTextFileName = "inputStory/inputStrory.txt";
	private TTSEngine tts;
	
	public UI(){
		tts = new TTSEngine("kb/farsnet.txt", "kb/injuredPigeon.txt");
	}
	
	private void print(String s){
		System.out.println(s);
	}
	/**
	 * main cycle of reading input sentences from user input file, and giving sentence to TTSEngine,
	 * and showing its output to the user.
	 */
	public void TTS(){
		
		//It must read sentence from user input.
		//temporarily it reads all input story from file instead of getting from user!
		ArrayList<String> inputs = importInputTexts(inputTextFileName);
		
		SceneModel primarySceneModel = new SceneModel();
		
		String command = "";
		//ArrayList<Node> nodes = null;
		for(String line:inputs){
			
			if(line.equals("«داستان جدید»")){				
				command = "new story";
				continue;
			}
			else if(line.equals("«صحنه جدید»")){
				command = "new scene";
				continue;
			}
			
			//Then give the sentence to TTSEngine to enrich. 
			SceneModel  enrichedScene = tts.TextToScene(line, command, primarySceneModel);
			
			//nodes = tts.TextToScene(line, command);
			
			//Then shows this enriched SceneModel to the user which contains enriched information about input sentence.
			if (enrichedScene!= null)
				print(enrichedScene.toString());			
		}
		//System.out.println("" + nodes);
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

	public static void main(String[] args) {
		System.out.println("بسم الله الرحمن الرحیم و توکلت علی الله");
	
		UI ui = new UI();
		ui.TTS();
		ui.tts.checkSemanticReasoner();
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~");
		ui.tts.checkSemanticReasoner2();
		
	/*	ArrayList<PlausibleAnswer> pas = ui.testSemanticReasoner();
		ui.printPlausibleAnswers(pas);
	*/
		System.out.println("الحمدلله");
	}
	


}
