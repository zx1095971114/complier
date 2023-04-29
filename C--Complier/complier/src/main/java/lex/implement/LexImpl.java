package lex.implement;

import lex.entity.DFA;
import lex.entity.NFA;
import lex.entity.State;
import lex.entity.Token;
import lex.itf.Lex;
import log.Log;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


import static lex.entity.State.*;

/**
 * @projectName: complier
 * @package: lex
 * @className: Lex
 * @author: Zhou xiang
 * @description: 词法分析器的主程序
 * @version: 1.0
 */
public class LexImpl implements Lex {

    private static final Logger logger = LoggerFactory.getLogger(LexImpl.class);
    /**
     * @param input: 输入的字符串
     * @return List<Token> 识别出的符号表
     * @author ZhouXiang
     * @description 将输入的字符串经过词法分析转为对应的token序列。输出符号表
     */
    public List<Token> lexAnalysis(String input) throws FileNotFoundException {
        List<Token> tokens = new ArrayList<>();
        //加空格是为了保证能读取最后的token
        //当最后的字符的currentState为起始状态时，证明最后是由空格和\n组成的符号串，不该加入tokens，加入" "无影响
        //当最后的字符的currentState为非终结状态时，证明到最后了还没读到可判断断词的字符，说明出现错误，加入" "无影响
        //当最后的字符的currentState为终结状态时，因为所有终结状态遇到" "都会断词，且不会将最后一个" "读进，加入" "能帮助断词，否则该词就无法读进了
        input = input + " ";

        //获取dfa
        String fileName = "D:\\大学\\课程\\编译原理\\My大作业\\C--Complier\\complier\\src\\main\\resources\\initialStateTable.csv";
        NFA nfa = NFA.generateNFA(fileName);
        DFA dfa = nfa.determineNFA();
        dfa.minimizeDFA();

        State currentState = dfa.getStartState(); //字符当前所处的状态
        int currentNum = 0; //当前识别到哪一个字符
        int startNum = 0; //每个token内容的开始字符
        int endNum = 0; //每个token内容的结束字符

        while (currentNum < input.length()){
            //字符预处理
            char currentChar = input.charAt(currentNum);
            String currentLetter = "";
            if(Character.isDigit(currentChar)){
                currentLetter = "digit";
            }else if(Character.isLetter(currentChar)){
                currentLetter = "letter";
            }else if(currentChar == ','){
                currentLetter = "dot";
            }else {
                currentLetter = String.valueOf(currentChar);
            }
            if(!Arrays.asList(dfa.getAlpha()).contains(currentLetter)){
                currentLetter = "others";
            }

            //找开始位置,会将空格，\n都去除
            if(currentState.isStart()){
                startNum = currentNum;
            }

//            if(currentChar == '}'){
//                int a = 1;
//            }

            //找结束位置
            State nextState = dfa.move(currentState, currentLetter);
            if(nextState == null){
                if(currentState.isEnd()){
                    endNum = currentNum;
                    String content = input.substring(startNum, endNum);
                    String type = "";
                    if(currentState.getEndSymbol().length == 1){
                        type = currentState.getEndSymbol()[0];
                    }else {
                        if(content.equals("int") || content.equals("void") || content.equals("return") || content.equals("const")){
                            type = "KW";
                        }else {
                            type = "IDN";
                        }
                    }

                    Token token = new Token(type, content);
                    tokens.add(token);

                    currentState = dfa.getStartState();
                }else {
                    String errorInfo = currentLetter + "不能被识别，它是输入的第" + currentNum + "个字符";
                    Log.errorLog(errorInfo, logger);
                    System.exit(1);
                }
            }else {
                currentState = nextState;
                //后移currentNum
                currentNum++;
            }
        }

        //处理负数
        //找到应该合并的位置
        List<Integer> list = new ArrayList<>();
        for(int i = 0; i < tokens.size(); i++){
            Token before = null;
            if(i != 0){
                before = tokens.get(i - 1);
            }
            Token cur = tokens.get(i);
            Token after = null;
            if(i != tokens.size() - 1){
                after = tokens.get(i + 1);
            }

            if(cur.getContent().equals("-") && after != null && after.getType().equals("INT")){
                if(before == null || before.getType().equals("KW") || before.getType().equals("OP")){
                    list.add(new Integer(i));
                }else {
                    if(before.getContent().equals("(") || before.getContent().equals("{")){
                        list.add(new Integer(i));
                    }
                }
            }
        }

        List<Token> result = new ArrayList<>();
        int skip = 0;
        for(int i = 0; i < tokens.size() - list.size(); i++){
            if(list.contains(i + skip)){
                String content = tokens.get(i + skip).getContent() + tokens.get(i + skip + 1).getContent();
                Token token = new Token("INT", content);
                //加入结果前要计算其在语法分析中的dealing
                token.calculateDealing();
                result.add(token);
                skip++;
            }else {
                Token token = tokens.get(i + skip);
                //加入结果前要计算其在语法分析中的dealing
                token.calculateDealing();
                result.add(token);
            }
        }

        return result;
    }

    /**
     * @param inputFile: 输入文件路径
     * @param outputFile: 输出文件路径
     * @return boolean: 是否输出成功
     * @author ZhouXiang
     * @description 对输入文件的语句进行词法分析，结果输出到输出文件中
     */
    public boolean lexAnalysisToFile(String inputFile,String outputFile) throws IOException {
        //获取输入字符串
        String input = "";
        Path path = Paths.get(inputFile);
        Scanner scanner = new Scanner(path);
        while (scanner.hasNextLine()){
            input = input.concat(scanner.nextLine());
        }
//        scanner.close();

//        System.out.println(input);
        //分析，转成输出字符串
        List<Token> tokens = lexAnalysis(input);
        String output = "";
        for(Token token : tokens){
            output = output.concat(token.getContent() + "\t" + "<" + token.getType() + ">\n");
        }

        //将输出字符串输出到输出文件中
        File file = new File(outputFile);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,false);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(output);
        bw.close();

        return true;
    }
}
