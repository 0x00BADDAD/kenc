package space.unmei.ast.exps;

import space.unmei.ast.stmts.*;
import space.unmei.ast.types.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;

public class AstIfExp extends AstExp{

    private AstExp cond;
    private AstExp thenExp;
    private AstStmts thenStmts;

    public AstIfExp(Pos pos, AstType ty, AstExp cond, AstExp thenExp, AstStmts thenStmts){
        super(pos, ty);
        this.cond = cond;
        this.thenExp = thenExp;
        this.thenStmts = thenStmts;
    }


}

