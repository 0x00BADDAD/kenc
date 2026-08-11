package space.unmei.parser;

// T -> semantic action type
// U -> Lexical Token Type
public class LR1State<T, U>{

    private List<LR1item<T, U>> items = new ArrayList<>();
    private Map<GramSymbol<U>, List<GramProd<T, U>> lhsToProds;
    private boolean isClosed = false;

    private Map<GramSymbol<U>, Action> actions = HashMap<>();



    public LR1State(){}

    public void addAction(GramSymbol<U> sym, Action act){
        if(this.actions.containsKey(sym)){
            throw new IllegalArgumentException("Can't have more than 2 actions. LR1Parsing failed!");
        }
        this.actions.put(sym , act);
    }

    public Action getAction(GramSymbol<U> sym){
        if(!this.actions.containsKey(sym)){
            return null;
        }
        return this.actions.get(sym);
    }

    public LR1State(Map<GramSymbol<U>, List<GramProd<T, U>> lhsToProds){
        this.lhsToProds = lhsToProds;
    }

    public LR1State(List<LR1item<T, U>> items,  Map<GramSymbol<U>, List<GramProd<T, U>> lhsToProds){
        this.lhsToProds = lhsToProds;
        this.items = items;
    }

    public void setLhsToProds(Map<GramSymbol<U>, List<GramProd<T, U>> lhsToProds){
        this.lhsToProds =  lhsToProds;
    }

    public void setItems(List<LR1item<T, U>> items){
        this.items = items;
    }

    public List<LR1item<T, U>> getItems(){
        return this.items;
    }

    public void addItem(LR1item<T, U> item){
        this.items.add(items);
    }

    public void setIsClosed(boolean val){
        this.isClosed = val;
    }

    public boolean getIsClosed(){
        return this.isClosed;
    }

    public void closure(LR1State<T, U> state){
        // the LR1State returned is ref to the state passed
        List<LR1item> currItems = state.items;
        int idx = 0;
        Map<GramSymbol<U>, boolean> seenThisNonTerm = new HashMap<>();
        while(idx < items.size()){
            // for the item on this index see if the top of stack is before a nonTerm
            // then check if the nonTerm's GramProd hasn't been added before
            LR1item it = currItems.get(idx);

            if(it.getProd().getRhs().size() <= it.getStackTopIdx()){
                idx += 1;
                continue;
            }

            GramSymbol<U> sym = it.getProd().getRhs().get(it.getStackTopIdx());
            if(!sym.isNonTerm() && seenThisNonTerm.containsKey(sym)){
                idx+=1;
                continue;
            }
            seenThisNonTerm.put(sym, true);
            List<GramProd<T, U>> prods = state.lhsToProds.get(sym);
            for(GramProd<T, U> prod: prods){
                // calc the first set for the trailing syms after the dot
                List<GramSymbol<U>> firstSyms = new ArrayList<>();
                boolean allNull = true;
                for(int idx = it.getStackTopIdx(); idx < it.getProd().getRhs().size(); idx++){
                    List<GramSymbol<U>> currFirsts = it.getProd().getRhs().get(idx).getFirstSet();
                    firstSyms.addAll(currFirsts);
                    if(!it.getProd().getRhs().get(idx).isNonTerm()){
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

        if (!(obj instanceof LR1State<?, ?> other))
            return false;
        return items.equals(other.items);
    }

    @Override
    public int hashCode(){
        return Objects.hash(items);
    }
}
