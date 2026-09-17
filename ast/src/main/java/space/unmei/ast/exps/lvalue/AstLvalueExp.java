package space.unmei.ast.exps.lvalue;

import space.unmei.ast.types.*;
import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.*;

public class AstLvalueExp extends AstExp{

    // lvalues will be addresses in memory with a byte size
    // AstType is associated with a byteSize
    // and memory address is generated at the time of translation to ic
    public AstLvalueExp(Pos pos, AstType ty){
        super(pos, ty);
    }
}
