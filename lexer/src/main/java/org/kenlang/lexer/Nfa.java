package org.kenlang.lexer;


import java.util.Set;
import org.kenlang.lexer.AutoState;



public class Nfa{

    private Set<AutoState> nfaStates;
    private AutoState head; // head autostate won't have any outgoing egde logically
    private AutoState tail; // tail autostate would in general just have a single incoming edge
    private String namingPrefix;
    private int currIdx;

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
                // head of this nfa gets connected to tail of other Nfa
                AutoState otherTail = otherNfa.getTail();
                Map<String, AutoState> otherTailIn = otherTail.getIn();
                for(Map.Entry<String, AutoState> entry : otherTailIn.entrySet()){
                    if(entry.getValue().getName() == "dummy"){
                        otherTailIn.put(entry.getKey(), this.head);
                        this.head.put(entry.getKey(), otherTail);
                    }
                }
                otherNfaStates = otherNfa.getNfaStates();
                for(AutoState state: this.nfaStates){
                    otherNfaStates.add(state);
                }
                otherNfa.setTail(this.head);
                return otherNfa;
                break;

            case "or":
                AutoState extra1 = new AutoState(this.namingPrefix + "_" + String.valueOf(this.currIdx), new ArrayList<>(), new ArrayLisy<>());
                this.currIdx+=1;
                AutoState extra2 = new AutoState(this.namingPrefix + "_" + String.valueOf(this.currIdx), new ArrayList<>(), new ArrayLisy<>());
                this.currIdx+=1;
                String otherNamingPrefix = otherNfa.getNamingPrefix();

                // connect 'out' of extra1 to tail of otherNfa

                for(Map.Entry<String, AutoState> entry: otherNfa.getTail().getIn()){
                    // connect tail of otherNfa to extra1
                    otherNfa.getTail().getIn().put(entry.getKey(), extra1);
                    extra1.getOut().put(entry.getKey(), otherNfa.getTail());
                }

                for(Map.Entry<String, AutoState> entry: this.tail.getIn()){
                    // connect tail of this Nfa to extra1
                    this.tail.getIn().put(entry.getKey(), extra1);
                    extra1.getOut().put(entry.getKey(), this.tail);
                }
                extra1.getIn().put("epsilon", new AutoState("dummy", new ArrayList<>(), new ArrayLisy<>()));
                otherNfa.setTail(extra1);

                // connect all the out of head of this nfa and the otherNfa to extra2 In
                for(Map.Entry<String, AutoState> entry: otherNfa.getHead().getOut()){
                    // connect head of otherNfa to extra2
                    otherNfa.getHead().getOut().put("epsilon", extra2);
                    extra2.getIn().put("epsilon", otherNfa.getHead());
                }

                for(Map.Entry<String, AutoState> entry: this.head.getout()){
                    // connect head of this Nfa to extra2
                    this.head.getOut().put("epsilon", extra2);
                    extra2.getIn().put("epsilon", this.head);
                }
                otherNfa.setHead(extra2);
                for(AutoState s: this.nfaStates){
                    otherNfa.getNfaStates().add(s);
                }
                return otherNfa;
                break;
        }
    }



}
