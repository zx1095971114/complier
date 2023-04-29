package lex.entity;

import log.Log;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: Token
 * @author: Zhou xiang
 * @description: token序列类
 * @version: 1.0
 */
public class Token {
    private static final Logger log = LoggerFactory.getLogger(Token.class); //记录错误日志
    private String type; //符号种别
    private String content; //符号对应的字符串内容

    private String dealing; //符号在进行语法分析时的存在形式

    public Token(String type, String content) {
        this.type = type;
        this.content = content;
        this.dealing = null;
    }

    public Token() {
    }

    public String getDealing() {
        return dealing;
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

    //计算该token对应的dealing
    public void calculateDealing(){
        switch (this.type){
            case "KW":
            case "OP":
            case "SE":
            case "#":
                this.dealing = this.content;
                break;
            case "IDN":
            case "INT":
                this.dealing = this.type;
                break;
            default:
                Log.errorLog("错误的token类型:" + this.type, log);
                break;
        }
    }
}
