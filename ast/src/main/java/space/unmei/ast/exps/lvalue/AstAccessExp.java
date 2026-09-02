package space.unmei.ast.exps.lvalue;

import space.unmei.ast.Pos;

public class AstAccessExp{

    private Pos pos;
    private AstLvalue  target;
    private Symbol fieldName;

    public AstAccessExp(Pos pos, AstLvalue target, Symbol fieldName){
        this.pos = pos;
        this.target = target;
        this.fieldName = fieldName;
    }
}
