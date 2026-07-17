package org.kenlang.lexer;


import java.util.Set;
import org.kenlang.lexer.AutoState;



public class Nfa{

    private Set<AutoState> nfaStates;
    private AutoState head; // head autostate won't have any outgoing egde logically
    private AutoState tail; // tail autostate would in general just have a single incoming edge
    private String namingPrefix;
    private int currIdx;
    private String tailAlphabet;

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
                Nfa newNfa = 
                this.head.addOut(this.tailAlphabet, this.tail);
                this.tail.addIn(this.tailAlphabet, this.head);

                this.head.addIn("epsilon", null);
                this.tailAlphabet = "epsilon";
                this.tail = this.head;
                break;
            case "plus":
                if(otherNfa != null){
                    throw new IllegalArgumentException("otherNfa has to be null with plus op.");
                }
                Nfa plusNfa = this.transform(this.transform(null, "star"), "and");
                return plusNfa;
            default:
                throw new IllegalArgumentException("Unknown operator: " + op);
        }
    }



}
