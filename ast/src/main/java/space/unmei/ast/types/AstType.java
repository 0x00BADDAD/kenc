package space.unmei.ast.types;

import space.unmei.ast.Pos;
import space.unmei.ast.*;

public class AstType extends AstNode{

    private Integer sizeInBytes;

    public AstType(Pos pos){
        super(pos);
    }

    public void setSizeInBytes(Integer b){
        this.sizeInBytes = b;
    }
}

