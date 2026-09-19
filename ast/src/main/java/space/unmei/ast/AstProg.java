package space.unmei.ast;

import java.util.*;
import space.unmei.ast.decls.AstDecls;
import space.unmei.ast.decls.AstDecl;
import space.unmei.ast.*;

public class AstProg extends AstNode{

    private AstDecls decls;

    public AstProg(Pos pos, AstDecls decls){
        super(pos);
        this.decls = decls;
    }

    public void addDecl(AstDecl decl){
        this.decls.addDecl(decl);
    }
}
