package space.unmei.ast.exps;


import space.unmei.ast.Pos;

public class AstIfExp extends AstExp{

    private AstExp cond;
    private AstExp thenExp;
    private AstStmts thenStmts;

    public AstIfElseExp(Pos pos, AstType ty, AstExp cond, AstExp thenExp, AstStmts thenStmts){
        this.ty = ty;
        this.pos = pos;
        this.cond = cond;
        this.thenExp = thenExp;
        this.thenStmts = thenStmts;
    }


}

