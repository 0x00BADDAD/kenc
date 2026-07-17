package org.kenlang.lexer;

import java.util.Map;
import java.util.HashMap;

import org.kenlang.lexer.Pair;

public class AutoState{
    // keys are all the alphabets of the language incl. epsilon
    private final Map<String, AutoState> in = new HashMap<>();
    private final Map<String, AutoState> out = new HashMap<>();

    private String name;

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

    public void addIn(String alphabet, AutoState state){
        this.in.put(alphabet, state);
    }

    public void addOut(String alphabet, AutoState state){
        this.out.put(alphabet, state);
    }

}
