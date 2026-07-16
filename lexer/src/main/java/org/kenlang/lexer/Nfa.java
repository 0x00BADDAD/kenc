package org.kenlang.lexer;


import java.util.Set;
import org.kenlang.lexer.AutoState;


public class Nfa{
    
    private Set<AutoState> nfaStates;
    private AutoState head; // head autostate won't have any outgoing egde logically
    private AutoState tail; // tail autostate would in general just have a single incoming edge
    private String namingPrefix;
    private int currIdx;


    public Nfa(String prefix){
        this.namingPrefix = prefix;
        this.currIdx = 0;
    }


    public void addState(AutoState state, boolean isHead, boolean isTail){
        state.setName(namnPrefix + "_" + String.valueOf(currIdx));
        currIdx += 1;
        this.nfaStates.add(state);
        if(isHead){this.head = state;}
        if(isTail){this.tail = state;}
    }


    public Nfa transform(Nfa otherNfa, String op){
        // combine this Nfa and its states with another Nfa and return a new Nfa

        switch(op){
            case "and":

                break;
        
        
        }
    
    }



}
