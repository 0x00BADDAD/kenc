package space.unmei.lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class Dfa{
    // names of states is going to be numbers 0 1 2 3...
    private Map<Integer, AutoState> dfaStates = new HashMap<>();

    private int maxNumStates = 0;

    private Map<Integer, Set<AutoState>> states = new HashMap<>();
    private Map<Set<AutoState>, Integer> statesSet = new HashMap<>(); // auxillary class

    private Map<Integer, String> finalSet = new HashMap<>();

    private int start;

    private Map<Pair<Integer, String>, Integer> transTable = new HashMap<>();

    public Dfa(){
    }

    public void setDfaStates(Map<Integer, AutoState> dfaStates){
        this.dfaStates = dfaStates;
    }

    public Map<Integer, AutoState> getDfaStates(){
        return this.dfaStates;
    }

    public void setFinalSet(int state, String token){
        this.finalSet.put(state, token);
    }

    public Map<Integer, String> getFinalSetComp(){
        return this.finalSet;
    }

    public void setFinalSetComp(Map<Integer, String> fs){
        this.finalSet = fs;
    }

    public void setDfaEdge(int src, int tar, String alphabet){
        this.transTable.put(new Pair<>(src, alphabet), tar);
    }

    public int getDfaEdge(int src, String alphabet){
        return this.transTable.getOrDefault(new Pair<>(src, alphabet), -1);
    }

    public void printDfaTransTable(){
        for(Map.Entry<Pair<Integer, String>, Integer> ent: this.transTable.entrySet()){
            Pair<Integer, String> pk = ent.getKey();
            Integer pv = ent.getValue();

            System.out.println("-------------------");
            System.out.println("{" + String.valueOf(pk.first())+ ", "+ pk.second() +"} --> {" + String.valueOf(pv) + "}");
        }
    }

    public void printDfa(){
        // state-name(head|tail): {...}
        // in: {[name1, alp1], [name2, alp2], ...}
        // out: {[name1, alp1], [name2, alp2], ...}
        for(Map.Entry<Integer, AutoState> ent: this.dfaStates.entrySet()){
            String tokVal = "";
            if(this.finalSet.containsKey(ent.getKey())){
                tokVal = this.finalSet.get(ent.getKey());
            }
            AutoState state = ent.getValue();

            System.out.println("state-name("+ tokVal + "): " + state.getName());
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String, Set<AutoState>> entry : state.getIn().entrySet()){
                sb.append("[ ");
                sb.append(entry.getKey());
                sb.append(" -- ");
                for(AutoState ss: entry.getValue()){
                    sb.append(ss.getName() + ", ");
                }
                sb.append(" ], ");
            }
            System.out.println("in: {" + sb.toString() + "}");
            sb = new StringBuilder();
            for(Map.Entry<String, Set<AutoState>> entry : state.getOut().entrySet()){
                sb.append("[ ");
                sb.append(entry.getKey());
                sb.append(" -- ");
                for(AutoState ss: entry.getValue()){
                    sb.append(ss.getName() + ", ");
                }
                sb.append(" ], ");
            }
            System.out.println("out: {" + sb.toString() + "}");
        }
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
                //System.out.println(String.valueOf(state) + "-- " + currTok);
                this.finalSet.put(state, currTok);
            }
            this.maxNumStates = Math.max(this.maxNumStates, state+1);
        }
        this.states.put(state, states);
        this.statesSet.put(states, state);
    }

    public Set<AutoState> getStates(int state){
        return this.states.getOrDefault(state, new HashSet<>());
    }

    public Map<Integer, Set<AutoState>> getStatesComp(){
        return this.states;
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

    public void setTransTable(Map<Pair<Integer, String>, Integer> tt){
        this.transTable = tt;
    }

    public void setMaxNumStates(int x){
        this.maxNumStates = x;
    }

    public int getMaxNumStates(){
        return this.maxNumStates;
    }

    public Dfa minDfa(){
        // initially non-final states and final states are in 2 different partitions.
        // but final states are also divided by the token
        List<Partition> partitions = new ArrayList<>();
        Map<String, Pair<Integer, Partition>> finalParts = new HashMap<>();
        Partition nonFinalParts = new Partition();
        Map<Integer, Integer> nodeToPart = new HashMap<>();

        Set<Partition> parts = new HashSet<>();

        int currFinIdx = 1;
        for(int i=0; i < this.maxNumStates; ++i){
            if(!finalSet.containsKey(i)){
                nonFinalParts.addNode(i);
                nodeToPart.put(i, 0);
            }else{
                String tok = finalSet.get(i);

                if(!finalParts.containsKey(tok)){
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
        parts.add(nonFinalParts);
        for (Pair<Integer, Partition> p : finalParts.values()) {
            parts.add(p.second());
        }

        Pair<List<Partition>, Map<Integer, Integer>> currParts;
        while(true){
            currParts = this.pokeParts(parts.stream().toList(), nodeToPart);
            Set<Partition> partSet = new HashSet<>(currParts.first());
            if(partSet.equals(parts)){break;}
            nodeToPart = currParts.second();
            parts = partSet;
        }

        Dfa miniDfa = this.consMinDfa(parts.stream().toList(), nodeToPart);
        return miniDfa;
    }

    public Dfa consMinDfa(List<Partition> parts, Map<Integer, Integer> nodeToPart){
        Dfa finalMinDfa = new Dfa();

        Map<Integer, String> newFinalSet = new HashMap<>();
        Map<Integer, AutoState> _dfaStates = new HashMap<>();
        Map<Pair<Integer, String>, Integer> newTransTable  = new HashMap<>();
        finalMinDfa.setMaxNumStates(parts.size());
        int i = 0;

        for(Partition part: parts){
            AutoState state = new AutoState("finalDfa_" + String.valueOf(i), new ArrayList<>(), new ArrayList<>());
            int node = part.pickOne();
            int partNode = nodeToPart.get(node);
            _dfaStates.put(partNode, state);
            i+=1;
        }

        for(Partition part: parts){
            for(int p: part){
                if(finalSet.containsKey(p) && !newFinalSet.containsKey(nodeToPart.get(p))){
                    newFinalSet.put(nodeToPart.get(p), finalSet.get(p));
                    Set<AutoState> statesInPart = this.states.getOrDefault(p, Collections.emptySet());
                    int maxPri = -1;
                    for(AutoState s: statesInPart){
                        if(s.getIsFinal() && s.getPri() > maxPri){
                            maxPri = s.getPri();
                            _dfaStates.get(nodeToPart.get(p)).markFinal(finalSet.get(p), maxPri);
                        }
                    }
                }
                if(p == 0){
                    // this partition contains the start state  hence it is  a start state in the minDfa
                    finalMinDfa.setStart(nodeToPart.get(0));
                }
                List<String> alps = Alphabets.ALL_ALPHAS;
                for(String alp: alps){
                    String transAlp = alp;
                    int tarOldNode = this.transTable.getOrDefault(new Pair<>(p, transAlp), -1);
                    if(tarOldNode != -1 && !newTransTable.containsKey(new Pair<>(nodeToPart.get(p), transAlp))){
                        newTransTable.put(new Pair<>(nodeToPart.get(p), transAlp), nodeToPart.get(tarOldNode));
                        _dfaStates.get(nodeToPart.get(p)).addOut(transAlp, _dfaStates.get(nodeToPart.get(tarOldNode)));
                        _dfaStates.get(nodeToPart.get(tarOldNode)).addIn(transAlp, _dfaStates.get(nodeToPart.get(p)));
                    }

                }
            }
        }
        finalMinDfa.setDfaStates(_dfaStates);
        finalMinDfa.setFinalSetComp(newFinalSet);
        finalMinDfa.setTransTable(newTransTable);
        return finalMinDfa;
    }

    public Pair<List<Partition>, Map<Integer, Integer>> pokeParts(List<Partition> parts, Map<Integer, Integer> nodeToPart){
        List<String> alps = Alphabets.ALL_ALPHAS;
        List<Partition> newParts = new ArrayList<>();
        Map<Integer, Integer> newNodeToPart = new HashMap<>();
        for(int i=0; i<parts.size(); ++i){
            if(parts.get(i).size() < 2){
                int currPartIdx = newParts.size();
                newParts.add(parts.get(i));
                newNodeToPart.put(parts.get(i).pickOne(), currPartIdx);
                continue;
            }
            // the key idea is to sort "string"(s)
            List<Pair<Integer, String>> partMap = new ArrayList<>();
            Partition parList = parts.get(i);
            for(int p: parList){
                //Pair<int, String> partMapEle = new Pair<>();
                //String transStr = "";
                StringBuilder transStr = new StringBuilder();
                for(String alp: alps){
                    int tarNode = transTable.getOrDefault(new Pair<>(p, alp), -1);
                    int tarPart = nodeToPart.getOrDefault(tarNode, -1);
                    transStr.append('+').append(tarPart);
                }
                partMap.add(new Pair<>(p, transStr.toString()));
            }
            partMap.sort(Comparator.comparing(Pair::second));
            Partition cpar = new Partition();
            cpar.addNode(partMap.get(0).first());
            for(int j=1; j<partMap.size(); j++){
                if(partMap.get(j).second().equals(partMap.get(j-1).second())){
                    cpar.addNode(partMap.get(j).first());
                }else{
                    int currPartIdx = newParts.size();
                    for(int p: cpar){
                        newNodeToPart.put(p, currPartIdx);
                    }
                    newParts.add(cpar);
                    cpar = new Partition();
                    cpar.addNode(partMap.get(j).first());
                }
            }

            int currPartIdx = newParts.size();
            for(int p: cpar){
                newNodeToPart.put(p, currPartIdx);
            }
            newParts.add(cpar); //last partition
        }
        return new Pair<>(newParts, newNodeToPart);
    }
}
