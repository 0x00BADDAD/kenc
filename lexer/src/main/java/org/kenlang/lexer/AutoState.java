package org.kenlang.lexer;

import java.util.Map;
import java.util.HashMap;


public class AutoState{
    // keys are all the alphabets of the language incl. epsilon
    private final Map<String, boolean> in = new HashMap<>();
    private final Map<String, boolean> out = new HashMap<>();

    private String name;

    public AutoState(String name, List<String> ins, List<String> outs){
        this.name = name;
        for(String s:ins){
            this.addIn(s);
        }
        for(String s:outs){
            this.addOut(s);
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void addIn(String alphabet){
        this.in.put(alphabet, true);
    }

    public void addOut(String alphabet){
        this.out.put(alphabet, true);
    }

}
