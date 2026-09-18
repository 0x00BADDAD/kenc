package space.unmei.ast.exps;

import space.unmei.ast.*;
import space.unmei.ast.types.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstArrayExp extends AstExp{

    private AstExp arrSize;
    private AstUnaryOpExp arrInitVal;

    public AstArrayExp(Pos pos, AstArrayType ty, AstExp arrSize, AstUnaryOpExp arrInitVal){
        super(pos, ty);
        this.arrSize = arrSize;
        this.arrInitVal = arrInitVal;
    }
}
