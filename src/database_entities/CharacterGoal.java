package database_entities;

import java.util.Vector;

import pbmain.util.DBObject;
import sentencegenerator.component.DBLexicalObject;

//added implements DBObject
public class CharacterGoal implements DBObject {
	String CharacterGoalID;
	String Name,
		Action,
		Agens,
		Patiens = "";
	boolean isNegated;
	
	//others
	String target;
	String instrument;
	
	//DBObject
	DBLexicalObject actionDB;
	DBObject agensDB;
	DBObject patiensDB;
	DBObject targetDB;
	DBObject instrumentDB;
	boolean hideTarget = false;
	
	//for lexicalisation
	Vector<String> agensVector = null;
	Vector<String> patiensVector = null;
	Vector<String> actionVector = null;
	Vector<String> targetVector = null;
	Vector<String> instrumentVector = null;
	
	public CharacterGoal() {}
	
	public CharacterGoal(String characterGoalID, String name, String action,
			String agens, String patiens, boolean isNegated) {
		CharacterGoalID = characterGoalID;
		Name = name;
		Action = action;
		Agens = agens;
		Patiens = patiens;
		this.isNegated = isNegated;
	}
	
	public CharacterGoal(String characterGoalID, String name, DBLexicalObject action, DBObject agens, DBObject patiens,
			DBObject target, DBObject instrument) {
		CharacterGoalID = characterGoalID;
		Name = name;
		actionDB = action;
		agensDB = agens;
		patiensDB = patiens;
		targetDB = target;
		instrumentDB = instrument;
		this.isNegated = false;
	}

	public String getAction() {
		return Action;
	}
	public void setAction(String action) {
		Action = action;
	}
	public String getAgens() {
		return Agens;
	}
	public void setAgens(String agens) {
		Agens = agens;
	}
	public String getPatiens() {
		return Patiens;
	}
	public void setPatiens(String patiens) {
		Patiens = patiens;
	}
	public boolean isNegated() {
		return isNegated;
	}
	public void setNegated(boolean isNegated) {
		this.isNegated = isNegated;
	}

	/*
	 * Added
	 */
	
	public DBLexicalObject getActionDB() {
		return actionDB;
	}
	
	public void setActionDB(DBLexicalObject action) {
		this.actionDB = action;
	}
	
	public DBObject getAgensDB() {
		return agensDB;
	}
	
	public void setAgensDB(DBObject agens){
		this.agensDB = agens;
	}
	
	public DBObject getPatiensDB(){
		return patiensDB;
	}
	
	public void setPatiensDB(DBObject patiens){
		this.patiensDB = patiens;
	}
	
	public DBObject getTargetDB(){
		return targetDB;
	}
	
	public void setTargetDB(DBObject target){
		this.targetDB = target;
	}
	
	public DBObject getInstrumentDB(){
		return instrumentDB;
	}
	
	public void setInstrumentDB(DBObject instrument){
		this.instrumentDB = instrument;
	}
	
	public String getTarget(){
		return target;
	}
	
	public void setTarget(String target){
		this.target = target;
	}
	
	public String getInstrument(){
		return instrument;
	}
	
	public void setInstrument(String instrument){
		this.instrument = instrument;
	}
	
	public boolean isHideTarget(){
		return hideTarget;
	}
	
	public void setHideTarget(boolean hideTarget){
		this.hideTarget = hideTarget;
	}
	
	public Vector<String> getActionVector() {
		return actionVector;
	}

	public void setActionVector(Vector<String> actionVector) {
		this.actionVector = actionVector;
	}

	public Vector<String> getAgensVector() {
		return agensVector;
	}

	public void setAgensVector(Vector<String> agensVector) {
		this.agensVector = agensVector;
	}

	public Vector<String> getInstrumentVector() {
		return instrumentVector;
	}

	public void setInstrumentVector(Vector<String> instrumentVector) {
		this.instrumentVector = instrumentVector;
	}

	public Vector<String> getPatiensVector() {
		return patiensVector;
	}

	public void setPatiensVector(Vector<String> patiensVector) {
		this.patiensVector = patiensVector;
	}

	public Vector<String> getTargetVector() {
		return targetVector;
	}

	public void setTargetVector(Vector<String> targetVector) {
		this.targetVector = targetVector;
	}
	
	@Override
	public void setID(String characterGoalID) {
		// TODO Auto-generated method stub
		this.CharacterGoalID = characterGoalID;
	}

	@Override
	public String getID() {
		// TODO Auto-generated method stub
		return CharacterGoalID;
	}

	@Override
	public void setString(String name) {
		// TODO Auto-generated method stub
		this.Name = name;
	}

	@Override
	public String getString() {
		// TODO Auto-generated method stub
		return Name;
	}
	
	public CharacterGoal(String characterGoalID, DBLexicalObject action, DBObject agens, 
			DBObject patiens, DBObject target, DBObject instrument, String goalName) {
		this.CharacterGoalID = characterGoalID;
		this.actionDB = action;
		setAgensDB(agens);
		setPatiensDB(patiens);
		setTargetDB(target);
		setInstrumentDB(instrument);
		this.Name = goalName;
		setNegated(false);
	}
}
