package com.ryubai.sgnf.scenarioserver;

import java.util.ArrayList;
import java.util.List;

public class ISSocketModel {
    public int command;
    public List<String> message;
    
    public ISSocketModel(){
    	message = new ArrayList<String>();
    }
}