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

    private String dealing;

    public Token(String type, String content,String symbol) {
        this.type = type;
        this.content = content;
        this.symbol = symbol;

        calculateDealing();
    }

    public void calculateDealing(){
        if(this.type.equals("KW") || this.type.equals("SE") || this.type.equals("OP")){
            this.dealing = this.content.toUpperCase();
            if(this.dealing.equals("GROUP BY")){
                this.dealing = "GROUP_BY";
            }
            if(this.dealing.equals("ORDER BY")){
                this.dealing = "ORDER_BY";
            }
        } else if (this.type.equals("STR")) {
            this.dealing = "STRING";
        } else {
            this.dealing = this.type;
        }
    }

    public Token() {
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public String getDealing() {
        return dealing;
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
