package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;

public class AstRecordType extends AstType{

    private AstTypeFields tyFields;

    public AstRecordType(Pos pos, AstTypeFields tyFields){
        this.pos = pos;
        this.tyFields = tyFields;
    }
}


