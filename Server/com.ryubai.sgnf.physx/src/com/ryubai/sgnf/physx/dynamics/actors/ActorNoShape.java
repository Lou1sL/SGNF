package com.ryubai.sgnf.physx.dynamics.actors;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.WorldPhysX;

public class ActorNoShape extends Actor{

	public ActorNoShape(ActorParameters parameters,WorldPhysX world,int sid) {
		super(world);
		Functions.actorCreateAsNoShape(sid,id, parameters.isDynamic(), parameters.isUseCCD());
	}
	
}
