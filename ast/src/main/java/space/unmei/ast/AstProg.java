package space.unmei.ast;

import java.util.*;
import space.unmei.ast.decls.AstDecl;
import space.unmei.ast.*;

public class AstProg extends AstNode{

    private List<AstDecl> decls;

    public AstProg(Pos pos, List<AstDecl> decls){
        super(pos);
        this.decls = decls;
    }

    public void addDecl(AstDecl decl){
        this.decls.add(decl);
    }
}
