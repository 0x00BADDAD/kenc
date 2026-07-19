package org.kenlang.lexer;



public class Dfa{
    // names of states is going to be numbers 0 1 2 3...
    private Map<int, AutoState> dfaStates = new HashMap<>();

    private Map<int, Set<AutoState>> states = new HashSet<>();
    private Map<Set<AutoState>, int> statesSet = new HashMap<>(); // auxillary class

    private Map<int, String> finalSet = new HashMap<>();

    private AutoState start;

    private Map<Pair<int, String>, int> transTable = new HashMap<>();

    public Dfa(){
    
    
    }

    public void setFinalSet(int state, String token){
        this.finalSet.put(state, token);
    }

    public void setDfaEdge(int src, int tar, String alphabet){
        this.transTable.put(new Pair<>(src, alphabet), tar);
    }

    public int getDfaEdge(int src, String alphabet){
        return this.transTable.getOrDefualt(new Pair<>(src, alphabet), -1);
    }

    public void setStates(int state, Set<AutoState> states){
        if(!this.states.containsKey(state)){
            // first time the state is being put into the Dfa. This a great chance to also check
            // if this state is having final Nfa states in it and if it does then find the most
            // appropriate token for that Dfa state.
            int currPri = -1;
            String currTok = "";
            for(AutoState s: states){
                if(s.getIsFinal() && currPri < s.getPri()){
                    currPri = s.getPri();
                    currTok = s.getToken();
                }
            }
            if(currPri > -1){
                // at least one final Nfa state found
                finalSet.put(state, currTok);
            }
        }
        this.states.put(state, states);
        this.statesSet.put(states, state);
    }

    public Set<AutoState> getStates(int state){
        return this.states.getOrDefault(state, new HashSet<>());
    }

    public int getState(Set<AutoState> states){
        return this.statesSet.getOrDefault(states, -1);
    }

    public boolean isStatesAlreadyExists(Set<AutoState> states){
        return this.statesSet.containsKey(states);
    }
    
    public void setStart(AutoState start){
        this.start = start;
    }

    public AutoState getStart(){
        return this.start;
    }
}
