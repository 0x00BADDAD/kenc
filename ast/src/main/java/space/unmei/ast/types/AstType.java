package space.unmei.ast.types;

import space.unmei.ast.Pos;

public class AstType{

    private Pos pos;
    private AstNameType tyName;
    private AstRecordType record;
    private AstArrayType arr;

    public AstType(Pos pos, AstNameType tyName, AstRecordType record, AstArrayType arr){
        this.pos = pos;
        // only one of the 3 is non-null
        this.tyName = tyName;
        this.record = record;
        this.arr = arr;
    }
}

