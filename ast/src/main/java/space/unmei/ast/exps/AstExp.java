package space.unmei.ast.exps;

import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;

public class AstExp extends AstNode{

    protected AstType ty;

    public AstExp(Pos pos, AstType ty){super(pos); this.ty = ty;}

    public void setExpType(AstType t){
        this.ty = t;
    }

    public AstType getExpType(){
        return this.ty;
    }

}
