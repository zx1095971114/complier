package lex.implement;

import lex.entity.State;
import lex.entity.Token;
import lex.itf.Lex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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

    /**
     * @param input: 输入的字符串
     * @return List<Token> 识别出的符号表
     * @author ZhouXiang
     * @description 将输入的字符串经过词法分析转为对应的token序列。输出符号表
     */
    public List<Token> lexAnalysis(String input){
        List<Token> result = new ArrayList<>();
        State currentState = ;
        int firstLetter = 0;
        int endLetter = 0;
        int currentLetter = 0;
        //保证最后一个元素可以被读取到,
        //要加两个空格，INSERT识别的时候currentLetter会到7
        //最后空格会在S状态打转，直到currentLetter限制其退出
        input = input + "  ";

        //走状态转换图
        while (currentLetter < input.length()){
            if(currentState.isEnd()){
                //特定状态游标前移
                if(currentState == E || currentState == FLOAT || currentState == INT || currentState == OP2 || currentState == OP3 || currentState == OP5 || currentState == OP14){
                    endLetter = currentLetter - 1;
                } else{
                    endLetter = currentLetter;
                }

                //识别出相应的Token
                Token token = new Token();
                token.setContent(input.substring(firstLetter,endLetter));
                String compareContent = token.getContent().toUpperCase();
                switch (currentState){
                    case E://未处理order by和group by
                        switch (compareContent){
                            case "SELECT":
                                token.setType("KW");
                                token.setSymbol("1");
                                break;
                            case "FROM":
                                token.setType("KW");
                                token.setSymbol("2");
                                break;
                            case "WHERE":
                                token.setType("KW");
                                token.setSymbol("3");
                                break;
                            case "AS":
                                token.setType("KW");
                                token.setSymbol("4");
                                break;
                            case "INSERT":
                                token.setType("KW");
                                token.setSymbol("6");
                                break;
                            case "INTO":
                                token.setType("KW");
                                token.setSymbol("7");
                                break;
                            case "VALUES":
                                token.setType("KW");
                                token.setSymbol("8");
                                break;
                            case "VALUE":
                                token.setType("KW");
                                token.setSymbol("9");
                                break;
                            case "DEFAULT":
                                token.setType("KW");
                                token.setSymbol("10");
                                break;
                            case "UPDATE":
                                token.setType("KW");
                                token.setSymbol("11");
                                break;
                            case "SET":
                                token.setType("KW");
                                token.setSymbol("12");
                                break;
                            case "DELETE":
                                token.setType("KW");
                                token.setSymbol("13");
                                break;
                            case "JOIN":
                                token.setType("KW");
                                token.setSymbol("14");
                                break;
                            case "LEFT":
                                token.setType("KW");
                                token.setSymbol("15");
                                break;
                            case "RIGHT":
                                token.setType("KW");
                                token.setSymbol("16");
                                break;
                            case "ON":
                                token.setType("KW");
                                token.setSymbol("17");
                                break;
                            case "MIN":
                                token.setType("KW");
                                token.setSymbol("18");
                                break;
                            case "MAX":
                                token.setType("KW");
                                token.setSymbol("19");
                                break;
                            case "AVG":
                                token.setType("KW");
                                token.setSymbol("20");
                                break;
                            case "SUM":
                                token.setType("KW");
                                token.setSymbol("21");
                                break;
                            case "UNION":
                                token.setType("KW");
                                token.setSymbol("22");
                                break;
                            case "ALL":
                                token.setType("KW");
                                token.setSymbol("23");
                                break;
                            case "GROUP":
                                token.setType("KW");
                                token.setSymbol("24");
                                break;
                            case "HAVING":
                                token.setType("KW");
                                token.setSymbol("25");
                                break;
                            case "DISTINCT":
                                token.setType("KW");
                                token.setSymbol("26");
                                break;
                            case "ORDER":
                                token.setType("KW");
                                token.setSymbol("27");
                                break;
                            case "TRUE":
                                token.setType("KW");
                                token.setSymbol("28");
                                break;
                            case "FALSE":
                                token.setType("KW");
                                token.setSymbol("29");
                                break;
                            case "UNKNOWN":
                                token.setType("KW");
                                token.setSymbol("30");
                                break;
                            case "IS":
                                token.setType("KW");
                                token.setSymbol("31");
                                break;
                            case "NULL":
                                token.setType("KW");
                                token.setSymbol("32");
                                break;
                            case "AND":
                                token.setType("OP");
                                token.setSymbol("8");
                                break;
                            case "OR":
                                token.setType("OP");
                                token.setSymbol("10");
                                break;
                            case "XOR":
                                token.setType("OP");
                                token.setSymbol("12");
                                break;
                            case "NOT":
                                token.setType("OP");
                                token.setSymbol("13");
                                break;
                           default:
                               token.setType("IDN");
                               token.setSymbol(token.getContent());
                        }
                        result.add(token);
                        break;

                    case FLOAT:
                        token.setType("FLOAT");
                        token.setSymbol(token.getContent());
                        result.add(token);
                        break;

                    case INT:
                        token.setType("INT");
                        token.setSymbol(token.getContent());
                        result.add(token);
                        break;

                    case STR:
                        token.setType("STR");
//                        System.out.println(token.getContent());
//                        System.exit(2);
                        token.setSymbol(token.getContent().substring(1,token.getContent().length() -1));
                        result.add(token);
                        break;

                    case OP1:
                        token.setType("OP");
                        token.setSymbol("1");
                        result.add(token);
                        break;

                    case OP2:
                        token.setType("OP");
                        token.setSymbol("2");
                        result.add(token);
                        break;

                    case OP3:
                        token.setType("OP");
                        token.setSymbol("3");
                        result.add(token);
                        break;

                    case OP4:
                        token.setType("OP");
                        token.setSymbol("4");
                        result.add(token);
                        break;

                    case OP5:
                        token.setType("OP");
                        token.setSymbol("5");
                        result.add(token);
                        break;

                    case OP6:
                        token.setType("OP");
                        token.setSymbol("6");
                        result.add(token);
                        break;

                    case OP7:
                        token.setType("OP");
                        token.setSymbol("7");
                        result.add(token);
                        break;

                    case OP14:
                        token.setType("OP");
                        token.setSymbol("14");
                        result.add(token);
                        break;

                    case OP16:
                        token.setType("OP");
                        token.setSymbol("16");
                        result.add(token);
                        break;

                    case OP11:
                        token.setType("OP");
                        token.setSymbol("11");
                        result.add(token);
                        break;

                    case SE1:
                        token.setType("SE");
                        token.setSymbol("1");
                        result.add(token);
                        break;

                    case SE2:
                        token.setType("SE");
                        token.setSymbol("2");
                        result.add(token);
                        break;

                    case SE3:
                        token.setType("SE");
                        token.setSymbol("3");
                        result.add(token);
                        break;

                    case ERROR:
                        System.out.println("进入ERROR状态");
                        System.out.println("当前识别内容" + token.getContent());
                        System.out.println("当前识别的字母是" + currentLetter);
                        System.out.println("当前识别状态为" + currentState);
                        System.exit(0);

                    default:
                        System.out.println("在未定义状态终止");
                        System.exit(1);

                }

                //下一个元素游标的初始化
                firstLetter = endLetter;
                currentLetter = firstLetter;
                currentState = S;
            } else {
                //S状态空格,\n空转要去掉
                if(currentState == S){
                    if(input.charAt(currentLetter) == ' ' || input.charAt(currentLetter) == '\n'){
                        firstLetter++;
                    }
                }

                currentState = currentState.getNextState(input.charAt(currentLetter));
                currentLetter++;
//                if(currentLetter == input.length() - 2){
//                    System.out.println(6);
//                }
            }
        }

        //修改order by和group by
        int size = result.size();
        for(int i = 0; i < size; i++){
//            if(i == 40){
//                System.out.println("1");
//            }
            Token token = result.get(i);
            if(token.getType().equals("KW")){
                switch (token.getSymbol()){
                    case "24":
                    case "27":
                         if(i < size - 1) {
                            Token nextToken = result.get(i + 1);
                            String content = nextToken.getContent();
                            if(nextToken.getType().equals("IDN") && content.toUpperCase().equals("BY")){
                                token.setContent(token.getContent() + " " + nextToken.getContent());
                                result.remove(i + 1);
                                size--;
                            }
                        } else {
                             token.setType("IDN");
                             token.setSymbol(token.getContent());
                         }
                        break;
                }
            }
        }


        for(Token token : result){
            token.calculateDealing();
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
            output = output.concat(token.getContent() + "   " + "<" + token.getType() + "," + token.getSymbol() + ">\n");
        }

        //将输出字符串输出到输出文件中
        File file = new File(outputFile);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(output);
        bw.close();

        return true;
    }
}
