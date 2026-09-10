package space.unmei.ast.stmts;

import space.unmei.ast.Pos;
import space.unmei.ast.AstNode;

public class AstStmts extends AstNode{

    private List<AstStmt> stmtList = new ArrayList<>();

    public AstStmts(Pos pos, List<AstStmt> stmtList){
        this.pos = pos;
        this.stmtList = stmtList;
    }

    public AstStmts(Pos pos){
        this.pos = pos;
    }

    public void addStmt(AstStmt stmt){
        this.stmtList.add(stmt);
    }

}

