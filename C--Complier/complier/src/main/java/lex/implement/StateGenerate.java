package lex.implement;

import lex.entity.State;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @projectName: complier
 * @package: lex.implement
 * @className: StateGenerate
 * @author: Zhou xiang
 * @description: 状态转换图的初始化
 * @date: 2023/4/7 17:30
 * @version: 1.0
 */
class StateGenerate {
    /**
     * @param :fileName NFA表文件所在路径
     * @return List<State>
     * @author ZhouXiang
     * @description 获取初始的NFA
     * @exception
     */
    List<State> generateNFA(String fileName) throws FileNotFoundException {
        List<State> stateList = new ArrayList<>();
        Scanner scanner = new Scanner(new FileReader(fileName));
        String[] attribute = null;
        if(scanner.hasNextLine()){
            attribute = scanner.nextLine().split(",");
        }

        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split(",");
            int stateId = -1;
            boolean start = false;
            String endSymbol = null;
            //从表中读除了状态转移以外的数据
            for (int i = 0; i < line.length; i++){
                switch (attribute[i]){
                    case "stateId":
                        stateId = Integer.parseInt(line[i]);
                        break;
                    case "start":
                        if(line[i].equals("TRUE")){
                            start = true;
                        }
                        break;
                    case "endSymbol":
                        endSymbol = line[i];
                        break;
                    default:
                }
            }

            //取出状态转移的部分
            int attributeNum = 3;
            int[] move = new int[line.length - attributeNum];
            for(int i = 0; i < move.length; i++){
                move[i] = Integer.parseInt(line[i + attributeNum]);
            }

            String[] finalAttribute = attribute;
            State state = new State(stateId, start, endSymbol, Arrays.copyOfRange(finalAttribute, attributeNum, finalAttribute.length)){
                @Override
                //处理状态转移
                public int getNextStateId(char input){
                    for(int i = 0; i < move.length; i++){
                        if (finalAttribute[i + attributeNum].equals("letter")) {
                            if(Character.isLetter(input)){
                                return move[i];
                            }
                        } else if (finalAttribute[i + attributeNum].equals("digit")) {
                            if(Character.isDigit(input)){
                                return move[i];
                            }
                        } else {
                            if(Character.toString(input).equals(finalAttribute[i])){
                                return move[i];
                            }
                        }
                    }
                    return -1;
                }
            };
            stateList.add(state);
        }

        return stateList;
    }

    List<State> determineNFA(List<State> nfa){
        List<State> dfa = new ArrayList<>();

    }
}
