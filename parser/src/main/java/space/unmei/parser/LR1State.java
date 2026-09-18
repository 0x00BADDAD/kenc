package space.unmei.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.*;
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
            //System.out.printf("Err state is: " + this.toString() + "\n");
            //System.out.println("The actions are repeated for sym: " + sym.toString());
            throw new IllegalArgumentException("Can't have more than 2 actions. LR1Parsing failed table formation failed!");
        }
        this.actions.put(sym , act);
    }

    public Action<T, U> getAction(GramSymbol<U> sym){
        if(!this.actions.containsKey(sym)){
            return null;
        }
        return this.actions.get(sym);
    }

    //public LR1State(Map<GramSymbol<U>, List<GramProd<T, U>>> lhsToProds){
    //    this.lhsToProds = lhsToProds;
    //}

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

    public static <T, U extends LexToken> LR1State<T, U> closure(LR1State<T, U> state){

        List<LR1item<T,U>> currItems = state.getItems();

        int idx = 0;

        Set<LR1item<T, U>> seenThisItem = new HashSet<>(currItems);

        while (idx < currItems.size()) {

            LR1item<T, U> it = currItems.get(idx);

            if (it.getProd().getRhs().size() <= it.getStackTopIdx()) {
                idx++;
                continue;
            }

            GramSymbol<U> sym =
                it.getProd().getRhs().get(it.getStackTopIdx());

            if (!sym.getIsNonTerm()) {
                idx++;
                continue;
            }

            List<GramProd<T, U>> prods =
                state.lhsToProds.get(sym);

            for (GramProd<T, U> prod : prods) {

                List<GramSymbol<U>> firstSyms = new ArrayList<>();
                boolean allNull = true;

                for (int j = it.getStackTopIdx() + 1;
                     j < it.getProd().getRhs().size();
                     j++) {

                    GramSymbol<U> trailingSym =
                        it.getProd().getRhs().get(j);

                    firstSyms.addAll(
                        trailingSym.getFirstSet()
                    );

                    if (!trailingSym.getIsNullable()) {
                        allNull = false;
                        break;
                    }
                }

                if (allNull) {
                    firstSyms.add(it.getLookahead());
                }

                for (GramSymbol<U> looksym : firstSyms) {

                    //System.out.println(
                    //    "    Adding: " + prod
                    //    + " , lookahead = " + looksym
                    //);

                    LR1item<T, U> itemCand =
                        new LR1item<>(prod, 0, looksym);

                    if (seenThisItem.add(itemCand)) {
                        currItems.add(itemCand);
                    }
                }
            }

            idx++;
        }

       state.setItems(currItems);
       //System.out.println("closure done!! for a state with size: " + String.valueOf(currItems.size()));
       state.setIsClosed(true);
       return state;

    }
    //public static <T, U extends LexToken> LR1State<T, U> closure(LR1State<T, U> state){
    //    System.out.println("Strating to make closure!!");
    //    // the LR1State returned is ref to the state passed
    //    List<LR1item<T,U>> currItems = state.getItems();

    //    int idx = 0;
    //    Map<LR1item<T, U>, Boolean> seenThisItem = new HashMap<>();

    //    for(LR1item<T, U> knownItem: currItems){
    //        seenThisItem.put(knownItem, true);
    //    }

    //    while(idx < currItems.size()){
    //        // for the item on this index see if the top of stack is before a nonTerm
    //        // then check if the nonTerm's GramProd hasn't been added before
    //        LR1item<T,U> it = currItems.get(idx);

    //        if(it.getProd().getRhs().size() <= it.getStackTopIdx()){
    //            idx += 1;
    //            continue;
    //        }

    //        GramSymbol<U> sym = it.getProd().getRhs().get(it.getStackTopIdx());

    //        if(!sym.getIsNonTerm()){
    //            // sym is terminal so will not contribute to closure!
    //            idx+=1;
    //            continue;
    //        }
    //        //System.out.println("the sym that does not have a prod is: "+ sym.toString());
    //        List<GramProd<T, U>> prods = state.lhsToProds.get(sym);
    //        for(GramProd<T, U> prod: prods){
    //            // calc the first set for the trailing syms after the dot
    //            List<GramSymbol<U>> firstSyms = new ArrayList<>();
    //            boolean allNull = true;
    //            for(int idx_ = it.getStackTopIdx()+1; idx_ < it.getProd().getRhs().size(); idx_++){
    //                List<GramSymbol<U>> currFirsts = new ArrayList<>(it.getProd().getRhs().get(idx_).getFirstSet());
    //                firstSyms.addAll(currFirsts);
    //                if(!it.getProd().getRhs().get(idx_).getIsNullable() || !it.getProd().getRhs().get(idx_).getIsNonTerm()){
    //                    allNull = false;
    //                    break;
    //                }

    //            }
    //            if(allNull){
    //                firstSyms.addAll(it.getLookahead().getFirstSet());
    //            }
    //            for(GramSymbol<U> looksym : firstSyms){
    //                LR1item<T, U> itemCand = new LR1item<>(prod, 0, looksym);
    //                if(!seenThisItem.containsKey(itemCand)){
    //                    currItems.add(itemCand);
    //                    seenThisItem.put(itemCand, true);
    //                }
    //            }
    //        }
    //        idx += 1;
    //    }
    //    state.setItems(currItems);
    //    System.out.println("closure done!! for a state with size: " + String.valueOf(currItems.size()));
    //    state.setIsClosed(true);
    //    return state;
    //}

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (other instanceof LR1State<?, ?> otherState) {
            // Safe comparison because List.equals() compares elements in order
            return Objects.equals(this.items, otherState.items);
        }
        return false;
    }


    @Override
    public int hashCode(){
        return Objects.hash(this.items);
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("------------------------\n");
        for(LR1item it: this.items){
            sb.append(it.toString());
            sb.append("\n");
        }
        sb.append("------------------------");
        return sb.toString();
    }
}
