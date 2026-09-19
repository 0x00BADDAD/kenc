package space.unmei.parser;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Reader;

import space.unmei.regex.*;


import space.unmei.lexer.FinalNfa;
import space.unmei.lexer.Dfa;
import space.unmei.lexer.Lexer;
import space.unmei.lexer.LexToken;
import space.unmei.lexer.LexerException;

import space.unmei.ast.*;

import static org.junit.jupiter.api.Assertions.*;


public class KenlangParserTest{

    @Test
    void lexerAndParserFullRun(){

        //RegexParser parser = new RegexParser();
        //boolean parSetup = parser.setup();
        //System.out.println(parSetup + "\n");
        //assertEquals(parSetup, true);

        //parSetup = parser.setup();

        //assertEquals(parSetup, true);

        //FinalNfa finalNfa = new FinalNfa();

        //// Reserved words (priority 900)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("if").lex()), "IF", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("else").lex()), "ELSE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("while").lex()), "WHILE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("elif").lex()), "ELIF", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("for").lex()), "FOR", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("var").lex()), "VAR", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("type").lex()), "TYPE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("break").lex()), "BREAK", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("continue").lex()), "CONTINUE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("of").lex()), "OF", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("array").lex()), "ARRAY", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("return").lex()), "RETURN", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("func").lex()), "FUNC", 900);

        //finalNfa.addNfa(parser.runParse(new RegexLexer("true").lex()), "TRUE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("false").lex()), "FALSE", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("nil").lex()), "NIL", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("new").lex()), "NEW", 900);

        //// Punctuation (priority 600)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("{").lex()), "OPEN_BRACE", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("}").lex()), "CLOSE_BRACE", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer(":").lex()), "COLON", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer(";").lex()), "SEMI_COLON", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer(".").lex()), "ACCESSOR", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\(").lex()), "OPEN_PAREN", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\)").lex()), "CLOSE_PAREN", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\[").lex()), "OPEN_SQUARE", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\]").lex()), "CLOSE_SQUARE", 600);
        //finalNfa.addNfa(parser.runParse(new RegexLexer(",").lex()), "COMMA", 600);

        //// Arithmetic operators (priority 700)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("=").lex()), "ASSIGN", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\+").lex()), "PLUS", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("-").lex()), "MINUS", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("/").lex()), "DIV", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\*").lex()), "MUL", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("%").lex()), "MOD", 700);

        //// Logical operators (priority 800 / 900)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("==").lex()), "ISEQUAL", 800);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("!=").lex()), "NOT_EQUAL", 800);
        //finalNfa.addNfa(parser.runParse(new RegexLexer(">").lex()), "GREATER_THAN", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("<").lex()), "LESS_THAN", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("!").lex()), "NEGATION", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\+=").lex()), "ACC_PLUS", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("-=").lex()), "ACC_SUB", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("/=").lex()), "ACC_DIV", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\*=").lex()), "ACC_MUL", 700);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("%=").lex()), "ACC_MOD", 700);

        //finalNfa.addNfa(parser.runParse(new RegexLexer("&&").lex()), "AND", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\|\\|").lex()), "OR", 900);

        //finalNfa.addNfa(parser.runParse(new RegexLexer("&").lex()), "CON", 900);
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\\|").lex()), "DIS", 900);

        //// Identifier (priority 300)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("[a-z_]([a-z0-9_])*").lex()), "ID", 300);

        //// String literal (priority 500)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("\"([ -!#-\\[\\]-~]|\\\\[ -~])*\"").lex()), "STRING_LIT", 500);

        //// Number (priority 400)
        //finalNfa.addNfa(parser.runParse(new RegexLexer("[0-9]+").lex()), "NUM", 400);

        //// Whitespace (priority 1000)
        ////for(RegexToken tok: new RegexLexer("//\\n").lex()){
        ////        System.out.println(tok.getText()+ " " + tok.getType());
        ////}

        //finalNfa.addNfa(
        //    parser.runParse(
        //        new RegexLexer(
        //            "//([ -~\\t]*\\n)|(/\\*[ -~\\n\\t]*\\*/)|([ \\n\\t])*"
        //        ).lex()
        //    ),
        //    "WHITESPACE",
        //    1000
        //);


        //Dfa dfa = finalNfa.makeDfa();
        //Dfa minDfa = dfa.minDfa();
        //Reader reader = new InputStreamReader(
        //    Objects.requireNonNull(
        //        getClass().getResourceAsStream("/small.kl")
        //    )
        //);

        //Lexer lexer = new Lexer(reader);
        //List<LexToken> toks = new ArrayList<>();
        //try{
        //    toks = lexer.lexComplete(minDfa);
        //} catch (IOException | LexerException e) {
        //    System.err.println(e.getMessage());

        //    if (e instanceof LexerException le) {
        //        System.err.println("Line: " + le.getLine());
        //        System.err.println("Column: " + le.getColumn());
        //    }
        //}
        // lexing done successfully!


        // LexToken("VAR", "var");
        LexToken tok1 = new LexToken("VAR", "var");
        tok1.setLineNo(1);
        tok1.setColNo(1);

        LexToken tok2 = new LexToken("ID", "foo");
        tok2.setLineNo(1);
        tok2.setColNo(5);

        LexToken tok3 = new LexToken("ASSIGN", "=");
        tok3.setLineNo(1);
        tok3.setColNo(6);

        LexToken tok4 = new LexToken("NUM", "3");
        tok4.setLineNo(1);
        tok4.setColNo(8);

        //LexToken tok5 = new LexToken("SEMI_COLON", ";");
        //tok5.setLineNo(1);
        //tok5.setColNo(9);

        LexToken tok11 = new LexToken("EOF", "$");
        tok11.setLineNo(1);
        tok11.setColNo(11);


        // string lit variable
        //LexToken tok6 = new LexToken("VAR", "var");
        //tok6.setLineNo(1);
        //tok6.setColNo(1);

        //LexToken tok7 = new LexToken("ID", "my_name");
        //tok7.setLineNo(1);
        //tok7.setColNo(5);

        //LexToken tok8 = new LexToken("ASSIGN", "=");
        //tok8.setLineNo(1);
        //tok8.setColNo(6);

        //LexToken tok9 = new LexToken("STRING_LIT", "\"Hello World\"");
        //tok9.setLineNo(1);
        //tok9.setColNo(8);

        //LexToken tok10 = new LexToken("SEMI_COLON", ";");
        //tok10.setLineNo(1);
        //tok10.setColNo(9);

        //LexToken tok11 = new LexToken("EOF", "$");
        //tok11.setLineNo(1);
        //tok11.setColNo(11);

        // temp tok array
        List<LexToken> toks = new ArrayList<>(List.of(tok1, tok2, tok3, tok4, tok11));

        KenlangParser lr1parser = new KenlangParser();
        lr1parser.setup();
        lr1parser.setTokens(toks);

        Pair<AstNode, List<ParseErr<LexToken>>> ast_res = lr1parser.parse();

        if(ast_res.second().size() > 0){
            System.out.println("Error while parsing AST!!");
        }else{
            System.out.println("Ast has been parsed!!");
            // print the ast
            if(ast_res.first() == null){
                System.out.println("No ast found!");
            }
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            System.out.println("---------------------------------------------------");
            (ast_res.first()).printNode();
        }

    }


}


