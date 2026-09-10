package space.unmei.ast.exps.lvalue;

public class AstLvalueExp extends AstExp{

    // lvalues will be addresses in memory with a byte size
    // AstType is associated with a byteSize
    // and memory address is generated at the time of translation to ic
    public AstLvalueExp(Pos pos, AstType ty){
        this.pos = pos;
        this.ty = ty;
    }
}
