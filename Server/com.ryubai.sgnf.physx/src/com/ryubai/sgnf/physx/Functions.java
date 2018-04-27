package com.ryubai.sgnf.physx;

import javax.vecmath.Matrix3f;
import javax.vecmath.Vector3f;

public class Functions {
	/**
	Contact pair flags.
	 */
	public enum NxContactPairFlag
		{
		/**
		 * Disable contact generation for this pair
		 */
		NX_IGNORE_PAIR(1<<0),	 
		/**
		 * Pair callback will be called when the pair starts to be in contact
		 */
		NX_NOTIFY_ON_START_TOUCH		 (1<<1),	 
		/**
		 * Pair callback will be called when the pair stops to be in contact
		 */
		NX_NOTIFY_ON_END_TOUCH			 (1<<2),	 
		/**
		 * Pair callback will keep getting called while the pair is in contact
		 */
		NX_NOTIFY_ON_TOUCH				(1<<3),	 
		/**
		 *Not yet implemented] pair callback will be called when it may be appropriate for the pair to play an impact sound 
		 */
		NX_NOTIFY_ON_IMPACT				(1<<4),	
		/**
		 *Not yet implemented] pair callback will be called when the pair is in contact and rolling. 
		 */
		NX_NOTIFY_ON_ROLL				(1<<5),	
		/**
		 * Not yet impleumented] pair callback will be called when the pair is in contact and sliding (and not rolling).
		 */
		NX_NOTIFY_ON_SLIDE				(1<<6),	 
		/**
		 * The friction force and normal force will be available in the contact report
		 */
		NX_NOTIFY_FORCES				(1<<7),	

		/**
		 * Generate a callback for all associated contact constraints, making it possible to edit the constraint. This flag is not included in NX_NOTIFY_ALL for performance reasons. \see NxUserContactModify
		 */
		NX_NOTIFY_CONTACT_MODIFICATION	(1<<16),	 

		/**
		 * 
		 */
		NX_NOTIFY_ALL					(NX_NOTIFY_ON_START_TOUCH.getValue()|NX_NOTIFY_ON_END_TOUCH.getValue()|NX_NOTIFY_ON_TOUCH.getValue()|NX_NOTIFY_ON_IMPACT.getValue()|NX_NOTIFY_ON_ROLL.getValue()|NX_NOTIFY_ON_SLIDE.getValue()|NX_NOTIFY_FORCES.getValue())
		;
		int value;
		public int getValue() {
			return value;
		}
		NxContactPairFlag(int value) {
			this.value = value;
		}
		}
	public static void onContactNotify() {
		System.out.println("SKODNOTIFY");
		
	};
		
	/*
	 * Enumerations
	 * 
	 * 
	 */
	public enum NxRevoluteJointFlag
	{
	/**
	true if limits is enabled

	@see NxRevoluteJointDesc.limit
	*/
	NX_RJF_LIMIT_ENABLED (1 << 0),
	
	/**
	true if the motor is enabled

	@see NxRevoluteJoint.motor
	*/
	NX_RJF_MOTOR_ENABLED (1 << 1),
	
	/**
	true if the spring is enabled. The spring will only take effect if the motor is disabled.

	@see NxRevoluteJoint.spring
	*/
	NX_RJF_SPRING_ENABLED (1 << 2);
	int value;
	public int getValue() {
		return value;
	}
	NxRevoluteJointFlag(int value) {
		this.value = value;

	}
	};
	public enum NxJointProjectionMode
	{
	NX_JPM_NONE(0),				//!< don't project this joint
	NX_JPM_POINT_MINDIST(1),		//!< linear and angular minimum distance projection
	NX_JPM_LINEAR_MINDIST(2);		//!< linear only minimum distance projection
	//there may be more modes later
	int value;
	public int getValue() {
		return value;
	}
	NxJointProjectionMode(int value) {
		this.value = value;

	}
	};
	
	/**
	\brief Used to specify a particular drive method. i.e. Having a position based goal or a velocity based goal.
	*/
	public enum NxD6JointDriveType 
		{
		/**
		\brief Used to set a position goal when driving.

		Note: the appropriate target positions/orientations should be set.

		@see NxD6JointDesc.xDrive NxD6Joint.swingDrive NxD6JointDesc.drivePosition
		*/
		NX_D6JOINT_DRIVE_POSITION(1<<0),

		/**
		\brief Used to set a velocity goal when driving.

		Note: the appropriate target velocities should beset.

		@see NxD6JointDesc.xDrive NxD6Joint.swingDrive NxD6JointDesc.driveLinearVelocity
		*/
		NX_D6JOINT_DRIVE_VELOCITY(1<<1);
		int value;
		public int getValue() {
			return value;
		}
		NxD6JointDriveType(int value) {
			this.value = value;

		}
		};
	
