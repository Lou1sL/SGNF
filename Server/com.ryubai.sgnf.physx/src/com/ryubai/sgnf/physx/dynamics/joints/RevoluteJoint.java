package com.ryubai.sgnf.physx.dynamics.joints;

import com.ryubai.sgnf.physx.Functions;
import com.ryubai.sgnf.physx.WorldPhysX;

/**
 * A joint which behaves in a similar way to a hinge or axel. 
	A hinge joint removes all but a single rotational degree of freedom from two objects. The axis along which the two bodies may rotate is specified with a point and a direction vector.
 * @author MikL
 *
 */
public class RevoluteJoint extends Joint {

	public RevoluteJoint(RevoluteJointDesc desc,WorldPhysX world) {
		super(desc);
		Functions.jointRevoluteCreate(world.getId(),jointDesc.getId());

		// TODO Auto-generated constructor stub
	}

	/**
	 * Retrieves the current revolute joint angle.
	 * 
	 * The relative orientation of the bodies is stored when the joint is
	 * created, or when setAxis() or setAnchor() is called. This initial
	 * orientation returns an angle of zero, and joint angles are measured
	 * relative to this pose. The angle is in the range [-Pi, Pi], with positive
	 * angles CCW around the axis, measured from body2 to body1.
	 * 
	 * Unit: Radians Range: [-PI,PI]
	 * 
	 * 
	 * Returns: The current hinge angle. Platform: PC SW: Yes PPU : Yes PS3 :
	 * Yes XB360: Yes
	 * 
	 * @return
	 */
	public float getAngle() {
		return Functions.jointRevoluteGetAngle(jointDesc.id);
	}

	/**
	 * Retrieves the revolute joint angle's rate of change (angular velocity).
	 * 
	 * It is the angular velocity of body1 minus body2 projected along the axis.
	 * 
	 * 
	 * Returns: The hinge velocity. Platform: PC SW: Yes PPU : Yes PS3 : Yes
	 * XB360: Yes
	 * 
	 * @return
	 */
	public float getVelocity() {
		return Functions.jointRevoluteGetVelocity(jointDesc.id);
	}

	/**
	 * Gets the joint description
	 * 
	 * @return
	 */
	public RevoluteJointDesc getRevoluteJointDesc() {
		// TODO Auto-generated method stub
		return (RevoluteJointDesc) super.getJointDesc();
	}
	public void setFlags(int flags) {
		Functions.jointRevoluteSetFlags(getRevoluteJointDesc().getId(), flags);
	}
	/**
	 *   Optional limits for the angular motion of the joint. 
 
	 * @param hardness1
	 * @param restitution1
	 * @param value1
	 * @param hardness2
	 * @param restitution2
	 * @param value2
	 */
	public void setLimit(float hardness1, float restitution1, float value1, float hardness2, float restitution2, float value2) {
		Functions.jointRevoluteSetLimit(getId(), hardness1, restitution1, value1, hardness2, restitution2, value2);
	}
	/**
	 * Optional motor. 
	 * @param velTarget
	 * @param maxForce
	 * @param freeSpin
	 */
	public void setMotor(float velTarget, float maxForce, boolean freeSpin) {
		Functions.jointRevoluteSetMotor(getId(), velTarget, maxForce, freeSpin);
	}
	/**
	 * 
	 * @param spring
	 * @param damper
	 * @param targetValue
	 */
	public void setSpring(float spring, float damper, float targetValue) {
		Functions.jointRevoluteSetSpring(getId(), spring, damper, targetValue);
	}
}
