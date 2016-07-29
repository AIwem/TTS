package sceneReasoner;

import ir.ac.itrc.qqa.semantic.enums.ExecutionMode;
//import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.enums.SourceType;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.Common;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;

import model.MainSemanticTag;
import model.POS;
import model.SceneModel;
import model.ScenePart;
import model.SemanticTag;
import model.SentenceModel;
import model.Word;
import model.StoryModel;
import model.SubSemanticTag;

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
	private String verbSemanticCapacitiesPath;
	private String verbVisualCapacitiesPath;
	
	private Preprocessor _pp;
	private SceneReasoner _sr;
	
//	Node mozaf_root;	
//	Node adjective_root;
//	Node verb_root;
	
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
	
	
	public TTSEngine(String mainKbFilePath, String myKbFilePath, String verbSemanticCapacitiesPath, String verbVisualCapacitiesPath){
		this.mainKbFilePath = mainKbFilePath;
		this.myKbFilePath = myKbFilePath;
		this.verbSemanticCapacitiesPath = verbSemanticCapacitiesPath;
		this.verbVisualCapacitiesPath = verbVisualCapacitiesPath;
		
		this._TTSKb = new KnowledgeBase();
		this._re = new SemanticReasoner(_TTSKb, ExecutionMode.DEBUG);
		_re.setMaxReasoningDepth(17);
		_re.setMaximumAnswers(1);
		
		loadKb();
		
		_pp = new Preprocessor(_TTSKb, _re, this);
		_sr = new SceneReasoner(_TTSKb, _re, this);
		
//		mozaf_root = _TTSKb.addConcept("mozaf#n");
//		adjective_root = _TTSKb.addConcept("adjective#a");
//		verb_root = _TTSKb.addConcept("verb#v");
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
	 * @param writer 
	 * @return 
	 */
	public void TextToScene(ArrayList<String> scene_inputNL, StoryModel storyModel, boolean isLastScene, PrintWriter writer){
		
		if(scene_inputNL == null || scene_inputNL.size() == 0 || storyModel == null)
			MyError.exit("bad input lines!");			
		
					
		if(!isKbInitialized)
			loadKb();
		
		SceneModel primarySceneModel = new SceneModel(storyModel);
		storyModel.addScene(primarySceneModel);
				
		for(String NLsentence:scene_inputNL){
			
			print("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ NSentence: " + NLsentence);
			
			SentenceModel sentence = _pp.preprocessSentence(NLsentence);	
			
			if(sentence == null){
				MyError.error(" the " + NLsentence + " couldn't get preprocessed!");
				continue;
			}
									
			primarySceneModel.addSentence(sentence);
			sentence.setScene(primarySceneModel);
			
			_pp.preprocessScene(sentence, primarySceneModel, storyModel);
			
			ArrayList<SentenceModel> nested_sents = sentence.get_nested_sentences();
			
			if(!Common.isEmpty(nested_sents))
				for(SentenceModel nest:nested_sents)
					_pp.preprocessScene(nest, primarySceneModel, storyModel);
				
			//moved in preporecessSentence just like loadVerbSemanticCapacities
			//_pp.loadVerbVisualCapacities(sentence);						
		}
				
		if(isLastScene){//the last scene of story
			
			//-------------- postprocessing Location and Time of different scenes previously added to storyModel ------------
			_sr.postprocessLocation(storyModel);
			
			_sr.postprocessTime(storyModel);
			
			//-------------- enriching primarySceneModels of different scenes previously added to storyModel ----------------
			_sr.enrichStoryModel(storyModel);
						
			for(SceneModel scene:storyModel.getScenes())
				writer.println("\nprimarySceneModel\n" + scene);
		}
	
	}
		
	public void checkSemanticReasoner1(Node argument, Node descriptor, Node referent)	{
		

		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;
		pq.referent = referent;
//		PlausibleStatement ps = (PlausibleStatement)descriptor;
		pq.descriptor = descriptor;//.relationType;		
		
//		ArrayList<PlausibleStatement> ans = descriptor.findOutRelations(KnowledgeBase.HPR_CXTIME);
//		pq.cxTime = ans.get(0).referent;
		
	  

		
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
		pq.referent = _TTSKb.addConcept("جانور§n-12239", false);		
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

	/**
	 * 
	 * @param descriptor
	 * @param argument
	 * @param referent
	 * @return list of answers, not null
	 */
	public ArrayList<PlausibleAnswer> inferRuleFromKB(Node descriptor, Node argument, Node referent){
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;		
		pq.referent = referent;
		pq.descriptor = descriptor;
		
		print("\nQuestion: " + pq.argument + " -->" + pq.descriptor + " --> " + pq.referent);
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestionByRule(pq);	
		
		if (answers == null)
		{
			answers = new ArrayList<PlausibleAnswer>();
		}

		return answers;		
	}
	
	public ArrayList<PlausibleAnswer> writeAnswersTo(Node descriptor, Node argument, Node referent){
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.argument = argument;		
		pq.referent = referent;
		pq.descriptor = descriptor;
		
		print("\nQuestion: " + pq.argument + " -->" + pq.descriptor + " --> " + pq.referent);
		
		ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
				
		print("Answers: " + answers.size());
		
		print("\tInferences: " + _re.totalCalls);
		print("\tTime: " + _re.reasoningTime / 100 + " ms");
		
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
//		print("\tThroughput: " + (_re.totalCalls / (_re.reasoningTime*1000)) * 1000 + " inference/s");
		return answers;
		
	}
	
	public int loadKb()	
	{
		int loaded = 0;
		Long start = System.currentTimeMillis();
		
		loaded = _TTSKb.importKb(mainKbFilePath);
		loaded = _TTSKb.importKb(verbSemanticCapacitiesPath);
		loaded = _TTSKb.importKb(verbVisualCapacitiesPath);
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
	public Node findorCreateInstance(String pure_name, boolean newNode){
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
					 Node pure_node = _TTSKb.addConcept(pure_name, false);//find original node from kb.					 
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
			Node pure_node = _TTSKb.addConcept(pure_name, false);//find original node from kb.
			
			ArrayList<Node> newlySeen = new ArrayList<Node>();				
			newlySeen.add(pure_node);
			Node instance = createInstance(pure_name, pure_node);//creates an instance from its pure version fetched from kb.
			newlySeen.add(instance);
			addToTTSEngine(pure_node, newlySeen);	
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
	public void addRelationInstance(String pure_name, PlausibleStatement relationInstance){
		if(pure_name == null || pure_name.equals("") || relationInstance == null)
			return;
		
		//it is the first time this relation is seen.
		if(!seen_nodes.containsKey(pure_name)){
			ArrayList<Node> seenRelation = new ArrayList<Node>();
			seenRelation.add(relationInstance.relationType);
			seenRelation.add(relationInstance);
			addToTTSEngine(relationInstance.relationType, seenRelation);		
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
	 	
		instanceName = instanceName + " ["+ index + "]";
		
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
		int index = name.indexOf("[");
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
			
			int index1 = pure_name.indexOf("[");			
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
	private void addToTTSEngine(Node pure_node, ArrayList<Node> instanceNodes){
		String pure_name = pure_node.getName();		
		addTo_seen_nodes(pure_name, instanceNodes);
		
//		ScenePart sp = getScenePart(pure_node, pure_node.getPos(), synTag);		
//		addTo_seen_sceneParts(pure_name, sp);
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
		
		print(node + ".............. in isHuman ...........................");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("نفر§n-13075", false);
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
	
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
	 * checks if node isAnimal or ... returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	
	private boolean isDynamicObject(Node node){
		return isAnimal(node);
	}
	
	/**
	 * checks if node is a child of "جانور§n-12239" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	private boolean isAnimal(Node node){		
		if(node == null)
			return false;
		
		print(node + ".............. in isAnimal ..........................");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("جانور§n-12239", false);
		
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
		
		print(node + ".............. in isLocation ........................");
			
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("جا§n-12733", false);
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation جا \n");
				return true;				
			}
		}		
		
		pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("محل§n-12748", false);
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation محل \n");
				return true;				
			}
		}
				
		pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("ساختار§n-12875", false);
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation ساختار \n");
				return true;				
			}
		}

		
		pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("راه§n-12894", false);
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation راه \n");
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
				
		print(node + ".............. in isTime ............................");
		
		//TODO: add "زمان خاص§n-12649"
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _TTSKb.addConcept("دوره زمانی§n-12603", false);
		
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
		pq.referent = _TTSKb.addConcept("فاصله زمانی§n-12691", false);
		
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
	
	/**
	 * recognizing that which scenePart has the node: 
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, ACTION, LOCATION, TIME, EMOTION, GOAL or UNKNOWN?!
	 * this method checks:
	 * <li> if semanticTag is ARG0, 
	 * 		then calls getArg0ScenePart() method. 
	 * </li>
	 * <li> if semanticTag is ARG1,  
	 * 		then calls getArg1ScenePart() method.
	 * </li> 		  			
	 * <li>if semanticTag is ARG2,
	 *  	then calls getArg2ScenePart() method.
	 * </li>
	 * <li>if semanticTag is ARG3,
	 *  	then calls getArg3ScenePart() method.
	 * </li>
	 * <li>if semanticTag is ARG4,
	 *  	then calls getArg4ScenePart() method.
	 * </li>
	 * <li>if semanticTag is a SubSemanticTag,
	 *  	then calls getSubArgScenePart method.
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @param semanticTag the semanticTag of this node in the sentence.
	 * @return the ScenePart of pureNode, otherwise ScenePart.UNKNOWN not null.
	 */
	private ScenePart getScenePart(Node pureNode, POS pos, SemanticTag semanticTag) {
		
		print(pureNode + " ............. in getScenePart ........... " + semanticTag);
		
		//TODO: I must remove these lines!-------			
		if(pureNode.getName().equals("پسر#n2"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("مادر#n1"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("یک#n1"))
			return ScenePart.SCENE_OBJECT;
		if(pureNode.getName().equals("کبوتر#n1"))
			return ScenePart.DYNAMIC_OBJECT;
		if(pureNode.getName().equals("راه#n9"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("سمت#n6"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("طرف#n3"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("خانه#n10"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("اتاق#n2"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("بلافاصله#r1"))
			return ScenePart.TIME;		
		if(pureNode.getName().equals("نفر#n4"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("معلم#n1"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("محمد#n"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("جا#n5"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("وسایل#n1"))
			return ScenePart.STATIC_OBJECT;	
		if(pureNode.getName().equals("صدا#n2"))
			return ScenePart.DYNAMIC_OBJECT;
		if(pureNode.getName().equals("یک‌باره#r1"))
			return ScenePart.TIME;
		if(pureNode.getName().equals("دختر#n3"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("بچه#n6"))
			return ScenePart.ROLE;
		if(pureNode.getName().equals("نوک#n1"))
			return ScenePart.STATIC_OBJECT;
		if(pureNode.getName().equals("شیشه#n3"))
			return ScenePart.STATIC_OBJECT;
		if(pureNode.getName().equals("روی#n5"))
			return ScenePart.LOCATION;
		if(pureNode.getName().equals("خانه#n10"))
			return ScenePart.LOCATION;		
		//---------------------------------------			
		
		if(pureNode == null || pos == null || semanticTag == null)
			return ScenePart.UNKNOWN;

		//It is a mainSemanticTag
		if(semanticTag.isMainSemanticTag()){
			MainSemanticTag mainSemTag = semanticTag.convertToMainSemanticTag();
			
			if(mainSemTag == null){
				MyError.error("wrong MainsemanticTag" + semanticTag);
				return ScenePart.UNKNOWN;
			}
				
			if(mainSemTag.isArg0())
				return getArg0ScenePart(pureNode, pos);
			
			if(mainSemTag.isArg1())
				return getArg1ScenePart(pureNode, pos);
			
			if(mainSemTag.isArg2())				
				return getArg2ScenePart(pureNode, mainSemTag, pos);
			
			if(mainSemTag.isArg3())
				return getArg3ScenePart(pureNode, mainSemTag, pos);
			
			if(mainSemTag.isArg4())
				return getArg4ScenePart(pureNode);
		}
		//It is a subSemanticTag
		else if(semanticTag.isSubSemanticTag()){
//			MyError.error("SemanticTag here should only be MainSemanticTag not subSemanticTag!");
			
			SubSemanticTag subSemArg = semanticTag.convertToSubSemanticTag();
			
			if(subSemArg == null){
				MyError.error("wrong SubSemanticTag" + semanticTag);
				return ScenePart.UNKNOWN;
			}
			
			return getSubArgScenePart(pureNode, subSemArg, pos);
		}				
		return ScenePart.UNKNOWN;
	}		
	
	/**
	 * 
	 * 
	 * @param pureNode the pure node fetched from kb.  
	 * @return the ScenePart of pureNode, otherwise ScenePart.UNKNOWN not null.
	 */
	private ScenePart getGeneralScenePart(Node pureNode) {
		
		//TODO: I must remove these lines!-------	
		if(pureNode.getName().equals("خانه#n10"))
			return ScenePart.LOCATION;	
		
		if(isDynamicObject(pureNode))
			return ScenePart.DYNAMIC_OBJECT;
		
		if(isLocation(pureNode))
			return  ScenePart.LOCATION;
		
		if(isHuman(pureNode))
			return ScenePart.ROLE;
		
		if(isTime(pureNode))
			return ScenePart.TIME;
				
		return ScenePart.STATIC_OBJECT;		
	}
	
	/**
	 * recognizing which scenePart has this ARG0 node: 
	 * a ROLE, DYNAMIC_OBJECT?
	 * 
	 * because this is ARG0 node so it must perform an action.
	 * only ROLE or DYNAMIC_OBJECT can perform action.
	 * this method checks: 
	 * <li> it checks weather
	 * 		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> otherwise, returns ScenePart.DYNAMIC_OBJECT </ul> 		
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @return the ScenePart of pureNode, including ROLE, DYNAMIC_OBJECT, or UNKNOWN. no null will be returned.
	 */
	private ScenePart getArg0ScenePart(Node pureNode, POS pos) {
				
//		if(pos == POS.N){
			if(isHuman(pureNode))
				return ScenePart.ROLE;
			
			return ScenePart.DYNAMIC_OBJECT; 
//		}			
//		return ScenePart.UNKNOWN;
	}
	
	/**
	 * recognizing which scenePart has this ARG1 node: 
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, LOCATION?
	 * 
	 * because this is ARG1 node so the action is performed on it.
	 * only ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, or LOCATION can got action performed on!
	 * this method checks: 
	 * <li> it checks weather
	 * 		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul>
	 * 		<ul> if isLocation, then returns ScenePart.LOCATION </ul>
	 * 		<ul> otherwise, returns ScenePart.STATIC_OBJECT </ul> 		
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @return the ScenePart of pureNode, including ROLE, DYNAMIC_OBJECT, or UNKNOWN. no null will be returned.
	 */
	private ScenePart getArg1ScenePart(Node pureNode, POS pos) {
		
//		if(pos == POS.N){		
		
		if(isDynamicObject(pureNode))
			return ScenePart.DYNAMIC_OBJECT;
		
		if(isLocation(pureNode))
			return  ScenePart.LOCATION;
		
		if(isHuman(pureNode))
			return ScenePart.ROLE;
		
		return ScenePart.STATIC_OBJECT;
//	}
		
//		return ScenePart.UNKNOWN;
	}
	
	/**
	 * recognizing which scenePart has this ARG2 node:
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, LOCATION?
	 * 
	 * because this is ARG2 node so it can be any of extend, obj2, beneficiary, instrument, attribute, and goal-endstate.
	 * this method checks: 
	 * <li> if ARG2 is ARG2_OBJ2 or ARG2_BENEFICIARY,
	 * 		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul>
	 * 		<ul> if isLocation, then returns ScenePart.LOCATION </ul>
	 * 		<ul> otherwise, returns ScenePart.STATIC_OBJECT </ul> 		
	 * </li>
	 * <li> if ARG2 is ARG2_INSTRUMENT
	 *		<ul>then returns ScenePart.STATIC_OBJECT</ul> 
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb.
	 * @param semanticTag 
	 * @param pos the part of speech this node has.
	 * @return the ScenePart of pureNode, including ROLE, DYNAMIC_OBJECT, or UNKNOWN. no null will be returned.
	 */
	private ScenePart getArg2ScenePart(Node pureNode, MainSemanticTag semanticTag, POS pos) {
		
		if(semanticTag == MainSemanticTag.ARG2_OBJ2 || semanticTag == MainSemanticTag.ARG2_BENEFICIARY){
		
//			if(pos == POS.N){
	
			if(isDynamicObject(pureNode))
				return ScenePart.DYNAMIC_OBJECT;
			
			if(isLocation(pureNode))
				return  ScenePart.LOCATION;
			
			if(isHuman(pureNode))
				return ScenePart.ROLE;
					
			return ScenePart.STATIC_OBJECT;
//		}
		}
		else if(semanticTag == MainSemanticTag.ARG2_INSTRUMENT){//TODO: check if it is correct?

			return ScenePart.STATIC_OBJECT;
		}
		else if(semanticTag == MainSemanticTag.ARG2_GOAL_ENDSTATE){
			return ScenePart.SCENE_GOAL;
		}
		
		return ScenePart.UNKNOWN;
	}

	/**
	 * recognizing which scenePart has this ARG3 node:
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, LOCATION?
	 * 
	 * because this is ARG3 node so it can be any of source-startpoint, beneficiary, instrument, attribute.
	 * this method checks: 
	 * <li> if ARG3 is ARG3_BENEFICIARY,
	 * 		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul>
	 * 		<ul> if isLocation, then returns ScenePart.LOCATION </ul>
	 * 		<ul> otherwise, returns ScenePart.STATIC_OBJECT </ul> 		
	 * </li>
	 * <li> if ARG3 is ARG3_INSTRUMENT
	 *		<ul>then returns ScenePart.STATIC_OBJECT</ul> 
	 * </li>
	 * <li> if ARG3 is ARG3_STARTPOINT
	 *		<ul> if isLocation, then returns ScenePart.LOCATION</ul> 
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb.
	 * @param semanticTag 
	 * @param pos the part of speech this node has.
	 * @return the ScenePart of pureNode, including ROLE, DYNAMIC_OBJECT, or UNKNOWN. no null will be returned.
	 */
	private ScenePart getArg3ScenePart(Node pureNode, MainSemanticTag semanticTag, POS pos) {
		
		if(semanticTag == MainSemanticTag.ARG3_BENEFICIARY){
					
//			if(pos == POS.N){
				
				if(isDynamicObject(pureNode))
					return ScenePart.DYNAMIC_OBJECT;
				
				if(isLocation(pureNode))
					return  ScenePart.LOCATION;
				
				if(isHuman(pureNode))
					return ScenePart.ROLE;
	
				return ScenePart.STATIC_OBJECT;
//			}
		}
		else if(semanticTag == MainSemanticTag.ARG3_INSTRUMENT){//TODO: check if it is correct?
			return ScenePart.STATIC_OBJECT;
		}
		else if(semanticTag == MainSemanticTag.ARG3_SOURCE_STARTPOINT){
			if(isLocation(pureNode))
				return ScenePart.LOCATION;
		}		
		return ScenePart.UNKNOWN;
	}
	
	/**
	 * recognizing which scenePart has this ARG4 node:
	 * a LOCATION?
	 * 
	 * because this is ARG4 node so it can be only endpoint.
	 * this method checks: 
	 * <li> if ARG4 is ARG4_ENDPOINT
	 *		<ul> if isLocation returns ScenePart.LOCATION</ul> 
	 * </li>
	 *  
	 * @param pureNode the pure node fetched from kb. 
	 * @return the ScenePart of pureNode, only LOCATION, or UNKNOWN. no null will be returned.
	 */
	private ScenePart getArg4ScenePart(Node pureNode) {		
		
		if(isLocation(pureNode))
			return  ScenePart.LOCATION;
		
		return ScenePart.UNKNOWN;
	}
	
	/**
	 * recognizing which scenePart has this SubSemanticArg node:
	 * a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, LOCATION?
	 *	 
	 * this method checks:
	 * <li> if subSemArg is Arg-LOC	کجا رویداد(فعل) اتفاق می‌افتد؟مکان فیزکی و انتزاعی
	 *		<ul> if isLocation returns ScenePart.LOCATION</ul> 
	 * </li> 
	 * <li> if subSemArg is Arg-DIR	مسیر حرکت
	 *		<ul> if isLocation returns ScenePart.LOCATION</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-TMP	زمان
	 *		<ul> if isTime returns ScenePart.TIME</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-MNR	چگونه؟
	 *		<ul> this method won't be called!</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-PRP	منظور، هدف و انگیزه رویداد
	 *		<ul> returns ScenePart.SCENE_GOAL</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-GOL	غایت فعل + برای کس یا چیز دیگر(مقصد افعال حرکتی،سودبرنده افعال دیگر)
	 * 		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul>
	 * 		<ul> if isLocation, then returns ScenePart.LOCATION </ul>
	 * 		<ul> otherwise, returns ScenePart.STATIC_OBJECT </ul>
	 * </li> 
	 * <li> if subSemArg is Arg-CAU	هدف انجام عمل، چرا؟ </li>
	 *		<ul> returns ScenePart.SCENE_GOAL</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-COM	همراه با چه کسی/نهادی (انسان یا سازمان، شیء نمیشود)ه
	 *		<ul> if isHuman, then returns ScenePart.ROLE </ul>
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul> 		 
	 * </li>
	 * <li> if subSemArg is Arg-INS	ابزار یا شیء انجام رویداد
	 * 		<ul> if isAnimal, then returns ScenePart.DYNAMIC_OBJECT </ul>
	 *		<ul> else returns ScenePart.STATIC_OBJECT</ul> 
	 * </li>
	 * <li> if subSemArg is Arg-EXT	میزان تغییر حاصل از فعل
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-REC	ضمایر انعکاسی و دوطرفه (خود، یکدیگر)
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-MOD	فعل وجهی(باید، ممکن‌است، قادراست، شاید، احتمالا)
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-NEG	عدم وقوع رویداد
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-CND	تحلیل جملات شرطی
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-DIS	علایم گفتمان (بنابراین، ازاین رو)
	 *		<ul> nothing yet! </ul> 
	 * </li>
	 * <li> if subSemArg is Arg-ADV	هر قید دیگری که در بالا نگنجد
	 *		<ul> nothing yet! </ul> 
	 * </li>		
	 *  
	 * @param pureNode the pure node fetched from kb. 
	 * @return the ScenePart of pureNode, LOCATION, TIME or UNKNOWN. no null will be returned.
	 */
	private ScenePart getSubArgScenePart(Node pureNode, SubSemanticTag subSemArg, POS pos) {
		
		if(subSemArg == SubSemanticTag.DIR)
			if(isLocation(pureNode))
				return ScenePart.LOCATION;
		
		if(subSemArg == SubSemanticTag.LOC)
			if(isLocation(pureNode))
				return ScenePart.LOCATION;
		
		if(subSemArg == SubSemanticTag.TMP)		
			if(isTime(pureNode))
				return ScenePart.TIME;
		
		
		if(subSemArg == SubSemanticTag.GOL)	{			
//			if(pos == POS.NOUN){
				if(isHuman(pureNode))
					return ScenePart.ROLE;
				
				if(isDynamicObject(pureNode))
					return ScenePart.DYNAMIC_OBJECT;
				
				if(isLocation(pureNode))
					return  ScenePart.LOCATION;
				
				return ScenePart.STATIC_OBJECT;
//			}			
		}
		
		if(subSemArg == SubSemanticTag.PRP || subSemArg == SubSemanticTag.CAU)			
			return ScenePart.SCENE_GOAL;
		
		if(subSemArg == SubSemanticTag.COM){
			if(isHuman(pureNode))
				return ScenePart.ROLE;
			
			if(isDynamicObject(pureNode))
				return ScenePart.DYNAMIC_OBJECT;
		}
		
		if(subSemArg == SubSemanticTag.INS){
			if(isDynamicObject(pureNode))
				return ScenePart.DYNAMIC_OBJECT;
			
//			if(isHuman(pureNode))
//				return ScenePart.ROLE;
			
			return ScenePart.STATIC_OBJECT;
		}		
		
		return ScenePart.UNKNOWN;
	}
	
//	/**
//	 * this methods searches the internal structure of this TTSEngine (seen_sceneParts) to check whether this node is DYNAMIC_OBJECT?
//	 * If didn't find it calls isDynamicObejct to reason whether this word ScenePart is DYNAMIC_OBJECT or not.
//	 * 
//	 * @param word the word which its ScenePart is to be reasoned about.
//	 * @return returns whether this word is DYNAMIC_OBJECT or not?
//	 */
//
//	public boolean isWordDynamicObject(Word word){		
//		
//		print(word + " ............. in isWordDynamicObject ...............");
//		
//		if(word == null || word._wsd == null)
//			return false;
//		
//		Node partNode = word._wsd;
//				
//		String pureName = makePureName(partNode);
//		
//		if(pureName == null || pureName.equals(""))
//			return false;	
//		
//		ScenePart sp = null;	
//		
//		boolean isDyn = false;
//		
//		//if it is seen before!
//		if(seen_sceneParts.containsKey(pureName)){			
//			sp = seen_sceneParts.get(pureName);
//			if(sp == ScenePart.DYNAMIC_OBJECT)
//				isDyn = true;		
//		} 
//		else{//if it isn't seen before!
//			Node pureNode = getPureNode(partNode);
//			
//			isDyn = isDynamicObject(pureNode);
//					
//			if(isDyn)				
//				sp = ScenePart.DYNAMIC_OBJECT;		
//			else
//				sp = ScenePart.UNKNOWN;
//			
//			addTo_seen_sceneParts(pureName, sp);		
//		}
//
//		print(partNode + " ScenePart is " + sp);			
//		
//		return isDyn;
//
//	}
//
//	/**
//	 * this methods searches the internal structure of this TTSEngine (seen_sceneParts) to check whether this node is ROLE?
//	 * If didn't find it calls isHuman to reason whether this word ScenePart is ROLE or not.
//	 * 
//	 * @param word the word which its ScenePart is to be reasoned about.
//	 * @return returns whether this word is ROLE or not?
//	 */
//
//	public boolean isWordRole(Word word){		
//		
//		print(word + " ............. in isWordRole .....................");
//		
//		if(word == null || word._wsd == null)
//			return false;
//		
//		Node partNode = word._wsd;
//				
//		String pureName = makePureName(partNode);
//		
//		if(pureName == null || pureName.equals(""))
//			return false;	
//		
//		ScenePart sp = null;	
//		
//		boolean isRole = false;
//		
//		//if it is seen before!
//		if(seen_sceneParts.containsKey(pureName)){			
//			sp = seen_sceneParts.get(pureName);
//			if(sp == ScenePart.ROLE)
//				isRole = true;		
//		} 
//		else{//if it isn't seen before!
//			Node pureNode = getPureNode(partNode);
//			
//			isRole = isHuman(pureNode);
//					
//			if(isRole)				
//				sp = ScenePart.ROLE;		
//			else
//				sp = ScenePart.UNKNOWN;
//			
//			addTo_seen_sceneParts(pureName, sp);		
//		}
//
//		print(partNode + " ScenePart is " + sp);			
//		
//		return isRole;
//
//	}

	
	/**
	 * this methods searches the internal structure of this TTSEngine (seen_sceneParts) to find the ScenePart mapped to this node.
	 * If if finds then return the previously reasoned ScenePart,
	 * otherwise it calls getScenePart to reason about the ScenePart of this word
	 * 
	 * @param word the word which its ScenePart is to be reasoned about.
	 * @return returns a ROLE, DYNAMIC_OBJECT, STATIC_OBJECT, ACTION, LOCATION, TIME, EMOTION, GOAL or UNKNOWN?! no null will be returned!
	 */
	
	public ScenePart whichScenePart(Word word){

		print(word + " ............. in whichScenePart ....................");
		
		if(word == null || word._wsd == null)
			return ScenePart.UNKNOWN;
		
		Node partNode = word._wsd;
		
		String pureName = makePureName(partNode);
		
		if(pureName == null || pureName.equals(""))
			return ScenePart.UNKNOWN;	
		
		ScenePart sp = null;	
		boolean flag = false;
		
		//if it is seen before!
		if(seen_sceneParts.containsKey(pureName)){			
			sp = seen_sceneParts.get(pureName);	
			flag = true;
			
		}//if it isn't seen before!
		else if(word._semanticTag != null){
			Node pureNode = getPureNode(partNode);
		
			sp = getScenePart(pureNode, word._gPOS, word._semanticTag);			
		}
		else{
			Node pureNode = getPureNode(partNode);
			
			sp = getGeneralScenePart(pureNode);		
		}
		
		if(!flag && sp != null && sp != ScenePart.UNKNOWN)
			addTo_seen_sceneParts(pureName, sp);
		
		print(partNode + " ScenePart is " + sp);			
		return sp;
	}

}
