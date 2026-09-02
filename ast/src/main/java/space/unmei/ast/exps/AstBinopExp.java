package space.unmei.ast.exps;

import space.unmei.ast.exps.BinOpType;
import space.unmei.ast.Pos;

public class AstBinopExp extends AstExp{

    private AstExp leftExp;
    private AstExp rightExp;
    private BinOpType opType;

    public AstBinopExp(Pos pos, AstExp l, AstExp r, BinOpType ty){this.pos = pos; this.leftExp = l; this.rightExp = r; BinOpType opType = ty;}

}

