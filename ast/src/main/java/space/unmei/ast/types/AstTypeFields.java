package space.unmei.ast.types;

import space.unmei.ast.*;
import space.unmei.semant.Symbol;
import java.util.*;

public class AstTypeFields extends AstNode{

    private List<Pair<Symbol, AstType>> tyFields;

    public AstTypeFields(Pos pos){
        super(pos);
    }

    public AstTypeFields(Pos pos, List<Pair<Symbol, AstType>> tyFields){
        super(pos);
        this.tyFields = tyFields;
    }

    public void addTyField(Symbol sym, AstType tyVal){
        this.pos = new Pos(sym.getToken());
        this.tyFields.add(new Pair<>(sym, tyVal));
    }


}
