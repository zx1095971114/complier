package lex.implement;

import ch.qos.logback.core.util.FileUtil;
import lex.itf.Lex;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.Assert.*;

public class LexImplTest {

    @Test
    public void lexAnalysisToFile() throws IOException {
        String dir = "./src/test/resources";
        Lex lex = new LexImpl();
        for(int i = 0; i < 5; i++){
            String inputFile = dir + "/" + i + "/" + i + ".txt";
            String testFile = dir + "/" + i + "/38lex.txt";
            String expectedFile = dir + "/" + i + "/" + i + "_lexical.txt";

            lex.lexAnalysisToFile(inputFile, testFile);

            Scanner sc = new Scanner(new File(testFile));
            StringBuilder testBuilder = new StringBuilder();
            while (sc.hasNext()){
                String line = sc.nextLine();
                testBuilder.append(line + System.lineSeparator());
            }
            String test = testBuilder.toString();

            sc = new Scanner(new File(expectedFile));
            StringBuilder expectedBuilder = new StringBuilder();
            while (sc.hasNext()){
                String line = sc.nextLine();
                expectedBuilder.append(line + System.lineSeparator());
            }
            String expected = expectedBuilder.toString();
            assertEquals(expected, test);
        }
    }
}