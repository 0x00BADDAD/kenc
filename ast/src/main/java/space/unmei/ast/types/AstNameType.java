package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstNameType extends AstType{

    private Symbol tyName;

    public AstNameStmt(Pos pos, Symbol tyName){
        this.pos = pos;
        this.tyName = tyName;
    }

}


