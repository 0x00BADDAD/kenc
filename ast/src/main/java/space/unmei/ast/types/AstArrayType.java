package space.unmei.ast.types;

import space.unmei.ast.Pos;

public class AstArrayType extends AstType{

    private AstType arrEleTy;

    public AstArrayType(Pos pos, AstType arrEleTy){
        this.pos = pos;
        this.arrEleTy = arrEleTy;
    }

    public AstType getArrEleType(){
        return this.arrEleTy;
    }


}


