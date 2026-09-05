package space.unmei.parser;

import space.unmei.ast.*;

public class KenlangParser extends LR1Parser<AstNode, LexToken>{

    private boolean parserSetup = false;

    public KenlangParser(){}

    @Override
    public void setup(){
        if(this.parserSetup){
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
        List<Pair<List<String>, BiConsumer<Deque<LR1State<AstNode, LexToken>>, Deque<Pair<AstNode, GramSymbol<LexToken>>>>> prodStrs =  new ArrayList<>(
                List.of(
                    new Pair<>(
                        List.of("Start", "Prog", "EOF"),
                        (stateStack, symStack) -> {
                            // we assume the stack are already lined up
                            // with rhs syms of this prod
                            stateStack.pop();
                            symStack.pop(); // op EOF

                            // pop Prog amd insert Start
                            LR1State<?,?> progState = stateStack.pop();
                            Pair<AstNode, GramSymbol<LexToken>> progSym = symStack.pop();

                            Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(progSym.first(), this.getLhs()));
                        }
                        ),
                    new Pair<>(
                        List.of("Prog", "Decls"),
                        (stateStack, symStack)->{
                            // pop Decls
                            stateStack.pop();
                            Pair<AstNode, GramSymbol<LexToken>> progSym = symStack.pop();

                            Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(progSym.first(), this.getLhs()));
                        }
                        ),
                    new Pair<>(
                        List.of("Decls", "Decl", "Decls"),
                        (stateStack, symStack)->{
                            stateStack.pop();
                            Pair<AstNode, GramSymbol<LexToken>> declsSym = symStack.pop();
                            stateStack.pop();
                            Pair<AstNode, GramSymbol<LexToken>> declSym = symStack.pop();
                            AstProg prog = declsSym.first();
                            AstDecl decl = declSym.first();
                            prog.addDecl(decl);
                            prog.pos = decl.pos;

                            Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(prog, this.getLhs()));
                        }
                        ),
                    new Pair<>(
                            List.of("Decls"),
                            (stateStack, symStack)->{
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(new AstProg(null, new ArrayList<>()), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "VarDec", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                // pop SEMI_COLON
                                stateStack();
                                symStack.pop();

                                // pop VarDec
                                statStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> varDecSym = symStack.pop();
                                AstDecl newDecl = new AstDecl(varDecSym.first().pos, varDecSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "VarDecInit", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> varDecInitSym = symStack.pop();
                                AstDecl newDecl = new AstDecl(varDecInitSym.first().pos, varDecInitSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "TypeDec", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> typeDecSym = symStack.pop();
                                AstDecl newDecl = new AstDecl(typeDecSym.first().pos, typeDecSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "FunDec"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> funDecSym = symStack.pop();
                                AstDecl newDecl = new AstDecl(funDecSym.first().pos, funDecSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("VarDecInit", "VAR", "ID", "ASSIGN", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                symStack.pop(); // pop ASSIGN

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSymTok = symStack.pop();
                                LexToken idTok = idSymTok.second().getSymbolToken();
                                Symbol idSymbol = new Symbol(idTok.getContent(), idTok);

                                stateStack.pop();
                                symStack.pop();

                                AstVarDecInit varDecInit = new AstVarDecInit(new Pos(idTok), idSymbol, null, expSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecInit, this.getLhs()));

                                        }
                                        ),
                    new Pair<>(
                            List.of("VarDecInit", "VAR", "ID", "COLON","TypeVal", "ASSIGN" ,"Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                symStack.pop(); // pop ASSIGN

                                statStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> typeValSym = symStack.pop();

                                stateStack.pop(); symStack.pop(); // pop COLON

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSymTok = symStack.pop();
                                LexToken idTok = idSymTok.second().getSymbolToken();
                                Symbol idSymbol = new Symbol(idTok.getContent(), idTok);

                                stateStack.pop();
                                symStack.pop();

                                AstVarDecInit varDecInit = new AstVarDecInit(new Pos(idTok), idSymbol, typeValSym.first(), expSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecInit, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("VarDec", "VAR", "ID"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idTokSym = symStack.pop();

                                LexToken idTok = idTokSym.second().getSymbolToken();
                                Symbol idSym = new Symbol(idTok.getContent(), idTok);
                                AstVarDec varDec = new AstVarDec(new Pos(idTok), idSym, null);
                                stateStack.pop();
                                symStack.pop(); // pop VAR

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDec, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("VarDec", "VAR", "ID", "COLON", "TypeVal"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> tyValSym = symStack.pop();
                                AstType tyVal = tyValSym.first();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                Symbol idSymbol = new Symbol(idSym.second().getSymbolToken().getContent(), idSym.second().getSymbolToken());

                                stateStack.pop(); symStack.pop();

                                AstVarDec varDec = new AstVarDec(new Pos(idSym.second().getSymbolToken()), idSymbol, tyVal);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDec, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeDec", "TYPE", "ID", "ASSIGN", "TypeVal"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                AstType tyVal = symStack.pop().first();

                                stateStack.pop(); symStack.pop(); // pop ASSIGN

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop(); // pop TYPE

                                AstTypeDec tyDec = new AstTypeDec(new Pos(idTok), new Symbol(idTok.getContent(), idTok), tyVal);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(tyDec, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "ID"),
                            (stateStack, symStack) -> {
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();

                                AstNameType tyVal = new AstNameType(new Pos(idTok), new Symbol(idTok.getContent(), idTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(tyVal, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "OPEN_BRACE", "TypeFields", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = symStack.pop();
                                stateStack.pop(); LexToken opBraceTok = symStack.pop().second().getSymbolToken();

                                AstRecordType recordType = new AstRecordType(new Pos(opBraceTok), tyFieldsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recordType, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "ARRAY", "OF", "TypeVal"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                LexToken arrTok = symStack.pop().second().getSymbolToken();
                                AstArrayType arrType = new AstArrayType(new Pos(arrTok), tyValSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrType, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeFields", "ID", "COLON", "TypeVal", "COMMA", "TypeFields"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = symStack.pop();
                                AstTypeFields currTyFields = tyFieldsSym.first();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();

                                currTyFields.addTyField(new Symbol(idTok.getContent(), idTok), tyValSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(currTyFields, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeFields", "ID", "COLON", "TypeVal"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexTok idTok = idSym.second().getSymbolToken();
                                AstTypeFields astTyFields = new AstTypeFields(new Pos(idTok), new ArrayList<>());
                                astTyFields.addTyField(new Symbol(idTok.getContent(), idTok), tyValSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(astTyFields, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FunDec", "FUNC", "ID", "OPEN_PAREN", "TypeFields", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                // function with no return type
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken funIdTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop(); // pop FUNC

                                AstFunDec funDec = new AstFunDec(new Pos(funIdTok), new Symbol(funIdTok.getContent(), funIdTok), tyFieldsSym.first(), null);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funDec, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FunDec", "FUNC", "ID", "OPEN_PAREN", "TypeFields", "CLOSE_PAREN", "COLON", "TypeVal", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyVal = symStack.pop();
                                stateStack.pop(); symStack.pop(); // pop COLON

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken funIdTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop();

                                AstFunDec funDec = new AstFunDec(new Pos(funIdTok), new Symbol(funIdTok.getContent(), funIdTok), tyFieldsSym.first(), tyVal.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funDec, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Exp", "LogicalOr"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logicalOrSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(logicalOrSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("LogicalOr", "LogicalOr", "OR", "LogicalAnd"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logAndSym = symStack.pop();
                                stateStack.pop();
                                LexToken orTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logOrSym = symStack.pop();
                                AstBinopExp binOpExp = new AstBinopExp(new Pos(orTok), new AstBooleanType(new Pos(orTok)), logOrSym.first(), logAndSym.first(), AstBinOpType.LOGICAL_OR);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binOpExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("LogicalOr", "LogicalAnd"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logAndSym = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(logAndSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            ),
                    new Pair<>(
                            List.of(),
                            (stateStack, symStack)->{

                            }
                            )
                    )
                );
        this.setup_(nonTermSyms, termSyms, prodStrs);
        this.parserSetup = true;
    }

}
