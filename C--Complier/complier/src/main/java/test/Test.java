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
import java.util.Scanner;

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
    public static void doTest(String inputFile, String outputFileLex, String outputFileYacc) throws IOException {
        Yacc yacc = new YaccImpl();
        Lex lex = new LexImpl();

        if(lex.lexAnalysisToFile(inputFile,outputFileLex)){
            System.out.println("succeed in lexical analysis");
        }

        String input = readFile(inputFile);
        List<Token> tokens = lex.lexAnalysis(input);

        if(yacc.printYacc(tokens,outputFileYacc)){
            System.out.println("succeed in parsing");
        }
    }


    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入input文件路径(绝对路径)");
        String inputFile = sc.nextLine();
        System.out.println("\n请输入词法分析后文件输出路径(绝对路径)");
        String outputFileLex = sc.nextLine();
        System.out.println("\n请输入语法分析后文件输出路径(绝对路径)");
        String outputFileYacc = sc.nextLine();
        System.out.println("");

//        String inputFile = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00.txt";
//        String outputFileLex = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00_lexical.txt";
//        String outputFileYacc = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\Test1\\00_grammar.txt";

        doTest(inputFile, outputFileLex, outputFileYacc);

//        printPredictTable();
    }


    //打印预测分析表使用
    public static void printPredictTable() throws IOException {
        String grammarFile = "config/grammar.txt";
        Grammar grammar = Grammar.getGrammarByFile(grammarFile, "program");
        String out = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\生成结果\\predictTable.csv";
        grammar.printPredictTable(out);
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
