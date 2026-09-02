package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstStringExp extends AstExp{

    private String strLiteral;

    public AstStringExp(Pos pos, String strLiteral){

        this.pos = pos;
        this.strLiteral = strLiteral;
    }
}

