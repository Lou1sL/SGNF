package com.ryubai.sgnf.physx.dynamics.joints;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.WorldPhysX;
import com.ryubai.sgnf.physx.dynamics.actors.Actor;

public class FixedJoint extends Joint{

	public FixedJoint(WorldPhysX world,FixedJointDesc desc) {
		super(desc);
		
		Functions.jointFixedCreate(world.getId(),getId(), getId(getJointDesc().actor1), getId(getJointDesc().actor2));
		
	}
	public int getId(Actor actor) {
		if(actor==null) return -1;
		return actor.getId();
	}
}
