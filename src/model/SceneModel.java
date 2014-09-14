package model;

import ir.ac.itrc.qqa.semantic.enums.POS;
import ir.ac.itrc.qqa.semantic.enums.SourceType;
import ir.ac.itrc.qqa.semantic.kb.KnowledgeBase;
import ir.ac.itrc.qqa.semantic.kb.Node;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleAnswer;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleQuestion;
import ir.ac.itrc.qqa.semantic.reasoning.PlausibleStatement;
import ir.ac.itrc.qqa.semantic.reasoning.SemanticReasoner;
import ir.ac.itrc.qqa.semantic.util.MyError;

import java.util.ArrayList;


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
	
	private KnowledgeBase _kb = null;
	
	private SemanticReasoner _re = null;
	
	private StoryModel story;
	
	private ArrayList<SentenceModel> sentences = new ArrayList<SentenceModel>();
	
	
		
	private ArrayList<Role> roles = new ArrayList<Role>();	
	
	private ArrayList<StaticObject> static_objs = new ArrayList<StaticObject>();
	
	private ArrayList<DynamicObject> dynamic_objs = new ArrayList<DynamicObject>();
	
	private Location location;
	
	private Time time;
	
	private ArrayList<SceneGoal> scene_goals = new ArrayList<SceneGoal>();
		
	private ArrayList<SceneEmotion> scene_emotions = new ArrayList<SceneEmotion>();
	
		
	public SceneModel(KnowledgeBase _kb, SemanticReasoner _re) {		
		this._kb = _kb;
		this._re = _re;		
				
		this.sentences = new ArrayList<SentenceModel>();
		
		this.roles = new ArrayList<Role>();
		this.static_objs = new ArrayList<StaticObject>();
		this.dynamic_objs = new ArrayList<DynamicObject>();
		this.scene_goals = new ArrayList<SceneGoal>();
		this.scene_emotions = new ArrayList<SceneEmotion>();
	
	}
		
		
	public StoryModel getStory() {
		return story;
	}

	public void setStory(StoryModel story) {
		this.story = story;
	}

	public ArrayList<SentenceModel> getSentences() {
		return sentences;
	}

	public void addAllSentences(ArrayList<SentenceModel> sentences) {
		if(this.sentences == null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentences != null && sentences.size()>0)
			this.sentences.addAll(sentences);
	}
	
	public void addSentence(SentenceModel sentence) {
		if(this.sentences == null)
			this.sentences = new ArrayList<SentenceModel>();
		
		if(sentence != null)
			this.sentences.add(sentence);
	}

	public ArrayList<Role> getRoles() {
		return roles;
	}

	public void addAllRoles(ArrayList<Role> roles) {
		if(this.roles == null)
			this.roles = new ArrayList<Role>();
		
		if(roles != null && roles.size()>0)
			this.roles.addAll(roles);
	}
	
	public void addRole(Role role) {
		if(this.roles == null)
			this.roles = new ArrayList<Role>();
		
		if(role != null)
			this.roles.add(role);
	}

	public ArrayList<StaticObject> getStatic_objects() {
		return static_objs;
	}
	
	public void addAllStatic_bjects(ArrayList<StaticObject> static_objects) {
		if(this.static_objs == null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_objects != null && static_objects.size()>0)
			this.static_objs.addAll(static_objects);
	}
	
	public void addStatic_object(StaticObject static_object) {
		if(this.static_objs == null)
			this.static_objs = new ArrayList<StaticObject>();
		
		if(static_object != null)
			this.static_objs.add(static_object);
	}

	public ArrayList<DynamicObject> getDynamic_objects() {
		return dynamic_objs;
	}

	public void addAllDynamic_objects(ArrayList<DynamicObject> dynamic_objects) {
		if(this.dynamic_objs == null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_objects != null && dynamic_objects.size()>0)
			this.dynamic_objs.addAll(dynamic_objects);
	}
	
	public void addDynamic_object(DynamicObject dynamic_object) {
		if(this.dynamic_objs == null)
			this.dynamic_objs = new ArrayList<DynamicObject>();
		
		if(dynamic_object != null)
			this.dynamic_objs.add(dynamic_object);
	}
	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		if(this.location != null)
			MyError.error("this SceneModel previouly has a location " + this.location);
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

	public void addAllScene_goals(ArrayList<SceneGoal> scene_goals) {
		if(this.scene_goals == null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goals != null && scene_goals.size()>0)
			this.scene_goals.addAll(scene_goals);
	}
	
	public void addScene_goal(SceneGoal scene_goal) {
		if(this.scene_goals == null)
			this.scene_goals = new ArrayList<SceneGoal>();
		
		if(scene_goal != null)
			this.scene_goals.add(scene_goal);
	}

	public ArrayList<SceneEmotion> getScene_emotions() {
		return scene_emotions;
	}

	public void addAllScene_emotions(ArrayList<SceneEmotion> scene_emotions) {
		if(this.scene_emotions == null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotions != null && scene_emotions.size()>0)
			this.scene_emotions.addAll(scene_emotions);
	}
	
	public void addScene_emotion(SceneEmotion scene_emotion) {
		if(this.scene_emotions == null)
			this.scene_emotions = new ArrayList<SceneEmotion>();
		
		if(scene_emotion != null)
			this.scene_emotions.add(scene_emotion);
	}
	
	private void print(String toPrint){
		System.out.println(toPrint);
		
	}

	
	/**
	 * findorCreateInstace searches this sceneModel scene_nodes to find a Node named "name".
	 * if it didn't find it, then load it from kb and adds it to the scene_nodes_dic.
	 * 
	 * TODO: we have temporarily assumed that every redundant input concept refers to the old seen one, not the new,
	 * for example all "پسرک" in the story refers to "پسرک1"
	 * for the new concept of "پسرک" the newNode parameter must be set to true.
	 * 
	 * @param pure_name name of Node to be searched in sceneModel's scene_nodes_dic or kb.
	 * @param newNode is it a new Node or it is previously seen? 
	 * @return Node an instance node which the name of its original node is "pure_name".
	 */	
	public Node findorCreateInstance(String pure_name, boolean newNode){
		if(pure_name == null || pure_name.equals(""))
			return null;		
		
		//an instance with name "name" exists in scene_nodes_dic.
		if(story.isSeenNode(pure_name)){
			
			ArrayList<Node> seenNodes = story.getSeenNode(pure_name);
			 
			 if(seenNodes == null){
				 seenNodes = new ArrayList<Node>();			 
				 MyError.error(pure_name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
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
			 
			//It is a new Node, so a new instance must be created and added to scene_nodes_dic.
			 else{
				 if(seenNodes.size() > 0){					 
					 
					//creates an instance from its pure version fetched from kb.
					 Node newInstance = createInstance(pure_name, seenNodes.get(0)); 
					 seenNodes.add(newInstance);
					 return newInstance;
				 }
				 else{
					 MyError.error(pure_name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");
					 Node pure_node = _kb.addConcept(pure_name);//find original node from kb.
					 
					 seenNodes.add(pure_node);
					 Node instance = createInstance(pure_name, pure_node);//creates an instance from its pure version fetched from kb.
					 seenNodes.add(instance);
					 return instance;
				 }
			 }
		}
		//scene_nodes_dic dose not contain this name, so it is not seen yet.
		else{			
			Node pure_node = _kb.addConcept(pure_name);//find original node from kb.
			
			ArrayList<Node> newlySeen = new ArrayList<Node>();				
			newlySeen.add(pure_node);
			Node instance = createInstance(pure_name, pure_node);//creates an instance from its pure version fetched from kb.
			newlySeen.add(instance);
			addToSceneModel(pure_node, newlySeen);	
			return instance;			
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
		if(!story.isSeenNode(pure_name)){
			ArrayList<Node> seenRelation = new ArrayList<Node>();
			seenRelation.add(relationInstance.relationType);
			seenRelation.add(relationInstance);
			addToSceneModel(relationInstance.relationType, seenRelation);		
		}
		else{// we have seen this relation before
			ArrayList<Node> seenRelation = story.getSeenNode(pure_name);
			
			if(seenRelation == null){
				MyError.error(pure_name + " key exists in scene_nodes_dic but no relation exists! probably the map is corrupted!");
				seenRelation = new ArrayList<Node>();				
				seenRelation.add(relationInstance.relationType);
			}
			seenRelation.add(relationInstance);	
		}
	}
	
	/**
	 * findRelation searches the scene_nodes_dic to find the previous seen plausibleStatement with name "relation_name", if any.
	 * if found returns its pure version, means first element of its list, 
	 * if scene_nodes_dic dosen't have such a relation_name as key, it return null.
	 * 
	 * @param relation_name the name of PlausibleStatement to be search
	 * @param kb 
	 * @return the PlausibleStatement object previously seen, it is cloned PlausibleStatement.
	 */
	public PlausibleStatement findRelationInstance(String relation_name){
		if(relation_name == null || relation_name.equals("-"))
			return null;		
		try{
			if(story.isSeenNode(relation_name)){
				ArrayList<Node> seenRelations = story.getSeenNode(relation_name);
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
		
	 	ArrayList<Node> instances = story.getSeenNode(originalName);
	 	
	 	int index = 0;
	 	if(instances == null || instances.size() == 0)
	 		index = 1;
	 	else
	 		index = instances.size();
	 	
	 	String instanceName = originalName;
	 	
		instanceName = instanceName + " ("+ index + ")";
		
		//adding this instance concept to the knowledge base.
		Node instanceNode = _kb.addConcept(instanceName, false, SourceType.TTS);
		
		//---------- finding the synset Node of original Node ---------------------------
		ArrayList<PlausibleAnswer> answers = originalNode.findTargetNodes(KnowledgeBase.HPR_SYN);
		
		Node SynSetNode = null;
		for (PlausibleAnswer answer: answers)
			if(answer.answer != null){
				SynSetNode = answer.answer;
				break;
			}		
		//option a: instaceNode ISA originalNode --> dose not work !!!!!!
		//_kb.addRelation(instanceNode, originalNode, KnowledgeBase.HPR_ISA, SourceType.TTS);
		
		//option b: instaceNode ISA synSetNode --> works :)
		if(SynSetNode != null){
			print("option b: " + instanceNode + " ISA " + SynSetNode);
			_kb.addRelation(instanceNode, SynSetNode, KnowledgeBase.HPR_ISA, SourceType.TTS);
		}
		//option c: instaceNode SIM originalNode  --> works :)
		else{
			print("option c: " + instanceNode + " SIM " + originalNode + " ------ NOTE ---- NOTE ---- NOTE ---- NOTE ---- NOTE ----");		
			_kb.addRelation(instanceNode, originalNode, KnowledgeBase.HPR_SIM, SourceType.TTS);
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
			if(index2 != -1)
				pure_name =  pure_name.substring(index2);
			return pure_name;
		}
		return node.getName();
	}
	
	
	/**
	 * this method adds pure_node and newlySeen to  scene_nodes_dic through addToScene_nodes_dic() method and then
	 * adds pure_node and its ScenePart through addToScene_Parts() method.
	 * this is done in order to preventing re-querying the kb to detect ScenePart of every instance of a original node in this sceneModel.
	 * 
	 * @param pure_node
	 * @param newlySeen
	 * @param scenePart
	 */
	private void addToSceneModel(Node pure_node, ArrayList<Node> newlySeen){
		String pure_name = pure_node.getName();		
		story.addTo_story_nodes(pure_name, newlySeen);
		
		ScenePart sp = getScenePart(pure_node, pure_node.getPos());		
		story.addTo_story_sceneParts(pure_name, sp);
	}

	public Node getPureNode(Node instance) {
		if(instance == null)
			return null;
		
		String pure_name = makePureName(instance);
		
		if(story.isSeenNode(pure_name)){
			ArrayList<Node> allInstances = story.getSeenNode(pure_name);
			if(allInstances != null && allInstances.size()>0)
				return allInstances.get(0);
			MyError.error(pure_name + " key exists in scene_nodes_dic but no Node exists! probably the map is corrupted!");			
		}
		else
			MyError.error("no such a \"" + pure_name + "\" exists in the scene_nodes_dic!");
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
		pq.referent = _kb.addConcept("نفر§n-13075");
		
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
		pq.referent = _kb.addConcept("جانور§n-12239");
		
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
	 * @param node
	 * @return
	 */
	private boolean isLocation(Node node){		
		if(node == null)
			return false;
		
		print(node + "~~~~~~~~~~~~~~~~in isLocation ~~~~~~~~~~~~~~~~~");
		
		PlausibleQuestion pq = new PlausibleQuestion();
		pq.descriptor = KnowledgeBase.HPR_ISA;
		pq.argument = node;			
		pq.referent = _kb.addConcept("راه§n-12894");
		
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
		pq.referent = _kb.addConcept("جا§n-12733");
		
		answers = writeAnswersTo(pq.descriptor, node, pq.referent);
		//ArrayList<PlausibleAnswer> answers = _re.answerQuestion(pq);
		for(PlausibleAnswer ans:answers ){
			print("answer: " + ans);
			if(ans.answer == KnowledgeBase.HPR_YES){
				print(node.getName() + " isLocation جا \n");
				return true;				
			}
		}		
		
		print(node + " is NOT Loation \n");
		return false;
	}
	
	private boolean isTime(Node pure_node) {
		// TODO Auto-generated method stub
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
		print("\tTime: " + _re.reasoningTime / 1000);
		print("\tThroughput: " + (_re.totalCalls / _re.reasoningTime) * 1000 + " inference/s");
		return answers;
		
	}
	
	
	/**
	 * TODO: It must be improved: recognizing that which scenePart has the node: a Role (human) or DynamicObject or StaticObject?!
	 * this method checks:
	 * <ul> if node POS is NOUN:
	 * 		<li> if node is a child of "نفر§n-13075" returns ROLE </li>
	 * 		<li> if node is a child of "جانور§n-12239" returns DYNAMIC_OBJ </li>
	 * 		<li> if node is a child of "راه§n-12894" or "جا§n-12733" returns LOCATION </li>
	 * 		<li> otherwise returns STATIC_OBJ </li>  			
	 * </ul> 
	 * <ul> if node POS is ADVERB: 		
	 *  </ul>
	 *  <ul> if node POS is VERB: 		
	 *  </ul>  
	 *  <ul> we don't make a ScenePart for an adjective. 		
	 *  </ul> 
	 * 
	 * @param pure_node the pure node fetched from kb.
	 * @param pos the part of speech this node has.
	 * @return the ScenePart of pure_node, including ScenePart.UNKNOWN.
	 */
	private ScenePart getScenePart(Node pure_node, POS pos){
		if(pure_node == null)
			return ScenePart.UNKNOWN;
		
		if(pos == POS.NOUN){
			
			//TODO: I must remove these lines!-------			
			if(pure_node.getName().equals("پسر#n2"))
				return ScenePart.ROLE;
			if(pure_node.getName().equals("یک#n1"))
				return ScenePart.SCENE_OBJECT;
			if(pure_node.getName().equals("راه#n9"))
				return ScenePart.LOCATION;
			if(pure_node.getName().equals("سمت#n4"))
				return ScenePart.LOCATION;
			if(pure_node.getName().equals("خانه#n10"))
				return ScenePart.SCENE_OBJECT;
			
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
		if(pos == POS.VERB){
			return ScenePart.ACTION;
		}
		
		if(pos == POS.ADVERB)
			return null;
		
		return ScenePart.UNKNOWN;
	}
	
	

	/**
	 * this methods searches the internal structure of this SceneModel (scene_parts) to find the ScenePart mapped to this node.
	 *  
	 * @param node the node which is ScenePart is to be found. it may be pure or instance node!
	 * @return 
	 */
	public ScenePart whichScenePart(Node node){
		print(node + "~~~~~~~~~~~~~~~ in whichScenePart ~~~~~~~~~~~~");
		if(node == null)
			return ScenePart.UNKNOWN;
		
		String pure_name = makePureName(node);
		ScenePart sp = null;
		
		//if it is seen before!
		if(story.isSeenScenePart(pure_name))			
			sp = story.getSeenScenePart(pure_name);		
		//if it isn't seen before!
		else{
			Node pure_node = getPureNode(node);	
			POS pos = node.getPos();
		
			sp = getScenePart(pure_node, pos);
			
			story.addTo_story_sceneParts(pure_name, sp);			
		}

		print(node + " pos is " + sp + "\n");			
		return sp;
	}




	@Override
	public String toString() {
		String st = "SceneModel [" + "\nroles= "; 
		for (Role r : this.roles)
			st += "\n" + r;
		
		st+= "\ndynamic_objs= ";
		for(DynamicObject dOb:this.dynamic_objs)
			st += "\n" + dOb;
		
		st += "\nstatic_objs= ";
		for(StaticObject stOb:this.static_objs)
			st += "\n" + stOb;
		
		st += "\nlocation= " + location + 
		"\ntime= " + time +
		"\nscene_goals= " + scene_goals + 
		"\nscene_emotions= " + scene_emotions + "]\n";
		
		return st;
	}




	public Role getRole(Node role_node) {
		if(role_node == null)
			return null;
		
		for(Role role:this.roles)
			if(role._node == role_node)
				return role;
		
		MyError.error("this SceneModel has no such a " + role_node + " Role.");
		return null;
	}

	public boolean hasRole(Node role_node) {
		if(role_node == null)
			return false;
		
		for(Role role:this.roles)
			if(role._node == role_node)
				return true;
		return false;
	}
	
	
	public DynamicObject getDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return null;
		
		for(DynamicObject dynObj: this.dynamic_objs)
			if(dynObj._node == dynamin_object_node)
				return dynObj;
		
		MyError.error("this SceneModel has no such a " + dynamin_object_node + " DynamicObject.");
		return null;
	}
	
	public boolean hasDynamic_object(Node dynamin_object_node) {
		if(dynamin_object_node == null)
			return false;
		
		for(DynamicObject dynObj: this.dynamic_objs)
			if(dynObj._node == dynamin_object_node)
				return true;
		return false;
	}

	public StaticObject getStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return null;
		
		for(StaticObject staObj: this.static_objs)
			if(staObj._node == static_object_node)
				return staObj;
		
		MyError.error("this SceneModel has no such a " + static_object_node + " StaticObject.");
		return null;
	}

	public boolean hasStatic_object(Node static_object_node) {
		if(static_object_node == null)
			return false;
		
		for(StaticObject staObj: this.static_objs)
			if(staObj._node == static_object_node)
				return true;
		return false;
	}




	}
			
		
	