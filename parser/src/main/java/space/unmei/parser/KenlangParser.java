package space.unmei.parser;

public class KenlangParser extends LR1Parser<AstNode, LexToken>{

    private boolean parserSetup = false;

    public KenlangParser(){}

    public void setup(){
        (this.parserSetup){
            return;
        }
        String[] nonTermSyms = new String[]{
            "Start",
            "Prog",
            "Decls",
            "Decl",
            "VarDecInit",
            "VarDec",
            "TypeDec",
            "TypeVal",
            "TypeFields",
            "TypeFields\'",
            "FunDec",
            "Return",
            "Stmts",
            "Stmt",
            "Exp",
            "LogicalOr",
            "LogicalAnd",
            "Equality",
            "Rel",
            "Add",
            "Mul",
            "Unary",
            "Primary",
            "FieldInit",
            "FieldTail",
            "BreakStmt",
            "ContinueStmt",
            "Assign",
            "Lvalue",
            "IfElseExpr",
            "IfElseStmt",
            "IfExpr",
            "IfStmt",
            "For",
            "While",
            "ForFirst",
            "ForSecond",
            "ForThird",
            "FunCall",
            "Args",
            "ArgTail"
        };

        List<LexToken> termSyms = new ArrayList<>(
                List.of(
                    new LexToken("EOF", "$"),

                    new LexToken("SEMI_COLON", ";"),

                    new LexToken("VAR", "var"),
                    new LexToken("ID", ""),
                    new LexToken("ASSIGN", "="),

                    new LexToken("TYPE", "type"),

                    new LexToken("OPEN_BRACE", "{"),
                    new LexToken("CLOSE_BRACE", "}"),

                    new LexToken("ARRAY", "array"),
                    new LexToken("OF", "of"),

                    new LexToken("COLON", ":"),

                    new LexToken("OPEN_PAREN", "("),
                    new LexToken("CLOSE_PAREN", ")"),

                    new LexToken("RETURN", "return"),

                    new LexToken("OR", "or"),
                    new LexToken("AND", "and"),

                    new LexToken("ISEQUAL", "=="),
                    new LexToken("NOT_EQUAL", "!="),
                    new LexToken("LESS_THAN", "<"),
                    new LexToken("GREATER_THAN", ">"),

                    new LexToken("PLUS", "+"),
                    new LexToken("MINUS", "-"),
                    new LexToken("MUL", "*"),
                    new LexToken("DIV", "/"),
                    new LexToken("MOD", "%"),

                    new LexToken("NEGATION", "!"),

                    new LexToken("NUM", ""),
                    new LexToken("STRING_LIT", ""),

                    new LexToken("NIL", "nil"),

                    new LexToken("COMMA", ","),

                    new LexToken("BREAK", "break"),
                    new LexToken("CONTINUE", "continue"),

                    new LexToken("ACC_DIV", "/="),
                    new LexToken("ACC_SUB", "-="),
                    new LexToken("ACC_MUL", "*="),
                    new LexToken("ACC_MOD", "%="),
                    new LexToken("ACC_PLUS", "+="),

                    new LexToken("ACCESSOR", "."),

                    new LexToken("OPEN_SQUARE", "["),
                    new LexToken("CLOSE_SQUARE", "]"),

                    new LexToken("IF", "if"),
                    new LexToken("ELSE", "else"),
                    new LexToken("WHILE", "while"),
                    new LexToken("FOR", "for")
                )
        );
        List<Pair<List<String>, Supplier<T>>> prodStrs =  new ArrayList<>(
                List.of(
                    new Pair<>(
                        List.of("Start", "Prog", "EOF"),
                        () -> {
                            
                        }
                        ),
                    new Pair<>(),
                    new Pair<>(),
                    new Pair<>(),
                    new Pair<>(),
                    new Pair<>(),
                    new Pair<>()
                    )
                );
        this.parserSetup = true;
    }

}
