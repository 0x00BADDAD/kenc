package space.unmei.ast.types;

import space.unmei.ast.Pos;

public class AstArrayType extends AstType{

    private AstType arrTy;

    public AstArrayType(Pos pos, AstType arrTy){
        this.pos = pos;
        this.arrTy = arrTy;
    }


}


