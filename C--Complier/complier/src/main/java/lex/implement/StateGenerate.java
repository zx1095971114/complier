package lex.implement;

import lex.entity.State;
import lex.entity.StateList;

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
        String[] attribute = null; //存所有状态转移表的所有属性，即initialStateTable的第一行
        if(scanner.hasNextLine()){
            attribute = scanner.nextLine().split(",");
        }

        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split(",");
            int stateId = -1;
            boolean start = false;
            String[] endSymbol = new String[0];
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
                        if(!line[i].equals("NOT_END")){
                            endSymbol = line[i].split("/");
                        }
                        break;
                    default:
                }
            }

            //取出状态转移的部分
            int attributeNum = 3; //State类中除状态转移Map外的属性数
            Integer[][] move = new Integer[line.length - attributeNum][];
            for(int i = 0; i < move.length; i++){
                String[] moveString = line[i + attributeNum].split("/");
                Integer[] moveInteger = new Integer[moveString.length];
                for(int j = 0; j < moveInteger.length; j++){
                    moveInteger[j] = Integer.parseInt(moveString[j]);
                }

                move[i] = moveInteger;
            }
            Map<String, Integer[]> moveMap = new HashMap<>();
            for(int i = 0; i < move.length; i++){
                moveMap.put(attribute[i + attributeNum], move[i]);
            }

            State state = new State(stateId, start, endSymbol, moveMap);

            stateList.add(state);
        }

        return stateList;
    }

    /**
     * @param nfa:
     * @return List<State>
     * @author ZhouXiang
     * @description 将NFA确定化
     * @exception
     */
    List<State> determineNFA(List<State> nfa){
        List<StateList> dfa1 = new ArrayList<>();

        List<State> begin = new ArrayList<>();
        //找到初始状态集合
        for(State state: nfa){
            if(state.isStart()){
                begin.add(state);
            }
        }

        bfs(new StateList(begin, 0), dfa1, 1, nfa);

        //将StateList转为State
        List<State> dfa = new ArrayList<>();
        for(StateList stateList: dfa1){
            dfa.add(stateList.turn2State());
        }

        return dfa;
    }

    //广度优先搜索整个nfa
    private void bfs(StateList stateList, List<StateList> result, int id, List<State> fa){
        List<StateList> tempStateList = new ArrayList<>(); //暂存一行中状态转移的StateList

        //获得暂存的一行的状态转移的StateList
        State state0 = stateList.getStates().get(0);
        String[] inputs = state0.getMoveMap().keySet().toArray(new String[0]);
        for(String input: inputs){
            List<State> states = new ArrayList<>();
            for(State state: stateList.getStates()){
                states.addAll(state.getMove(fa, input));
            }
        }

        //判断每一个StateList是否已经在收集的结果里了
        for(StateList list: tempStateList){
            boolean isRepeat = false;
            for(StateList stateList1: result){
                if(stateList1.equals(list)){
                    isRepeat = true;
                    break;
                }
            }

            if(!isRepeat){
                result.add(list);
                id++;
                bfs(list, result, id, fa);
            }
        }
    }


}
