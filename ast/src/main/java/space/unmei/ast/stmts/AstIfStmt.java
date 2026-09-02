package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;

public class AstIfStmt extends AstStmt{

    private AstExp cond;
    private List<AstStmt> thenStmts;

    public AstIfElseStmt(Pos pos, AstExp cond, List<AstStmt> thenStmts){
        this.pos = pos;
        this.cond = cond;
        this.thenStmts = thenStmts;
    }


}