	public enum NxD6JointMotion {
		
		NX_D6JOINT_MOTION_LOCKED(0), // !< The DOF is locked, it does not allow
									// relative motion.
		NX_D6JOINT_MOTION_LIMITED(1), // !< The DOF is limited, it only allows
									// motion within a specific range.
		NX_D6JOINT_MOTION_FREE(2);
		// !< The DOF is free and has its full range of motions.
		int value;
		public int getValue() {
			return value;
		}
		NxD6JointMotion (int value) {
			this.value = value;

		}

	}

	

	public enum NxD6JointFlag {
		/**
		 * \brief Drive along the shortest spherical arc.
		 * 
		 * @see NxD6JointDesc.slerpDrive
		 */
		NX_D6JOINT_SLERP_DRIVE(1 << 0),
		/**
		 * \brief Apply gearing to the angular motion, e.g. body 2s angular
		 * motion is twice body 1s etc.
		 * 
		 * @see NxD6JointDesc.gearRatio
		 */
		NX_D6JOINT_GEAR_ENABLED(1 << 1);

		private final int flag;
		public int getValue() {
			return flag;
		}
		NxD6JointFlag(int flag) {
			this.flag = flag;

		}

	};

	

	public static float[] toArray(Matrix3f m) {
		float a[] = new float[9];

		a[0] = m.m00;
		a[1] = m.m01;
		a[2] = m.m02;
		a[3] = m.m10;
		a[4] = m.m11;
		a[5] = m.m12;
		a[6] = m.m20;
		a[7] = m.m21;
		a[8] = m.m22;
		return a;
	}

	// test function
	public static native void testRunner();

	public static float[] toArray(Vector3f v) {
		if(v==null) return null;
		float a[] = new float[3];
		a[0] = v.x;
		a[1] = v.y;
		a[2] = v.z;
		return a;
	}

	public static native void worldSetStepTiming(int sid,float timing);
	
	public static native void  worldEnableUserContactReport(int sid);
	public static native void worldSetContactPairFlags(int sid,int actorid1,int actorid2,int flags);
	public static native void worldCreate(int sid);
	public static native void actorSetSolverIterations(int id,int iter);
	// public static native void worldCreateGroundPlane();
	public static void actorSetRotation(int id, Matrix3f m) {
		actorSetRotation(id, toArray(m));
	}

	public static native void actorSetRotation(int id, float[] a);

	public static native float[] actorGetRotation(int id);
	
	
	//
	public static native void worldStep(int sid,float step);

	//
	public static native void worldSetGravity(int sid,float x, float y, float z);
	
	public static native void worldUseCCD(int sid);

	public static native float worldGetGravityX(int sid);

	public static native float worldGetGravityY(int sid);

	public static native float worldGetGravityZ(int sid);

	public static native void actorAddForce(int id, float force_x,
			float force_y, float force_z);

	public static native void actorCreateAsBoxShape(int sid,int id, boolean isDynamic,
			boolean useCCD, float density,float x, float y, float z);
	public static native void actorCreateAsNoShape(int sid,int id, boolean isDynamic,
			boolean useCCD);
	public static native void actorCreateAsSphereShape(int sid,int id,
			boolean isDynamic, float density, float radius);

	public static native void actorCreateAsGroundPlane(int sid,int id,float density);

	public static native void actorAddBoxShape(int id, float x, float y, float z);

	public static native void actorAddSphereShape(int id, float radius);

	public static native void actorSetMass(int id, double mass);

	public static native double actorGetMass(int id);

	public static native void actorSetPosition(int id, float x, float y, float z);

	public static native void actorSetLinearVelocity(int id, float vel_x,
			float vel_y, float vel_z);

	public static native void actorSetAngularVelocity(int id, float vel_x,
			float vel_y, float vel_z);

	public static native void actorSetLinearMomentum(int id, float m_x,
			float m_y, float m_z);

	public static native void actorSetAngularMomentum(int id, float m_x,
			float m_y, float m_z);

	public static native float[] actorGetPosition(int id);

	public static native float[] actorGetLinearVelocity(int id);

	public static native float[] actorGetAngularVelocity(int id);

	public static native float[] actorGetAngularMomentum(int id);

	public static native float[] actorGetLinearMomentum(int id);

	public static native float actorGetLinearDamping(int id);

	public static native float actorGetAngularDamping(int id);

