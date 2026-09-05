package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstIfElseExp extends AstExp{

    private AstExp cond;
    private AstExp then;
    private AstExp esle;

    public AstIfElseExp(Pos pos, AstType ty, AstExp cond, AstExp then, AstExp eles){
        this.ty = ty;
        this.pos = pos;
        this.cond = cond;
        this.then = then;
        this.esle = esle;
    }


}
