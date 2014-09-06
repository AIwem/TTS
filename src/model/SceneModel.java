package model;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;
import java.util.Hashtable;

import sceneElement.DynamicObject;
import sceneElement.Location;
import sceneElement.Role;
import sceneElement.SceneEmotion;
import sceneElement.SceneGoal;
import sceneElement.StaticObject;
import sceneElement.Time;

/**
 * 
 * @author hashemi
 *
 */
public class SceneModel {
	
	public SceneModel(KnowledgeBase _kb, SemanticReasoner _re) {		
		this._kb = _kb;
		this._re = _re;
	}


	private KnowledgeBase _kb = null;
	
	private SemanticReasoner _re = null;
	
	private StoryModel story;
	
	private ArrayList<SentenceModel> sentences = new ArrayList<SentenceModel>();
	
	/**
	 * this map holds the Node object seen yet in this SceneModel object. 
	 * It maps the Node pure name as key to the ArrayList<Node> of seen Nodes with this name in this SceneModel object.
	 * for each key, the first Node in ArrayList contains the pure Node object fetched from kb.
	 * the others are different instances of that pure Node.
	 */	
	private Hashtable<String, ArrayList<Node>> scene_nodes_dic = new Hashtable<String,ArrayList<Node>>();
	
	private Hashtable<String, ScenePart> scene_parts = new Hashtable<String, ScenePart>();
		
	private ArrayList<Role> roles = new ArrayList<Role>();
	
	private ArrayList<StaticObject> static_objs = new ArrayList<StaticObject>();
	
	private ArrayList<DynamicObject> dynamic_objs = new ArrayList<DynamicObject>();
	
	private Location location;
	
	private Time time;
	
	private ArrayList<SceneGoal> scene_goals = new ArrayList<SceneGoal>();
		
	private ArrayList<SceneEmotion> scene_emotions = new ArrayList<SceneEmotion>();
		
	
	
	
	public StoryModel getStory() {
		return story;
	}

	public void setStory(StoryModel story) {
		this.story = story;
	}

	public ArrayList<SentenceModel> getSentences() {
		return sentences;
	}

	public void addSentences(ArrayList<SentenceModel> sentences) {
		if(this.sentences != null)
			this.sentences = new ArrayList<SentenceModel>();
		if(sentences != null && sentences.size()>0)
			this.sentences.addAll(sentences);
	}
	
	public void addSentence(SentenceModel sentence) {
		if(this.sentences != null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentence != null)
			this.sentences.add(sentence);
	}

	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void addRoles(ArrayList<Role> roles) {
		if(this.roles != null)
			this.roles = new ArrayList<Role>();
		
		if(roles != null && roles.size()>0)
			this.roles.addAll(roles);
	}
	
	public void addRole(Role role) {
		if(this.roles != null)
			this.roles = new ArrayList<Role>();
		
		if(role != null)
			this.roles.add(role);
	}

	public ArrayList<StaticObject> getStatic_obj() {
		return static_objs;
	}
	