	public static native double actorComputeKineticEnergy(int id);

	public static native void actorSetLinearDamping(int id, float damping);

	public static native void actorSetAngularDamping(int id, float damping);

	public static native void materialCreateMaterial(int sid,int id);

	public static native void materialSetStaticFriction(int id, float friction);

	public static native void materialSetDynamicFriction(int id, float friction);
	public static native void materialRestitution(int id, float restitution);
	public static native void actorSetMaterial(int actorId, int materialId);

	/*
	 * Joints Desc
	 */
	public static native void jointDescSetActors(int jointId, int actorId1,
			int actorId2);
	public static native void jointDescSetGlobalAxis(int jointId, float x,float y,float z); 

	public static native void jointDescSetLocalAxis(int jointId,int index,
			float axis[]);
	public static native void jointDescSetLocalNormal(int jointId,int index,
			float normal[]);

	public static native void jointDescSetLocalAnchor(int jointId, float f1[],
			float f2[]);

	public static native void jointDescSetGlobalAnchor(int jointId,
			float x,float y,float z);

	/*
	 * D6 joint desc
	 */
	/*
	 * Create a D6JointDesc
	 */
	public static native void jointDescD6Create(int id);

	
	public static native void jointD6DescSetLinearDegreesOfFreedom(int jointId,
			int  motion_x, int  motion_y, int motion_z);

	public static native void jointD6DescSetAngularDegreesOfFreedom(
			int jointId, int motion_x, int  motion_y, int motion_z);

	public static native void jointD6DescSetLinearLimit(int jointId,
			float value, float restitution, float spring, float damping);

	public static native void jointD6DescSetSwing1Limit(int jointId,
			float value, float restitution, float spring, float damping);

	public static native void jointD6DescSetSwing2Limit(int jointId,
			float value, float restitution, float spring, float damping);

	public static native void jointD6DescSetTwistLimit(int jointId,
			float low_value, float low_restitution, float low_spring,
			float low_damping, float high_value, float high_restitution,
			float high_spring, float high_damping);

	public static native void jointD6DescSetXDrive(int jointId, int driveType,
			float damping, float forceLimit, float spring);

	public static native void jointD6DescSetYDrive(int jointId, int driveType,
			float damping, float forceLimit, float spring);

	public static native void jointD6DescSetZDrive(int jointId, int driveType,
			float damping, float forceLimit, float spring);

	public static native void  jointFixedCreate(int sid,int jointId,int actorid1,int actorid2);
		
	public static native void jointD6DescSetSwingDrive(int jointId,
			int driveType, float damping, float forceLimit, float spring);

	public static native void jointD6DescSetTwistDrive(int jointId,
			int driveType, float damping, float forceLimit, float spring);

	public static native void jointD6DescSetSlerpDrive(int jointId,
			int driveType, float damping, float forceLimit, float spring);

	public static native void jointD6SetDrivePosition(int jointId, float x,
			float y, float z);

	public static native void jointD6SetDriveOrientation(int jointId,
			float x, float y, float z, float w);

	public static native void jointD6SetDriveLinearVelocity(int jointId,
			float x, float y, float z);

	public static native void jointD6SetDriveAngularVelocity(int jointId,
			float x, float y, float z);

	public static native void jointD6DescSetProjectionMode(int jointId, int mode);

	public static native void jointD6DescSetProjectionDistance(int jointId,
			float distance);

	public static native void jointD6DescSetProjectionAngle(int jointId,
			float angle);

	public static native void jointD6DescSetGearRatio(int jointId,
			float gearRatio);

	public static native void jointD6DescSetFlags(int jointId, float flags);

	public static native void jointD6DescSetToDefault(int jointId);

	/*
	 * Joint D6 functions
	 */
	public static native void jointD6Create(int jointId);
	public static native void testjointCreate(int jointId,int a1,int a2);
	/*
	 * Joint revolute
	 */
	public static native void jointRevoluteCreate(int sid,int jointId);
	public static native void jointRevoluteDescCreate(int jointId);
	public static native void jointRevoluteSetLimit(int jointId,float hardness1,float restitution1,float value1,float hardness2,float restitution2,float value2);
	public static native void jointRevoluteSetMotor(int jointId,float velTarget,float maxForce,boolean freeSpin);
	public static native void jointRevoluteSetSpring(int jointId,float spring,float damper,float targetValue);
	public static native void jointRevoluteSetFlags(int jointId,int flags);
	public static native float jointRevoluteGetAngle(int jointId);
	public static native float  jointRevoluteGetVelocity(int jointId);
	static {
		System.loadLibrary("physx4j");
	}
}
