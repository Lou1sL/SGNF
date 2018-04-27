package com.ryubai.sgnf.physx.demos.simple;

import com.ryubai.sgnf.physx.WorldPhysX;
import com.ryubai.sgnf.physx.dynamics.actors.Actor;
import com.ryubai.sgnf.physx.dynamics.actors.ActorParameters;
import com.ryubai.sgnf.physx.dynamics.actors.BoxActor;
import com.ryubai.sgnf.physx.dynamics.actors.GroundPlaneActor;
import com.ryubai.sgnf.physx.dynamics.actors.Material;
import com.ryubai.sgnf.physx.dynamics.actors.SphereActor;

public class SimpleActorDemo {
	public static void main(String args[]) throws Exception {
		//Create a new world
		WorldPhysX world =  new WorldPhysX();
		//set gravity
		world.setGravity(0, -9, 0);
		//create a groundplane
		new GroundPlaneActor(world,1f);
		/*
		 * create actors (a box and a sphere)
		 */
		ActorParameters params = new ActorParameters();
		params.setDynamic(true);
		params.setDensity(10f);
		
		Actor box = new BoxActor(params,world,0.5f,0.5f,0.5f);
		Actor sphere = new SphereActor(params,world,0.5f);
		//set mass of actors
		box.setMass(1);
		sphere.setMass(2);
		//set actor position
		box.setPosition(100, 1000, 100);
		sphere.setPosition(100, 100, 10);
		//assign material to box and sphere
		Material material1 = new Material(1);
		material1.setDynamicFriction(10);
		box.setMaterial(material1);
		sphere.setMaterial(material1);
		//iterate the world
		for(int i=0;i<90000;i++) {
			//add force just for fun
			sphere.addForce(100, 0, 0);
			Thread.sleep(10);
			material1.setDynamicFriction((float)(Math.random()*10f));
			//step the world
			world.step(0.01f);//set stepsize here
			//print results
			System.out.println("Position of objects : "+box.getPosition()+";"+sphere.getPosition());
			System.out.println("Velocity ="+box.getLinearVelocity()+" "+sphere.getLinearVelocity());
			System.out.println("Force ="+box.computeKineticEnergy()+" "+sphere.computeKineticEnergy());
			//System.out.println("Rotation:\n "+box.getRotation()+";\n"+sphere.getRotation());
		}
	}
}
