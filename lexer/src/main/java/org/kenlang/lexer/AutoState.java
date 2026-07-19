package org.kenlang.lexer;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;

import org.kenlang.lexer.Pair;


import org.kenlang.lexer.Pair;

public class AutoState{
    // keys are all the alphabets of the language incl. epsilon
    private final Map<String, Set<AutoState>> in = new HashMap<>();
    private final Map<String, Set<AutoState>> out = new HashMap<>();

    private String name; // this name will be unique

    private String token;
    private boolean isFinal = false;
    private int tokenPri;

    public AutoState(String name, List<Pair<String, AutoState>> ins, List<Pair<String, AutoState>> outs){
        this.name = name;
        for(Pair<String, AutoState> s:ins){
            this.addIn(s.first(), s.second());
        }
        for(Pair<String, AutoState> s:outs){
            this.addOut(s.first(), s.second());
        }
    }

    public AutoState getIn(){
        rerurn this.in;
    }

    public AutoState getOut(){
        rerurn this.out;
    }


    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void addIn(String alphabet, AutoState node){
        Set<AutoState> currSet = this.in.getOrDefault(alphabet, new HashSet<>());
        if(node != null){
            currSet.add(node);
        }
        this.in.put(alphabet, currSet);
    }

    public void addOut(String alphabet, AutoState node){
        Set<AutoState> currSet = this.out.getOrDefault(alphabet, new HashSet<>());
        if(node != null){
            currSet.add(node);
        }
        this.out.put(alphabet, currSet);
    }

    public void markFinal(String token, int pri){
        this.token = token;
        this.isFinal = true;
        this.tokenPri = pri;
    }

    public int getPri(){
        return this.tokenPri;
    }

    public void setPri(int pri){
        this.tokenPri = pri;
    }

    public boolean getIsFinal(){
        return this.isFinal;
    }

    public String getToken(){
        return this.token;
    }


}
