package space.unmei.ast.types;

import space.unmei.ast.Pos;

public class AstArrayType{

    private Pos pos;
    private AstType arrTy;

    public AstArrayType(Pos pos, AstType arrTy){
        this.pos = pos;
        this.arrTy = arrTy;
    }


}


