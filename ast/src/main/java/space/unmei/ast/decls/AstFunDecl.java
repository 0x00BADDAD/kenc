package space.unmei.ast.decls;

import space.unmei.ast.*;
import space.unmei.ast.nodes.*;
import space.unmei.ast.Pos;
import space.unmei.ast.types.*;
import space.unmei.semant.Symbol;

public class AstFunDecl extends AstDecl{

    private AstFunDec funDec;

    public AstFunDecl(Pos pos, AstFunDec funDec)
    {
        super(pos);
        this.funDec = funDec;
    }
}
