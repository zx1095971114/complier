package yacc.entity;

/**
 * @projectName: complier
 * @package: yacc.entity
 * @className: Analysis
 * @author: Zhou xiang
 * @description: 分析过程类
 * @date: 2022/6/12 14:49
 * @version: 1.0
 */
public class Analysis {
    private String no;
    private String roolNo;
    private String stackSym;
    private String faceSym;
    private String action;

    public Analysis(String no, String roolNo, String stackSym, String faceSym, String action) {
        this.no = no;
        this.roolNo = roolNo;
        this.stackSym = stackSym;
        this.faceSym = faceSym;
        this.action = action;
    }

    public Analysis() {
    }

    public String getNo() {
        return no;
    }

    public String getRoolNo() {
        return roolNo;
    }

    public String getStackSym() {
        return stackSym;
    }

    public String getFaceSym() {
        return faceSym;
    }

    public String getAction() {
        return action;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setRoolNo(String roolNo) {
        this.roolNo = roolNo;
    }

    public void setStackSym(String stackSym) {
        this.stackSym = stackSym;
    }

    public void setFaceSym(String faceSym) {
        this.faceSym = faceSym;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
