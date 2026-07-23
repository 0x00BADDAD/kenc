package org.kenlang.lexer;



public class Dfa{
    // names of states is going to be numbers 0 1 2 3...
    private Map<int, AutoState> dfaStates = new HashMap<>();

    private int maxNumStates = 0;

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
            this.maxNumStates = Math.max(this.maxNumStates, state);
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

    public Dfa minDfa(){
        // initially non-final states and final states are in 2 different partitions.
        List<Partition> partitions = new ArrayList<>();
        Partition finalParts = new Partition();
        Partition nonFinalParts = new Partition();
        Map<int, int> nodeToPart = new HashMap<>();

        for(int i=0; i <= this.maxNumStates; ++i){
            if(!finalSet.containsKey(i)){
                nonFinalParts.addNode(i);
                nodeToPart.put(i, 1);
            }else{
                finalParts.addNode(i);
                nodeToPart.put(i, 0);
            }
        }
        Set<Partition> parts = new HashSet<>(List.of(finalParts, nonFinalParts));
        Pair<Set<Partition>, Map<int, int>> currParts;
        while(true){
            currParts = this.pokeParts(parts.stream().toList(), nodeToPart);
            if(currParts.first().equals(parts)){break;}
            nodeToPart = currParts.second();
            parts = currParts.first();
        }
    }

    public Pair<List<Partition>, Map<int, int>> pokeParts(List<Partition> parts, Map<int, int> nodeToPart){
        Alphabets alp = new Alphabets();
        List<Partition> newParts = new ArrayList<>();
        Map<int, int> newNodeToPart = new HashMap<>();
        for(int i=0; i<parts.size(); ++i){
            if(parts.get(i).size() < 2){
                newParts.add(parts.get(i));
                continue;
            }
            // the key idea is to sort "string"(s)
            List<Pair<int, String>> partMap = new ArrayList<>();
            List parList = parts.get(i);
            for(int p: parList.toList()){
                //Pair<int, String> partMapEle = new Pair<>();
                String transStr = "";
                for(int j=32; j<127; ++j){
                    int tarNode = transTable.getOrDefault(new Pair<>(p, alp.getAlphabet(j)), -1);
                    int tarPart = nodeToPart.getOrDefault(tarNode, -1);
                    transStr += "+" + String.valueOf(tarPart);
                }
                partMap.add(new Pair<int, String>(p, transStr));
            }
            partMap.sort(Comparator.comparing(Pair::getSecond));
            Partition cpar = new Partition();
            cpar.addNode(partMap.get(0).first());
            for(int j=1; j<partMap.size(); j++){
                if(partMap.get(j).second() == part.get(j-1).second()){
                    cpar.addNode(partMap.get(j).first());
                }else{
                    int currPartIdx = newParts.size();
                    for(int p: cpar.toList()){
                        newNodeToPart.put(p, currPartIdx);
                    }
                    newParts.add(cpar);
                    cpar = new Partition();
                    cpar.addNode(partMap.get(j).first());
                }
            }
        }
        return new Pair(newParts, newNodeToPart);
    }
}
