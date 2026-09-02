package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;

public class AstIfElseStmt extends AstStmt{

    private AstExp cond;
    private List<AstStmt> thenStmts;
    private List<AstStmt> elseStmts;

    public AstIfElseStmt(Pos pos, AstExp cond, List<AstStmt> thenStmts, List<AstStmt> elseStmts){
        this.pos = pos;
        this.cond = cond;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }


}
