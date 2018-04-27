package com.ryubai.sgnf.physx.dynamics.actors;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.WorldPhysX;

public class BoxActor extends Actor {

	public BoxActor( ActorParameters parameters,WorldPhysX world,float x,float y,float z) {
		super(world);
		Functions.actorCreateAsBoxShape(world.getId(),id,parameters.isDynamic(),parameters.isUseCCD(),parameters.getDensity(), x, y, z);
	}

}