	public void addStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(this.static_objs != null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_objects != null && static_objects.size()>0)
			this.static_objs.addAll(static_objects);
	}
	
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objs != null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_object != null)
			this.static_objs.add(static_object);
	}

	public ArrayList<DynamicObject> getDynamic_obj() {
		return dynamic_objs;
	}

	public void addDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {
		if(this.dynamic_objs != null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_objects != null && dynamic_objects.size()>0)
			this.dynamic_objs.addAll(dynamic_objects);
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objs != null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			this.dynamic_objs.add(dynamic_object);
	}
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public ArrayList<SceneGoal> getScene_goals() {
		return scene_goals;
	}

	public void addScene_goals(ArrayList<SceneGoal> scene_goals) {
		if(this.scene_goals != null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goals != null && scene_goals.size()>0)
			this.scene_goals.addAll(scene_goals);
	}
	
	public void addScene_goal(SceneGoal scene_goal) {
		if(this.scene_goals != null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goal != null)
			this.scene_goals.add(scene_goal);
	}

	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
	}

	public void addScene_emotions(ArrayList<SceneEmotion> scene_emotions) {
		if(this.scene_emotions != null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotions != null && scene_emotions.size()>0)
			this.scene_emotions.addAll(scene_emotions);
	}
	
	public void addScene_emotion(SceneEmotion scene_emotion) {
		if(this.scene_emotions != null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotion != null)
			this.scene_emotions.add(scene_emotion);
	}
	/**
	 * findorCreateInstace searches this sceneModel scene_nodes to find a Node named "name".
	 * if it didn't find it, then load it from kb and adds it to the scene_nodes_dic.
	 * 
	 * TODO: we have temporarily assumed that every redundant input concept refers to the old seen one, not the new,
	 * for example all "پسرک" in the story refers to "پسرک1"
	 * for the new concept of "پسرک" the newNode parameter must be set to true.
	 * 
	 * @param name name of Node to be searched in sceneModel's scene_nodes_dic or kb.
	 * @param newNode is it a new Node or it is previously seen!
	 * @param _kb
	 * @return Node object named "name".
	 */	
	public Node findorCreateInstance(String name, boolean newNode){
		if(name == null || name.equals("-"))
			return null;		
		
		//an instance with name "name" exists in scene_nodes_dic.
		if(scene_nodes_dic.containsKey(name)){
			
			ArrayList<Node> seenNodes = scene_nodes_dic.get(name);
			 
			 if(seenNodes == null)
				 seenNodes = new ArrayList<Node>();
			 if(seenNodes.size() == 0)
				 MyError.error(name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
			 
			//It is not a new Node, so return the seenNode
			 if(!newNode){
				 
				//the first Node is the pure Node fetched from kb, and the second is the only instance of it.
				 if(seenNodes.size() == 2)
					 return seenNodes.get(1);
				 
				//TODO: I must select the really desired one, but temporarily I return the last one!
				 else
					 return seenNodes.get(seenNodes.size()-1);
			 }
			 
			//It is a new Node, so a new instance must be created and added to scene_nodes_dic.
			 else{
				 if(seenNodes.size() > 0){					 
					 
					//creates an instance from its pure version fetched from kb.
					 Node newInstance = createInstance(name, seenNodes.get(0)); 
					 seenNodes.add(newInstance);
					 return newInstance;
				 }
				 else{
					 MyError.error(name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
					 Node n = _kb.addConcept(name);
					 if(n!= null){
						 seenNodes.add(n);
						 Node instance = createInstance(name, n);//creates an instance from its pure version fetched from kb.
						 seenNodes.add(instance);
						 return instance;
					 }
					 else{
						 MyError.error("the node named " + name + " could not be found or even added to the knowledgebase!");
						 return null;
					 }
				 }
			 }
		}
		//scene_nodes_dic dose not contain this name, so it is not seen yet.
		else{			
			Node n = _kb.addConcept(name.toLowerCase());
			if(n!= null){
				ArrayList<Node> newlySeen = new ArrayList<Node>();				
				newlySeen.add(n);
				Node instance = createInstance(name, n);//creates an instance from its pure version fetched from kb.
				newlySeen.add(instance);
				addNodeToScene_nodes_dic(name, newlySeen, null);
				
				return instance;
			 }
			 else{
				 MyError.error("the node named " + name + " could not be found or even added to the knowledgebase!");
				 return null;
			 }
		}
	}
	
	/**
	 * addRelationInstance searches the scene_nodes_dic for pure_name, 
	 * if it dosen't find it add pure_name as key and an ArrayList of Node with pure relation as first element and its instance as second element.
	 * if it finds pure_name, adds its instance to the end of the list of pure_name mapped ArrayList<Nodes>.
	 * it is important to note that this method dose NOE CREATE an instance, just add the given instance to the scene_nodes_dic. 
	 *  
	 * @param pure_name the name of PlausibleStatement to be added.
	 * @param relationInstance the instance of PlausibleStatement to be added.
	 */
	public void addRelationInstance(String pure_name, PlausibleStatement relationInstance){
		if(pure_name == null || pure_name.equals("") || relationInstance == null)
			return;
		
		//it is the first time this relation is seen.
		if(!scene_nodes_dic.containsKey(pure_name)){
			ArrayList<Node> seenRelation = new ArrayList<Node>();
			seenRelation.add(relationInstance.relationType);
			seenRelation.add(relationInstance);
			//addNodeToScene_nodes_dic(pure_name, seenRelation); it is not a concept, so it shouldn;t added to scene_parts
			scene_nodes_dic.put(pure_name, seenRelation);
		}
		else{// we have seen this relation before
			ArrayList<Node> seenRelation = scene_nodes_dic.get(pure_name);
			
			if(seenRelation == null)
				seenRelation = new ArrayList<Node>();
			
			if(seenRelation.size() == 0){
				MyError.error(pure_name + " key exists in scene_nodes_dic but no relation exists! probably the map is corrupted!");
				seenRelation.add(relationInstance.relationType);
			}
			seenRelation.add(relationInstance);		//TODO check name of وضعیت سلامتی	
		}
	}
	
	/**
	 * findRelation searches the scene_nodes_dic to find the previous seen plausibleStatement with name "relation_name", if any.
	 * if found returns it, if scene_nodes_dic dosen't have such a relation_name as key, it return null.
	 * 
	 * @param relation_name the name of PlausibleStatement to be search
	 * @param kb 
	 * @return the PlausibleStatement object previously seen, it is cloned PlausibleStatement.
	 */
	public PlausibleStatement findRelation(String relation_name){
		if(relation_name == null || relation_name.equals("-"))
			return null;		
		try{
			if(scene_nodes_dic.containsKey(relation_name)){
				ArrayList<Node> seenRelations = scene_nodes_dic.get(relation_name);
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
	
	/**
	 * this method adds an instance from originalNode by adding a unique index to the end of its name, 
	 * then adds this instance to kb,
	 * then adds an ISA relation between this instance and the originalNode.
	 *   
	 * @param originalNode the original Node the instance to be created from.
	 * @param kb
	 * @return the instance created from originalNode. 
	 */
	private Node createInstance(String originalName, Node originalNode){
		if(originalNode == null)
			return null;
		
		if(originalName == null || originalName.equals(""))
			originalName = originalNode.getName();
		
	 	ArrayList<Node> instances = scene_nodes_dic.get(originalName);
	 	
	 	int index = 0;
	 	if(instances == null || instances.size() == 0)
	 		index = 1;
	 	else
	 		index = instances.size();
	 	
	 	String instanceName = "";
	 	int s = originalName.indexOf("#");
	 	if(s != -1)
	 		instanceName = originalName.substring(0, s);
	 	
	 	s = originalName.indexOf("§");
	 	if(s != -1)
	 		instanceName = originalName.substring(0, s);
	 	
	 	
	 	
		instanceName = instanceName + "-"+ index;
		
		Node fromKB = _kb.addConcept(instanceName);		
		
		_kb.addRelation(fromKB, originalNode, KnowledgeBase.HPR_ISA);
		//_kb.addRelation(fromKB, originalNode, KnowledgeBase.HPR_SIM);
		
		
		return fromKB;		
	}
	
	public void printDictionary(){
		print("\n scene_node_dic");
		for(String key:scene_nodes_dic.keySet()){
			print(key +":\n " + scene_nodes_dic.get(key));
		}
			
		print("\n scene_parts");
		for(String key:scene_parts.keySet()){
			print(key +":\n " + scene_parts.get(key));
		}
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
		
		boolean isInstance = true;
		int index = name.indexOf("-");
		if(index != -1)
			isInstance = true;
		
		index = name.indexOf("#");		
		if(index != -1)
			isInstance = false;
		
		index = name.indexOf("§");
		if(index != -1)
			isInstance = false;
			
		index = name.indexOf("*");
		if(index != -1)
			isInstance = true;
		return isInstance;
	}
	
	/**
	 * returns the pure version of concept name as are in kb.
	 * @param node
	 * @return
	 */
	private String getPureName(Node node){
		//TODO correct it!
		if(node == null)
			return null;
		
		if(isInstanceNode(node)){
			String name = node.getName();
			if(name == null)
				return null;
			int index = name.indexOf("-");
			String pure_name = "";
			if(index != -1){
				pure_name = name.substring(0, index);
				pure_name += "#n1";
				return pure_name;
			}
			int index1 = name.indexOf("*");
			int index2 = name.indexOf("(");
			return name.substring(index1+1, index2);
		}
		return node.getName();
	}
	
	/**
	 * TODO: It must be improved: recognizing that which scenePart has the node: a Role (human) or DynamicObject or StaticObject?!
	 * this method checks if node is a child of "نفر§n-13075" returns ROLE,
	 *  if node is a child of "جانور§n-12239" returns DYNAMIC_OBJ,
	 *  otherwise return STATIC_OBJ.
	 *  
	 * @param node the pure node fetched from kb.
	 * @param pos the part of speech this node has, only POS of NOUN,VERB, and ADVERB comes here!
	 * @return
	 */
	private ScenePart getScenePart(Node node, POS pos){
		if(node == null)
			return ScenePart.UNKNOWN;
		
		if(pos == POS.NOUN){
			
			//TODO: I must remove these lines!-------
//			if(node.getName().equals("پسرک#n"))
//				return ScenePart.ROLE;
//			if(node.getName().equals("پسر#n2"))
//				return ScenePart.ROLE;
//			//---------------------------------------
			

			if(isHuman(node))
				return ScenePart.ROLE;
			
			if(isAnimal(node))
				return ScenePart.DYNAMIC_OBJ;
			
			return ScenePart.STATIC_OBJ;
		}
		if(pos == POS.VERB)
			return null;
		
		if(pos == POS.ADVERB)
			return null;
		
		return ScenePart.UNKNOWN;
	}
	
	/**
	 * checks if node is a child of "نفر§n-13075" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	public boolean isHuman(Node node){
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isHuman ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _kb.addConcept("نفر§n-13075");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);	
		for(PlausibleAnswer ans:answers){
				print("answer: " + ans);					
				if(ans.answer == KnowledgeBase.HPR_YES){
					print(node.getName() + " isHuman");
					return true;
				}			
		}	
		print(node + " is NOT Human");
		return false;
	}
	
	/**
	 * checks if node is a child of "جانور§n-12239" returns true.
	 * @param node the pure node fetched from kb.
	 * @return
	 */
	public boolean isAnimal(Node node){		
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isAnimal ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _kb.addConcept("جانور§n-12239");
		
		ArrayList<PlausibleAnswer> answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isAnimal");
				return true;				
			}
		}		
		print(node + " is NOT Animal");
		return false;
	}
	
	private void addNodeToScene_nodes_dic(String pure_name, ArrayList<Node> instances, POS pos){
		if(pure_name == null || instances == null)
			return;
		
		scene_nodes_dic.put(pure_name, instances);
		
		if(pos == null){
			Node pure_node = null;
			
			if(instances.size() > 0)
				pure_node = instances.get(0);
			else
				pure_node = _kb.addConcept(pure_name);
			
			pos = pure_node.getPos();
			
			if(pos == POS.ADJECTIVE || pos == POS.SETELLITE_ADJECTIVE || pos == POS.UNKNOWN || pos == POS.ANY){
				print(pure_node + "skipped  from getScenePart!!!!!!!!!!!!!!!!!!!!!!1");
				return;
			}
			
			if(!scene_parts.containsKey(pure_name)){
				ScenePart sp = getScenePart(pure_node, pos);
				if(sp != ScenePart.UNKNOWN)
					scene_parts.put(pure_name, sp);
			}
		}
		
	}
		
	private void print(String toPrint){
		System.out.println(toPrint);
		
	}
	
	public ScenePart whichScenePart(Node node){
		print(node + "~~~~~~~~~~~~~~~ in whichScenePart ~~~~~~~~~~~~");
		if(node == null)
			return ScenePart.UNKNOWN;
		
		String pure_name = "";
		
		if(isInstanceNode(node))
			pure_name = getPureName(node);
		else
			pure_name = node.getName();
		
		//if it is seen before!
		if(scene_parts.containsKey(pure_name)){
			print(node + " is" + scene_parts.get(pure_name));
			return scene_parts.get(pure_name);
		}
		//if it isn't seen before!
		else{
			Node pure_node  = null;
			ScenePart sp = ScenePart.UNKNOWN;
			
			if(scene_nodes_dic.containsKey(pure_name)){
				ArrayList<Node> instances = scene_nodes_dic.get(pure_name);
				if(instances.size() > 0)
					pure_node = instances.get(0);				
			}
//			else{
//				findorCreateInstance(pure_name, false);
//			}
//				
			else
				pure_node = _kb.addConcept(pure_name);
			
			if(pure_node != null){
				sp = getScenePart(pure_node, pure_node.getPos());
				if(sp != null){
//					addNodeToScene_nodes_dic(pure_name, instances, pos);
					scene_parts.put(pure_name, sp);					
				}
			}
			print(node + " pos is " + sp);
			return sp;
		}
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
			
			ArrayList<String> justifications = answer.GetTechnicalJustifications();
			
			int countJustification = 0;
			for (String justification: justifications)
			{
				System.out.println("-------" + ++countJustification + "--------");
				System.out.println(justification);
			}
		}
		print("\tInferences: " + _re.totalCalls);
		print("\tTime: " + _re.reasoningTime / 1000);
		print("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
		return answers;
		
	}
}
			
		
	