package space.unmei.parser;

import java.util.*;
import java.lang.FunctionalInterface;

@FunctionalInterface
public interface ReduceAction<T, U> {
    void apply(
        GramProd<T, U> prod,
        Deque<LR1State<T, U>> stateStack,
        Deque<Pair<T, GramSymbol<U>>> symStack
    );
}
