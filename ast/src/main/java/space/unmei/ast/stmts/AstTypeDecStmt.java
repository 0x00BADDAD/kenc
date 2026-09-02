package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstTypeDecStmt extends AstStmt{

    private Symbol tyName;
    private AstType tyVal;

    public AstTypeDecStmt(Pos pos, Symbol tyName, AstType tyVal){
        this.pos = pos;
        this.tyName = tyName;
        this.tyVal = tyVal;
    }


}

