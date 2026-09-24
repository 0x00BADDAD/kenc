package space.unmei.parser;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.*;
import space.unmei.lexer.LexToken;
import space.unmei.semant.Symbol;

import space.unmei.ast.*;

import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.exps.record_exp.*;

import space.unmei.ast.nodes.*;

import space.unmei.ast.stmts.*;
import space.unmei.ast.stmts.fors.*;

import space.unmei.ast.types.*;
import space.unmei.ast.types.default_types.*;

import space.unmei.ast.decls.*;

public class KenlangParser extends LR1Parser<AstNode, LexToken>{

    private boolean parserSetup = false;

    @SuppressWarnings("unchecked")
    private <V extends AstNode> Pair<V, GramSymbol<LexToken>> popAst(
            Deque<Pair<AstNode, GramSymbol<LexToken>>> symStack) {

        Pair<AstNode, GramSymbol<LexToken>> pair = symStack.pop();

        return new Pair<>(
            (V) pair.first(),
            pair.second()
        );
    }

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
            "Post",
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

        Map<LexToken, Boolean> syncToks = new HashMap<>();

        syncToks.put(new LexToken("EOF", "$"), true);
        syncToks.put(new LexToken("SEMI_COLON", ";"), true);
        syncToks.put(new LexToken("CLOSE_BRACE", "}"), true);
        syncToks.put(new LexToken("CLOSE_SQUARE", "]"), true);
        syncToks.put(new LexToken("CLOSE_PAREN", ")"), true);


