package space.unmei.ast.exps;

import space.unmei.ast.*;
import space.unmei.ast.Pos;

import space.unmei.ast.types.*;
public class AstStringExp extends AstExp{

    private String strLiteral;

    public AstStringExp(Pos pos, AstType ty, String strLiteral){
        super(pos, ty);
        this.strLiteral = strLiteral;
    }
}

