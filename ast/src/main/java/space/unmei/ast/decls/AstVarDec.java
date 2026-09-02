package space.unmei.ast.decls;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstVarDec extends AstDecl{

    private Pos pos;
    private Symbol varName;
    private AstType tyVal;

    public AstVarDec(Pos pos, Symbol varName, AstType tyVal) {
        this.pos = pos;
        this.varName = varName;
        this.tyVal = tyVal;
    }
}

