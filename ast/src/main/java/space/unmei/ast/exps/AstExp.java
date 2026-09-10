package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstExp extends AstNode{

    private AstType ty;

    public AstExp(Pos pos, AstType ty){this.pos = pos; this.ty = ty;}

    public void setExpType(AstType t){
        this.ty = t;
    }

    public AstType getExpType(){
        return this.ty;
    }

}
