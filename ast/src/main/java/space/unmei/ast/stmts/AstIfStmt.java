package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.*;

public class AstIfStmt extends AstStmt{

    private AstExp cond;
    private AstStmts thenStmts;

    public AstIfStmt(Pos pos, AstExp cond, AstStmts thenStmts){
        super(pos);
        this.cond = cond;
        this.thenStmts = thenStmts;
    }


}

