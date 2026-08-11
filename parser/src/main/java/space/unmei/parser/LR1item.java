package space.unmei.parser;


public class LR1item<T, U>{

    // GramProd, idx of top of stack, single lookahead term symbol
    private GramProd<T, U> prod;
    // dot is present before this idx. So prod.getRhs().size() value means that dot is at the end of prod
    private int stackTopIdx;
    private GramSymbol<U> lookahead; // ref to a GramSymbol in GramProd
    private boolean reducible; // the dot is at the end of prod and so can be reduced

    public LR1item(){
    }

    public LR1item(GramProd<T, U> prod, int stackTopIdx, GramSymbol<U> lookahead){
        if (stackTopIdx < 0 || stackTopIdx > prod.getRhs().size()) {
            throw new IllegalArgumentException(
                "Invalid dot position: " + stackTopIdx
            );
        }
        this.prod = prod;
        this.stackTopIdx = stackTopIdx;
        this.lookahead = lookahead;
        this.reducible = (stackTopIdx == prod.getRhs().size());
    }

    public GramProd<T, U> getProd(){
        return this.prod;
    }

    public int getStackTopIdx(){
        return this.stackTopIdx;
    }

    public GramSymbol<U> getLookahead(){
        return this.lookahead;
    }

    @Override
    public boolean equals(Object other){
        if (this == obj)
            return true;

        if (!(obj instanceof LR1item<?, ?> other))
            return false;
        return prod.equals(other.prod) && other.stackTopIdx == stackTopIdx && lookahead.equals(other.lookahead);
    }

    @Override
    public int hashCode(){
        return Objects.hash(prod, stackTopIdx, lookahead);
    }





}
