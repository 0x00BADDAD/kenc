package space.unmei.regex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import space.unmei.lexer.Dfa;
import space.unmei.lexer.Nfa;
import space.unmei.lexer.FinalNfa;
import space.unmei.lexer.LexerException;
import space.unmei.lexer.LexToken;
import space.unmei.lexer.Lexer;
import space.unmei.lexer.AutoState;




public class RegexParserTest{

    void regexParserRunSmallTest(){
        RegexParser parser = new RegexParser();
        boolean parSetup = parser.setup();
        System.out.println(parSetup + "\n");
        assertEquals(parSetup, true);

        parSetup = parser.setup();

        assertEquals(parSetup, true);

        //parser.printPredParseTable();
        FinalNfa finalNfa = new FinalNfa();
        Nfa nfa1 = parser.runParse(new RegexLexer("/").lex());
        Nfa nfa2 = parser.runParse(new RegexLexer("//([ -~\\t]*\\n)|(/\\*[ -~\\n\\t]*\\*/)|([ \\n\\t])*").lex());

        //System.out.println("nfa1 sz: " + nfa1.getNfaStates().size());
        //System.out.println("nfa2 sz: " + nfa2.getNfaStates().size());

        finalNfa.addNfa(nfa1, "DIV", 700);
        finalNfa.addNfa(nfa2, "WHITESPACE", 1000);

        int totStates = finalNfa.getTotalNumStates();
        //System.out.println("finalNfa sz: " + totStates);

        Dfa dfa = finalNfa.makeDfa();
        Map<Integer, Set<AutoState>> states = dfa.getStatesComp();
        //for(Map.Entry<Integer, Set<AutoState>> ent: states.entrySet()){
        //    Integer k = ent.getKey();
        //    Set<AutoState> v = ent.getValue();
        //    System.out.println(String.valueOf(k) + "-- "+ String.valueOf(v.size()));
        //}
        //dfa.printDfaTransTable(); // this only has 2 states?? why?

        Dfa minDfa = dfa.minDfa();
        //minDfa.printDfa();

        Reader reader = new InputStreamReader(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/ws.kl")
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
        //nfa.printNfa();
    }



    @Test
    void regexParserRunTest(){
        RegexParser parser = new RegexParser();
        boolean parSetup = parser.setup();
        System.out.println(parSetup + "\n");
        assertEquals(parSetup, true);

        parSetup = parser.setup();

        assertEquals(parSetup, true);

        FinalNfa finalNfa = new FinalNfa();

        // Reserved words (priority 900)
        finalNfa.addNfa(parser.runParse(new RegexLexer("if").lex()), "IF", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("else").lex()), "ELSE", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("while").lex()), "WHILE", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("elif").lex()), "ELIF", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("for").lex()), "FOR", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("var").lex()), "VAR", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("type").lex()), "TYPE", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("break").lex()), "BREAK", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("continue").lex()), "CONTINUE", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("of").lex()), "OF", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("array").lex()), "ARRAY", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("return").lex()), "RETURN", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("func").lex()), "FUNC", 900);

        // Punctuation (priority 600)
        finalNfa.addNfa(parser.runParse(new RegexLexer("{").lex()), "OPEN_BRACE", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer("}").lex()), "CLOSE_BRACE", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer(":").lex()), "COLON", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer(";").lex()), "SEMI_COLON", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer(".").lex()), "ACCESSOR", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\(").lex()), "OPEN_PAREN", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\)").lex()), "CLOSE_PAREN", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\[").lex()), "OPEN_SQUARE", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\]").lex()), "CLOSE_SQUARE", 600);
        finalNfa.addNfa(parser.runParse(new RegexLexer(",").lex()), "COMMA", 600);

        // Arithmetic operators (priority 700)
        finalNfa.addNfa(parser.runParse(new RegexLexer("=").lex()), "ASSIGN", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\+").lex()), "PLUS", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("-").lex()), "MINUS", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("/").lex()), "DIV", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("\\*").lex()), "MUL", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("%").lex()), "MOD", 700);

        // Logical operators (priority 800 / 900)
        finalNfa.addNfa(parser.runParse(new RegexLexer("==").lex()), "ISEQUAL", 800);
        finalNfa.addNfa(parser.runParse(new RegexLexer("!=").lex()), "NOT_EQUAL", 800);
        finalNfa.addNfa(parser.runParse(new RegexLexer(">").lex()), "GREATER_THAN", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("<").lex()), "LESS_THAN", 700);
        finalNfa.addNfa(parser.runParse(new RegexLexer("!").lex()), "NEGATION", 700);

        finalNfa.addNfa(parser.runParse(new RegexLexer("and").lex()), "AND", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("or").lex()), "OR", 900);
        finalNfa.addNfa(parser.runParse(new RegexLexer("xor").lex()), "XOR", 900);

        // Identifier (priority 300)
        finalNfa.addNfa(parser.runParse(new RegexLexer("[a-z_]([a-z0-9_])*").lex()), "ID", 300);

        // String literal (priority 500)
        finalNfa.addNfa(parser.runParse(new RegexLexer("\"([ -!#-\\[\\]-~]|\\\\[ -~])*\"").lex()), "STRING_LIT", 500);

        // Number (priority 400)
        finalNfa.addNfa(parser.runParse(new RegexLexer("[0-9]+").lex()), "NUM", 400);

        // Whitespace (priority 1000)
        //for(RegexToken tok: new RegexLexer("//\\n").lex()){
        //        System.out.println(tok.getText()+ " " + tok.getType());
        //}

        finalNfa.addNfa(
            parser.runParse(
                new RegexLexer(
                    "//([ -~\\t]*\\n)|(/\\*[ -~\\n\\t]*\\*/)|([ \\n\\t])*"
                ).lex()
            ),
            "WHITESPACE",
            1000
        );


        Dfa dfa = finalNfa.makeDfa();
        Dfa minDfa = dfa.minDfa();
        Reader reader = new InputStreamReader(
            Objects.requireNonNull(
                getClass().getResourceAsStream("/hello.kl")
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

