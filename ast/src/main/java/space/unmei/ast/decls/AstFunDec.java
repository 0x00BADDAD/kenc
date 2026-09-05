package space.unmei.ast.decls;

import space.unmei.ast.Pos;
import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;

public class AstFunDec extends AstDecl{

    private Symbol funName;
    private AstTypeFields formalSignature;
    private AstType returnType;

    public AstFunDec(Pos pos, Symbol funName, AstTypeFields formalSignature, AstType returnType)
    {
        this.pos = pos;
        this.funName = funName;
        this.formalSignature = formalSignature;
        this.returnType = returnType;
    }
}
