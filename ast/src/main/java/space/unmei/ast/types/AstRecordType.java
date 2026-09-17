package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.*;

public class AstRecordType extends AstType{

    private AstTypeFields tyFields;

    public AstRecordType(Pos pos, AstTypeFields tyFields){
        super(pos);
        this.tyFields = tyFields;
    }
}


