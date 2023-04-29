package yacc.itf;

import lex.entity.Token;
import yacc.entity.Grammar;

import java.io.IOException;
import java.util.List;

/**
 * @projectName: complier
 * @package: yacc.itf
 * @className: itf
 * @author: Zhou xiang
 * @description: 语法分析暴露的接口
 * @date: 2022/6/11 9:25
 * @version: 1.0
 */
public interface Yacc {
    /**
     * @param tokens: 词法分析产生的序列
     * @param filePath: 语法分析产生文件的路径
     * @return boolean 语法分析是否成功
     * @author ZhouXiang
     * @description 根据词法分析的结果，产生语法分析的结果，输入到文件中
     */
    public boolean printYacc(List<Token> tokens, String filePath) throws IOException;

    /**
     * @param grammar: 要产生first集合的语法
     * @param filePath: 打印文件路径
     * @return boolean 是否成功
     * @author ZhouXiang
     * @description 产生某一语法对应的First集合, 暂时弃用
     */
    public boolean printFirstAndFollow(Grammar grammar,String filePath) throws IOException;
}
