package space.unmei.ast.exps;

import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;

public class AstUnaryOpExp extends AstExp{

    private AstUnaryOpType op;
    private AstExp right;

    public AstUnaryOpExp(Pos pos, AstType ty, AstUnaryOpType op, AstExp right){
        super(pos, ty);
        this.op = op;
        this.right = right;
    }
}
