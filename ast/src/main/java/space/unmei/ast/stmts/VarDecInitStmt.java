package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;

public class AstVarDecInitStmt extends AstStmt{

    private Symbol varName;
    private AstType varTy;
    private AstExp initVal;

    public AstVarDecInitStmt(Pos pos, Symbol varName, AstType varTy, AstExp initVal){
        this.pos = pos;
        this.varName = varName;
        this.varTy = varTy;
        this.initVal = initVal;
    }


}

