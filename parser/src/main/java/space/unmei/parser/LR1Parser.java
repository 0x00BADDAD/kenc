package space.unmei.parser;

// this class expects tokens to be of type LexToken
// T -> class of AST Node returned by gram prod reduction
// U -> LexToken class
public abstract class LR1Parser<T, U>{

    private List<U> tokSet;

    private List<U> tokens = new ArrayList<>();

    private Set<LR1State> states = new HashSet<>();
    private LR1State startState;

    private Map<GramSymbol<U>, List<GramProd<T, U>> lhsToProds = new HashMap<>();

    private List<GramProd<T, U>> gramProds  = new ArrayList<>();
    private List<GramSymbol<U>> nonTermSyms = new ArrayList<>();
    private List<GramSymbol<U>> termSyms = new ArrayList<>();

    private Map<String, GramSymbol<U>> valueToSym = new HashMap<>();

    // it expects GramProd(s) and GramSymbol(s) list(s)
    // it will generate the LR1 parse graph.
    public LR1Parser(){}

    public List<U> getTokens(){
        return this.tokens;
    }

    public void setTokens(List<U> tokens){
        this.tokens = tokens;
    }

    private LR1State stateExists(LR1State state){
        for (LR1State x : this.states) {
            if (x.equals(state)) {
                return x;
            }
        }
        return null;
    }

    private void makeFixedSets(){
        // first add term symbols in the first set of term symbols.
        // The algo runs until a fixed point is acheived where,
        // First map and follow map are same. That will require overriding equals on
        // GramSymbol object and jvm will take care of the rest.
        for(GramSymbol<U> sym: this.termSyms){
            sym.getFirstSet().add(sym);
        }
        for(GramProd<T, U> prod: this.gramProds){
            // this prod is essentially an outer loop
            if(!prod.getLhs().getIsNullable()){
                // if we think that it's not nullable then we check
                boolean thisProdLhsNullable = true;
                for(GramSymbol<U> sym: prod.getRhs()){
                    if(!sym.getIsNullable()){
                        thisProdLhsNullable = false;
                        break;
                    }
                }
                prod.getLhs().setIsNullable(thisProdLhsNullable);
            }
            // calc FIRST of prod.getRhs()
            boolean nullPrefix = true;
            for(GramSymbol<U> sym: prod.getRhs()){
                if(!nullPrefix){break;}
                for(GramSymbol<U> firstSym : sym.getFirstSet()){
                    prod.getLhs().getFirstSet().add(firstSym);
                }
                nullPrefix = sym.getIsNullable();
            }

            // calc FOLLOW of syms on the Rhs from left to right
            boolean nullSuffix = true;
            int idxMax = prod.getRhs().size();
            for(int idx = idxMax-1; idx >= 0; --idx){
                if(!nullSuffix){break;}
                GramSymbol<U> currSym = prod.getRhs().get(idx);
                for(GramSymbol<U> symInLhs : prod.getLhs().getFollowSet()){
                    currSym.getFollowSet().add(symInLhs);
                }
                nullSuffix = currSym.getIsNullable();
            }

            // calc FOLLOW(s) of the symbols on rhs by i,j iteration
            for(int idx1=idxMax-1; idx1 >= 0; idx1--){
                nullSuffix = true;
                for(int idx2=idx1-1; idx2 >= 0; idx2--){
                    if(!nullSuffix){break;}
                    for(GramSymbol<U> sym_ : prod.getRhs().get(idx1).getFirstSet()){
                        prod.getRhs().get(idx2).addFollow(sym_);
                    }
                    nullSuffix = prod.getRhs().get(idx2).getIsNullable();
                }
            }

        }
    }


    private Triplet<Map<String, List<String>>,
           Map<String, List<String>>,
           List<String>> extrFirstAndFollow(){
        // extract the Map<String, List<U>> for first and follow and
        // List<String> for nullability
        Map<String, List<String>> firsts = new HashMap<>();
        Map<String, List<String>> follows = new HashMap<>();
        List<String> nulls = new ArrayList<>();
        for(GramSymbol<U> sym : this.nonTermSyms){

            List<String> currFirsts = firsts.getOrDefault(sym.getValue(), new ArrayList<>());
            for(GramSymbol<U> sym_ : sym.getFirstSet()){
                currFirsts.add(sym_.getSymbolType().name());
            }
            firsts.put(sym.getValue(), currFirsts);


            List<String> currFollows = follows.getOrDefault(sym.getValue(), new ArrayList<>());
            for(GramSymbol<U> sym_ : sym.getFollowSet()){
                currFollows.add(sym_.getSymbolType().name());
            }
            follows.put(sym.getValue(), currFollows);


            if(sym.getIsNullable()){
                nulls.add(sym.getValue());
            }
        }

        return new Triplet<>(firsts, follows, nulls);
    }


