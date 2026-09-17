package space.unmei.ast.exps;

import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.ast.stmts.*;

public class AstIfElseExp extends AstExp{

    private AstExp cond;
    private AstExp tExp;
    private AstStmts thenStmts;
    private AstExp eExp;
    private AstStmts elseStmts;

    public AstIfElseExp(Pos pos, AstType ty, AstExp cond, AstExp tExp, AstExp eExp, AstStmts thenStmts, AstStmts elseStmts){
        super(pos, ty);
        this.cond = cond;
        this.tExp = tExp;
        this.eExp = eExp;
        this.thenStmts = thenStmts;
        this.elseStmts = elseStmts;
    }


}
