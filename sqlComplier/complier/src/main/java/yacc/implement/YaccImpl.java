package yacc.implement;

import lex.entity.Token;
import yacc.entity.Analysis;
import yacc.entity.Grammar;
import yacc.entity.Rool;
import yacc.itf.Yacc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * @projectName: complier
 * @package: yacc.implement
 * @className: YaccImpl
 * @author: Zhou xiang
 * @description: Yacc的实现
 * @date: 2022/6/11 9:34
 * @version: 1.0
 */
public class YaccImpl implements Yacc {
    /**
     * @param tokens: 词法分析产生的序列
     * @param filePath: 语法分析产生文件的路径
     * @return boolean 语法分析是否成功
     * @author ZhouXiang
     * @description 根据词法分析的结果，产生语法分析的结果，输入到文件中
     */
    public boolean printYacc(List<Token> tokens, String filePath,Grammar grammar) throws IOException {
        Stack<String> stack = new Stack<>();
        Map<String,Map<String, Rool>> table = grammar.getPredictMap();

        stack.push("#");
        stack.push(grammar.getStartSymbol());
        List<Analysis> analysises = new ArrayList<>();
        Token endToken = new Token("#","#","#");
        tokens.add(endToken);

        int i = 0;
        Integer analysisNo = 1;
        while (!stack.empty()){

            Token token = tokens.get(i);
            Analysis analysis = new Analysis();

            analysis.setNo(analysisNo.toString());
            analysis.setStackSym(stack.peek());
            analysis.setFaceSym(token.getDealing());

            String now = analysis.getStackSym();
            String face = analysis.getFaceSym();


            if(now.equals(face)){
                if(now.equals("#")){
                    analysis.setAction("accept");
                    analysis.setRoolNo("/");
                    analysises.add(analysis);
                    break;
                }else {
                    analysis.setAction("move");
                    analysis.setRoolNo("/");
                    analysises.add(analysis);

                    stack.pop();
                    i++;

                }
            } else {
                Rool rool = this.dictionary(now,face,table);
                if(rool.getNo() == "error"){
                    analysis.setAction("error");
                    analysis.setRoolNo("/");
                    analysises.add(analysis);

                    System.exit(401);

                    stack.pop();
                    i++;
                } else {
                    analysis.setAction("reduction");
                    analysis.setRoolNo(rool.getNo());
                    analysises.add(analysis);

                    if(rool.getRight().equals("$")){
                        stack.pop();
                    }else {
                        stack.pop();
                        String[] rights = rool.getRight().split(" ");
                        for(int j = rights.length - 1; j >= 0; j--){
                            stack.push(rights[j]);
                        }
                    }
                }
            }

            analysisNo++;
        }

        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);

        for(Analysis analysis : analysises){
            if(analysis.getFaceSym().equals("#")){
                analysis.setFaceSym("");
            }
            if(analysis.getStackSym().equals("#")){
                analysis.setStackSym("");
            }

            if(analysis.getFaceSym().equals("ORDER_BY")){
                analysis.setFaceSym("ORDER BY");
            }
            if(analysis.getStackSym().equals("ORDER_BY")){
                analysis.setStackSym("ORDER BY");
            }

            if(analysis.getFaceSym().equals("GROUP_BY")){
                analysis.setFaceSym("GROUP BY");
            }
            if(analysis.getStackSym().equals("GROUP_BY")){
                analysis.setStackSym("GROUP BY");
            }

            String content = "";
            content = content + analysis.getNo() + "    " + analysis.getRoolNo() + "    " + analysis.getStackSym() + "#" + analysis.getFaceSym() + "    " + analysis.getAction() + "\n";
            bw.write(content);
        }

        bw.close();
        return true;

    }

    private Rool dictionary(String non,String end,Map<String,Map<String, Rool>> table){

//        System.out.println("non: " + non);
//        System.out.println("end: " + end + "\n");

        Rool result = table.get(non).get(end);
        if(result == null){
            System.out.println("nonNull: " + non);
            System.out.println("endNull: " + end);
            System.exit(886);
        }
        return table.get(non).get(end);
    }


    /**
     * @param grammar: 要产生first集合的语法
     * @param filePath: 打印文件路径
     * @return boolean 是否成功
     * @author ZhouXiang
     * @description 产生某一语法对应的First集合
     */
    public boolean printFirstAndFollow(Grammar grammar, String filePath) throws IOException {
        Map<String,List<String>> first = grammar.getFirst();
        File file = new File(filePath);
        if(!file.exists()){
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file,true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("First:\n");
        Iterator<Map.Entry<String,List<String>>> iterator = first.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String,List<String>> entry = iterator.next();
            String content = "  " + entry.getKey() + "=[";
            for (int i = 0;i < entry.getValue().size();i++){
                if(i < entry.getValue().size() - 1){
                    content = content + entry.getValue().get(i) + ", ";
                } else {
                    content = content + entry.getValue().get(i) + "],\n";
                }
            }
            content = content.replace("GROUP_BY","GROUP BY");
            content = content.replace("ORDER_BY","ORDER BY");
            bw.write(content);
        }


        bw.write("FOLLOW:\n");
        Map<String, Set<String>> follow = grammar.getNonFollow();
        Iterator<Map.Entry<String,Set<String>>> iterator1 = follow.entrySet().iterator();
        while (iterator1.hasNext()){
            Map.Entry<String,Set<String>> entry = iterator1.next();
            String content = "  " + entry.getKey() + "=[";
            for(String value : entry.getValue()){
                content = content + value + ", ";
            }
            content = content.substring(0,content.length() - 2);
            content = content + "],\n";
            content = content.replace("GROUP_BY","GROUP BY");
            content = content.replace("ORDER_BY","ORDER BY");
            bw.write(content);
        }

        bw.close();
        return true;
    }
}