    private void makeFirstAndFollow(){
        while(true){
            Triplet<Map<String, List<String>>,
                       Map<String, List<String>>,
                       List<String>> trip1 = this.extrFirstAndFollow();

            this.makeFixedSets();

            Triplet<Map<String, List<String>>,
                       Map<String, List<String>>,
                       List<String>> trip2 = this.extrFirstAndFollow();

            if(trip1.equals(trip2)){break;}
        }
    }

    private void makeStates(){
        // Start -> .Prog $, ? (no lookahead)
        // have a running idx on a growing list of LR1State
        // for every state check if it has a reducible action for a lookahead.
        // then seperate out states for each of the symbols and the take their closures
        // for terms add as shift action and for non-term add as goto action
        // reuse the state incase the state was already produced before.

        // adding the starting state's item {Start -> . Prog $, ?}
        List<LR1State> currStates = new ArrayList<>();
        GramProd<T, U> startProd = this.lhsToProds(this.valueToSym.get("Start"));
        LR1item<T, U> startItem = new LR1Item(startProd, 0, null);
        LR1State startState = new LR1State(new ArrayList<>(List.of(startItem)), this.lhsToProds);

        this.startState = startState;

        startState.closure()
        currStates.add(startState);

        int idx = 0;
        while(idx < currStates.size()){
            Map<GramSymbol<T, U>, List<LR1item<T, U>> symToItems = new HashMap<>();
            for(LR1Item<T, U> it: currStates.get(idx).getItems()){
                if(it.getStackTopIdx() < it.getProd().getRhs().size()){
                    // shift/goto action
                    GramSymbol<U> lookaheadsym = it.getProd().getRhs().get(it.getStackTopIdx());
                    List<LR1item<T, U>> itemlist = symToItems.getOrDefault(lookaheadsym, new ArrayList<>());
                    itemlist.add(new LR1item(it.getProd(), it.getStackTopIdx()+1, it.getLookahead()));
                    symToItems.put(lookaheadsym, itemlist);

                }else{
                    // reducible item and so this state must have Reduce action for
                    // this lookahead
                    GramSymbol<U> lookaheadsym = it.getLookahead();
                    if(!lookaheadsym.getIsNonTerm() && lookaheadsym.getSymbolToken().getName().equals("EOF")){
                        currStates.get(idx).addAction(lookaheadsym, new Action.Accept());
                    }else{
                        currStates.get(idx).addAction(lookaheadsym, new Action.Reduce(it.getProd()));
                    }
                }
            }
            // for each of the List of the LR1items gathered. take closure of each of
            // these and see if it is a new LR1State, if it is then add it into this.states
            // and also into the currStates list. Finally put the action into the actions
            // of this LR1State
            for(Map.Entry<GramSymbol<U>, List<LR1item>> ent: symToItems.entrySet()){
                LR1State newState = new LR1State(ent.getValue(), this.lhsToProds);
                newState = newState.closure();
                LR1State existingState = this.stateExists(newState);
                if(existingState != null){
                    newState = existingState;
                }else{
                    // new state found
                    currStates.add(newState);
                    this.states.add(newState);
                }
                currStates.get(idx).addAction(ent.getKey(), new Action.Shift(newState));
            }
            idx += 1;
        }
    }

