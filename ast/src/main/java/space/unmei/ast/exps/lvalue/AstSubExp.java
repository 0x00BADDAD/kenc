package space.unmei.ast.exps.lvalue;

import space.unmei.ast.Pos;

public class AstSubExp extends AstLvalueExp{

    private AstLvalue target;
    private AstExp idxExp;

    public AstSubExp(Pos pos, AstType ty, AstLvalue target, AstExp idxExp){
        this.ty = ty;
        this.pos = pos;
        this.target = target;
        this.idxExp = idxExp;
    }
}
