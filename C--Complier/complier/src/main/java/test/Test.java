package test;

import lex.entity.Token;
import lex.implement.LexImpl;
import lex.itf.Lex;
import yacc.entity.Grammar;
import yacc.implement.YaccImpl;
import yacc.itf.Yacc;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @projectName: complier
 * @package: test
 * @className: Test
 * @author: Zhou xiang
 * @description: 测试类
 * @date: 2022/6/9 0:09
 * @version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        String inputFile = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00.txt";
        String outputFileLex = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00_lexical.txt";

        String outputFileYacc = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00_grammar.txt";
//        String outputFileFirstAndFollow = "./src/main/myTest/testA/grammarTest1.tsv";

        Yacc yacc = new YaccImpl();
        Lex lex = new LexImpl();

        if(lex.lexAnalysisToFile(inputFile,outputFileLex)){
            System.out.println("ok1");
        }
//        if(yacc.printFirstAndFollow(grammar,outputFileFirstAndFollow)){
//            System.out.println("ok2");
//        }


        String input = readFile(inputFile);
        List<Token> tokens = lex.lexAnalysis(input);

        if(yacc.printYacc(tokens,outputFileYacc)){
            System.out.println("ok3");
        }


    }

    private static String readFile(String filePath) throws IOException {
        //BufferedReader是可以按行读取文件
        FileInputStream inputStream = new FileInputStream(filePath);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String content = "";
        String str = null;
        while((str = bufferedReader.readLine()) != null)
        {
            content = content + str;
        }

        //close
        inputStream.close();
        bufferedReader.close();

        return content;
    }
}
