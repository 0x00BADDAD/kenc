package space.unmei.ast.exps.lvalue;

public class AstLvalueExp extends AstExp{

    public AstLvalueExp(Pos pos, AstType ty){
        this.pos = pos;
        this.ty = ty;
    }
}
