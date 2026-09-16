package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstPrimaryExp extends AstExp{

    private AstExp primExp;

    public AstPrimaryExp(Pos pos, AstType ty, AstExp primExp){
        this.pos = pos;
        this.ty = ty;
        this.primExp = primExp;
    }


}

