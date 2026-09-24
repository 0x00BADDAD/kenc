package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.ast.types.*;
import space.unmei.semant.Symbol;
import space.unmei.ast.stmts.*;

public class AstFunDec extends AstNode{

    private Symbol funName;
    private AstStmts stmts;
    private AstTypeFields formalSignature;
    private AstType returnType;

    public AstFunDec(Pos pos, Symbol funName, AstStmts stmts, AstTypeFields formalSignature, AstType returnType)
    {
        super(pos);
        this.funName = funName;
        this.stmts = stmts;
        this.formalSignature = formalSignature;
        this.returnType = returnType;
    }
}

