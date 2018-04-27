package com.ryubai.sgnf.physx.dynamics.actors;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.WorldPhysX;

public class GroundPlaneActor extends Actor{

	public GroundPlaneActor(WorldPhysX world,float den) {
		super(world);
		Functions.actorCreateAsGroundPlane(world.getId(),id,den);
	}
	
}
