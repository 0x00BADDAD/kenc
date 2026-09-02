package space.unmei.ast.exps;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstArrayExp extends AstExp{

    private Symbol arrName;
    private AstExp arrSize;
    private AstExp arrInitVal;

    public AstArrayExp(Pos pos, Symbol arrName, AstExp arrSize, AstExp arrInitVal){
        this.pos = pos;
        this.arrName = arrName;
        this.arrSize = arrSize;
        this.arrInitVal = arrInitVal;
    }
}
