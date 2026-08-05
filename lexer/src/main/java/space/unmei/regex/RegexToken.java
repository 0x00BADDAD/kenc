package space.unmei.regex;

public class RegexToken implements LLToken<RegexTokenType>{

    private final RegexTokenType type;
    private final String text;

    public RegexToken(RegexTokenType type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public boolean equals(Object otherObj){
        if(this == otherObj){return true;}

        if(!(otherObj instanceof RegexToken other)){
            return false;
        }
        return this.type.equals(other.type) && this.text.equals(other.text);
    }

    @Override
    public RegexTokenType getType() {
        return type;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return type + "(" + text + ")";
    }
}
