package space.unmei.lexer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class FinalNfa{
    // this class is for the Nfa when all the smaller Nfas for each of the regex is
    // combined.
    private final Set<Nfa> nfaSet = new HashSet<>();
    private final AutoState startNode = new AutoState("START", new ArrayList<>(), new ArrayList<>());
    private int totalNumStates = 1;

    public Set<Nfa> getNfaSet(){
        return this.nfaSet;
    }

    public int getTotalNumStates(){
        return this.totalNumStates;
    }

    public void addNfa(Nfa nfa, String token, int pri){
        // connect tail of nfa to the START node
        nfa.getTail().addIn(nfa.getTailAlphabet(), startNode);
        startNode.addOut(nfa.getTailAlphabet(), nfa.getTail());
        this.nfaSet.add(nfa);
        this.totalNumStates += nfa.getNfaStates().size();
        nfa.markFinal(token, pri);
    }



    public Dfa makeDfa(){
        Dfa newDfa = new Dfa();
        //Set<AutoState> states0 = this.closure(new HashSet<>(this.startNode));
        Set<AutoState> start = new HashSet<>();
        start.add(this.startNode);

        Set<AutoState> states0 = this.closure(start);
        newDfa.setStates(0, states0);
        newDfa.setStart(0);
        int idx1 = 0, idx2 = 0;

        List<String> alps = Alphabets.ALL_ALPHAS;
        while(idx1 <= idx2){
            for(String alp : alps){
                Set<AutoState> possibleNewDfaState = this.closure(this.makeDfaEdge(newDfa.getStates(idx1), alp));

                if(possibleNewDfaState.size() <= 0){continue;}

                boolean isDup = newDfa.isStatesAlreadyExists(possibleNewDfaState);
                if(isDup){
                    int tar = newDfa.getState(possibleNewDfaState);
                    newDfa.setDfaEdge(idx1, tar, alp);
                }else{
                    idx2+=1;
                    newDfa.setStates(idx2, possibleNewDfaState);
                    newDfa.setDfaEdge(idx1, idx2, alp);
                }
            }
            idx1+=1;
        }
        return newDfa;
    }

    public Set<AutoState> makeDfaEdge(Set<AutoState> states, String alphabet){
        Set<AutoState> updatedStates = new HashSet<>();
        for(AutoState s: states){
            for(AutoState s_:s.getOut().getOrDefault(alphabet, new HashSet<>())){
                updatedStates.add(s_);
            }
        }
        return updatedStates;
    }

    public Set<AutoState> closure(Set<AutoState> states){
        if(states.size() <= 0){
            return states;
        }
        Set<AutoState> currStates = new HashSet<>(states);
        while(true){
            Set<AutoState> updatedStates = new HashSet<>(currStates);
            for(AutoState s: currStates){
                // all the states that can be reached by the epsilon alphabet
                for(AutoState s_: s.getOut().getOrDefault("epsilon", Collections.emptySet())){
                    //TODO: optimise this further currenty when the states expand we are repestedly looking on
                    // states that have been scanned earlier
                    if(!updatedStates.contains(s_)){
                        updatedStates.add(s_);
                    }
                }
            }

            if(updatedStates.equals(currStates)){break;}
            currStates = updatedStates;
        }
        return currStates;
    }


}