        List<LexToken> termSyms = new ArrayList<>(
                List.of(
                    new LexToken("EOF", "$"),

                    new LexToken("SEMI_COLON", ";"),

                    new LexToken("VAR", "var"),
                    new LexToken("FUNC", "func"),
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
                    new LexToken("LESS_EQUAL", "<="),
                    new LexToken("GREATER_THAN", ">"),
                    new LexToken("GREATER_EQUAL", ">="),

                    new LexToken("CON", "&"),
                    new LexToken("DIS", "|"),

                    new LexToken("PLUS", "+"),
                    new LexToken("INC_PLUS", "++"),
                    new LexToken("MINUS", "-"),
                    new LexToken("DEC_MINUS", "--"),
                    new LexToken("MUL", "*"),
                    new LexToken("DIV", "/"),
                    new LexToken("MOD", "%"),

                    new LexToken("BOOL_NEGATION", "!"),

                    new LexToken("NUM", ""),
                    new LexToken("STRING_LIT", ""),

                    new LexToken("NIL", "nil"),
                    new LexToken("TRUE", "true"),
                    new LexToken("FALSE", "false"),

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

        List<Pair<List<String>, ReduceAction<AstNode, LexToken>>> prodStrs =  new ArrayList<>(
                List.of(
                    new Pair<>(
                        List.of("Start", "Prog", "EOF"),
                        (prod, stateStack, symStack) -> {
                            // we assume the stack are already lined up
                            // with rhs syms of this prod
                            stateStack.pop();
                            symStack.pop(); // op EOF

                            // pop Prog and insert Start
                            LR1State<?,?> progState = stateStack.pop();
                            Pair<AstProg, GramSymbol<LexToken>> progSym = this.popAst(symStack);
                            AstStart start = new AstStart(progSym.first().pos, progSym.first());

                            Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(start, prod.getLhs()));
                        }
                        ),
                    new Pair<>(
                        List.of("Prog", "Decls"),
                        (prod, stateStack, symStack)->{
                            // pop Decls
                            stateStack.pop();
                            Pair<AstDecls, GramSymbol<LexToken>> declsSym = this.popAst(symStack);
                            AstProg prog = new AstProg(declsSym.first().pos, declsSym.first());

                            Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(prog, prod.getLhs()));
                        }
                        ),
                    new Pair<>(
                        List.of("Decls", "Decls", "Decl"),
                        (prod, stateStack, symStack)->{
                            System.out.println("a Decl has been parsed!");
                            stateStack.pop();
                            Pair<AstDecl, GramSymbol<LexToken>> declSym = this.popAst(symStack);

                            stateStack.pop();
                            Pair<AstDecls, GramSymbol<LexToken>> declsSym = this.popAst(symStack);

                            AstDecls decls = declsSym.first();
                            AstDecl decl = declSym.first();
                            decls.addDecl(decl);

                            if(decls.pos == null){
                                decls.pos = decl.pos;
                            }

                            Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                            stateStack.push(gotoAct.state());
                            symStack.push(new Pair<>(decls, prod.getLhs()));
                        }
                        ),
                    new Pair<>(
                            List.of("Decls"),
                            (prod, stateStack, symStack)->{
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(new AstDecls(null, new ArrayList<>()), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "VarDec", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                // pop SEMI_COLON
                                stateStack.pop();
                                symStack.pop();

                                // pop VarDec
                                stateStack.pop();
                                Pair<AstVarDec, GramSymbol<LexToken>> varDecSym = this.popAst(symStack);
                                AstVarDecl newDecl = new AstVarDecl(varDecSym.first().pos, varDecSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "VarDecInit", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                symStack.pop();

                                stateStack.pop();
                                Pair<AstVarDecInit, GramSymbol<LexToken>> varDecInitSym = this.popAst(symStack);
                                AstVarDecInitDecl newDecl = new AstVarDecInitDecl(varDecInitSym.first().pos, varDecInitSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "TypeDec", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeDec, GramSymbol<LexToken>> typeDecSym = this.popAst(symStack);
                                AstTypeDecl newDecl = new AstTypeDecl(typeDecSym.first().pos, typeDecSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Decl", "FunDec"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstFunDec, GramSymbol<LexToken>> funDecSym = this.popAst(symStack);
                                AstFunDecl newDecl = new AstFunDecl(funDecSym.first().pos, funDecSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(newDecl, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("VarDecInit", "VAR", "ID", "ASSIGN", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                symStack.pop(); // pop ASSIGN

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSymTok = symStack.pop();
                                LexToken idTok = idSymTok.second().getSymbolToken();
                                Symbol idSymbol = new Symbol(idTok.getContent(), idTok);

                                stateStack.pop();
                                symStack.pop();

                                AstVarDecInit varDecInit = new AstVarDecInit(new Pos(idTok), idSymbol, null, expSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecInit, prod.getLhs()));

                                        }
                                        ),
                    new Pair<>(
                            List.of("VarDecInit", "VAR", "ID", "COLON","TypeVal", "ASSIGN" ,"Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                symStack.pop(); // pop ASSIGN

                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> typeValSym = this.popAst(symStack);

                                stateStack.pop(); symStack.pop(); // pop COLON

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSymTok = symStack.pop();
                                LexToken idTok = idSymTok.second().getSymbolToken();
                                Symbol idSymbol = new Symbol(idTok.getContent(), idTok);

                                stateStack.pop();
                                symStack.pop();

                                AstVarDecInit varDecInit = new AstVarDecInit(new Pos(idTok), idSymbol, typeValSym.first(), expSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDecInit, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("VarDec", "VAR", "ID"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idTokSym = symStack.pop();

                                LexToken idTok = idTokSym.second().getSymbolToken();
                                Symbol idSym = new Symbol(idTok.getContent(), idTok);
                                AstVarDec varDec = new AstVarDec(new Pos(idTok), idSym, null);
                                stateStack.pop();
                                symStack.pop(); // pop VAR

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDec, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("VarDec", "VAR", "ID", "COLON", "TypeVal"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = this.popAst(symStack);
                                AstType tyVal = tyValSym.first();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                Symbol idSymbol = new Symbol(idSym.second().getSymbolToken().getContent(), idSym.second().getSymbolToken());

                                stateStack.pop(); symStack.pop();

                                AstVarDec varDec = new AstVarDec(new Pos(idSym.second().getSymbolToken()), idSymbol, tyVal);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(varDec, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeDec", "TYPE", "ID", "ASSIGN", "TypeVal"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                AstType tyVal = (AstType) symStack.pop().first();

                                stateStack.pop(); symStack.pop(); // pop ASSIGN

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop(); // pop TYPE

                                AstTypeDec tyDec = new AstTypeDec(new Pos(idTok), new Symbol(idTok.getContent(), idTok), tyVal);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(tyDec, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "ID"),
                            (prod, stateStack, symStack) -> {
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();

                                AstNameType tyVal = new AstNameType(new Pos(idTok), new Symbol(idTok.getContent(), idTok));

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(tyVal, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "OPEN_BRACE", "TypeFields", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = this.popAst(symStack);
                                stateStack.pop(); LexToken opBraceTok = symStack.pop().second().getSymbolToken();

                                AstRecordType recordType = new AstRecordType(new Pos(opBraceTok), tyFieldsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recordType, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeVal", "ARRAY", "OF", "TypeVal"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = this.popAst(symStack);
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                LexToken arrTok = symStack.pop().second().getSymbolToken();
                                AstArrayType arrType = new AstArrayType(new Pos(arrTok), tyValSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrType, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeFields", "TypeFields", "COMMA", "ID", "COLON", "TypeVal"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = this.popAst(symStack);
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = this.popAst(symStack);
                                AstTypeFields currTyFields = tyFieldsSym.first();


                                LexToken idTok = idSym.second().getSymbolToken();

                                currTyFields.addTyField(new Symbol(idTok.getContent(), idTok), tyValSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(currTyFields, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("TypeFields", "ID", "COLON", "TypeVal"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyValSym = this.popAst(symStack);
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken idTok = idSym.second().getSymbolToken();
                                AstTypeFields astTyFields = new AstTypeFields(new Pos(idTok), new ArrayList<>());
                                astTyFields.addTyField(new Symbol(idTok.getContent(), idTok), tyValSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(astTyFields, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FunDec", "FUNC", "ID", "OPEN_PAREN", "TypeFields", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                // function with no return type
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = this.popAst(symStack);
                                stateStack.pop(); symStack.pop();

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = this.popAst(symStack);

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken funIdTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop(); // pop FUNC

                                AstFunDec funDec = new AstFunDec(new Pos(funIdTok), new Symbol(funIdTok.getContent(), funIdTok), stmtsSym.first(), tyFieldsSym.first(), null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());

                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funDec, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FunDec", "FUNC", "ID", "OPEN_PAREN", "TypeFields", "CLOSE_PAREN", "COLON", "TypeVal", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = this.popAst(symStack);
                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> tyVal = this.popAst(symStack);
                                stateStack.pop(); symStack.pop(); // pop COLON

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstTypeFields, GramSymbol<LexToken>> tyFieldsSym = this.popAst(symStack);

                                stateStack.pop(); symStack.pop();

                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> idSym = symStack.pop();
                                LexToken funIdTok = idSym.second().getSymbolToken();

                                stateStack.pop(); symStack.pop();

                                AstFunDec funDec = new AstFunDec(new Pos(funIdTok), new Symbol(funIdTok.getContent(), funIdTok), stmtsSym.first(), tyFieldsSym.first(), tyVal.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funDec, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Exp", "LogicalOr"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logicalOrSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(logicalOrSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("LogicalOr", "LogicalOr", "OR", "LogicalAnd"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logAndSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken orTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logOrSym = this.popAst(symStack);
                                AstBinopExp binOpExp = new AstBinopExp(new Pos(orTok), new AstBooleanType(new Pos(orTok)), logOrSym.first(), logAndSym.first(), AstBinOpType.LOGICAL_OR);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binOpExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("LogicalOr", "LogicalAnd"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> logAndSym = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(logAndSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("LogicalAnd", "LogicalAnd", "AND", "Disjunction"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken andTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> andSym = this.popAst(symStack);
                                AstBinopExp binExp = new AstBinopExp(new Pos(andTok), new AstBooleanType(new Pos(andTok)), disSym.first(), andSym.first(), AstBinOpType.LOGICAL_AND);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("LogicalAnd", "Disjunction"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(disSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Disjunction", "Disjunction", "DIS", "Conjunction"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken disTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> disSym = this.popAst(symStack);
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

                                AstBinopExp binExp = new AstBinopExp(new Pos(disTok), binExpType, disSym.first(), conSym.first(), AstBinOpType.DIS);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Disjunction", "Conjunction"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(conSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Conjunction", "Conjunction", "CON", "Equality"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken conTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> conSym = this.popAst(symStack);
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

                                AstBinopExp binExp = new AstBinopExp(new Pos(conTok), binExpType, conSym.first(), eqSym.first(), AstBinOpType.CON);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Conjunction", "Equality"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(eqSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Equality", "ISEQUAL", "Rel"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken eqTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(eqTok), new AstBooleanType(new Pos(eqTok)), eqSym.first(), relSym.first(), AstBinOpType.EQUAL);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Equality", "NOT_EQUAL", "Rel"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken neqTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> eqSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(neqTok), new AstBooleanType(new Pos(neqTok)), eqSym.first(), relSym.first(), AstBinOpType.NOT_EQUAL);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Equality", "Rel"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(relSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "LESS_THAN", "Add"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken ltTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(ltTok), new AstBooleanType(new Pos(ltTok)), relSym.first(), addSym.first(), AstBinOpType.LESS_THAN);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "GREATER_THAN", "Add"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken gtTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(gtTok), new AstBooleanType(new Pos(gtTok)), relSym.first(), addSym.first(), AstBinOpType.GREATER_THAN);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "LESS_EQUAL", "Add"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken leTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(leTok), new AstBooleanType(new Pos(leTok)), relSym.first(), addSym.first(), AstBinOpType.LESS_EQUAL);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Rel", "GREATER_EQUAL", "Add"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken geTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> relSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(geTok), new AstBooleanType(new Pos(geTok)), relSym.first(), addSym.first(), AstBinOpType.GREATER_EQUAL);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Rel", "Add"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(addSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Add", "PLUS", "Mul"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken plusTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(plusTok), new AstInt64Type(new Pos(plusTok)), addSym.first(), mulSym.first(), AstBinOpType.ADD);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Add", "MINUS", "Mul"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken minusTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> addSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(minusTok), new AstInt64Type(new Pos(minusTok)), addSym.first(), mulSym.first(), AstBinOpType.SUBTRACT);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Add", "Mul"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(mulSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "MUL", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken mulTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(mulTok), new AstInt64Type(new Pos(mulTok)), mulSym.first(), unarySym.first(), AstBinOpType.MULTIPLY);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "DIV", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken divTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(divTok), new AstInt64Type(new Pos(divTok)), mulSym.first(), unarySym.first(), AstBinOpType.DIVIDE);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Mul", "MOD", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken modTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> mulSym = this.popAst(symStack);

                                AstBinopExp binExp = new AstBinopExp(new Pos(modTok), new AstInt64Type(new Pos(modTok)), mulSym.first(), unarySym.first(), AstBinOpType.MODULO);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(binExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Mul", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unarySym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "BOOL_NEGATION", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken bnegTok = symStack.pop().second().getSymbolToken();
                                AstUnaryOpExp unOpExp = new AstUnaryOpExp(new Pos(bnegTok), new AstBooleanType(new Pos(bnegTok)), AstUnaryOpType.BOOL_NEGATION, unarySym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unOpExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "MINUS", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> unarySym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken minusTok = symStack.pop().second().getSymbolToken();
                                AstUnaryOpExp unOpExp = new AstUnaryOpExp(new Pos(minusTok), new AstInt64Type(new Pos(minusTok)), AstUnaryOpType.INT_NEGATION, unarySym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(unOpExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Unary", "Primary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> primSym = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(primSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "NUM"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken numTok = symStack.pop().second().getSymbolToken();
                                AstNumExp numExp = new AstNumExp(new Pos(numTok), new AstInt64Type(new Pos(numTok)), Integer.valueOf(numTok.getContent()), 64);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(numExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "STRING_LIT"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken strLit = symStack.pop().second().getSymbolToken();

                                AstStringExp strExp = new AstStringExp(new Pos(strLit), new AstStringType(new Pos(strLit)), strLit.getContent());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(strExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "Lvalue"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);


                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(lvalSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "ID", "OPEN_PAREN", "FunArgs", "CLOSE_PAREN"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken closeParenTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstFunArgs, GramSymbol<LexToken>> funArgsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken openParenTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();

                                // return type of the funcall exp has to be set by the
                                // sematic phase. for now its null
                                AstFuncallExp fnCallExp = new AstFuncallExp(new Pos(idTok), null, new Symbol(idTok.getContent(), idTok),funArgsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fnCallExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "NIL"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken nilTok = symStack.pop().second().getSymbolToken();

                                AstNilExp nilExp = new AstNilExp(new Pos(nilTok), new AstNilType(new Pos(nilTok)));
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(nilExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "IfExpr"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfExp, GramSymbol<LexToken>> ifSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "IfElseExpr"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfElseExp, GramSymbol<LexToken>> ifElseSym = this.popAst(symStack);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifElseSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "OPEN_PAREN", "Exp", "CLOSE_PAREN"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> primExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(primExpSym.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "RecordExpr"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstRecordExp, GramSymbol<LexToken>> recExprSym = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recExprSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "ArrayExpr"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArrayExp, GramSymbol<LexToken>> arrExprSym = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrExprSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "TRUE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken trueTok = symStack.pop().second().getSymbolToken();
                                AstBooleanExp trueExpr = new AstBooleanExp(new Pos(trueTok),new AstBooleanType(new Pos(trueTok)), true);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(trueExpr, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Primary", "FALSE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken falseTok = symStack.pop().second().getSymbolToken();
                                AstBooleanExp falseExpr = new AstBooleanExp(new Pos(falseTok),new AstBooleanType(new Pos(falseTok)), false);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(falseExpr, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("RecordExpr", "NEW", "TypeVal", "OPEN_BRACE", "FieldInit", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstFieldInit, GramSymbol<LexToken>> fieldInit = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> recTypeSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken newTok = symStack.pop().second().getSymbolToken();

                                AstRecordExp recExp = new AstRecordExp(new Pos(newTok), recTypeSym.first(), fieldInit.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(recExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("ArrayExpr","NEW", "TypeVal", "OPEN_SQUARE", "Exp", "CLOSE_SQUARE", "OF", "Unary"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstUnaryOpExp, GramSymbol<LexToken>> arrInitVal = this.popAst(symStack);

                                stateStack.pop();
                                LexToken ofTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> arrSz = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstType, GramSymbol<LexToken>> arrTySym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken newTok = symStack.pop().second().getSymbolToken();

                                AstArrayExp arrExp = new AstArrayExp(new Pos(oParen),(AstArrayType) arrTySym.first(), arrSz.first(), arrInitVal.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(arrExp, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FieldInit", "ID", "ASSIGN", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken assgnTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                AstFieldInit fieldInit = new AstFieldInit(new Pos(idTok));
                                fieldInit.addFieldInit(new Symbol(idTok.getContent(), idTok), expSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fieldInit, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("FieldInit", "ID", "ASSIGN", "Exp", "COMMA", "FieldInit"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstNode, GramSymbol<LexToken>> fInitSym = symStack.pop();
                                stateStack.pop();
                                LexToken comTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken assgnTok =  symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                AstFieldInit fInitList = (AstFieldInit) fInitSym.first();

                                fInitList.addFieldInit(new Symbol(idTok.getContent(), idTok), expSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fInitList, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmts","Stmts", "Stmt"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstStmt, GramSymbol<LexToken>> stmtSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> stmtsSym = this.popAst(symStack);

                                if(stmtsSym.first().pos == null){
                                    stmtsSym.first().pos = stmtSym.first().pos;
                                }

                                stmtsSym.first().addStmt(stmtSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmtsSym.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmts"),
                            (prod, stateStack, symStack)->{
                                AstStmts stmts = new AstStmts(null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmts, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "Assign", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstAssign, GramSymbol<LexToken>> assgnStmtSym = this.popAst(symStack);
                                AstAssignStmt assignStmt = new AstAssignStmt(assgnStmtSym.first().pos, assgnStmtSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "FunCall", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstFuncallStmt, GramSymbol<LexToken>> fnCallStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fnCallStmt.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "IfElseStmt"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfElseStmt, GramSymbol<LexToken>> ieStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieStmt.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "IfStmt"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstIfStmt, GramSymbol<LexToken>> ifStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifStmt.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "While"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstWhileStmt, GramSymbol<LexToken>> whileStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(whileStmt.first(), prod.getLhs()));

                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "For"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForStmt, GramSymbol<LexToken>> forStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forStmt.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "VarDec", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstVarDec, GramSymbol<LexToken>> varDecSym = this.popAst(symStack);
                                AstVarDecStmt stmt = new AstVarDecStmt(varDecSym.first().pos, varDecSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "VarDecInit", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstVarDecInit, GramSymbol<LexToken>> varDecInitSym = this.popAst(symStack);

                                AstVarDecInitStmt stmt = new AstVarDecInitStmt(varDecInitSym.first().pos, varDecInitSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "TypeDec", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstTypeDec, GramSymbol<LexToken>> typeDecSym = this.popAst(symStack);
                                AstTypeDecStmt stmt = new AstTypeDecStmt(typeDecSym.first().pos, typeDecSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "BreakStmt"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstBreakStmt, GramSymbol<LexToken>> breakStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(breakStmt.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "ContinueStmt"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstContinueStmt, GramSymbol<LexToken>> contStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(contStmt.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "Return"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstReturnStmt, GramSymbol<LexToken>> retStmt = this.popAst(symStack);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(retStmt.first(), prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Stmt", "Post", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstPost, GramSymbol<LexToken>> postSym = this.popAst(symStack);
                                AstPostStmt stmt = new AstPostStmt(postSym.first().pos, postSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(stmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Post", "Lvalue", "INC_PLUS"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken incPlusTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);
                                AstPost post = new AstPost(lvalSym.first().pos, lvalSym.first(), AstPostOpType.INC_PLUS);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(post, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Post", "Lvalue", "DEC_MINUS"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken decMinusTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);
                                AstPost post = new AstPost(lvalSym.first().pos, lvalSym.first(), AstPostOpType.DEC_MINUS);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(post, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Return", "RETURN", "Exp", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken retTok = symStack.pop().second().getSymbolToken();
                                AstReturnStmt retStmt = new AstReturnStmt(new Pos(retTok), expSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(retStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("BreakStmt", "BREAK", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken breakTok = symStack.pop().second().getSymbolToken();
                                AstBreakStmt breakStmt = new AstBreakStmt(new Pos(breakTok));

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(breakStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ContinueStmt", "CONTINUE", "SEMI_COLON"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken semiColTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken contTok = symStack.pop().second().getSymbolToken();
                                AstContinueStmt contStmt = new AstContinueStmt(new Pos(contTok));

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(contStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunCall", "ID", "OPEN_PAREN", "FunArgs", "CLOSE_PAREN"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken closeParenTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstFunArgs, GramSymbol<LexToken>> funArgsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken openParenTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                AstFuncallStmt fnCallStmt = new AstFuncallStmt(new Pos(idTok), new Symbol(idTok.getContent(), idTok), funArgsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(fnCallStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunArgs"),
                            (prod, stateStack, symStack)->{
                                AstFunArgs funargs = new AstFunArgs(null, true, null, null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funargs, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("FunArgs", "Exp", "ArgTail"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArgTail, GramSymbol<LexToken>> argTailSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                AstFunArgs funargs = new AstFunArgs(expSym.first().pos, false, expSym.first(), argTailSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(funargs, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ArgTail"),
                            (prod, stateStack, symStack)->{
                                AstArgTail argtail = new AstArgTail(null, true, null, null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(argtail, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ArgTail", "COMMA", "Exp", "ArgTail"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstArgTail, GramSymbol<LexToken>> argTailSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();

                                AstArgTail argtail = new AstArgTail(new Pos(commaTok), false, expSym.first(), argTailSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(argtail, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ASSIGN", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignNode = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ASSIGN);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignNode, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_DIV", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignStmt = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_DIV);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_SUB", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignStmt = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_SUB);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_MUL", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignStmt = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_MUL);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_MOD", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignStmt = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_MOD);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Assign", "Lvalue", "ACC_PLUS", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken assignTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);

                                AstAssign assignStmt = new AstAssign(new Pos(assignTok), lvalSym.first(), expSym.first(), AstAssignOpType.ACC_PLUS);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(assignStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "ID"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                // IMP: the type of lvalue for simpleVar must be filled at semantic phase
                                AstLvalueExp lvalExp = new AstVarExp(new Pos(idTok), null, new Symbol(idTok.getContent(), idTok));

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(lvalExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "Lvalue", "ACCESSOR", "ID"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken idTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken accessorTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);
                                // ty field of AstAccessExp to be filled by semantic phase
                                AstAccessExp accessExp = new AstAccessExp(new Pos(accessorTok), null, lvalSym.first(), new Symbol(idTok.getContent(), idTok));

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(accessExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("Lvalue", "Lvalue", "OPEN_SQUARE", "Exp", "CLOSE_SQUARE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cSquare = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> idxExpSym = this.popAst(symStack);

                                stateStack.pop();
                                LexToken oSquare = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstLvalueExp, GramSymbol<LexToken>> lvalSym = this.popAst(symStack);
                                // ty field of AstSubExp to be filled by semantic phase
                                AstSubExp subscriptExp = new AstSubExp(new Pos(oSquare), null, lvalSym.first(), idxExpSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(subscriptExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfElseExpr", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE", "ELSE", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> elseExpSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> elseStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken elseTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> thenExpSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();
                                // assuming that type of thenExp and elseExp is same
                                // this is something to be tested at semantic pass
                                AstType resType = thenExpSym.first().getExpType();

                                AstIfElseExp ieExp = new AstIfElseExp(condExpSym.first().pos, resType, condExpSym.first(), thenExpSym.first(), elseExpSym.first(), thenStmtsSym.first(), elseStmtsSym.first());


                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfElseStmt", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE", "ELSE", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> elseStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken elseTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfElseStmt ieStmt = new AstIfElseStmt(condExpSym.first().pos, condExpSym.first(), thenStmtsSym.first(), elseStmtsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ieStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfExpr", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "Exp", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();

                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> thenExpSym = this.popAst(symStack);
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfExp ifExp = new AstIfExp(condExpSym.first().pos, thenExpSym.first().getExpType(), condExpSym.first(), thenExpSym.first(), thenStmtsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifExp, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("IfStmt", "IF", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> thenStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken ifTok = symStack.pop().second().getSymbolToken();

                                AstIfStmt ifStmt = new AstIfStmt(condExpSym.first().pos, condExpSym.first(), thenStmtsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(ifStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("While", "WHILE", "OPEN_PAREN", "Exp", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> whileStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> condExpSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken whileTok = symStack.pop().second().getSymbolToken();

                                AstWhileStmt whileStmt = new AstWhileStmt(condExpSym.first().pos, condExpSym.first(), whileStmtsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(whileStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("For", "FOR", "OPEN_PAREN", "ForFirsts", "SEMI_COLON", "ForSeconds", "SEMI_COLON", "ForThirds", "CLOSE_PAREN", "OPEN_BRACE", "Stmts", "CLOSE_BRACE"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                LexToken cBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstStmts, GramSymbol<LexToken>> forStmtsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oBrace = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken cParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForThirds, GramSymbol<LexToken>> forThirdsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken semiTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForSeconds, GramSymbol<LexToken>> forSecondsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken semiTok1 = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForFirsts, GramSymbol<LexToken>> forFirstsSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken oParen = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                LexToken forTok = symStack.pop().second().getSymbolToken();

                                AstForStmt forStmt = new AstForStmt(new Pos(forTok), forFirstsSym.first(), forSecondsSym.first(), forThirdsSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forStmt, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirsts"),
                            (prod, stateStack, symStack)->{
                                AstForFirsts forFirsts = new AstForFirsts(null, null);
                                if(symStack.size() > 0){
                                    forFirsts.pos = symStack.peek().first().pos;
                                }
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirsts, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirsts", "ForFirstList"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirstList, GramSymbol<LexToken>> firstListSym = this.popAst(symStack);

                                AstForFirsts forFirsts = new AstForFirsts(firstListSym.first().pos, firstListSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirsts, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirstList", "ForFirst"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirst, GramSymbol<LexToken>> firstSym = this.popAst(symStack);

                                AstForFirstList forFirstList = new AstForFirstList(firstSym.first().pos, firstSym.first(), null);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirstList, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirstList", "ForFirst", "COMMA", "ForFirstList"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForFirstList, GramSymbol<LexToken>> firstListSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForFirst, GramSymbol<LexToken>> firstSym = this.popAst(symStack);

                                AstForFirstList forFirstList = new AstForFirstList(firstSym.first().pos, firstSym.first(), firstListSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirstList, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirst", "VarDecInit"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstVarDecInit, GramSymbol<LexToken>> varDecInitSym = this.popAst(symStack);

                                AstForFirst forFirst = new AstForFirst(varDecInitSym.first().pos, varDecInitSym.first(), null);
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirst, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForFirst", "Assign"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstAssign, GramSymbol<LexToken>> assignSym = this.popAst(symStack);

                                AstForFirst forFirst = new AstForFirst(assignSym.first().pos, null, assignSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forFirst, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForSeconds"),
                            (prod, stateStack, symStack)->{
                                AstForSeconds forSeconds = new AstForSeconds(null, null);
                                if(symStack.size() > 0){
                                    forSeconds.pos = symStack.peek().first().pos;
                                }
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forSeconds, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForSeconds", "Exp"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstExp, GramSymbol<LexToken>> expSym = this.popAst(symStack);
                                AstForSeconds forSeconds = new AstForSeconds(expSym.first().pos, expSym.first());
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forSeconds, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirds"),
                            (prod, stateStack, symStack)->{
                                AstForThirds forThirds = new AstForThirds(null, null);
                                if(symStack.size() > 0){
                                    forThirds.pos = symStack.peek().first().pos;
                                }
                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirds, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirds", "ForThirdList"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThirdList, GramSymbol<LexToken>> thirdListSym = this.popAst(symStack);
                                AstForThirds forThirds = new AstForThirds(thirdListSym.first().pos, thirdListSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirds, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirdList", "ForThird"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThird, GramSymbol<LexToken>> thirdSym = this.popAst(symStack);
                                AstForThirdList forThirdList = new AstForThirdList(thirdSym.first().pos, thirdSym.first(), null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirdList, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThirdList", "ForThird", "COMMA", "ForThirdList"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstForThirdList, GramSymbol<LexToken>> thirdListSym = this.popAst(symStack);
                                stateStack.pop();
                                LexToken commaTok = symStack.pop().second().getSymbolToken();
                                stateStack.pop();
                                Pair<AstForThird, GramSymbol<LexToken>> thirdSym = this.popAst(symStack);
                                AstForThirdList forThirdList = new AstForThirdList(thirdSym.first().pos, thirdSym.first(), thirdListSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThirdList, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThird", "Assign"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstAssign, GramSymbol<LexToken>> assignSym = this.popAst(symStack);
                                AstForThird forThird = new AstForThird(assignSym.first().pos, assignSym.first(), null);

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThird, prod.getLhs()));
                            }
                            ),
                    new Pair<>(
                            List.of("ForThird", "Post"),
                            (prod, stateStack, symStack)->{
                                stateStack.pop();
                                Pair<AstPost, GramSymbol<LexToken>> postSym = this.popAst(symStack);
                                AstForThird forThird = new AstForThird(postSym.first().pos, null, postSym.first());

                                Action.Shift gotoAct = (Action.Shift) stateStack.peek().getAction(prod.getLhs());
                                stateStack.push(gotoAct.state());
                                symStack.push(new Pair<>(forThird, prod.getLhs()));
                            }
                            )

                    )
                );

        GramSymbol<LexToken> extraEof = new GramSymbol<>(false, null);
        extraEof.setSymbolToken(new LexToken("EEOF", ""));

        this.setup_(nonTermSyms, termSyms, prodStrs, extraEof, syncToks);
        this.parserSetup = true;
    }

}
