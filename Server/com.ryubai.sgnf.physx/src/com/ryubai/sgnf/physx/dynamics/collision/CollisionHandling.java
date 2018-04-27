package com.ryubai.sgnf.physx.dynamics.collision;

import java.util.Collection;

import javax.vecmath.Vector3f;

import com.ryubai.sgnf.physx.WorldPhysX;
import com.ryubai.sgnf.physx.dynamics.actors.Actor;

public class CollisionHandling {
	
	public static void onContactNotify(int actorid1,int actorid2,int flags,float sumFrictionForce_x,float sumFrictionForce_y,float sumFrictionForce_z,float sumNormalForce_x,float sumNormalForce_y,float sumNormalForce_z) {
		//first create and fill contactpair
		ContactPair contactPair = new ContactPair();
		
		
		Actor actor1 = WorldPhysX.getActorById(actorid1);
		Actor actor2 = WorldPhysX.getActorById(actorid2);
		contactPair.setActor1(actor1);
		contactPair.setActor2(actor2);
		
		
		//get world
		WorldPhysX world = actor1.getWorld();
		//System.out.println(actor1+" "+actor2+" "+world);
		//if(2+2==4) return;
		contactPair.setSumFrictionForce(new Vector3f(sumFrictionForce_x,sumFrictionForce_y,sumFrictionForce_z));
		contactPair.setSumNormalforce(new Vector3f(sumNormalForce_x,sumNormalForce_y,sumNormalForce_z));
		contactPair.setFlags(flags);
		//call listeners
		Collection<CollisionListener> listeners = world.getListeners();
		for (CollisionListener listener : listeners) {
			listener.onContactNotify(contactPair);
		}
	}
	
	
}
