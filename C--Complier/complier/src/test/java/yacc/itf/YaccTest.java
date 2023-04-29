package yacc.itf;

import lex.entity.Token;
import lex.implement.LexImpl;
import lex.itf.Lex;
import org.junit.Assert;
import org.junit.Test;
import utils.Util;
import yacc.entity.Grammar;
import yacc.implement.YaccImpl;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;

public class YaccTest {

    @Test
    public void printYacc() throws IOException {
        String dir = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources";
        Lex lex = new LexImpl();
        Yacc yacc = new YaccImpl();

        for(int i = 0; i < 4; i++){
            String inputFile = dir + "\\" + i + "\\" + i + ".txt";
            String testFile = dir + "\\" + i + "\\grammar.txt";
            String expectedFile = dir + "\\" + i + "\\" + i + "_grammar.txt";

            String input = Util.readFile(inputFile);
            List<Token> tokens = lex.lexAnalysis(input);
            yacc.printYacc(tokens, testFile);

            String expected = Util.readFile(expectedFile);
            String test = Util.readFile(testFile);

            Assert.assertEquals(expected, test);
        }
    }
}