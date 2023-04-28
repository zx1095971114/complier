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
    private String type; //符号种别
    private String content; //符号对应的字符串内容

    public Token(String type, String content) {
        this.type = type;
        this.content = content;
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

}
