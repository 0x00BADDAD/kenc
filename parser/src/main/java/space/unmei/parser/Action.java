package space.unmei.parser;

public interface Action {
    record Shift(LR1State state) implements Action {}

    record Reduce(GramProd<?, ?> prod) implements Action {}

    record Accept() implements Action {}
}
