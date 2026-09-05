package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstStringExp extends AstExp{

    private String strLiteral;

    public AstStringExp(Pos pos, AstType ty, String strLiteral){
        this.ty = ty;
        this.pos = pos;
        this.strLiteral = strLiteral;
    }
}

