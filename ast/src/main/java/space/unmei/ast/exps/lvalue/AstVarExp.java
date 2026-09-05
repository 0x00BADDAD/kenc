package space.unmei.ast.exps.lavlue;

import space.unmei.ast.Pos;

public class AstVarExp extends AstLvalueExp{

    private Symbol varName;

    public AstVarExp(Pos pos, AstType ty, Symbol varName){
        this.ty = ty;
        this.pos = pos;
        this.varName = varName;
    }
}
