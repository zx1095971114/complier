package yacc.entity;

import java.util.List;

/**
 * @projectName: complier
 * @package: yacc.entity
 * @className: Rool
 * @author: Zhou xiang
 * @description: 描述每一条文法规则的类
 * @date: 2022/6/10 23:55
 * @version: 1.0
 */
public class Rool {
    private String no;
    private String left;
    private String right;


    public Rool(String no, String left, String right) {
        //规则序号
        this.no = no;
        //左边产生式
        this.left = left;
        //右边产生式
        this.right = right;

    }

    public Rool() {
    }

    public String getNo() {
        return no;
    }

    public String getLeft() {
        return left;
    }

    public String getRight() {
        return right;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public void setRight(String right) {
        this.right = right;
    }

}
