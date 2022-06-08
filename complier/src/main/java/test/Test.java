package test;

import lex.implement.LexImpl;

import java.io.File;
import java.io.IOException;

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
        String inputFile = "./src/main/lexTest/testB/test2.sql";
        String outputFile = "./src/main/lexTest/testB/lexTest2.tsv";
        LexImpl lexImpl = new LexImpl();
        if(lexImpl.lexAnalysisToFile(inputFile,outputFile)){
            System.out.println("ok");
        }

    }
}
