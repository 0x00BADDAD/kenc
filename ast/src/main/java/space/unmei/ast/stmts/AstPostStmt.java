package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.AstNode;
import space.unmei.ast.*;

import space.unmei.ast.nodes.*;

public class AstPostStmt extends AstStmt{

    private AstPost post;

    public AstPostStmt(Pos pos, AstPost post){
        super(pos);
        this.post = post;
    }


}

