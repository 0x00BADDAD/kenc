package space.unmei.ast.types;

import space.unmei.ast.Pos;

import space.unmei.ast.*;

public class AstArrayType extends AstType{

    private AstType arrEleTy;

    public AstArrayType(Pos pos, AstType arrEleTy){
        super(pos);
        this.arrEleTy = arrEleTy;
    }

    public AstType getArrEleType(){
        return this.arrEleTy;
    }


}


