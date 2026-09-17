package space.unmei.ast.stmts;

import java.util.*;
import space.unmei.ast.Pos;
import space.unmei.ast.AstNode;
import space.unmei.ast.*;

public class AstStmts extends AstNode{

    private List<AstStmt> stmtList = new ArrayList<>();

    public AstStmts(Pos pos, List<AstStmt> stmtList){
        super(pos);
        this.stmtList = stmtList;
    }

    public AstStmts(Pos pos){
        super(pos);
    }

    public void addStmt(AstStmt stmt){
        this.stmtList.add(stmt);
    }

}

