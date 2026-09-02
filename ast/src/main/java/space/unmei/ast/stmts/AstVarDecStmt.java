package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;

public class AstVarDecStmt extends AstStmt{

    private Symbol varName;
    private AstType varTy;

    public AstVarDecStmt(Pos pos, Symbol varName, AstType varTy){
        this.pos = pos;
        this.varName = varName;
        this.varTy = varTy;
    }


}

