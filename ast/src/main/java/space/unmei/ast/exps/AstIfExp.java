package space.unmei.ast.exps;


import space.unmei.ast.Pos;

public class AstIfExp extends AstExp{

    private AstExp cond;
    private AstExp then;

    public AstIfElseExp(Pos pos, AstType ty, AstExp cond, AstExp then){
        this.ty = ty;
        this.pos = pos;
        this.cond = cond;
        this.then = then;
    }


}

