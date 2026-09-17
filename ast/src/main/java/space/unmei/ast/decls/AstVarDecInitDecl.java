package space.unmei.ast.decls;

import space.unmei.ast.*;
import space.unmei.ast.nodes.*;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;
import space.unmei.ast.exps.AstExp;
import space.unmei.ast.Pos;

public class AstVarDecInitDecl extends AstDecl{

    private AstVarDecInit varDecInit;

    public AstVarDecInitDecl(Pos pos, AstVarDecInit varDecInit) {
        super(pos);
        this.varDecInit = varDecInit;
    }
}


