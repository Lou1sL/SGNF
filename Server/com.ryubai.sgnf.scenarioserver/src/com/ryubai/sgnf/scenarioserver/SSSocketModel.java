package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class SSSocketModel {
    public int command;
    public int currentTick;
    public List<Integer> message = new ArrayList<Integer>();
    public List<Vec> vector = new ArrayList<Vec>();
    
    
    public class Vec{
    	public int tag = -1;
    	public float x = 0;
    	public float y = 0;
    	public float z = 0;
    }
}