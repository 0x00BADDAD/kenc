package space.unmei.ast.decls;

import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;
import space.unmei.ast.exps.AstExp;

public class AstVarDecInit extends AstDecl{

    private Symbol varName;
    private AstType tyVal;
    private AstExp expVal;

    public AstVarDec(Symbol varName, AstType tyVal, AstExp expVal) {
        this.varName = varName;
        this.tyVal = tyVal;
        this.expVal = expVal;
    }
}


