package space.unmei.ast.exps;

import space.unmei.ast.Pos;

public class AstUnaryOpExp extends AstExp{

    private AstUnaryOpType op;
    private AstExp right;

    public AstUnaryOpExp(Pos pos, AstType ty, AstUnaryOpType op, AstExp right){
        this.ty = ty;
        this.pos = pos;
        this.op = op;
        this.right = right;
    }
}
