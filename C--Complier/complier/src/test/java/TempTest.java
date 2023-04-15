import lex.entity.DFA;
import lex.entity.NFA;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @projectName: complier
 * @package: PACKAGE_NAME
 * @className: TempTest
 * @author: Zhou xiang
 * @description: 调试用
 * @date: 2023/4/9 9:17
 * @version: 1.0
 */
public class TempTest {
    public static void main(String[] args) throws FileNotFoundException {

        String fileNameSrc = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\main\\resources\\initialStateTable.csv";
        String fileNameTest = "";
        NFA nfa = NFA.generateNFA(fileNameSrc);

        DFA dfa = nfa.determineNFA();
        int b = 0;


    }
}
