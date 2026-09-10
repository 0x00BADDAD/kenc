package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstBooleanExp extends AstExp{

    private boolean val;

    public AstBooleanExp(Pos pos, AstType ty, boolean val){
        this.pos = pos;
        this.ty = ty;
        this.val = val;
    }

}


