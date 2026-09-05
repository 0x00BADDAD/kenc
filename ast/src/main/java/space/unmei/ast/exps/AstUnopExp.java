package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstUnopExp extends AstExp{

    private UnaryOp op;
    private AstExp right;

    public AstUnopExp(Pos pos, AstType ty, UnaryOp op, AstExp right){
        this.ty = ty;
        this.pos = pos;
        this.op = op;
        this.right = right;
    }
}
