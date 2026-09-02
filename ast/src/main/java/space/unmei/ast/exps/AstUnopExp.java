package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstUnopExp extends AstExp{

    private UnaryOp op;
    private AstExp right;

    public AstUnopExp(Pos pos, UnaryOp op, AstExp right){
        this.pos = pos;
        this.op = op;
        this.right = right;
    }
}
