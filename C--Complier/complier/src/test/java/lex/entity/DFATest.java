package lex.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class DFATest {
    private List<Map<String, Integer>> expectedFunction;
    private DFA minDfa;

    public DFATest(List<Map<String, Integer>> list, DFA dfa){
        this.expectedFunction = list;
        this.minDfa = dfa;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> getData() throws FileNotFoundException {
        String fileNameTest = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\minimizeTest.csv";

        List<Map<String, Integer>> testList = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(fileNameTest));
        String[] attribute = scanner.nextLine().split(",");
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            Map<String, Integer> map = new HashMap<>();

            for (int i = 0; i < line.length; i++) {
                Integer integer = Integer.parseInt(line[i]);
                map.put(attribute[i], integer);
            }
            testList.add(map);
        }

        String fileNameSrc = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\main\\resources\\initialStateTable.csv";
        NFA nfa = NFA.generateNFA(fileNameSrc);
        DFA dfa = nfa.determineNFA();
        dfa.minimizeDFA();

        Object[] test = new Object[2];
        test[0] = testList;
        test[1] = dfa;

        List<Object[]> result = new ArrayList<>();
        result.add(test);

        return result;
    }

    //只检测转换表
    @Test
    public void minimizeDFA() {
        List<Map<String, Integer>> function = minDfa.getFunctionList();

        for(int i = 0; i < function.size(); i++){
            Map<String, Integer> testMap = function.get(i);
            Map<String, Integer> expectedMap = expectedFunction.get(i);

            Set<String> alphas = testMap.keySet();
            for(String alpha: alphas){
                Integer test = testMap.get(alpha);
                Integer expected = expectedMap.get(alpha);

                if(!test.equals(expected)){
                    int a = 3;
                }

                assertEquals(test, expected);
            }
        }
    }
}