package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstNilExp extends AstExp{


    public AstNilExp(Pos pos, AstType ty){
        this.pos = pos;
        this.ty = ty;
    }


}

