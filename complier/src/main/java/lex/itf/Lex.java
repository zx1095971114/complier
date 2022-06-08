package lex.itf;

/**
 * @projectName: complier
 * @package: lex.itf
 * @className: Lex
 * @author: Zhou xiang
 * @description: 词法分析器暴露的接口
 * @version: 1.0
 */
public interface Lex {
    /**
     * @param input: 输入的字符串
     * @return String token序列所在的文件名
     * @author ZhouXiang
     * @description 将输入的字符串经过词法分析转为对应的token序列。输入到对应文件中
     */
    public String LexAnalysis(String input);
}
