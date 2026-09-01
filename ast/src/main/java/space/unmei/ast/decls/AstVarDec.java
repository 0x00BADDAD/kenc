package space.unmei.ast.decls;

import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstVarDec extends AstDecl{

    private Symbol varName;
    private AstType tyVal;

    public AstVarDec(Symbol varName, AstType tyVal) {
        this.varName = varName;
        this.tyVal = tyVal;
    }
}

