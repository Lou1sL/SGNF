package com.ryubai.sgnf.physx.dynamics.joints;

import com.ryubai.sgnf.physx.Functions;

public class D6Joint extends Joint{
	
	public D6Joint(D6JointDesc desc) {
		super(desc);
		Functions.jointD6Create(desc.getId());
		
	}
	public D6JointDesc getJointD6Desc() {
		return (D6JointDesc) jointDesc;
	} 
	

	@Override
	public JointDesc getJointDesc() {
		// TODO Auto-generated method stub
		return jointDesc;
	}
	/**
	 * If the type of xDrive (yDrive,zDrive) is NX_D6JOINT_DRIVE_POSITION,
	 * drivePosition defines the goal position.
	 * 
	 * Range: position vector Default: Zero
	 * 
	 * 
	 * @param driveType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setDrivePosition(Functions.NxD6JointDriveType driveType,
			float x, float y, float z) {
		Functions.jointD6SetDrivePosition(getId(), x, y, z);
	}

	/**
	 * If the type of swingDrive or twistDrive is NX_D6JOINT_DRIVE_POSITION,
	 * driveOrientation defines the goal orientation.
	 * 
	 * Range: unit quaternion Default: Identity Quaternion
	 * 
	 * Platform:
	 * 
	 * PC SW: Yes PPU : Yes PS3 : Yes XB360: Yes
	 * 
	 * @param driveType
	 * @param x
	 * @param y
	 * @param z
	 * @param w
	 */
	public void setDriveOrientation(Functions.NxD6JointDriveType driveType,
			float x, float y, float z, float w) {
		Functions.jointD6SetDriveOrientation(getId(), x, y, z, w);
	}

	/**
	 * If the type of xDrive (yDrive,zDrive) is NX_D6JOINT_DRIVE_VELOCITY,
	 * driveLinearVelocity defines the goal linear velocity.
	 * 
	 * Range: velocity vector Default: Zero
	 * 
	 * Platform:
	 * 
	 * PC SW: Yes PPU : No PS3 : Yes XB360: Yes
	 * 
	 * @param driveType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setDriveLinearVelocity(Functions.NxD6JointDriveType driveType,
			float x, float y, float z) {
		Functions.jointD6SetDriveLinearVelocity(getId(), x, y, z);
	}

	/**
	 * If the type of swingDrive or twistDrive is NX_D6JOINT_DRIVE_VELOCITY,
	 * driveAngularVelocity defines the goal angular velocity.
	 * 
	 * driveAngularVelocity.x - goal angular velocity about the twist axis
	 * driveAngularVelocity.y - goal angular velocity about the swing1 axis
	 * driveAngularVelocity.z - goal angular velocity about the swing2 axis
	 * Range: angular velocity vector Default: Zero Platform:
	 * 
	 * PC SW: Yes PPU : No PS3 : Yes XB360: Yes
	 * 
	 * @param driveType
	 * @param x
	 * @param y
	 * @param z
	 */
	public void setDriveAngularVelocity(Functions.NxD6JointDriveType driveType,
			float x, float y, float z) {
		Functions.jointD6SetDriveAngularVelocity(getId(), x, y, z);
	}
	
}
