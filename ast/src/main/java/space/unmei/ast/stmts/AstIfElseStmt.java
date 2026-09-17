package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.*;

public class AstIfElseStmt extends AstStmt{

    private AstExp cond;
    private AstStmts thenStmts;
    private AstStmts elseStmts;

    public AstIfElseStmt(Pos pos, AstExp cond, AstStmts thenStmts, AstStmts elseStmts){
        super(pos);
        this.cond = cond;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }


}
