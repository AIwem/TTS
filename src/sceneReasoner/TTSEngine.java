package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.ExecutionMode;
import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.enums.SourceType;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;
import java.util.Hashtable;

import model.SceneModel;
import model.ScenePart;
import model.SentenceModel;
import model.StoryModel;
import model.SyntaxTag;

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
	
	Node mozaf_root;	
	Node adjective_root;
	Node verb_root;
	
	/**
	 * this map holds the Node object seen yet in this TTSEngine. 
	 * It maps the Node pure name as key to the ArrayList<Node> of seen Nodes with this name in this SceneModel object.
	 * for each key, the first Node in ArrayList contains the pure Node object fetched from kb.
	 * the others are different instances of that pure Node.
	 */	
	private Hashtable<String, ArrayList<Node>> seen_nodes = new Hashtable<String,ArrayList<Node>>();
	
	/**
	 * this map holds the ScenePart of Node object seen yet in this TTSEngine. 
	 * It maps the pure name of Node (as in kb) as key and the ScenePart of the Node with this name as value. 
	 * 
	 */	
	private Hashtable<String, ScenePart> seen_sceneParts = new Hashtable<String, ScenePart>();
	
	
	public TTSEngine(String mainKbFilePath, String myKbFilePath){
		this.mainKbFilePath = mainKbFilePath;
		this.myKbFilePath = myKbFilePath;
		
		this._TTSKb = new KnowledgeBase();
		this._re = new SemanticReasoner(_TTSKb, ExecutionMode.RELEASE);
		_re.setMaxReasoningDepth(12);
		_re.setMaximumAnswers(1);
		
		loadKb();
		
		_pp = new Preprocessor(_TTSKb, _re, this);
		_sr = new SceneReasoner(_TTSKb, _re, this);
		
		mozaf_root = _TTSKb.addConcept("mozaf#a");
		adjective_root = _TTSKb.addConcept("adjective#a");
		verb_root = _TTSKb.addConcept("verb#v");
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
	 * @param isLastScene is scene_inputNL describing the last scene of the story?  
	 * @return 
	 */
	public void TextToScene(ArrayList<String> scene_inputNL, StoryModel storyModel, boolean isLastScene){
		
		if(scene_inputNL == null || scene_inputNL.size() == 0 || storyModel == null){
			MyError.error("bad input lines!");
			return;
		}
					
		if(!isKbInitialized)
			loadKb();
		
		//-------------- converting natural language sentences of a scene to their equivalent sentencs_scenes SceneModel ------
		ArrayList<SceneModel> sentencs_scenes = new ArrayList<SceneModel>();
		
		for(String NLsentence:scene_inputNL){
			
			print("natural sentence: " + NLsentence);
			
			SentenceModel sentence = _pp.preprocessSentence(NLsentence);
			
			SceneModel cur_sen_scene = _pp.preprocessScene(sentence);
			
			if(cur_sen_scene != null)
				sentencs_scenes.add(cur_sen_scene);
							
			System.out.println("sentenceModel after preprocess: \n" + sentence + "\n\n");			
		}
		
		//-------------- merging primary SceneModels of each sentences of this scene ----------------------------------------
		SceneModel currentPrimaryScene = _sr.mergeScenesOfSentences(sentencs_scenes);
		
		//-------------- adding primarySceneModel of the current scene to the stroyModel ------------------------------------
		storyModel.addScene(currentPrimaryScene);
		currentPrimaryScene.setStory(storyModel);
		
		print("merged primary SceneModel\n" + currentPrimaryScene);
		
		if(isLastScene){//the last scene of story
			
			//-------------- merging primarySceneModels of different scenes previously added to storyModel ------------------
			_sr.mergeScenes(storyModel);
			
			//-------------- enriching primarySceneModels of different scenes previously added to storyModel ----------------
			_sr.enrichSceneModel(storyModel);
		}
	}
		
	public void checkSemanticReasoner1(Node argument, Node descriptor, Node referent)	{
		

		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;
		pq.referent = referent;
		pq.descriptor = descriptor;		
		
		ArrayList<PlausibleStatement> ans = descriptor.findOutRelations(KnowledgeBase.HPR_CXTIME);
		pq.cxTime = ans.get(0).referent;
		
	  

		
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
		
	public void printDictionary(){
		print("\n story_nodes");
		for(String key:seen_nodes.keySet()){
			print(key +": " + seen_nodes.get(key));
		}
			
		print("\n story_sceneParts");
		for(String key:seen_sceneParts.keySet()){
			print(key +": " + seen_sceneParts.get(key));
		}
		print("\n");
	}
	
	private void print(String toPrint) {
		System.out.println(toPrint);		
	}
	
	/**
	 * this method adds a row to seen_nodes with pure_name as key and instances arrayList as values.
	 * 
	 * @param pure_name the name of original node fetched from FarsNet.
	 * @param instances the list of instances of this original node in this sceneModel. 
	 */
	private void addTo_seen_nodes(String pure_name, ArrayList<Node> instances){
		if(pure_name == null || instances == null) 
			return;
		
		if(seen_nodes.containsKey(pure_name))
			MyError.error("story_nodes_ previosly contains this pure name " + seen_nodes.get(pure_name));
		
		seen_nodes.put(pure_name, instances);
			
	}
	
	/**
	 * this method adds a row to the seen_sceneParts with pure_name as key and the scenePart as value.
	 * this is done in order to preventing re-querying the kb to detect ScenePart of every instance of a original node in this sceneModel. 
	 * 
	 * @param pure_name the name of original node fetched from FarsNet.
	 * @param scenePart if scenePart equals to "ScenePart.UNKNOWN" nothing will be added to scene_parts.
	 */
	private void addTo_seen_sceneParts(String pure_name, ScenePart scenePart) {
		if(pure_name == null || pure_name.equals("") || scenePart == null || scenePart == ScenePart.UNKNOWN)
			return;
		
		if(seen_sceneParts.containsKey(pure_name))
			MyError.error("story_sceneParts previosly contained such a \"" + pure_name + "\" with the value: " + seen_sceneParts.get(pure_name));
		seen_sceneParts.put(pure_name, scenePart); 
	}
	
	/**
	 * findorCreateInstace searches seen_nodes to find a Node named "name".
	 * if it didn't find it, then load it from kb and adds it to the seen_nodes.
	 * 
	 * TODO: we have temporarily assumed that every redundant input concept refers to the old seen one, not the new,
	 * for example all "پسرک" in the story refers to "پسرک1"
	 * for the new concept of "پسرک" the newNode parameter must be set to true.
	 * 
	 * @param pure_name name of Node to be searched in seen_nodes or kb.
	 * @param newNode is it a new Node or it is previously seen?
	 * @param synTag the SyntaxTag of this node in the sentence. 
	 * @return Node an instance node which the name of its original node is "pure_name".
	 */	
	public Node findorCreateInstance(String pure_name, boolean newNode, SyntaxTag synTag){
		if(pure_name == null || pure_name.equals(""))
			return null;		
		
		//an instance with name "name" exists in seen_nodes.
		if(seen_nodes.containsKey(pure_name)){
			
			ArrayList<Node> seenNodes = seen_nodes.get(pure_name);
			 
			 if(seenNodes == null){
				 seenNodes = new ArrayList<Node>();			 
				 MyError.error(pure_name + " key exists in seen_nodes but no Node exists! probably the map is corrupted!");
			 }
			 
			//It is not a new Node, so return the seenNode
			 if(!newNode){
				 
				//the first Node is the pure Node fetched from kb, and the second is the only instance of it.
				 if(seenNodes.size() == 2)
					 return seenNodes.get(1);
				 
				//TODO: I must select the really desired one, but temporarily I return the last one!
				 else
					 return seenNodes.get(seenNodes.size()-1);
			 }
			 
			//It is a new Node, so a new instance must be created and added to seen_nodes.
			 else{
				 if(seenNodes.size() == 0){
					 MyError.error(pure_name + " key exists in seen_nodes but no Node exists! probably the map is corrupted!");
					 Node pure_node = _TTSKb.addConcept(pure_name);//find original node from kb.					 
					 seenNodes.add(pure_node);
				 }
					 
				//creates an instance from its pure version.
				 Node newInstance = createInstance(pure_name, seenNodes.get(0)); 
				 seenNodes.add(newInstance);
				 return newInstance;			 	 				 
			 }
		}
		//seen_nodes dose not contain this name, so it is not seen yet.
		else{			
			Node pure_node = _TTSKb.addConcept(pure_name);//find original node from kb.
			
			ArrayList<Node> newlySeen = new ArrayList<Node>();				
			newlySeen.add(pure_node);
			Node instance = createInstance(pure_name, pure_node);//creates an instance from its pure version fetched from kb.
			newlySeen.add(instance);
			addToTTSEngine(pure_node, newlySeen, synTag);	
			return instance;			
		}
	}
	
	/**
	 * addRelationInstance searches the seen_nodes for pure_name, 
	 * if it dosen't find it add pure_name as key and an ArrayList of Node with pure relation as first element and its instance as second element.
	 * if it finds pure_name, adds its instance to the end of the list of pure_name mapped ArrayList<Nodes>.
	 * it is important to note that this method dose NOE CREATE an instance, just add the given instance to the seen_nodes. 
	 *  
	 * @param pure_name the name of PlausibleStatement to be added.
	 * @param relationInstance the instance of PlausibleStatement to be added.
	 * @param synTag the SyntaxTag of this node(relation) in the sentence.
	 */
	public void addRelationInstance(String pure_name, PlausibleStatement relationInstance, SyntaxTag synTag){
		if(pure_name == null || pure_name.equals("") || relationInstance == null)
			return;
		
		//it is the first time this relation is seen.
		if(!seen_nodes.containsKey(pure_name)){
			ArrayList<Node> seenRelation = new ArrayList<Node>();
			seenRelation.add(relationInstance.relationType);
			seenRelation.add(relationInstance);
			addToTTSEngine(relationInstance.relationType, seenRelation, synTag);		
		}
		else{// we have seen this relation before
			ArrayList<Node> seenRelation = seen_nodes.get(pure_name);
			
			if(seenRelation == null){
				MyError.error(pure_name + " key exists in seen_nodes but no relation exists! probably the map is corrupted!");
				seenRelation = new ArrayList<Node>();				
				seenRelation.add(relationInstance.relationType);
			}
			seenRelation.add(relationInstance);	
		}
	}
	
	/**
	 * findRelation searches the seen_nodes to find the previous seen plausibleStatement with name "relation_name", if any.
	 * if found returns its pure version, means first element of its list, 
	 * if seen_nodes dosen't have such a relation_name as key, it return null.
	 * 
	 * @param relation_name the name of PlausibleStatement to be search
	 * @return the PlausibleStatement object previously seen, it is cloned PlausibleStatement.
	 */
	public PlausibleStatement findRelationInstance(String relation_name){
		if(relation_name == null || relation_name.equals("-"))
			return null;		
		try{
			if(seen_nodes.containsKey(relation_name)){
				ArrayList<Node> seenRelations = seen_nodes.get(relation_name);
				if(seenRelations == null || seenRelations.size() == 0)
					return null;
				if(seenRelations.size() == 2)//the first Relation is the pure Node fetched from kb, and the second is the only instance of the Relation.
					return (PlausibleStatement)seenRelations.get(1);
				else//TODO: I must select the really desired one, but temporarily I return the last one!
					return (PlausibleStatement)seenRelations.get(seenRelations.size()-1);
			}
			return null;
		}
		catch(Exception e){
			MyError.error(e.getMessage());
			return null;
		}			
	}
	
	public ArrayList<PlausibleStatement> getRelationAllInstances(String relation_name){	
		if(relation_name == null || relation_name.equals(""))
			return null;		
		try{			
			ArrayList<PlausibleStatement> seen_relations = new ArrayList<PlausibleStatement>();
			
			if(seen_nodes.containsKey(relation_name)){
				int size = 0;
				ArrayList<Node> nodes = seen_nodes.get(relation_name);
				
				if(nodes != null)
					size = nodes.size();
				
				for(int i = 1; i < size; i++)					
					seen_relations.add((PlausibleStatement)nodes.get(i));				
			}
			return seen_relations;
		}
		catch(Exception e){
			MyError.error(e.getMessage());
			return null;
		}			
	}
		
	/**
	 * this method adds an instance from originalNode by adding a unique index to the end of its name, 
	 * then adds this instance to kb,
	 * then adds an ISA relation between this instance and the SynSet of originalNode.
	 *   
	 * @param originalName the name of original node which the instance is created from.
	 * @param originalNode the original Node the instance to be created from.
	 * @return the instance created from originalNode. 
	 */
	private Node createInstance(String originalName, Node originalNode){
		if(originalNode == null)
			return null;
		
		if(originalName == null || originalName.equals(""))
			originalName = originalNode.getName();
		
	 	ArrayList<Node> instances = seen_nodes.get(originalName);
	 	
	 	int index = 0;
	 	if(instances == null || instances.size() == 0)
	 		index = 1;
	 	else
	 		index = instances.size();
	 	
	 	String instanceName = originalName;
	 	
		instanceName = instanceName + " ("+ index + ")";
		
		//adding this instance concept to the knowledge base.
		Node instanceNode = _TTSKb.addConcept(instanceName, false, SourceType.TTS);
		
		//finding the synset Node of original Node ---------------------------
		Node SynSetNode = originalNode.getSynSet();
		
		//option a: instaceNode ISA originalNode --> dose not work !!!!!!
		//_kb.addRelation(instanceNode, originalNode, KnowledgeBase.HPR_ISA, SourceType.TTS);
		
		
		//option b: instaceNode ISA synSetNode --> works :)
		if(SynSetNode  != null){
			print("option b: " + instanceNode + " ISA " + SynSetNode);
			_TTSKb.addRelation(instanceNode, SynSetNode, KnowledgeBase.HPR_ISA, SourceType.TTS);
		}
		//option c: instaceNode SIM originalNode  --> works :)
		else{
			print("option c: " + instanceNode + " SIM " + originalNode + " ------ NOTE ---- NOTE ---- NOTE ---- NOTE ---- NOTE ----");		
			_TTSKb.addRelation(instanceNode, originalNode, KnowledgeBase.HPR_SIM, SourceType.TTS);
		}
				
		return instanceNode;		
	}

	/**
	 * checks if this node name is a pure node fetched from kb or 
	 * it is an instance created by "createInstance" method or a relation cloned by addRelation.
	 * @param node 
	 * @return
	 */
	private boolean isInstanceNode(Node node){
		if(node == null)
			return false;
		
		String name = node.getName();
		if(name == null)
			return false;
		
		boolean isInstance = false;
		//both instances created by "createInstance" and relations added by addRelation have (index) at the end of their names. 
		int index = name.indexOf("(");
		if(index != -1)
			isInstance = true;
		//instances created by addRelation have an * at their beginning.			
		index = name.indexOf("*");
		if(index != -1)
			isInstance = true;
		return isInstance;
	}
	
	/**
	 * returns the pure version of concept name as is in kb.
	 * or return the node name if it is pure node.
	 * 
	 * @param node
	 * @return
	 */
	private String makePureName(Node node){		
		if(node == null)
			return null;
		
		if(isInstanceNode(node)){
			String pure_name = node.getName();
			
			if(pure_name == null)
				return null;
			
			int index1 = pure_name.indexOf("(");			
			if(index1 != -1)
				pure_name = pure_name.substring(0, index1).trim();
				
			int index2 = pure_name.indexOf("*");
			if(index2 != -1 && (index2 + 1) < pure_name.length())
				pure_name =  pure_name.substring(index2 + 1);
			return pure_name;
		}
		return node.getName();
	}
		
	/**
	 * this method adds pure_node and instanceNodes to seen_nodes through addTo_seen_nodes method and then
	 * adds pure_node and its ScenePart through addTo_seen_sceneParts() method.
	 * this is done in order to preventing re-querying the kb to detect ScenePart of every instance of a original node in this sceneModel.
	 * 
	 * @param pure_node
	 * @param instanceNodes
	 * @param synTag the SyntaxTag of this node in the sentence. 
	 */
	private void addToTTSEngine(Node pure_node, ArrayList<Node> instanceNodes, SyntaxTag synTag){
		String pure_name = pure_node.getName();		
		addTo_seen_nodes(pure_name, instanceNodes);
		
		ScenePart sp = getScenePart(pure_node, pure_node.getPos(), synTag);		
		addTo_seen_sceneParts(pure_name, sp);
	}

	public Node getPureNode(Node instance) {
		if(instance == null)
			return null;
		
		String pure_name = makePureName(instance);
		
		if(seen_nodes.containsKey(pure_name)){
			
			ArrayList<Node> allInstances = seen_nodes.get(pure_name);
			
			if(!Common.isEmpty(allInstances))
				return allInstances.get(0);
			
			MyError.error(pure_name + " key exists in seen_nodes but no Node exists! probably the map is corrupted!");			
		}
		else
			MyError.error("no such a \"" + pure_name + "\" exists in the seen_nodes!");
		return null;
	}
	
	/**
	 * checks if node is a child of "نفر§n-13075" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	private boolean isHuman(Node node){
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isHuman ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("نفر§n-13075");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);	
		for(PlausibleAnswer ans:answers){
				print("answer: " + ans);					
				if(ans.answer == KnowledgeBase.HPR_YES){
					print(node.getName() + " isHuman \n");
					return true;
				}			
		}	
		print(node + " is NOT Human \n");
		return false;
	}
	
	/**
	 * checks if node is a child of "جانور§n-12239" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	private boolean isAnimal(Node node){		
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isAnimal ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("جانور§n-12239");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isAnimal \n");
				return true;				
			}
		}		
		print(node + " is NOT Animal \n");
		return false;
	}	
	
	/**
	 * checks if node is a child of "راه§n-12894" or "جا§n-12733" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	private boolean isLocation(Node node){		
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isLocation ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("راه§n-12894");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation راه \n");
				return true;				
			}
		}	
		
		pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("جا§n-12733");
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation جا \n");
				return true;				
			}
		}		
		
		print(node + " is NOT Location \n");
		return false;
	}
	
	/**
	 * checks if node is a child of "دوره زمانی§n-12603" or "فاصله زمانی§n-12691" returns true.
	 * @param node the pure node fetched from kb. 
	 * @return
	 */
	private boolean isTime(Node node) {
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isTime ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("دوره زمانی§n-12603");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isTime دوره زمانی \n");
				return true;				
			}
		}	
		
		pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("فاصله زمانی§n-12691");
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isTime فاصله زمانی \n");
				return true;				
			}
		}	
		
		print(node + " is NOT Time \n");
		return false;
	}

	private ArrayList<PlausibleAnswer> writeAnswersTo(Node descriptor, Node argument, Node referent){
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;		
		pq.referent = referent;
		pq.descriptor = descriptor;
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		
		System.out.println("Answers:");
		
		int count = 0;
		for (PlausibleAnswer answer: answers)
		{
			System.out.println(++count + ". " + answer.toString());
			
//			ArrayList<String> justifications = answer.GetTechnicalJustifications();
//			
//			int countJustification = 0;
//			for (String justification: justifications)
//			{
//				System.out.println("-------" + ++countJustification + "--------");
//				System.out.println(justification);
//			}
		}
		print("\tInferences: " + _re.totalCalls);
		print("\tTime: " + _re.reasoningTime / 100 + " ms");
		print("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
		return answers;
		
	}
//	
//	public enum ScenePart {
//		ROLE,
//		
//		SCENE_OBJECT,
//		DYNAMIC_OBJECT,
//		STATIC_OBJECT,
//		
//		LOCATION,
//		
//		TIME,
//		
//		ACTION,
//		OBJECT_ACTION,
//		ROLE_ACTION,
//		
//		EMOTION,
//		ROLE_EMOTION,
//		SCENE_EMOTION,
//		
//		GOAL,
//		ROLE_GOAL,
//		SCENE_GOAL,	
//		
//		UNKNOWN
//	}
	
	/**
	 * recognizing that which scenePart has the node: 
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, ACTION, LOCATION, TIME, EMOTION, GOAL or UNKNOWN?!
	 * this method checks:
	 * <ul> if node SyntaxTag is SUBJECT or SUBJECT_PART or OBJECT or OBJECT_PART, 
	 * 		then calls getSbjObjScenePart() method. 
	 * </ul>
	 * <ul> if node SyntaxTag is ADVERB or ADVERB_PART, 
	 * 		then calls getAdvScenePart() method.
	 * </ul> 		  			
	 * <ul> if node POS is VERB or VERB_PART:
	 *  	then calls getVerbScenePart() method.
	 * </ul>
	 * 
	 * @param pure_node the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @param synTag the SyntaxTag of this node in the sentence.
	 * @return the ScenePart of pure_node, including ScenePart.UNKNOWN.
	 */
	private ScenePart getScenePart(Node pure_node, POS pos, SyntaxTag synTag){
		if(pure_node == null)
			return ScenePart.UNKNOWN;
		
		if(synTag == SyntaxTag.SUBJECT || synTag == SyntaxTag.SUBJECT_PART || 
				synTag == SyntaxTag.OBJECT || synTag == SyntaxTag.OBJECT_PART)
			
			return getSbjObjScenePart(pure_node, pos, synTag);
			
		if(synTag == SyntaxTag.ADVERB || synTag == SyntaxTag.ADVERB_PART)
			
			return getAdvScenePart(pure_node, pos, synTag);
				
		if(synTag == SyntaxTag.VERB || synTag == SyntaxTag.VERB_PART)
		
			return getVerbScenePart(pure_node, pos, synTag);
					
		return ScenePart.UNKNOWN;
	}
	
	/**
	 * recognizing which scenePart has the node: 
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, ACTION, LOCATION, TIME, EMOTION, GOAL or UNKNOWN?!
	 * this method checks:
	 * <ul> if node SyntaxTag is SUBJECT or SUBJECT_PART or OBJECT or OBJECT_PART
	 * <ul> if node pos is NOUN, then checks weather
	 * 		<li> isHuman, than return ScenePart.ROLE </li>
	 * 		<li> isAnimal, than return ScenePart.DYNAMIC_OBJECT </li>
	 * 		<li> isLocation, than return ScenePart.LOCATION </li>
	 * 		<li> isTime, than return ScenePart.TIME </li> 
	 * 		<li> otherwise returns ScenePart.STATIC_OBJECT </li>
	 * </ul>
	 * <ul> we don't make a ScenePart for an adjective. 		
	 * </ul> 
	 *   			
	 * <ul> if node POS is VERB:
	 * 
	 *  	<li> it returns  ACTION. </li> 		
	 * </ul>  
	 * </ul> 
	 * <ul> if node POS is ADVERB: 		
	 * </ul>  
	 * 
	 * 
	 * @param pure_node the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @param synTag the SyntaxTag of this node in the sentence. 
	 * @return the ScenePart of pure_node, including ScenePart.UNKNOWN.
	 */
	private ScenePart getSbjObjScenePart(Node pure_node, POS pos, SyntaxTag synTag){
		if(pure_node == null)
			return ScenePart.UNKNOWN;
		
		if(synTag == SyntaxTag.SUBJECT || synTag == SyntaxTag.SUBJECT_PART || 
				synTag == SyntaxTag.OBJECT || synTag == SyntaxTag.OBJECT_PART){
		
			if(pos == POS.NOUN){			
				//TODO: I must remove these lines!-------			
				if(pure_node.getName().equals("پسر#n2"))
					return ScenePart.ROLE;
	//			if(pure_node.getName().equals("یک#n1"))
	//				return ScenePart.SCENE_OBJECT;
	//			if(pure_node.getName().equals("راه#n9"))
	//				return ScenePart.LOCATION;
	//			if(pure_node.getName().equals("سمت#n4"))
	//				return ScenePart.LOCATION;
	//			if(pure_node.getName().equals("خانه#n10"))
	//				return ScenePart.SCENE_OBJECT;			
				//---------------------------------------			
	
				if(isHuman(pure_node))
					return ScenePart.ROLE;
				
				if(isAnimal(pure_node))
					return ScenePart.DYNAMIC_OBJECT;
				
				if(isLocation(pure_node))
					return ScenePart.LOCATION;			
				
				if(isTime(pure_node))
					return ScenePart.TIME;
				
				return ScenePart.SCENE_OBJECT;
			}
			else if(pos == POS.ADJECTIVE)
//			else if(pos == pos.ADVERB)
		}
					
		return ScenePart.UNKNOWN;
	}
	
	/**
	 * TODO: It must be improved: recognizing that which scenePart has the node: 
	 * a Role (human) or DynamicObject or StaticObject?!
	 * this method checks:
	 * <ul> if node POS is NOUN:
	 * 		<li> if node is a child of "نفر§n-13075" returns ROLE. </li>
	 * 		<li> if node is a child of "جانور§n-12239" returns DYNAMIC_OBJ. </li>
	 * 		<li> if node is a child of "راه§n-12894" or "جا§n-12733" returns LOCATION. </li>
	 * 		<li> if node is a child of "" returns TIME. </li>
	 * 		<li> otherwise returns STATIC_OBJ. </li>  			
	 * <ul> if node POS is VERB:
	 *  	<li> it returns  ACTION. </li> 		
	 * </ul>  
	 * </ul> 
	 * <ul> if node POS is ADVERB: 		
	 * </ul>  
	 * <ul> we don't make a ScenePart for an adjective. 		
	 * </ul> 
	 * 
	 * @param pure_node the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @param synTag the SyntaxTag of this node in the sentence.
	 * @return the ScenePart of pure_node, including ScenePart.UNKNOWN.
	 */
	private ScenePart getAdvScenePart(Node pure_node, POS pos, SyntaxTag synTag){
		if(pos == POS.ADVERB || pos == POS.NOUN){
			if(isLocation(pure_node))
				return ScenePart.LOCATION;			
			
			if(isTime(pure_node))
				return ScenePart.TIME;			
		}
		
		return null;
	}
	
	/**
	 * TODO: It must be improved: recognizing that which scenePart has the node: 
	 * a Role (human) or DynamicObject or StaticObject?!
	 * this method checks:
	 * <ul> if node POS is NOUN:
	 * 		<li> if node is a child of "نفر§n-13075" returns ROLE. </li>
	 * 		<li> if node is a child of "جانور§n-12239" returns DYNAMIC_OBJ. </li>
	 * 		<li> if node is a child of "راه§n-12894" or "جا§n-12733" returns LOCATION. </li>
	 * 		<li> if node is a child of "" returns TIME. </li>
	 * 		<li> otherwise returns STATIC_OBJ. </li>  			
	 * <ul> if node POS is VERB:
	 *  	<li> it returns  ACTION. </li> 		
	 * </ul>  
	 * </ul> 
	 * <ul> if node POS is ADVERB: 		
	 * </ul>  
	 * <ul> we don't make a ScenePart for an adjective. 		
	 * </ul> 
	 * 
	 * @param pure_node the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @param synTag the SyntaxTag of this node in the sentence.
	 * @return the ScenePart of pure_node, including ScenePart.UNKNOWN.
	 */
	private ScenePart getVerbScenePart(Node pure_node, POS pos, SyntaxTag synTag){	
		if(pos == POS.VERB)
			return ScenePart.ACTION;
		return null;
	}
	
	
	/**
	 * this methods searches the internal structure of this TTSEngine (seen_sceneParts) to find the ScenePart mapped to this node.
	 *  
	 * @param node the node which is ScenePart is to be found. it may be pure or instance node!
	 * @param synTag the SyntaxTag of this node in the sentence.
	 * @return 
	 */
	public ScenePart whichScenePart(Node node, SyntaxTag synTag){
		print(node + "~~~~~~~~~~~~~~~ in whichScenePart ~~~~~~~~~~~~");
		if(node == null)
			return ScenePart.UNKNOWN;
		
		String pure_name = makePureName(node);
		ScenePart sp = null;
		
		//if it is seen before!
		if(seen_sceneParts.containsKey(pure_name))			
			sp = seen_sceneParts.get(pure_name);	

		//if it isn't seen before!
		else{
			Node pure_node = getPureNode(node);	
			POS pos = node.getPos();
		
			sp = getScenePart(pure_node, pos, synTag);
			
			addTo_seen_sceneParts(pure_name, sp);			
		}

		print(node + " ScenePart is " + sp + "\n");			
		return sp;
	}	

}
