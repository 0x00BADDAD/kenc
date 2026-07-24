package org.kenlang.lexer;



public class Dfa{
    // names of states is going to be numbers 0 1 2 3...
    private Map<int, AutoState> dfaStates = new HashMap<>();

    private int maxNumStates = 0;

    private Map<int, Set<AutoState>> states = new HashSet<>();
    private Map<Set<AutoState>, int> statesSet = new HashMap<>(); // auxillary class

    private Map<int, String> finalSet = new HashMap<>();

    private int start;

    private Map<Pair<int, String>, int> transTable = new HashMap<>();

    public Dfa(){
    }

    public void setDfaStates(Map<int, AutoState> dfaStates){
        this.dfaStates = dfaStates;
    }

    public Map<int, AutoState> getDfaStates(){
        return this.dfaStates;
    }

    public void setFinalSet(int state, String token){
        this.finalSet.put(state, token);
    }

    public void setFinalSetComp(Map<int, String> fS){
        this.finalSet = fs;
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
            this.maxNumStates = Math.max(this.maxNumStates, state+1);
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

    public void setStart(int start){
        this.start = start;
    }

    public int getStart(){
        return this.start;
    }

    public void setTransTable(Map<Pair<int, String>, int> tt){
        this.transTable = tt;
    }

    public void setMaxNumStates(int x){
        this.maxNumStates = x;
    }

    public Dfa minDfa(){
        // initially non-final states and final states are in 2 different partitions.
        List<Partition> partitions = new ArrayList<>();
        Map<String, Pair<int, Partition>> finalParts = new HashMap<>();
        Partition nonFinalParts = new Partition();
        Map<int, int> nodeToPart = new HashMap<>();

        Set<Partition> parts = new HashSet<>(Set.of(nonFinalParts));

        int currFinIdx = 1;
        for(int i=0; i < this.maxNumStates; ++i){
            if(!finalSet.containsKey(i)){
                nonFinalParts.addNode(i);
                nodeToPart.put(i, 0);
            }else{
                String tok = finalSet.get(i);

                if(!finalParts.conatinsKey(tok)){
                    // new tok partition
                    finalParts.put(tok, new Pair<>(currFinIdx, new Partition(List.of(i))));
                    nodeToPart.put(i, currFinIdx);
                    currFinIdx+=1;
                    continue;
                }

                finalParts.get(tok).second().addNode(i);
                nodeToPart.put(i, finalParts.get(tok).first());
            }
        }

        Pair<Set<Partition>, Map<int, int>> currParts;
        while(true){
            currParts = this.pokeParts(parts.stream().toList(), nodeToPart);
            Set<Partition> partSet = new HashSet<>(currParts.first());
            if(partSet.equals(parts)){break;}
            nodeToPart = currParts.second();
            parts = partSet;
        }

        Dfa miniDfa = this.consMinDfa(parts, nodeToPart);
        return miniDfa;
    }

    public Dfa consMinDfa(List<Partition> parts, Map<int, int> nodeToPart){
        Dfa finalMinDfa = new Dfa();

        Map<int, String> newFinalSet = new HashSet<>();
        Map<int, AutoState> _dfaStates = new HashMap<>();
        Map<Pair<int, String>, int> newTransTable  = new HashMap<>();
        finalMinDfa.setMaxNumStates(parts.size());
        int i = 0;

        for(Parititon part: parts){
            AutoState state = new AutoState("finalDfa_" + String.valueOf(i), new ArrayList<>(), new ArrayList<>());
            int node = part.pickOne();
            int partNode = nodeToPart.get(node);
            _dfaStates.put(partNode, state);
        }

        for(Partition part: parts){
            for(int p: part){
                if(finalSet.containsKey(p) && !newFinalSet.containsKey(nodeToPart.get(p))){
                    newFinalSet.put(nodeToPart.get(p), finalSet.get(p));
                    Set<AutoState> statesInPart = this.states.get(p);
                    for(AutoState s: statesInPart){
                        if(s.getIsFinal()){
                            _dfaStates.get(nodeToPart.get(p)).markFinal(finalSet.get(p), s.getPri());
                        }
                    }
                }
                if(p == 0){
                    // this partition contains the start state  hence it is  a start state in the minDfa
                    finalMinDfa.setStart(nodeToPart.get(0));
                }
                Alphabets alp = new Alphabets();
                for(int a =32; a<127; ++a){
                    String transAlp = alp.getAlphabet(a);
                    int tarOldNode = this.transTable.getOrDefault(new Pair<>(p, transAlp), -1);
                    if(tarOldNode != -1 && !newTransTable.containsKey(new Pair<>(nodeToPart.get(p), transAlp))){
                        newTransTable.put(new Pair<>(nodeToPart.get(p), transAlp), nodeToPart.get(tarOldNode));
                        _dfaStates.get(nodeToPart.get(p)).addOut(transAlp, _dfaStates.get(nodeToPart.get(tarOldNode)));
                        _dfaStates.get(nodeToPart.get(tarOldNode)).addIn(transAlp, _dfaStates.get(nodeToPart.get(p)));
                    }

                }
            }
            i+=1;
        }
        finalMinDfa.setDfaStates(_dfaStates);
        finalMinDfa.setFinalSet(newFinalSet);
        finalMinDfa.setTransTable(newTransTable);
        return finalMinDfa;
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
                if(partMap.get(j).second() == partMap.get(j-1).second()){
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
