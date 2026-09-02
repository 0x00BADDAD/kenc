package space.unmei.ast.exps;

import space.unmei.ast.Pos;


public class AstNumExp extends AstExp{

    private Integer numVal;
    private Integer width;

    public AstNumExp(Pos pos, Integer numVal, Integer width){
        this.pos = pos;
        this.numVal = numVal;
        this.width = width;
    }

}


