package space.unmei.ast.exps.lvalue;

import space.unmei.ast.Pos;

public class AstSubExp{

    private Pos pos;
    private AstLvalue target;
    private AstExp idxExp;

    public AstSubExp(Pos pos, AstLvalue target, AstExp idxExp){
        this.pos = pos;
        this.target = target;
        this.idxExp = idxExp;
    }
}
