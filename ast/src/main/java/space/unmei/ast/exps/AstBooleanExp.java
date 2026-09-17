package space.unmei.ast.exps;

import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;

public class AstBooleanExp extends AstExp{

    private boolean val;

    public AstBooleanExp(Pos pos, AstType ty, boolean val){
        super(pos, ty);
        this.val = val;
    }

}


