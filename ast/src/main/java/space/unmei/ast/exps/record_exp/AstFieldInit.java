package space.unmei.ast.exps.record_exp;

import space.unmei.ast.types.*;
import java.util.*;
import space.unmei.ast.exps.*;
import space.unmei.ast.exps.lvalue.*;
import space.unmei.ast.*;
import space.unmei.semant.Symbol;

public class AstFieldInit extends AstNode{
    private List<Pair<Symbol, AstExp>> fieldInitList = new ArrayList<>();

    public AstFieldInit(Pos pos, List<Pair<Symbol, AstExp>> l){
        super(pos);
        this.fieldInitList = l;
    }

    public AstFieldInit(Pos pos){
        super(pos);
    }

    public void addFieldInit(Symbol f, AstExp v){
        this.fieldInitList.add(new Pair(f, v));
    }

    public List<Pair<Symbol, AstExp>> getFieldInitList(){
        return this.fieldInitList;
    }
}
