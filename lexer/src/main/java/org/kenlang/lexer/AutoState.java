package org.kenlang.lexer;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.kenlang.lexer.Pair;



public class AutoState{
    // keys are all the alphabets of the language incl. epsilon
    private final Map<String, Set<AutoState>> in = new HashMap<>();
    private final Map<String, Set<AutoState>> out = new HashMap<>();

    private String name;

    public AutoState(String name, List<Pair<AutoState, String>> ins, List<Pair<AutoState, String>> outs){
        this.name = name;
        for(Pair<AutoState, String> s:ins){
            this.addIn(s.second(), s.first());
        }
        for(Pair<AutoState, String> s:outs){
            this.addOut(s.second(), s.first());
        }
    }

    public void setName(String name){
        this.name = name;
    }

    public void addIn(String alphabet, AutoState node){
        Set<AutoState> currSet = in.get(alphabet);
        currSet.add(node);
        this.in.put(alphabet, currSet);
    }

    public void addOut(String alphabet, AutoState node){
        Set<AutoState> currSet = out.get(alphabet);
        currSet.add(node);
        this.out.put(alphabet, currSet);
    }

}
