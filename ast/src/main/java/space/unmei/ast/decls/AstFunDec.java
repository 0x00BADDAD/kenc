package space.unmei.ast.decls;

import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;

public class AstFunDec extends AstDecl{
    private Symbol funName;
    private List<Pair<Symbol, AstType>> formalSignature;
    private AstType returnType;

    public AstFunDec(Symbol funName, List<Pair<Symbol, AstType>> formalSignature, AstType returnType)
    {
        this.funName = funName;
        this.formalSignature = formalSignature;
        this.returnType = returnType;
    }
}