    protected void setup_(String[] nonTermSyms, List<U> termSyms, List<Pair<List<String>, Supplier<T>>> prodStrs){

        this.tokSet = termSyms;

        for(String s: nonTermSyms){
            GramSymbol<U> gramSym = new GramSymbol<>(true, s);
            gramSym.setIsNullable(false);
            this.addNonTermSym(gramSym);
            this.addValueToSym(s, gramSym);
        }

        for(U tok: termSyms){
            GramSymbol<U> gramSym = new GramSymbol<>(false, null);
            gramSym.setSymbolToken(tok);
            gramSym.setIsNullable(false);
            this.addTermSym(gramSym);
            this.addValueToSym(s.getName(), gramSym);
        }

        for(Pair<List<String>, Supplier<T>> prodPair: prodStrs){
            List<String> prod = prodPair.first();
            Supplier<T> supp = prodPair.second();
            GramSymbol<U> lhs = this.getValueToSym(prod.get(0));
            List<GramSymbol<U>> rhs = new ArrayList<>();
            for(int i=1; i< prod.size(); i++){
                rhs.add(this.valueToSym.get(prod.get(i)));
            }
            this.addGramProd(new GramProd<T, U>(lhs, rhs, supp));
        }
        this.makeFirstAndFollow(); // Follow set is wasteful for LR1 parser but we do it
        // anyways since this method is legacy from lexer package
        this.makeStates();
        // lousey design, if parser fails it will throw an unchecked Excpetion.
        // TODO: to return boolean upon parser success
    }




