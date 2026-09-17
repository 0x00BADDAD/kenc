package space.unmei.ast.nodes;

import space.unmei.ast.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstVarDec extends AstNode{

    private Symbol varName;
    private AstType tyVal;

    public AstVarDec(Pos pos, Symbol varName, AstType tyVal) {
        super(pos);
        this.varName = varName;
        this.tyVal = tyVal;
    }
}

