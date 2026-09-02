package space.unmei.ast.exps;


import space.unmei.ast.Pos;

public class AstIfExp extends AstExp{

    private AstExp cond;
    private AstExp then;

    public AstIfElseExp(Pos pos, AstExp cond, AstExp then){
        this.pos = pos;
        this.cond = cond;
        this.then = then;
    }


}

