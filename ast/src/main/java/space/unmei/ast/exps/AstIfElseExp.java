package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstIfElseExp extends AstExp{

    private AstExp cond;
    private AstExp tExp;
    private AstStmts thenStmts;
    private AstExp eExp;
    private AstStmts elseStmts;

    public AstIfElseExp(Pos pos, AstType ty, AstExp cond, AstExp tExp, AstExp eExp, AstStmts thenStmts, AstSmts elseStmts){
        this.ty   = ty;
        this.pos  = pos;
        this.cond = cond;
        this.tExp = tExp;
        this.eExp = eExp;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }


}
