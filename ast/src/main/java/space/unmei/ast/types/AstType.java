package space.unmei.ast.types;

import space.unmei.ast.Pos;

public class AstType extends AstNode{

    private Integer sizeInBytes;

    public AstType(Pos pos){
        this.pos = pos;
    }

    public void setSizeInBytes(Integer b){
        this.sizeInBytes = b;
    }
}

