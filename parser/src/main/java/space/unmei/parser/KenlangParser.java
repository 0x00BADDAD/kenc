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
            "FunDec",
            "Stmts",
            "Stmt",
            "Exp",
            "LogicalOr",
            "LogicalAnd",
            "Disjunction",
            "Conjunction",
            "Equality",
            "Rel",
            "Add",
            "Mul",
            "Unary",
            "Primary",
            "FieldInit",
            "Return",
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
            "ForFirsts",
            "ForFirstList",
            "ForFirst",
            "ForSeconds",
            "ForThirds",
            "ForThirdList",
            "ForThird",
            "FunCall",
            "FunArgs",
            "ArgTail",
            "RecordExpr",
            "ArrayExpr"
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

                    new LexToken("NEW", "new"),

                    new LexToken("OR", "||"),
                    new LexToken("AND", "&&"),

                    new LexToken("ISEQUAL", "=="),
                    new LexToken("NOT_EQUAL", "!="),
                    new LexToken("LESS_THAN", "<"),
                    new LexToken("GREATER_THAN", ">"),

                    new LexToken("CON", "&"),
                    new LexToken("DIS", "|"),

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
                        List.of("Decls", "Decls", "Decl"),
                        (stateStack, symStack)->{
                            stateStack.pop();
                            Pair<AstDecl, GramSymbol<LexToken>> declSym = symStack.pop();

                            stateStack.pop();
                            Pair<AstDecls, GramSymbol<LexToken>> declsSym = symStack.pop();
                            AstDecls decls = declsSym.first();
                            AstDecl decl = declSym.first();
                            decls.addDecl(decl);

                            if(decls.pos == null){
                                decls.pos = decl.pos;
                            }

                            Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(decls, this.getLhs()));
                        }
                        ),
                    new Pair<>(
                            List.of("Decls"),
                            (stateStack, symStack)->{
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(new AstDecls(null, new ArrayList<>()), this.getLhs()));
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
                            List.of("TypeFields", "TypeFields", "COMMA", "ID", "COLON", "TypeVal"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = symStack.pop();
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = symStack.pop();
                                AstTypeFields currTyFields = tyFieldsSym.first();


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
                            List.of("LogicalAnd", "LogicalAnd", "AND", "Disjunction"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = symStack.pop();

                                stateStack.pop();
                                LexToken andTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> andSym = symStack.pop();
                                AstBinopExp binExp = new AstBinopExp(new Pos(andTok), new AstBooleanType(new Pos(andTok)), disSym.first(), eqSym.first(), AstBinOpType.LOGICAL_AND);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("LogicalAnd", "Disjunction"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(disSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Disjunction", "Disjunction", "DIS", "Conjunction"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = symStack.pop();
                                stateStack.pop();
                                LexToken disTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = symStack.pop();
                                // type of the binExp will be decided based upon
                                // types of the 2 input exps.
                                AstType binExpType;
                                if(!(disSym.first().getExpType() instanceof AstNilType)){
                                    // so the first one is non-nil. take this type.
                                    binExpType = disSym.first().getExpType();
                                }else if(!(conSym.first().getExpType() instanceof AstNilType)){
                                    binExpType = disSym.first().getExpType();
                                }else{
                                    binExpType = new AstNilType(new Pos(disTok));
                                }

                                AstBinopExp binExp = new AstBinopExp(new Pos(disTok), binExpType, disSym.first(), eqSym.first(), AstBinOpType.DIS);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Disjunction", "Conjunction"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(conSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Conjunction", "Conjunction", "CON", "Equality"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = symStack.pop();
                                stateStack.pop();
                                LexToken conTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = symStack.pop();
                                // type of the binExp will be decided based upon
                                // types of the 2 input exps.
                                AstType binExpType;
                                if(conSym.first().getExpType() instanceof AstNilType){
                                    // so the first one is non-nil. take this type.
                                    binExpType = new AstNilType(new Pos(conTok));
                                }else if(eqSym.first().getExpType() instanceof AstNilType){
                                    binExpType = new AstNilType(new Pos(conTok));
                                }else{
                                    binExpType = eqSym.first().getExpType();
                                }

                                AstBinopExp binExp = new AstBinopExp(new Pos(disTok), binExpType, disSym.first(), eqSym.first(), AstBinOpType.CON);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Conjunction", "Equality"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(eqSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Equality", "ISEQUAL", "Rel"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = symStack.pop();
                                stateStack.pop();
                                LexToken eqTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(eqTok), new AstBooleanType(new Pos(eqTok)), eqSym.first(), relSym.first(), AstBinOpType.EQUAL);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Equality", "NOT_EQUAL", "Rel"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = symStack.pop();
                                stateStack.pop();
                                LexToken neqTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(neqTok), new AstBooleanType(new Pos(neqTok)), eqSym.first(), relSym.first(), AstBinOpType.NOT_EQUAL);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Rel"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> relSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(relSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "LESS_THAN", "Add"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                stateStack.pop();
                                LexToken ltTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> relSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(ltTok), new AstBooleanType(new Pos(ltTok)), relSym.first(), addSym.first(), AstBinOpType.LESS_THAN);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "GREATER_THAN", "Add"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                stateStack.pop();
                                LexToken gtTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> relSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(gtTok), new AstBooleanType(new Pos(gtTok)), relSym.first(), addSym.first(), AstBinOpType.GREATER_THAN);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "LESS_EQUAL", "Add"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                stateStack.pop();
                                LexToken leTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> relSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(leTok), new AstBooleanType(new Pos(leTok)), relSym.first(), addSym.first(), AstBinOpType.LESS_EQUAL);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "GREATER_EQUAL", "Add"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                stateStack.pop();
                                LexToken geTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> relSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(geTok), new AstBooleanType(new Pos(geTok)), relSym.first(), addSym.first(), AstBinOpType.GREATER_EQUAL);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Add"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(addSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Add", "PLUS", "Mul"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                stateStack.pop();
                                LexToken plusTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(plusTok), new AstInt64Type(new Pos(plusTok)), addSym.first(), mulSym.first(), AstBinOpType.ADD);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Add", "MINUS", "Mul"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                stateStack.pop();
                                LexToken minusTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> addSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(minusTok), new AstInt64Type(new Pos(minusTok)), addSym.first(), mulSym.first(), AstBinOpType.SUBTRACT);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Mul"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(mulSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "MUL", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();

                                stateStack.pop();
                                LexToken mulTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(mulTok), new AstInt64Type(new Pos(mulTok)), addSym.first(), mulSym.first(), AstBinOpType.MULTIPLY);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "DIV", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();

                                stateStack.pop();
                                LexToken divTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(divTok), new AstInt64Type(new Pos(divTok)), addSym.first(), mulSym.first(), AstBinOpType.DIVIDE);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "MOD", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();

                                stateStack.pop();
                                LexToken modTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> mulSym = symStack.pop();

                                AstBinopExp binExp = new AstBinopExp(new Pos(modTok), new AstInt64Type(new Pos(modTok)), addSym.first(), mulSym.first(), AstBinOpType.MODULO);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unarySym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "BOOL_NEGATION", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();
                                stateStack.pop();
                                LexToken bnegTok = symStack.pop().second().getSymbolToken();
                                AstUnaryOpExp unOpExp = new AstUnaryOpExp(new Pos(bnegTok), new AstBooleanType(new Pos(bnegTok)), AstUnaryOpType.BOOL_NEGATION, unarySym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unOpExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "MINUS", "Unary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexTok>> unarySym = symStack.pop();
                                stateStack.pop();
                                LexToken minusTok = symStack.pop().second().getSymbolToken();
                                AstUnaryOpExp unOpExp = new AstUnaryOpExp(new Pos(minusTok), new AstInt64Type(new Pos(minusTok)), AstUnaryOpType.INT_NEGATION, unarySym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unOpExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "Primary"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> primSym = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(primSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "NUM"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken numTok = symStack.pop().second().getSymbolToken();
                                AstNumExp numExp = new AstNumExp(new Pos(numTok), new AstInt64Type(new Pos(numTok)), Integer.valueOf(numTok.getContent()), 64);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(numExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "STRING_LIT"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken strLit = symStack.pop().second().getSymbolToken();

                                AstStringExp strExp = new AstStringExp(new Pos(strLit), new AstStringType(new Pos(strLit)), strLit.getContent());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(strExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "Lvalue"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();


                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(lvalSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "ID", "OPEN_PAREN", "FunArgs", "CLOSE_PAREN"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstFuncallExp, GramSymbol<LexToken>> fnCallSym = symStack.pop();


                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fnCallSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "NIL"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken nilTok = symStack.pop().second().getSymbolToken();

                                AstNilExp nilExp = new AstNilExp(new Pos(nilTok), new AstNilType(new Pos(nilTok)));
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(nilExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "IfExpr"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfExp, GramSymbol<LexToken>> ifSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "IfElseExpr"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfElseExp, GramSymbol<LexToken>> ifElseSym = symStack.pop();

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifElseSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "OPEN_PAREN", "Exp", "CLOSE_PAREN"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> primExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(primExpSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "RecordExpr"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstRecordExp, GramSymbol<LexToken>> recExprSym = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recExprSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "ArrayExpr"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArrayExp, GramSymbol<LexToken>> arrExprSym = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrExprSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "TRUE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken trueTok = symStack.pop().second().getSymbolToken();
                                AstBooleanExp trueExpr = new AstBooleanExp(new Pos(trueTok),new AstBooleanType(new Pos(trueTok)), true);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(trueExpr, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "FALSE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken falseTok = symStack.pop().second().getSymbolToken();
                                AstBooleanExp falseExpr = new AstBooleanExp(new Pos(falseTok),new AstBooleanType(new Pos(falseTok)), false);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(falseExpr, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("RecordExpr", "NEW", "TypeVal", "OPEN_BRACE", "FieldInit", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstFieldInit, GramSymbol<LexToken>> fieldInit = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> recTypeSym = symStack.pop();
                                stateStack.pop();
                                LexToken newTok = symStack.pop().second().getSymbolToken();

                                AstRecordExp recExp = new AstRecordExp(new Pos(newTok), recTypeSym.first(), fieldInit.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("ArrayExpr","NEW", "TypeVal", "OPEN_SQUARE", "Exp", "CLOSE_SQUARE", "OF", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> arrInitVal = symStack.pop();

                                stateStack.pop();
                                LexToken ofTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> arrSz = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> arrTySym = symStack.pop();
                                stateStack.pop();
                                LexToken newTok = symStack.pop().second().getSymbolToken();

                                AstArrayExp arrExp = new AstArrayExp(new Pos(oParen), arrTySym.first(), arrSz.first(), arrInitVal.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrExp, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FieldInit", "ID", "ASSIGN", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                LexToken assgnTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                AstFieldInit fieldInit = new AstFieldInit(new Pos(idTok));
                                fieldInit.addFieldInit(new Symbol(idTok.getContent(), idTok), expSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fieldInit, this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FieldInit", "ID", "ASSIGN", "Exp", "COMMA", "FieldInit"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> fInitSym = symStack.pop();
                                stateStack.pop();
                                LexToken comTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                LexToken assgnTok =  symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();

                                fInitSym.first().addFieldInit(new Symbol(idTok.getContent(), idTok), expSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fInitSym.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmts","Stmts", "Stmt"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstStmt, GramSymbol<LexToken>> stmtSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = symStack.pop();

                                if(stmtsSym.first().pos == null){
                                    stmtsSym.first().pos = stmtSym.first().pos;
                                }

                                stmtsSym.first().addStmt(stmtSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmtsSym.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmts"),
                            (stateStack, symStack)->{
                                AstStmts stmts = new AstStmts(null);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmts, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "Assign", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstAssignStmt, GramSymbol<LexToken>> assgnStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assgnStmt.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "FunCall", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstFuncallStmt, GramSymbol<LexToken>> fnCallStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fnCallStmt.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "IfElseStmt"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfElseStmt, GramSymbol<LexToken>> ieStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieStmt.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "IfStmt"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfStmt, GramSymbol<LexToken>> ifStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifStmt.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "While"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstWhileStmt, GramSymbol<LexToken>> whileStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(whileStmt.first(), this.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "For"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForStmt, GramSymbol<LexToken>> forStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "VarDec", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstVarDecStmt, GramSymbol<LexToken>> varDecStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "VarDecInit", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstVarDecInitStmt, GramSymbol<LexToken>> varDecInitStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecInitStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "TypeDec", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstTypeDecStmt, GramSymbol<LexToken>> typeDecStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(typeDecStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "BreakStmt"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstBreakStmt, GramSymbol<LexToken>> breakStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(breakStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "ContinueStmt"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstContinueStmt, GramSymbol<LexToken>> contStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(contStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "Return"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstReturnStmt, GramSymbol<LexToken>> retStmt = symStack.pop();
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(retStmt.first(), this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Return", "RETURN", "Exp", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                LexToken retTok = symStack.pop().second().getSymbolToken();
                                AstReturnStmt retStmt = new AstReturnStmt(new Pos(retTok), expSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(retStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("BreakStmt", "BREAK", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken breakTok = symStack.pop().second().getSymbolToken();
                                AstBreakStmt breakStmt = new AstBreakStmt(new Pos(breakTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(breakStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ContinueStmt", "CONTINUE", "SEMI_COLON"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken contTok = symStack.pop().second().getSymbolToken();
                                AstContinueStmt contStmt = new AstContinueStmt(new Pos(contTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(contStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunCall", "ID", "OPEN_PAREN", "FunArgs", "CLOSE_PAREN"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken closeParenTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                AstContinueStmt contStmt = new AstContinueStmt(new Pos(contTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(contStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunArgs"),
                            (stateStack, symStack)->{
                                AstFunArgs funargs = new AstFunArgs(null, true, null, null);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funargs, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunArgs", "Exp", "ArgTail"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArgTail, GramSymbol<LexToken>> argTailSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                AstFunArgs funargs = new AstFunArgs(expSym.first().pos, false, expSym.first(), argTailSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funargs, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ArgTail"),
                            (stateStack, symStack)->{
                                AstArgTail argtail = new AstArgTail(null, true, null, null);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(argtail, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ArgTail", "COMMA", "Exp", "ArgTail"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArgTail, GramSymbol<LexToken>> argTailSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();

                                AstArgTail argtail = new AstArgTail(new Pos(commaTok), false, expSym.first(), argTailSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(argtail, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ASSIGN", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ASSIGN);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_DIV", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_DIV);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_SUB", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_SUB);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_MUL", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_MUL);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_MOD", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_MOD);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_PLUS", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();

                                AstAssignStmt assignStmt = new AstAssignStmt(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_PLUS);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "ID"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                // IMP: the type of lvalue for simpleVar must be filled at semantic phase
                                AstLvalueExp lvalExp = new AstVarExp(new Pos(idTok), null, new Symbol(idTok.getContent(), idTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(lvalExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "Lvalue", "ACCESSOR", "ID"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken accessorTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();
                                // ty field of AstAccessExp to be filled by semantic phase
                                AstAccessExp accessExp = new AstAccessExp(new Pos(accessorTok), null, lvalSym.first(), new Symbol(idTok.getContent(), idTok));

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(accessExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "Lvalue", "OPEN_SQUARE", "Exp", "CLOSE_SQUARE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cSquare = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> idxExpSym = symStack.pop();

                                stateStack.pop();
                                LexToken oSquare = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = symStack.pop();
                                // ty field of AstSubExp to be filled by semantic phase
                                AstSubExp subscriptExp = new AstSubExp(new Pos(oSquare), null, lvalSym.first(), idxExpSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(subscriptExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfElseExpr", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE", "ELSE", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> elseExpSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> elseStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken elseTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> thenExpSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();
                                // assuming that type of thenExp and elseExp is same
                                // this is something to be tested at semantic pass
                                AstType resType = thenExpSym.first().getExpType();

                                AstIfElseExp ieExp = new AstIfElseExp(condExpSym.first().pos, resType, thenExpSym.first(), elseExpSym.first(), thenStmtsSym.first(), elseStmtsSym.first());


                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfElseStmt", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE", "ELSE", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> elseStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken elseTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfElseStmt ieStmt = new AstIfElseStmt(condExpSym.first().pos, thenExpSym.first(), thenStmtsSym.first(), elseStmtsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfExpr", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> thenExpSym = symStack.pop();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfExp ifExp = new AstIfExp(condExpSym.first().pos, thenExpSym.first().getExpType(), condExpSym.first(), thenExpSym.first(), thenStmtsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifExp, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfStmt", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfStmt ifStmt = new AstIfStmt(condExpSym.first().pos, condExpSym.first(), thenStmtsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("While", "WHILE", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> whileStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken whileTok = symStack.pop().second().getSymbolToken();

                                AstWhileStmt whileStmt = new AstWhileStmt(condExpSym.first().pos, condExpSym.first(), whileStmtsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(whileStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("For", "FOR", "OPEN_PAREN", "ForFirsts", "SEMI_COLON", "ForSeconds", "SEMI_COLON", "ForThirds", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> forStmtsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForThirds, GramSymbol<LexToken>> forThirdsSym = symStack.pop();
                                stateStack.pop();
                                LexToken semiTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForSeconds, GramSymbol<LexToken>> forSecondsSym = symStack.pop();
                                stateStack.pop();
                                LexToken semiTok1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForFirsts, GramSymbol<LexToken>> forFirstsSym = symStack.pop();
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken forTok = symStack.pop().second().getSymbolToken();

                                AstForStmt forStmt = new AstForStmt(new Pos(forTok), forFirstsSym.first(), forSecondsSym.first(), forThirdsSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forStmt, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirsts"),
                            (stateStack, symStack)->{
                                AstForFirsts forFirsts = new AstForFirsts(null, null);
                                if(symStack.size() > 0){
                                    forFirsts.pos = symStack.peek().first().pos;
                                }
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirsts, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirsts", "ForFirstList"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirstList, GramSymbol<LexToken>> firstListSym = symStack.pop();

                                AstForFirsts forFirsts = new AstForFirsts(firstListSym.first().pos, firstListSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirsts, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirstList", "ForFirst"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirst, GramSymbol<LexToken>> firstSym = symStack.pop();

                                AstForFirstList forFirstList = new AstForFirstList(firstSym.first().pos, firstSym.first(), null);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirstList, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirstList", "ForFirst", "COMMA", "ForFirstList"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirstList, GramSymbol<LexToken>> firstListSym = symStack.pop();
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForFirst, GramSymbol<LexToken>> firstSym = symStack.pop();

                                AstForFirstList forFirstList = new AstForFirstList(firstSym.first().pos, firstSym.first(), firstListSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirstList, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirst", "VarDecInit"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstVarDecInitStmt, GramSymbol<LexToken>> varDecInitSym = symStack.pop();

                                AstForFirst forFirst = new AstForFirst(varDecInitSym.first().pos, varDecInitSym.first(), null);
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirst, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirst", "Assign"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstAssignStmt, GramSymbol<LexToken>> assignSym = symStack.pop();

                                AstForFirst forFirst = new AstForFirst(assignSym.first().pos, null, assignSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirst, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForSeconds"),
                            (stateStack, symStack)->{
                                AstForSeconds forSeconds = new AstForSeconds(null, null);
                                if(symStack.size() > 0){
                                    forSeconds.pos = symStack.peek().first().pos;
                                }
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forSeconds, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForSeconds", "Exp"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = symStack.pop();
                                AstForSeconds forSeconds = new AstForSeconds(expSym.pos, expSym.first());
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forSeconds, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirds"),
                            (stateStack, symStack)->{
                                AstForThirds forThirds = new AstForThirds(null, null);
                                if(symStack.size() > 0){
                                    forThirds.pos = symStack.peek().first().pos;
                                }
                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirds, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirds", "ForThirdList"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThirdList, GramSymbol<LexToken>> thirdListSym = symStack.pop();
                                AstForThirds forThirds = new AstForThirds(thirdListSym.first().pos, thirdListSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirds, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirdList", "ForThird"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThird, GramSymbol<LexToken>> thirdSym = symStack.pop();
                                AstForThirdList forThirdList = new AstForThirdList(thirdSym.first().pos, thirdSym.first(), null);

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirdList, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirdList", "ForThird", "COMMA", "ForThirdList"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThirdList, GramSymbol<LexToken>> thirdListSym = symStack.pop();
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForThird, GramSymbol<LexToken>> thirdSym = symStack.pop();
                                AstForThirdList forThirdList = new AstForThirdList(thirdSym.first().pos, thirdSym.first(), thirdListSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirdList, this.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThird", "Assign"),
                            (stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstAssignStmt, GramSymbol<LexToken>> assignSym = symStack.pop();
                                AstForThird forThird = new AstForThird(assignSym.first().pos, assignSym.first());

                                Action.shift gotoAct = stateStack.peek().getAction(this.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThird, this.getLhs()));
                            }
                            )
                    )
                );
        this.setup_(nonTermSyms, termSyms, prodStrs);
        this.parserSetup = true;
    }

}
