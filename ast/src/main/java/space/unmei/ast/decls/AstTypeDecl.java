package space.unmei.ast.decls;

import space.unmei.ast.*;
import space.unmei.ast.nodes.*;
import space.unmei.ast.Pos;
import space.unmei.semant.Symbol;
import space.unmei.ast.types.AstType;

public class AstTypeDecl extends AstDecl{

    private AstTypeDec tyDec;

    public AstTypeDecl(Pos pos, AstTypeDec tyDec) {
        super(pos);
        this.tyDec = tyDec;
    }
}
