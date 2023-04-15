package lex.entity;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class NFATest {
    private List<Map<String, Integer[]>> expectedNfaFunction;
    private List<Map<String, Integer>> expectedDfaFunction;
    private NFA nfa;

    @Parameterized.Parameters
    public static Collection<Object[]> getData() throws FileNotFoundException {
        String fileNameTest1 = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\initialTest.csv";
        String fileNameTest2 = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\determineTest.csv";

        List<Map<String, Integer[]>> testList1 = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(fileNameTest1));
        String[] attribute = scanner.nextLine().split(",");
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            Map<String, Integer[]> map = new HashMap<>();

            for (int i = 0; i < line.length; i++) {
                String[] integerStrings = line[i].split("/");
                Integer[] integers = new Integer[integerStrings.length];
                for (int j = 0; j < integers.length; j++) {
                    integers[j] = Integer.parseInt(integerStrings[j]);
                }

                map.put(attribute[i], integers);
            }
            testList1.add(map);
        }

        List<Map<String, Integer>> testList2 = new ArrayList<>();
        scanner = new Scanner(new FileReader(fileNameTest2));
        attribute = scanner.nextLine().split(",");
        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            Map<String, Integer> map = new HashMap<>();

            for (int i = 0; i < line.length; i++) {
                Integer integer = Integer.parseInt(line[i]);
                map.put(attribute[i], integer);
            }
            testList2.add(map);
        }

        String fileNameSrc = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\main\\resources\\initialStateTable.csv";
        NFA nfa = NFA.generateNFA(fileNameSrc);

        Object[] test = new Object[3];
        test[0] = testList1;
        test[1] = testList2;
        test[2] = nfa;

        List result = new ArrayList<>();
        result.add(test);
        return result;
    }

    public NFATest(List<Map<String, Integer[]>> list1, List<Map<String, Integer>> list2, NFA nfa){
        this.expectedNfaFunction = list1;
        this.expectedDfaFunction = list2;
        this.nfa = nfa;
    }




    //此处为简便，只测nfa的转换关系是否正确，只能保证转换关系没出错
    @Test
    public void generateNFA() throws FileNotFoundException {
        List<Map<String, Integer[]>> function = nfa.getFunctionList();

        for(int i = 0; i < function.size(); i++){
            Map<String, Integer[]> testMap = function.get(i);
            Map<String, Integer[]> expectedMap = expectedNfaFunction.get(i);

            Set<String> alphas = testMap.keySet();
            for(String alpha: alphas){
                Integer[] test = testMap.get(alpha);
                Integer[] expected = expectedMap.get(alpha);

                assertArrayEquals(test, expected);
            }
        }
    }

    @Test
    public void determineNFA() {
        DFA dfa = nfa.determineNFA();
        List<Map<String, Integer>> function = dfa.getFunctionList();

        for(int i = 0; i < function.size(); i++){
            Map<String, Integer> testMap = function.get(i);
            Map<String, Integer> expectedMap = expectedDfaFunction.get(i);

            Set<String> alphas = testMap.keySet();
            for(String alpha: alphas){
                Integer test = testMap.get(alpha);
                Integer expected = expectedMap.get(alpha);

//                if(!test.equals(expected)){
//                    int a = 3;
//                }

                assertEquals(test, expected);
            }
        }
    }
}