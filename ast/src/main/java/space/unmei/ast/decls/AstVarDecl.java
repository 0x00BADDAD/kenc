package space.unmei.ast.decls;

import space.unmei.ast.*;
import space.unmei.ast.nodes.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstVarDecl extends AstDecl{

    private AstVarDec varDec;

    public AstVarDecl(Pos pos, AstVarDec varDec) {
        super(pos);
        this.varDec = varDec;
    }
}


