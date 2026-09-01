package space.unmei.ast.exps;

import space.unmei.ast.exps.BinOpType;

public class AstBinopExp{

    private AstExp leftExp;
    private AstExp rightExp;
    private BinOpType opType;

    public AstBinopExp(AstExp l, AstExp r, BinOpType ty){this.leftExp = l; this.rightExp = r; BinOpType opType = ty;}

}

