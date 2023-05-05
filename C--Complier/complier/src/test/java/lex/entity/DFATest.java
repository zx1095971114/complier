package lex.entity;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utilsTest.Util;

import java.io.FileReader;
import java.io.IOException;
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
    public static Collection<Object[]> getData() throws IOException {
        String fileNameTest = "./src/test/resources/minimizeTest.csv";

        List<Map<String, Integer>> testList = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(fileNameTest));
        String[] attribute = scanner.nextLine().split(",");

//        String filePath = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\testListAndAttribute.txt";
//        StringBuilder content = new StringBuilder();
//        content.append("attribute: ");
//        for (String attr: attribute){
//            content.append(attr + " ");
//        }
//        content.append("\n\n");

        while (scanner.hasNextLine()) {
            String[] line = scanner.nextLine().split(",");
            Map<String, Integer> map = new HashMap<>();

            for (int i = 0; i < line.length; i++) {
                Integer integer = Integer.parseInt(line[i]);

//                if(i == 0){
//                    map.put("letter", integer);
//                    continue;
//                }

                map.put(attribute[i], integer);
            }
            testList.add(map);
        }

//        for (Map<String, Integer> map: testList){
//            for(Map.Entry<String, Integer> entry: map.entrySet()){
//                String key = entry.getKey();
//                Integer value = entry.getValue();
//                content.append(key + ": " + value + "       ");
//            }
//            content.append("\n");
//        }
//        Util.writeFIle(filePath, content.toString());


        String fileNameSrc = "config/initialStateTable.csv";
        NFA nfa = NFA.generateNFA(fileNameSrc);
        DFA dfa = nfa.determineNFA();
        dfa = dfa.minimizeDFA();

        String filePath = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\dfaFunction.txt";
        StringBuilder content = new StringBuilder();
        List<Map<String, Integer>> function = dfa.getFunctionList();
        for (int i = 0; i < function.size(); i++){
            content.append(i + ": ");
            Map<String, Integer> map = function.get(i);
            for(Map.Entry<String, Integer> entry: map.entrySet()){
                String key = entry.getKey();
                Integer value = entry.getValue();
                content.append(key + ": " + value + "    ");
            }
            content.append("\n");
        }
        Util.writeFIle(filePath, content.toString());

//        System.out.println("nfa size: " + nfa.getAllStates().size());
//        System.out.println("dfa size: " + dfa.getAllStates().size());

        Object[] test = new Object[2];
        test[0] = testList;
        test[1] = dfa;

        List<Object[]> result = new ArrayList<>();
        result.add(test);

        return result;
    }


    //这个方法在检测时错误根据编译器的不同时有时没有，暂时请忽略这个检测方法
    //只检测转换表
    @Test
    public void minimizeDFA() throws IOException {
        List<Map<String, Integer>> function = minDfa.getFunctionList();
//        String filePath = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\debug1.txt";
//        StringBuilder content = new StringBuilder();
//        for (Map<String, Integer> map: function){
//            for(Map.Entry<String, Integer> entry: map.entrySet()){
//                String key = entry.getKey();
//                Integer value = entry.getValue();
//                content.append(key + ": " + value + "       ");
//            }
//            content.append("\n");
//        }
//        Util.writeFIle(filePath, content.toString());

        for(int i = 0; i < function.size(); i++){
            Map<String, Integer> testMap = function.get(i);
            Map<String, Integer> expectedMap = expectedFunction.get(i);

//            Integer b = expectedMap.get("letter");

            Set<String> alphas = testMap.keySet();
            for(String alpha: alphas){
                Integer test = testMap.get(alpha);
                Integer expected = expectedMap.get(alpha);

                if(!test.equals(expected)){
                    String debug = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\test\\resources\\debug0.txt";
                    StringBuilder content0 = new StringBuilder();
                    content0.append("alpha: " + alpha + "\n"
                            + "i: " + i + "\n"
                            + "expected: " + expected + "\n"
                            + "test: " + test + "\n");
                    Util.writeFIle(debug, content0.toString());
                    System.out.println("here");
                }

                assertEquals(expected, test);
            }
        }
    }
}