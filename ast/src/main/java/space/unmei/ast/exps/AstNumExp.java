package space.unmei.ast.exps;

import space.unmei.ast.types.*;
import space.unmei.ast.Pos;
import space.unmei.ast.*;


public class AstNumExp extends AstExp{

    private Integer numVal;
    private Integer width; // bit-width of integer

    public AstNumExp(Pos pos, AstType ty, Integer numVal, Integer width){
        super(pos, ty);
        this.numVal = numVal;
        this.width = width;
    }
}


