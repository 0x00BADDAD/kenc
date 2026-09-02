package space.unmei.ast.exps.lavlue;

import space.unmei.ast.Pos;

public class AstVarExp{

    private Pos pos;
    private Symbol varName;

    public AstVarExp(Pos pos, Symbol varName){
        this.pos = pos;
        this.varName = varName;
    }
}
