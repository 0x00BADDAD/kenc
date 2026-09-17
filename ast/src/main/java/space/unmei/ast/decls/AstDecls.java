package space.unmei.ast.decls;

import java.util.*;
import space.unmei.ast.*;
import space.unmei.ast.Pos;

public class AstDecls extends AstNode{
    private List<AstDecl> decls = new ArrayList<>();

    public AstDecls(Pos pos, List<AstDecl> decls) {super(pos); this.decls = decls;}

    public void addDecl(AstDecl dec){
        this.decls.add(dec);
    }

}

