package space.unmei.lexer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

public class LexerTest{

    @Test
    void lexerHelloTestRun(){


    }

    @Test
    void lexerFileRunTest(){

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


        Dfa dfa = finNfa.makeDfa();
        Dfa minDfa =  dfa.minDfa();

        Reader reader = new InputStreamReader(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/lexInpfile_01.txt")
            )
        );
        Lexer lexer = new Lexer(reader);
        List<LexToken> toks = new ArrayList<>();
        try{
            toks = lexer.lexComplete(minDfa);
        } catch (IOException | LexerException e) {
            System.err.println(e.getMessage());

            if (e instanceof LexerException le) {
                System.err.println("Line: " + le.getLine());
                System.err.println("Column: " + le.getColumn());
            }
        }
        for(LexToken t: toks){
            System.out.println("----New Token----");
            System.out.println("Name: " + t.getName());
            System.out.println("Content: " + t.getContent());
            System.out.println("-----------------");
            System.out.println("-----------------");
        }
    }

}
