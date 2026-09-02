package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;

public class AstWhileStmt extends AstStmt{

    private AstExp cond;
    private List<AstStmt> stmts;

    public AstWhileStmt(Pos pos, AstExp cond, List<AstStmt> stmts){
        this.pos = pos;
        this.cond = cond;
        this.stmts = stmts;
    }


}

