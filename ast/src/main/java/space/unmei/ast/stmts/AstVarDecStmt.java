package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.types.AstType;
import space.unmei.semant.Symbol;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

public class AstVarDecStmt extends AstStmt{


    private AstVarDec varDec;

    public AstVarDecStmt(Pos pos, AstVarDec varDec){
        super(pos);
        this.varDec = varDec;
    }


}

