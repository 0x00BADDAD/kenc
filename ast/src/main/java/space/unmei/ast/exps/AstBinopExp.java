package space.unmei.ast.exps;

import space.unmei.ast.exps.BinOpType;
import space.unmei.ast.Pos;

public class AstBinopExp extends AstExp{

    private AstExp leftExp;
    private AstExp rightExp;
    private AstBinOpType opType;

    public AstBinopExp(Pos pos, AstType ty, AstExp l, AstExp r, AstBinOpType ty){this.pos = pos; this.leftExp = l; this.rightExp = r; this.opType = ty; this.ty = ty;}

}

