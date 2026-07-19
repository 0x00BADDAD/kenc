package org.kenlang.lexer;


import java.util.Set;
import org.kenlang.lexer.AutoState;



public class Nfa{

    private Set<AutoState> nfaStates = new HashSet<>();
    private AutoState head; // head can have edges going out of it
    private AutoState tail; // tail autostate would in general just have a single incoming edge
    private String namingPrefix;
    private int currIdx;
    private String tailAlphabet;

    private String token; // these will only be set when markFinal method is called on the nfa object
    private AutoState finalState;

    public String getTailAlphabet(){
        return this.tailAlphabet;
    }

    public void setTailAlphabet(String alphabet){
        this.tailAlphabet = alphabet;
    }

    public AutoState getTail(){
        return this.tail;
    }

    public void setTail(AutoState tail){
        this.tail = tail;
    }

    public AutoState getHead(){
        return this.head;
    }

    public void setHead(AutoState head){
        this.head = head;
    }

    public Set<AutoState> getNfaStates(){
        return this.nfaStates;
    }

    public String getNamingPrefix(){
        return this.namingPrefix;
    }

    public void setNamingPrefix(String name){
        this.namingPrefix = name;
    }

    public AutoState getFinalState(){
        return this.finalState;
    }

    public AutoState setFinalState(AutoState state){
        this.finalState = state;
    }

    public Nfa(String prefix, int currIdx){
        this.namingPrefix = prefix;
        this.currIdx = currIdx;
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
                // head of this nfa gets connected to tail of other Nfa
                AutoState otherTail = otherNfa.getTail();
                otherTail.addIn(otherNfa.getTailAlphabet(), this.head);
                this.head.addOut(otherNfa.getTailAlphabet(), otherTail);

                otherNfaStates = otherNfa.getNfaStates();
                for(AutoState state: this.nfaStates){
                    otherNfaStates.add(state);
                }
                otherNfa.setTail(this.tail);
                otherNfa.setTailAplhabet(this.tailAlphabet);
                return otherNfa;

            case "or":
                AutoState extra1 = new AutoState(this.namingPrefix + "_" + String.valueOf(this.currIdx), new ArrayList<>(), new ArrayList<>());
                this.currIdx+=1;
                AutoState extra2 = new AutoState(this.namingPrefix + "_" + String.valueOf(this.currIdx), new ArrayList<>(), new ArrayList<>());
                this.currIdx+=1;

                // connect 'out' of extra1 to tail of otherNfa
                String tailAlphabetOtherNfa = otherNfa.getTailAlphabet();
                otherNfa.getTail().addIn(tailAlphabetOtherNfa, extra1);
                extra1.addOut(tailAlphabetOtherNfa, otherNfa.getTail());

                String tailAlphabetThisNfa = this.tailAlphabet;
                this.tail.addIn(tailAlphabetThisNfa, extra1);
                extra1.addOut(tailAlphabetThisNfa, this.tail);

                extra1.addIn("epsilon", null);
                otherNfa.setTail(extra1);

                // connect all the out of head of this nfa and the otherNfa to extra2 In
                // connect head of otherNfa to extra2
                otherNfa.getHead().addOut("epsilon", extra2);
                extra2.addIn("epsilon", otherNfa.getHead());

                // connect head of this Nfa to extra2
                this.head.addOut("epsilon", extra2);
                extra2.addIn("epsilon", this.head);

                otherNfa.setHead(extra2);
                otherNfa.setTailAlphabet("epsilon");
                for(AutoState s: this.nfaStates){
                    otherNfa.getNfaStates().add(s);
                }
                return otherNfa;

            case "star":
                if(otherNfa != null){
                    throw new IllegalArgumentException("otherNfa has to be null with star op.");
                }
                Nfa newNfa =  this.copy();
                newNfa.getHead().addOut(newNfa.getTailAlphabet(), newNfa.geTail());
                newNfa.getTail().addIn(newNfa.getTailAlphabet(), newNfa.getHead());

                newNfa.getHead().addIn("epsilon", null);
                newNfa.setTailAlphabet("epsilon");
                newNfa.setTail(newNfa.getHead());
                return newNfa;
            case "plus":
                if(otherNfa != null){
                    throw new IllegalArgumentException("otherNfa has to be null with plus op.");
                }
                Nfa newNfa = this.copy();
                Nfa plusNfa = newNfa.transform(newNfa.transform(null, "star"), "and");
                return plusNfa;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }

    public Nfa copy(){
        // copy all the states with empty in and out maps
        // extract the edge info from this nfa
        // fill the in and out maps of the newly copied states with this info
        Map<Pair<String, String>, String> edgeInfo = new HashMap<>();
        Map<String, AutoState> nameToAutoState = new HashMap<>();

        Nfa newNfa = new Nfa(this.namingPrefix, this.currIdx);
        newNfa.setTailAlphabet(this.tailAlphabet);
        for(AutoState state: this.nfaStates){
            AutoState newState = new AutoState(state.name, new ArrayList<>(), new ArrayList<>());
            nameToAutoState.put(state.name, newState);
            for(Map.Entry<String, Set<AutoState>> entry: state.getIn().entrySet()){
                if(entry.getValue().isEmpty()){
                    edgeInfo.put(new Pair<>("dummy", state.name), entry.getKey());
                    continue;
                }
                for(AutoState outState: entry.getValue()){
                    String alphabetConn = edgeInfo.getOrDefault(new Pair<>(outState.name, state.name), "null");
                    if(!alphabetConn.equals(entry.getKey())){
                        edgeInfo.put(new Pair<>(outState.name, state.name), entry.getKey());
                    }
                }
            }

            for(Map.Entry<String, Set<AutoState>> entry: state.getOut().entrySet()){
                for(AutoState inState: entry.getValue()){
                    String alphabetConn = edgeInfo.getOrDefault(new Pair<>(state.name, inState.name), "null");
                    if(!alphabetConn.equals(entry.getKey())){
                        edgeInfo.put(new Pair<>(state.name, inState.name), entry.getKey());
                    }
                }
            }
        }

        for(Map.Entry<Pair<String, String>, String> entry: edgeInfo.entrySet()){
            Pair<String, String> k = entry.getKey();
            if(k.first() != "dummy"){
                nameToAutoState.get(k.first()).addOut(entry.getValue(), nameToAutoState.get(k.second()));
                nameToAutoState.get(k.second()).addIn(entry.getValue(), nameToAutoState.get(k.first()));
            }else{
                nameToAutoState.get(k.second()).addIn(entry.getValue(), null);
            }
        }
        for(Map.Entry<String, AutoState> entry: nameToAutoState.entrySet()){
            newNfa.addState(entry.getValue(), this.head.equals(entry.getValue()), this.tail.equals(entry.getValue()));
        }
        return newNfa;
    }
    
    public void markFinal(String token, int pri){
        this.token = token;
        this.finalState = this.head;
        this.head.markFinal(token, pri);
    }


}
