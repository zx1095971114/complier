import lex.entity.DFA;
import lex.entity.NFA;

import java.io.FileNotFoundException;

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

        String fileName = "./src/main/resources/initialStateTable.csv";
        NFA nfa = NFA.generateNFA(fileName);
        DFA dfa = nfa.determineNFA();
        int a = 0;
    }
}
