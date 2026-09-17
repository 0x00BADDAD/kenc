package space.unmei.ast.exps;

import space.unmei.ast.*;
import space.unmei.ast.Pos;

import space.unmei.ast.types.*;

public class AstPrimaryExp extends AstExp{

    private AstExp primExp;

    public AstPrimaryExp(Pos pos, AstType ty, AstExp primExp){
        super(pos, ty);
        this.primExp = primExp;
    }


}

