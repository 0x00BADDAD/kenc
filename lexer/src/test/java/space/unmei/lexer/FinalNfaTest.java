package space.unmei.lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashSet;



public class FinalNfaTest{
    @Test
    void makeFinalNfaAndConsDfa(){
        // and
        Nfa nfa1_and = new Nfa("n1_and", 0, "i");
        Nfa nfa2_and = new Nfa("n2_and", 0, "f");
        Nfa nfa_if = nfa1_and.transform(nfa2_and, "and");


        // or
        // a
        Nfa nfa1_or = new Nfa("n1_or", 0, "a");
        Nfa nfa2_or = new Nfa("n2_or", 0, "b");
        Nfa nfa_ab = nfa1_or.transform(nfa2_or, "or");

        // star
        // a
        Nfa nfa_star = new Nfa("n1_star", 0, "a");
        Nfa starNfa_a = nfa_star.transform(null, "star");

        // plus
        // a
        Nfa nfa_plus = new Nfa("n1_plus", 0, "a");
        Nfa plusNfa_a = nfa_plus.transform(null, "plus");

        FinalNfa finNfa = new FinalNfa();

        finNfa.addNfa(nfa_if, "IF", 99999);
        finNfa.addNfa(nfa_ab, "ID", 99998);
        finNfa.addNfa(starNfa_a, "STAR", 99997);
        finNfa.addNfa(plusNfa_a, "PLUS", 99996);

        assertEquals(4, finNfa.getNfaSet().size());
        System.out.printf("Was here in test and totalNumStates is: " + String.valueOf(finNfa.getTotalNumStates())+"\n");
        Dfa dfa = finNfa.makeDfa();



        assertEquals(3, dfa.getStates(0).size());
        for(Map.Entry<Integer, Set<AutoState>> entry: dfa.getStatesComp().entrySet()){
            System.out.println(String.valueOf(entry.getKey()) + " -- " + String.valueOf(entry.getValue().size()));
        }


        //assertEquals(5, dfa.getFinalSetComp().size());

        for(Map.Entry<Integer, String> entry: dfa.getFinalSetComp().entrySet()){
            System.out.println(String.valueOf(entry.getKey()) + " -- " + entry.getValue());
        }
        assertEquals(12, dfa.getMaxNumStates());
        Dfa minDfa =  dfa.minDfa();
        assertEquals(11, minDfa.getMaxNumStates());

    }


}
