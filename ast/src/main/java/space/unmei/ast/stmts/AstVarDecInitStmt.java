package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

public class AstVarDecInitStmt extends AstStmt{

    private AstVarDecInit varDecInit;

    public AstVarDecInitStmt(Pos pos, AstVarDecInit varDecInit){
        super(pos);
        this.varDecInit = varDecInit;
    }
}

