package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.ast.types.*;
import space.unmei.semant.Symbol;

public class AstFunDec extends AstNode{

    private Symbol funName;
    private AstTypeFields formalSignature;
    private AstType returnType;

    public AstFunDec(Pos pos, Symbol funName, AstTypeFields formalSignature, AstType returnType)
    {
        super(pos);
        this.funName = funName;
        this.formalSignature = formalSignature;
        this.returnType = returnType;
    }
}

