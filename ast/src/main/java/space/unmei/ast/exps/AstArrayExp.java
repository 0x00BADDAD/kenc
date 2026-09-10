package space.unmei.ast.exps;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstArrayExp extends AstExp{

    private AstExp arrSize;
    private AstExp arrInitVal;

    public AstArrayExp(Pos pos, AstArrayType ty, AstExp arrSize, AstExp arrInitVal){
        this.ty = ty;
        this.pos = pos;
        this.arrSize = arrSize;
        this.arrInitVal = arrInitVal;
    }
}
