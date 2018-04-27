package com.ryubai.sgnf.physx.dynamics.actors;

public class ActorParameters {
	private float density;
	private boolean useCCD;
	private boolean isDynamic = true;
	public boolean isDynamic() {
		return isDynamic;
	}
	public void setDynamic(boolean isDynamic) {
		this.isDynamic = isDynamic;
	}
	public float getDensity() {
		return density;
	}
	public void setDensity(float density) {
		this.density = density;
	}
	public boolean isUseCCD() {
		return useCCD;
	}
	public void setUseCCD(boolean useCCD) {
		this.useCCD = useCCD;
	}
}
