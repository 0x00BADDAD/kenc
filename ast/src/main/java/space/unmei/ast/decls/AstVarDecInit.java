package space.unmei.ast.decls;

import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.Pos;

public class AstVarDecInit extends AstDecl{

    private Pos pos;
    private Symbol varName;
    private AstType tyVal;
    private AstExp expVal;

    public AstVarDec(Pos pos, Symbol varName, AstType tyVal, AstExp expVal) {
        this.pos = pos;
        this.varName = varName;
        this.tyVal = tyVal;
        this.expVal = expVal;
    }
}


