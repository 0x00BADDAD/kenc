package space.unmei.regex;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;
import space.unmei.lexer.Nfa;


public class RegexParser extends LLParser<Nfa, RegexToken, RegexTokenType>{

    private String peekChar(){
                        RegexTokenType ch_ty = this.tokens.get(this.currTokIdx).getType();
                        if(ch_ty != RegexTokenType.CHAR){
        throw new IllegalArgumentException("Unexpected token: expecting char token but found: " + ch_ty.name());
                        }
                        String ch = this.tokens.get(this.currTokIdx).getText();
                        return ch;

    }

    private String peekCharset(){
                        RegexTokenType ch_ty = this.tokens.get(this.currTokIdx).getType();
                        if(ch_ty != RegexTokenType.CHARSET){
        throw new IllegalArgumentException("Unexpected token: expecting charset token but found: " + ch_ty.name());
                        }
                        String ch = this.tokens.get(this.currTokIdx).getText();
                        return ch;

    }

    @Override
    public boolean setup(){
        if(this.parserSetup){return this.isParserOk;}
        //Start  → Regex EOF
        //Regex   → Union

        //Union   → Concat Union'

        //Union'  → OR Concat Union'
        //        | ε

        //Concat  → Repeat Concat'

        //Concat' → CONCAT Repeat Concat'
        //        | ε

        //Repeat  → Atom
        //        | Atom STAR
        //        | Atom PLUS
        //        | Atom QUESTION

        //Atom    → CHAR
        //        | CHARSET
        //        | LPAREN Regex RPAREN
        String[] nonTermSyms = new String[]{"Start",
                                            "Regex",
                                            "Union",
                                            "Concat",
                                            "Union\'",
                                            "Repeat",
                                            "Concat\'",
                                            "Atom"};

        List<RegexTokenType> termToks = new ArrayList<>(List.of(RegexTokenType.EOF,
                                                                RegexTokenType.OR,
                                                                RegexTokenType.CONCAT,
                                                                RegexTokenType.STAR,
                                                                RegexTokenType.PLUS,
                                                                RegexTokenType.QUESTION,
                                                                RegexTokenType.CHAR,
                                                                RegexTokenType.CHARSET,
                                                                RegexTokenType.LPAREN,
                                                                RegexTokenType.RPAREN));

        List<Pair<List<String>, Supplier<Nfa>>> prodSupps = new ArrayList<>(List.of(

            new Pair<>(
                List.of("Start", "Regex", "EOF"),
                () -> {
                    //...
                    Nfa regex = this.getProdSupp("Regex").get();
                    RegexToken tok = new RegexToken(RegexTokenType.EOF, "EOF");
                    this.advance(tok);
                    return regex;
                }
                ),
            new Pair<>(
                List.of("Regex", "Union"),
                ()->{
                    return this.getProdSupp("Union").get();
                }
                ),
            new Pair<>(
                List.of("Union", "Concat", "Union\'"),
                ()->{
                    Nfa concat = this.getProdSupp("Concat").get();
                    Nfa union_ = this.getProdSupp("Union\'").get();
                    return concat.transform(union_, "or");
                }
                ),
            new Pair<>(
                    List.of("Union\'", "OR", "Concat", "Union\'"),
                    ()->{
                        this.advance(new RegexToken(RegexTokenType.OR, "|"));
                        Nfa concat = this.getProdSupp("Concat").get();
                        Nfa union_ = this.getProdSupp("Union\'").get();
                        return concat.transform(union_, "or");
                    }
                    ),
            new Pair<>(
                    List.of("Union\'"),
                    ()->{
                        return null;
                    }),
            new Pair<>(
                    List.of("Concat", "Repeat", "Concat\'"),
                    ()->{
                        Nfa repeat = this.getProdSupp("Repeat").get();
                        Nfa concat_ = this.getProdSupp("Concat\'").get();
                        return repeat.transform(concat_, "and");
                    }
                    ),
            new Pair<>(
                    List.of("Concat\'", "CONCAT", "Repeat", "Concat\'"),
                    ()->{
                        this.advance(new RegexToken(RegexTokenType.CONCAT, "."));
                        Nfa repeat = this.getProdSupp("Repeat").get();
                        Nfa concat_ = this.getProdSupp("Concat\'").get();
                        return repeat.transform(concat_, "and");
                    }
                    ),
            new Pair<>(
                    List.of("Concat\'"),
                    ()->{
                        return null;
                    }
                    ),
            new Pair<>(
                    List.of("Repeat", "Atom"),
                    ()->{
                         Nfa atom = this.getProdSupp("Atom").get();

                            switch (this.tokens.get(this.currTokIdx).getType()) {

                                case RegexTokenType.STAR:
                                    this.advance(new RegexToken(RegexTokenType.STAR, "*"));
                                    return atom.transform(null, "star");

                                case RegexTokenType.PLUS:
                                    this.advance(new RegexToken(RegexTokenType.PLUS, "+"));
                                    return atom.transform(null, "plus");

                                case RegexTokenType.QUESTION:
                                    this.advance(new RegexToken(RegexTokenType.QUESTION, "?"));
                                    return atom.transform(null, "question");

                                default:
                                    return atom;
                            }
                    }

                    ),
            new Pair<>(
                    List.of("Atom", "CHAR"),
                    ()->{
                        String ch = this.peekChar();
                        Nfa res = new Nfa(String.valueOf(this.nthRun) + "_Top_NS" + String.valueOf(this.currTokIdx) + "_", 0, ch);
                        this.advance(new RegexToken(RegexTokenType.CHAR, ch));
                        return res;
                    }
                    ),
            new Pair<>(
                    List.of("Atom", "CHARSET"),
                    ()->{
                        String charset = this.peekCharset();
                        if(charset.length() < 1){
                            throw new IllegalArgumentException("Parse Error: The charset is empty!");
                        }
                        String firstChar = String.valueOf(charset.charAt(0));
                        Nfa firstNfa = new Nfa(String.valueOf(this.nthRun) + "_Top_NS" + String.valueOf(this.currTokIdx) + "_", 0, firstChar);
                        for(int i=1; i < charset.length(); i++){
                            String currchar = String.valueOf(charset.charAt(i));
                        Nfa currNfa = new Nfa(String.valueOf(this.nthRun) + "_Top_NS" + String.valueOf(this.currTokIdx) + "_" + String.valueOf(i) + "_", 0, currchar);
                        firstNfa =  firstNfa.transform(currNfa, "or");

                        }
                        this.advance(new RegexToken(RegexTokenType.CHARSET, charset));
                        return firstNfa;
                    }),
            new Pair<>(
                    List.of("Atom", "LPAREN", "Regex", "RPAREN"),
                    ()->{
                        this.advance(new RegexToken(RegexTokenType.LPAREN, "("));
                        Nfa res = this.getProdSupp("Regex").get();
                        this.advance(new RegexToken(RegexTokenType.RPAREN, ")"));
                        return res;

                    }
                    )
                    ));

        this.isParserOk = this.setup_(nonTermSyms, termToks, prodSupps);
        this.parserSetup = true;
        return this.isParserOk;
    }

}
