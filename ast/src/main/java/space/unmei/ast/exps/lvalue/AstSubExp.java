package space.unmei.ast.exps.lvalue;

import space.unmei.ast.types.*;
import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.*;

public class AstSubExp extends AstLvalueExp{

    private AstLvalueExp target;
    private AstExp idxExp;

    public AstSubExp(Pos pos, AstType ty, AstLvalueExp target, AstExp idxExp){
        super(pos, ty);
        this.target = target;
        this.idxExp = idxExp;
    }
}
