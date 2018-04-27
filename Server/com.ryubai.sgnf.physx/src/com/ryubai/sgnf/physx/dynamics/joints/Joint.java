package com.ryubai.sgnf.physx.dynamics.joints;

import com.ryubai.sgnf.physx.dynamics.actors.Actor;

/**
 * Abstract base class for the different types of joints. 
All joints are used to connect two dynamic actors, or an actor and the environment.

A NULL actor represents the environment. Whenever the below comments mention two actors, one of them may always be the environment (NULL).


 * @author MikL
 *
 */
public  class Joint {
	Actor actor1,actor2;
	JointDesc jointDesc;
	
	public Joint(JointDesc desc) {
		super();
		this.jointDesc = desc;
		// TODO Auto-generated constructor stub
	}
	public Actor getActor1() {
		return actor1;
	}
	public Actor getActor2() {
		return actor2;
	}
	public JointDesc getJointDesc() {
		return jointDesc;
	}
		
	
	
	public int getId() {
		return getJointDesc().getId();
	}
}
