package space.unmei.ast.exps.lvalue;

import space.unmei.ast.Pos;

public class AstAccessExp extends AstLvalueExp{

    private AstLvalue target;
    private Symbol fieldName;

    public AstAccessExp(Pos pos, AstType ty, AstLvalue target, Symbol fieldName){
        this.ty = ty;
        this.pos = pos;
        this.target = target;
        this.fieldName = fieldName;
    }
}
