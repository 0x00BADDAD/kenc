package space.unmei.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import space.unmei.lexer.*;

// T -> semantic action type
// U -> Lexical Token Type
public class LR1State<T , U extends LexToken>{

    private List<LR1item<T, U>> items = new ArrayList<>();
    private Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds;
    private boolean isClosed = false;
    // the key in this map is a termSym and this map represents a row in the parser LR1
    // table.
    private Map<GramSymbol<U>, Action<T, U>> actions = new HashMap<>();



    public LR1State(){}

    public void addAction(GramSymbol<U> sym, Action<T, U> act){
        if(this.actions.containsKey(sym)){
            throw new IllegalArgumentException("Can't have more than 2 actions. LR1Parsing failed tabble formation failed!");
        }
        this.actions.put(sym , act);
    }

    public Action<T, U> getAction(GramSymbol<U> sym){
        if(!this.actions.containsKey(sym)){
            return null;
        }
        return this.actions.get(sym);
    }

    public LR1State(Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds){
        this.lhsToProds = lhsToProds;
    }

    public LR1State(List<LR1item<T, U>> items,  Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds){
        this.lhsToProds = lhsToProds;
        this.items = items;
    }

    public void setLhsToProds(Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds){
        this.lhsToProds =  lhsToProds;
    }

    public void setItems(List<LR1item<T, U>> items){
        this.items = items;
    }

    public List<LR1item<T, U>> getItems(){
        return this.items;
    }

    public void addItem(LR1item<T, U> item){
        this.items.add(item);
    }

    public void setIsClosed(boolean val){
        this.isClosed = val;
    }

    public boolean getIsClosed(){
        return this.isClosed;
    }

    public void closure(LR1State<T, U> state){
        // the LR1State returned is ref to the state passed
        List<LR1item<T,U>> currItems = state.items;
        int idx = 0;
        Map<GramSymbol<U>, Boolean> seenThisNonTerm = new HashMap<>();
        while(idx < items.size()){
            // for the item on this index see if the top of stack is before a nonTerm
            // then check if the nonTerm's GramProd hasn't been added before
            LR1item<T,U> it = currItems.get(idx);

            if(it.getProd().getRhs().size() <= it.getStackTopIdx()){
                idx += 1;
                continue;
            }

            GramSymbol<U> sym = it.getProd().getRhs().get(it.getStackTopIdx());
            if(!sym.getIsNonTerm() && seenThisNonTerm.containsKey(sym)){
                idx+=1;
                continue;
            }
            seenThisNonTerm.put(sym, true);
            List<GramProd<T, U>> prods = state.lhsToProds.get(sym);
            for(GramProd<T, U> prod: prods){
                // calc the first set for the trailing syms after the dot
                List<GramSymbol<U>> firstSyms = new ArrayList<>();
                boolean allNull = true;
                for(int idx_ = it.getStackTopIdx(); idx_ < it.getProd().getRhs().size(); idx_++){
                    List<GramSymbol<U>> currFirsts = it.getProd().getRhs().get(idx_).getFirstSet();
                    firstSyms.addAll(currFirsts);
                    if(!it.getProd().getRhs().get(idx_).getIsNonTerm()){
                        allNull = false;
                        break;
                    }

                }
                if(allNull){
                    firstSyms.addAll(it.getLookahead().getFirstSet());
                }
                for(GramSymbol<U> looksym : firstSyms){
                    currItems.add(new LR1item<>(prod, 0, looksym));
                }
            }
            idx += 1;
        }
        state.setIsClosed(true);
    }

    @Override
    public boolean equals(Object other){
        if (this == obj)
            return true;

        if (!(obj instanceof LR1State<?, ?> other_))
            return false;
        return items.equals(other.items);
    }

    @Override
    public int hashCode(){
        return Objects.hash(items);
    }
}
