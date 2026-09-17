package space.unmei.ast.exps;

import space.unmei.ast.*;
import space.unmei.ast.types.*;
import space.unmei.ast.exps.AstBinOpType;
import space.unmei.ast.Pos;

public class AstBinopExp extends AstExp{

    private AstExp leftExp;
    private AstExp rightExp;
    private AstBinOpType opType;

    public AstBinopExp(Pos pos, AstType ty, AstExp l, AstExp r, AstBinOpType opType){super(pos, ty); this.leftExp = l; this.rightExp = r;  this.opType = opType;}

}

