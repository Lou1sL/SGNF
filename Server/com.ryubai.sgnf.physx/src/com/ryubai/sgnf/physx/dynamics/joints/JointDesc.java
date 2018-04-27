package com.ryubai.sgnf.physx.dynamics.joints;

import javax.vecmath.Vector3f;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.dynamics.actors.Actor;

/**
 * Descriptor class for the {@link Joint} class. Joint descriptors for all the
 * different joint types are derived from this class.
 * 
 * 
 * @author MikL
 * 
 */
public abstract class JointDesc {
	Actor actor1, actor2;

	protected static int id_counter;

	int id;

	/**
	 * Gets the ID of this joint
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 *Creates a new {@link D6JointDesc} 
	 */
	public JointDesc() {
		super();
		id = id_counter++;
		// TODO Auto-generated constructor stub
	}

	/**
	 * The two actors connected by the joint. The actors must be in the same
	 * scene as this joint.
	 * 
	 * At least one of the two pointers must be a dynamic actor.
	 * 
	 * One of the two may be NULL to indicate the world frame. Neither may be a
	 * static actor!
	 * 
	 * Default: NULL
	 * 
	 * 
	 * @param actor1
	 * @param actor2
	 */
	public void setActors(Actor actor1, Actor actor2) {
		this.actor1 = actor1;
		this.actor2 = actor2;
		Functions.jointDescSetActors(getId(), actor1.getId(), actor2.getId());
	}
	

	/**
	 * X axis of joint space, in actor[i]'s space, orthogonal to localAxis[i].
	 * 
	 * localAxis and localNormal should be unit length and at right angles to
	 * each other, i.e. dot(localNormal[0],localAxis[0])==0 and
	 * dot(localNormal[1],localAxis[1])==0.
	 * 
	 * Range: direction vector Default: [0] 1.0f,0.0f,0.0f Default: [1]
	 * 1.0f,0.0f,0.0f
	 * 
	 * Platform:
	 * 
	 * PC SW: Yes PPU : Yes PS3 : Yes XB360: Yes
	 * 
	 * @param normal1
	 * @param normal2
	 */
	public void setLocalNormal(int index,Vector3f normal) {
		Functions.jointDescSetLocalNormal(id, index,Functions.toArray(normal));
	}
	public void setLocalAxis(int index,Vector3f axis) {
		Functions.jointDescSetLocalAxis(id, index,Functions.toArray(axis));
	}
	/**
	 * Attachment point of joint in actor[i]'s space.
	 * 
	 * Range: position vector Default: [0] Zero Default: [1] Zero
	 * 
	 * Platform:
	 * 
	 * PC SW: Yes PPU : Yes PS3 : Yes XB360: Yes
	 * 
	 * @param anchor1
	 * @param anchor2
	 */
	public void setLocalAnchor(Vector3f anchor1, Vector3f anchor2) {
		Functions.jointDescSetLocalAnchor(id, Functions.toArray(anchor1),
				Functions.toArray(anchor2));

	}
	public void setGlobalAnchor(Vector3f anchor) {
		Functions.jointDescSetGlobalAnchor(id, anchor.x,anchor.y,anchor.z);
	}
	/**
	 * Set the localAnchor[] members using a world space point.
	 * 
	 * sets the members localAnchor[0,1] by transforming the passed world space
	 * vector into actor1 resp. actor2's local space. The actor pointers must
	 * already be set!
	 * 
	 * 
	 * Parameters: [in] wsAnchor Global frame anchor point. Range: position
	 * vector
	 * 
	 * 
	 * 
	 */
	public void setGlobalAnchor(float x,float y,float z) {
		Functions.jointDescSetGlobalAnchor(id, x,y,z);
	}
	
	/**
	 * Set the local axis/normal using a world space axis.
	 * 
	 * sets the members localAxis[0,1] by transforming the passed world space
	 * vector into actor1 resp. actor2's local space, and finding arbitrary
	 * orthogonals for localNormal[0,1]. The actor pointers must already be set!
	 * 
	 * 
	 * @param axis
	 */
	public void setGlobalAxis(float x,float y,float z) {
		Functions.jointDescSetGlobalAxis(id, x,y,z);
	}
	public void setGlobalAxis(Vector3f v) {
		Functions.jointDescSetGlobalAxis(id, v.x,v.y,v.z);
	}
}
