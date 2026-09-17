package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.*;

public class AstNameType extends AstType{

    private Symbol tyName;

    public AstNameType(Pos pos, Symbol tyName){
        super(pos);
        this.tyName = tyName;
    }

}


