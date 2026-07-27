package space.unmei.lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;


public class NfaTest{

    @Test
    void nfaTranformTest_and(){
        Nfa myNfa1 = new Nfa("N_1", 0);

        //making Nfas from regex
        // IF: if
        // i
        AutoState n1_s1 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        n1_s1.addIn("i", null);
        myNfa1.setTailAlphabet("i");
        myNfa1.addState(n1_s1, true, true);


        Nfa myNfa2 = new Nfa("N_2", 0);
        // f
        AutoState n1_s2 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        n1_s2.addIn("f", null);
        myNfa2.setTailAlphabet("f");
        myNfa2.addState(n1_s2, true, true);

        // ->i->f
        Nfa myNfaif = myNfa1.transform(myNfa2, "and");
        myNfaif.markFinal("IF", 99999);

        // assertions
        AutoState tail = myNfaif.getTail();
        AutoState head = myNfaif.getHead();
        assertEquals("N_1_0", tail.getName());
        assertEquals("N_2_0", head.getName());
        assertTrue(tail.getOut().get("f").contains(head));
        assertTrue(head.getIsFinal());
        assertEquals("IF", head.getToken());
    }

    @Test
    void nfaTranformTest_or(){
        // a
        Nfa nfa1 = new Nfa("n1", 0);

        AutoState nfa1_s1 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        nfa1.addState(nfa1_s1, true, true);
        nfa1_s1.addIn("a", null);
        nfa1.setTailAlphabet("a");

        // b
        Nfa nfa2 = new Nfa("n2", 0);
        AutoState nfa2_s1 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        nfa2.addState(nfa2_s1, true, true);
        nfa2_s1.addIn("b", null);
        nfa2.setTailAlphabet("b");

        Nfa nfa_or = nfa1.transform(nfa2, "or");
        nfa_or.markFinal("ID", 99999);

        // assertions
        assertTrue(nfa_or.getHead().getIn().get("epsilon").contains(nfa1_s1));
        assertTrue(nfa_or.getHead().getIn().get("epsilon").contains(nfa2_s1));
        assertEquals(0, nfa_or.getHead().getOut().size());
        assertTrue(nfa_or.getTail().getOut().get("a").contains(nfa1_s1));
        assertTrue(nfa_or.getTail().getOut().get("b").contains(nfa2_s1));

    }

    @Test
    void nfaTransformTest_star() {
        // a
        Nfa nfa = new Nfa("n1", 0);

        AutoState s1 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        s1.addIn("a", null);
        nfa.setTailAlphabet("a");
        nfa.addState(s1, true, true);

        Nfa starNfa = nfa.transform(null, "star");
        starNfa.markFinal("ID", 99999);

        AutoState head = starNfa.getHead();
        AutoState tail = starNfa.getTail();

        // In a star NFA, head and tail should be the same state.
        assertSame(head, tail);

        // Start state should still have the original incoming transition.
        assertTrue(head.getIn().get("a").contains(head));

        // New epsilon entry transition.
        assertTrue(head.getIn().get("epsilon").isEmpty());

        // Loop back on 'a'
        assertTrue(head.getOut().get("a").contains(head));

        // Tail alphabet becomes epsilon.
        assertEquals("epsilon", starNfa.getTailAlphabet());

        // markFinal should mark the head.
        assertTrue(head.getIsFinal());
        assertEquals("ID", head.getToken());
    }

    @Test
    void nfaTransformTest_plus() {
        // a
        Nfa nfa = new Nfa("n1", 0);

        AutoState s1 = new AutoState("", new ArrayList<>(), new ArrayList<>());
        s1.addIn("a", null);
        nfa.setTailAlphabet("a");
        nfa.addState(s1, true, true);

        Nfa plusNfa = nfa.transform(null, "plus");
        plusNfa.markFinal("ID", 99999);

        AutoState tail = plusNfa.getTail();
        AutoState head = plusNfa.getHead();

        // Concatenation should produce two distinct states.
        assertNotSame(tail, head);

        // First state transitions to second on 'a'.
        assertTrue(tail.getOut().get("epsilon").contains(head));

        // Second state loops on 'a' because of the Kleene star.
        assertTrue(head.getOut().get("a").contains(head));

        // Head is final after markFinal().
        assertTrue(head.getIsFinal());
        assertEquals("ID", head.getToken());
    }

}
