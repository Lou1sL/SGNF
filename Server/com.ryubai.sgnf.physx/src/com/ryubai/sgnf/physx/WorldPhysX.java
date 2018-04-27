package com.ryubai.sgnf.physx;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import com.ryubai.sgnf.physx.dynamics.actors.Actor;
import com.ryubai.sgnf.physx.dynamics.collision.CollisionListener;

public class WorldPhysX {
	
	static int sceneId;
	int id;
	double stepNumber;
	float stepSize;
	HashMap<Integer, Actor> actorsById = new HashMap<Integer, Actor>();
	HashMap<String, Actor> actorsByName = new HashMap<String, Actor>();
	static HashMap<Integer, Actor> allActors = new HashMap<Integer, Actor>();
	Collection <CollisionListener> listeners = new ArrayList<CollisionListener>();
	public void addListener(CollisionListener listener) {
		listeners.add(listener);
	}
	public void removeListener(CollisionListener listener) {
		listeners.remove(listener);
	}
	public static Actor getActorById(int id) {
		return allActors.get(id);
	}
	public Collection<Actor> getAllActors() {
		return actorsById.values();
	}
	
	
	public Collection<CollisionListener> getListeners() {
		return listeners;
	}
	public void setListeners(Collection<CollisionListener> listeners) {
		this.listeners = listeners;
	}
	public  void addActor(Actor actor) {
		actorsById.put(actor.getId(), actor);
		actorsByName.put(actor.getName(), actor);
		allActors.put(actor.getId(), actor);
	}
	public void setGravityY(float gravityY) {
		Functions.worldSetGravity(id, 0, gravityY, 0);
	} 
	public Actor getActor(int id) {
		return actorsById.get(id);
	}
	public Actor getActor(String name) {
		return actorsByName.get(name);
	}
	public WorldPhysX() {
		super();
		id = sceneId++;
		Functions.worldCreate(id);
		
		
	}	
	
	public  void setGravity(float x,float y,float z) {
		Functions.worldSetGravity(id, x, y, z);
	}
	public  void enableContiniousCollisonDetectionD() {
		Functions.worldUseCCD(id);
	}
	public void setTiming(float f) {
		
		Functions.worldSetStepTiming(id,f);
	}
	public void step() {
		step(stepSize);
		
	}
	public void step(float step) {
		Functions.worldStep(id,step);
		stepNumber++;
	}
	public float getGravityX() {
		return Functions.worldGetGravityX(id);
	}
	public void setEnableContactUserReport(boolean enable) {
		Functions.worldEnableUserContactReport(id);;
	}
	public void enableContactUserReport() {
		Functions.worldEnableUserContactReport(id);;
	}
	public void setContactPairFlags(Actor  a1,Actor a2,int flags) {
		Functions.worldSetContactPairFlags(id,a1.getId(), a2.getId(), flags);
	}
	public float getGravityY() {
		return Functions.worldGetGravityY(id);
	}
	public float getGravityZ() {
		return Functions.worldGetGravityZ(id);
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public float getStepSize() {
		return stepSize;
	}
	public void setStepSize(float stepSize) {
		this.stepSize = stepSize;
	}
	public double getStepNumber() {
		return stepNumber;
	}
	public void setStepNumber(double stepNumber) {
		this.stepNumber = stepNumber;
	}
}
