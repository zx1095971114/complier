package lex.itf;

import lex.entity.Token;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

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
     * @return List<Token> 识别出的符号表
     * @author ZhouXiang
     * @description 将输入的字符串经过词法分析转为对应的token序列，输出Token序列
     */
    public List<Token> lexAnalysis(String input) throws IOException;

    /**
     * @param inputFile: 输入文件名
     * @param outputFile: 输出文件名
     * @return boolean: 是否输出成功
     * @author ZhouXiang
     * @description 对输入文件的语句进行词法分析，结果输出到输出文件中
     */
    public boolean lexAnalysisToFile(String inputFile,String outputFile) throws IOException;
}
