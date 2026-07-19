package org.kenlang.lexer;




public class FinalNfa{
    private final Set<Nfa> nfaSet = new HashSet<>();
    private final AutoState startNode = new AutoState("START", new ArrayList<>(), new ArrayList<>());


    public void addNfa(Nfa nfa, String token, int pri){
        // connect tail of nfa to the START node
        nfs.getTail().addIn(nfa.getTailAlphabet(), startNode);
        startNode.addOut(nfa.getTailAlphabet(), nfa.getTail());
        this.nfaSet.add(nfa);
        nfa.markFinal(token, pri);
    }



    public Dfa makeDfa(){
        Dfa newDfa = new Dfa();
        Set<AutoState> states0 = this.closure(new HashSet<>(this.startNode));
        newDfa.setStates(0, states0);
        int idx1 = 0, idx2 = 0;
        Alphabets alp = new Alphabets();
        while(idx1 <= idx2){
            for(int i = 32; i <= 126; ++i){
                Set<AutoState> possibleNewDfaState = this.closure(this.DfaEdge(newDfa.getStates(idx1)));
                boolean isDup = newDfa.isStatesAlreadyExists(possibleNewDfaState);
                if(isDup){
                    int tar = newDfa.getState(possibleNewDfaState);
                    newDfa.setDfaEdge(idx1, tar, alp.getAlphabet(i));
                }else{
                    idx2+=1;
                    newDfa.setStates(idx2, possibleNewDfaState);
                    newDfa.setDfaEdge(idx1, idx2, alp.getAlphabet(i));
                }
                idx1+=1;
            }
        }
        return newDfa;
    }

    public Set<AutoState> DfaEdge(Set<AutoState> states, String alphabet){
        Set<AutoState> updatedStates = new HashSet<>(states);
        for(AutoState s: states){
            for(AutoState s_:s.getOut().getOrDefault(alphabet, new HashSet<>())){
                updatedStates.add(s_);
            }
        }
        return updatedStates;
    }

    public Set<AutoState> closure(Set<AutoState> states){
        Set<AutoState> currStates = new HashSet<>(states);
        while(true){
            AutoState updatedStates = new HashSet<>(currStates);
            for(AutoState s: currStates){
                // all the states that can be reached by the epsilon alphabet
                for(AutoState s_: s.getOut().get("epsilon")){
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

