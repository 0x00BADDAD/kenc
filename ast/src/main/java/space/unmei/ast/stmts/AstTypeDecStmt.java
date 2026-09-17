package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;
import space.unmei.ast.*;
import space.unmei.ast.nodes.*;

public class AstTypeDecStmt extends AstStmt{

    private AstTypeDec tyDec;

    public AstTypeDecStmt(Pos pos, AstTypeDec tyDec){
        super(pos);
        this.tyDec = tyDec;
    }


}

