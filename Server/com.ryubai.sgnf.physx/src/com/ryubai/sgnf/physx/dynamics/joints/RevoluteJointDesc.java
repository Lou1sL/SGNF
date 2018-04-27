package com.ryubai.sgnf.physx.dynamics.joints;

import com.ryubai.sgnf.physx.Functions;

public class RevoluteJointDesc extends JointDesc{

	public RevoluteJointDesc() {
		super();
		Functions.jointRevoluteDescCreate(id);
	}

	
	
}	
