package lex.entity;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: Token
 * @author: Zhou xiang
 * @description: token序列类
 * @version: 1.0
 */
public class Token {
    private String type;
    private String content;
    private String symbol;

    public Token(String type, String content,String symbol) {
        this.type = type;
        this.content = content;
        this.symbol = symbol;
    }

    public Token() {
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }
}
