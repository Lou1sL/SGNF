package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class SSSocketModel {
    public int command;
    public List<String> message;
    
    public SSSocketModel(){
    	message = new ArrayList<String>();
    }
}