    public Pair<T, List<ParseErr<U>>> parse(){
        //LinkedList<U> tokLL = new LinkedList<>(this.tokens);

        Integer windowSz = 6;
        Integer R = 4;
        boolean currAccepted = false;

        // this is the burke-fisher err rec.
        // the currStack(s) only execute action and change states but don't really
        // perform any kind of actions.
        Deque<LR1State> oldStateStack = new ArrayDeque<>();
        Deque<GramSymbol<U>> oldSymStack = new ArrayDeque<>();
        oldStateStack.push(this.startState);

        Deque<LR1State> currStateStack = new ArrayDeque<>();
        Deque<GramSymbol<U>> currSymStack = new ArrayDeque<>();
        currStateStack.push(this.startState);

        // window is a LinkedList instead of a queue since we need to edit it
        LinkedList<GramSymbol<U>> window = new LinkedList<>();

        Integer N = this.tokens.size();

        oldIdx = 0; // the index of next lookahead for the old stack.
        currIdx = 0; // the index of the next lookahead for the new stack.

        List<ParseErr<U>> errList = new ArrayList<>();

        while(!currAccepted){
            // consume a token by the currStack
            U tok = this.tokens.get(currIdx);
            GramSymbol<U> gramSym = new GramSymbol<>(false, null);
            gramSym.setSymbolToken(tok);

            // push it into the window
            window.addLast(gramSym);

            // look for the action for this gramSym in the currState
            LR1State currState = currSymStack.peek();
            Action action = currState.getAction(gramSym);
            if(action == null){
                // error on currStack
                // we have to insert, sub, del every tok possible in bet
                // oldIdx and currIdx

                // we need to find the expected tok by either sub or insert
                U expectedTok = null;
                Integer tokIdx = null;
                Integer opCode = -1; // 0 for sub, 1 for insert, 2 for delete

                // add toks from tok list starting from currIdx upto R (or till the end if its smaller)
                for(int i = 1; i <= Math.min(R, this.tokens.size()-currIdx-1); i++){
                    U tok_ = this.tokens.get(currIdx+i);
                    GramSymbol<U> gramSym_ = new GramSymbol<>(false, null);
                    gramSym.setSymbolToken(tok_);
                    window.addLast(gramSym_);
                }

                // try substitution
                outer_:
                for(U candTok: this.tokSet){
                   GramSymbol<U> candGramSym = new GramSymbol<>(false, null);
                   candGramSym.setSymbolToken(candTok);

                   ListIterator<GramSymbol<U>> it = window.listIterator();

                   outer:
                   while(it.hasNext()){
                        GramSymbol<U> nowSym = it.next();
                        it.set(candGramSym); // edit the window

                        // now after subbing the gramSym with candGramSym
                        // we try to see if the cpy_oldSymStack can go beyond the
                        // currIdx upto 4 tokens.
                        Deque<LR1State> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<GramSymbol<U>> cpy_oldSymStack = new ArrayDeque<>(oldSymStack);
                        for(GramSymbol<U> nextSym: window){
                            LR1State st = cpy_oldStateStack.peek();
                            Action ac = st.getAction(nextSym);
                            if(ac == null){
                                it.set(nowSym); // restore window
                                continue outer;
                            }
                            if (ac instanceof Action.Shift sh) {
                                LR1State state = sh.state();
                                // use state
                                cpy_oldStateStack.push(state);
                                cpy_oldSymStack.push(nextSym);

                            } else if (ac instanceof Action.Reduce re) {
                                GramProd<?, ?> prod = re.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    cpy_oldSymStack.pop();
                                    cpy_oldStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.shift gotoAct = cpy_oldStateStack.peek().getAction(prod.getLhs());
                                cpy_oldStateStack.push(gotoAct.state());
                                cpy_oldSymStack.push(prod.getLhs());

                            } else if (ac instanceof Action.Accept ac) {
                                // accept
                                expectedTok = candTok; // found a token!! exit and
                                opCode = 0;
                                tokIdx = it.previousIndex();
                                // no need to try insert and del
                                break outer;
                            }
                        }
                        it.set(nowSym); // restore window

                        // if we came here then we found a candTok
                        expectedTok = candTok; // found a token!! exit and
                        opCode = 0;
                        tokIdx = it.previousIndex();
                        break outer_;
                   }
                }

                // try insertion
                if(expectedTok == null){
                    outerinsert_:
                    for(U candTok: this.tokSet){
                       GramSymbol<U> candGramSym = new GramSymbol<>(false, null);
                       candGramSym.setSymbolToken(candTok);

                       ListIterator<GramSymbol<U>> it = window.listIterator();

                       outerinsert:
                       while(it.hasNext()){
                            GramSymbol<U> nowSym = it.next();
                            Integer insertIdx = it.previousIndex();
                            it.add(candGramSym); // edit the window by inserting the candTok

                            // now after subbing the gramSym with candGramSym
                            // we try to see if the cpy_oldSymStack can go beyond the
                            // currIdx upto 4 tokens.
                            Deque<LR1State> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                            Deque<GramSymbol<U>> cpy_oldSymStack = new ArrayDeque<>();
                            for(GramSymbol<U> nextSym: window){
                                LR1State st = cpy_oldStateStack.peek();
                                Action ac = st.getAction(nextSym);
                                if(ac == null){
                                    it.next(); it.remove(); // restore window
                                    continue outerinsert;
                                }
                                if (ac instanceof Action.Shift sh) {
                                    LR1State state = sh.state();
                                    // use state
                                    cpy_oldStateStack.push(state);
                                    cpy_oldSymStack.push(nextSym);

                                } else if (ac instanceof Action.Reduce re) {
                                    GramProd<?, ?> prod = re.prod();
                                    // use prod
                                    for(GramSymbol<U> rhsSym: prod.getRhs()){
                                        cpy_oldSymStack.pop();
                                        cpy_oldStateStack.pop();
                                    }
                                    // warning: here assuming gotoAct will always shift
                                    Action.shift gotoAct = cpy_oldStateStack.peek().getAction(prod.getLhs());
                                    cpy_oldStateStack.push(gotoAct.state());
                                    cpy_oldSymStack.push(prod.getLhs());

                                } else if (ac instanceof Action.Accept ac) {
                                    // accept
                                    expectedTok = candTok; // found a token!! exit and
                                    opCode = 1;
                                    tokIdx = insertIdx;
                                    // no need to try insert and del
                                    break outerinsert;
                                }
                            }
                            it.next(); it.remove(); // restore the window by deleting the tok

                            // if we came here then we found a candTok
                            expectedTok = candTok; // found a token!! exit and
                            opCode = 1;
                            tokIdx = insertIdx;
                            break outerinsert_;
                       }
                    }
                }

                // try deletion
                if(expectedTok == null){
                   outerdel:
                   while(it.hasNext()){
                        GramSymbol<U> nowSym = it.next();
                        Integer delIdx = it.previousIndex();

                        it.remove(); // edit the window by removing a tok
                        // we try to see if the cpy_oldSymStack can go beyond the
                        // currIdx upto 4 tokens.
                        Deque<LR1State> cpy_oldStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<GramSymbol<U>> cpy_oldSymStack = new ArrayDeque<>();
                        for(GramSymbol<U> nextSym: window){
                            LR1State st = cpy_oldStateStack.peek();
                            Action ac = st.getAction(nextSym);
                            if(ac == null){
                                it.add(nowSym); // restore window
                                continue outerdel;
                            }
                            if (ac instanceof Action.Shift sh) {
                                LR1State state = sh.state();
                                // use state
                                cpy_oldStateStack.push(state);
                                cpy_oldSymStack.push(nextSym);

                            } else if (ac instanceof Action.Reduce re) {
                                GramProd<?, ?> prod = re.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    cpy_oldSymStack.pop();
                                    cpy_oldStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.shift gotoAct = cpy_oldStateStack.peek().getAction(prod.getLhs());
                                cpy_oldStateStack.push(gotoAct.state());
                                cpy_oldSymStack.push(prod.getLhs());

                            } else if (ac instanceof Action.Accept ac) {
                                // accept
                                expectedTok = null; // no token in case of del
                                opCode = 2;
                                tokIdx = delIdx;
                                // no need to try insert and del
                                break outerdel;
                            }
                        }
                        it.add(nowSym); // restore the window by deleting the tok

                        // if we came here then we found a candTok
                        expectedTok = null; // found a token!! exit and
                        opCode = 2;
                        tokIdx = delIdx;
                        break outerdel;
                   }
                }

                // bringing back the size of window to its normal size
                for(int i = 0; i < Math.min(R, this.tokens.size()-currIdx-1); i++){
                    window.removeLast();
                }

                switch(opCode){
                    case 0:
                        currIdx++;
                        window.set(tokIdx, expectedTok);

                        Deque<LR1State> currStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<GramSymbol<U>> currSymStack = new ArrayDeque<>(oldSymStack);

                        for(GramSymbol<U> nextSym: window){
                            LR1State st = currStateStack.peek();
                            Action ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift sh) {
                                LR1State state = sh.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(nextSym);

                            } else if (ac instanceof Action.Reduce re) {
                                GramProd<?, ?> prod = re.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.shift gotoAct = currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(prod.getLhs());

                            } else if (ac instanceof Action.Accept ac) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                                               tok.getColNo(),
                                                               expectedTok,
                                                               tok);
                        errList.add(errParse);
                        continue;

                    case 1:
                        currIdx++;
                        window.add(tokIdx, expectedTok);

                        Deque<LR1State> currStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<GramSymbol<U>> currSymStack = new ArrayDeque<>(oldSymStack);

                        for(GramSymbol<U> nextSym: window){
                            LR1State st = currStateStack.peek();
                            Action ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift sh) {
                                LR1State state = sh.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(nextSym);

                            } else if (ac instanceof Action.Reduce re) {
                                GramProd<?, ?> prod = re.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.shift gotoAct = currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(prod.getLhs());

                            } else if (ac instanceof Action.Accept ac) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                                               tok.getColNo(),
                                                               expectedTok,
                                                               tok);
                        errList.add(errParse);
                        continue;

                    case 2:
                        currIdx++;
                        window.remove(tokIdx);

                        Deque<LR1State> currStateStack = new ArrayDeque<>(oldStateStack);
                        Deque<GramSymbol<U>> currSymStack = new ArrayDeque<>(oldSymStack);

                        for(GramSymbol<U> nextSym: window){
                            LR1State st = currStateStack.peek();
                            Action ac = st.getAction(nextSym);

                            if(ac == null){
                                // we shouldn't get here
                            }

                            if (ac instanceof Action.Shift sh) {
                                LR1State state = sh.state();
                                // use state
                                currStateStack.push(state);
                                currSymStack.push(nextSym);

                            } else if (ac instanceof Action.Reduce re) {
                                GramProd<?, ?> prod = re.prod();
                                // use prod
                                for(GramSymbol<U> rhsSym: prod.getRhs()){
                                    currSymStack.pop();
                                    currStateStack.pop();
                                }
                                // warning: here assuming gotoAct will always shift
                                Action.shift gotoAct = currStateStack.peek().getAction(prod.getLhs());
                                currStateStack.push(gotoAct.state());
                                currSymStack.push(prod.getLhs());

                            } else if (ac instanceof Action.Accept ac) {
                                // accept
                                currAccepted = true;
                            }
                        }
                        ParseErr<U> errParse  = new ParseErr<>(tok.getLineNo(),
                                                               tok.getColNo(),
                                                               expectedTok,
                                                               tok);
                        errList.add(errParse);
                        continue;
                }
            }

            // no error on this tok
            // update currStateStack and currSymStack
            if (action instanceof Action.Shift shift) {
                LR1State state = shift.state();
                // use state

            } else if (action instanceof Action.Reduce reduce) {
                GramProd<T, U> prod = reduce.prod();
                // use prod

            } else if (action instanceof Action.Accept accept) {
                // accept
            }


        }
        // curr has accepted. we need to consume the tokens in the window.


    }

    public abstract void setup();


}
