package lex.entity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: NFA
 * @author: Zhou xiang
 * @description: nfa实体
 * @date: 2023/4/8 16:01
 * @version: 1.0
 */
public class NFA {
    private List<State> allStates; //所有状态的集合
    private String[] alpha; //字母表
    private List<Map<String, Integer[]>> functionList; //状态转化表，因为是NFA，所以一个输入字符可能对应多个输出
    private List<State> startStates; //初始状态集合
    private List<State> endStates; //终止状态集合

    private NFA(){
        allStates = new ArrayList<>();
        alpha = new String[0];
        functionList = new ArrayList<>();
        startStates = new ArrayList<>();
        endStates = new ArrayList<>();
    }

    /**
     * @param states:
     * @return NFA
     * @author ZhouXiang
     * @description 获取一个nfa实例
     * @exception
     */
    public static NFA getNFAInstance(List<State> states){
        NFA nfa = new NFA();
        nfa.allStates = states;

        assert states != null;
        State state = states.get(0);
        nfa.alpha = state.getMoveMap().keySet().toArray(new String[0]);

        for(State state1: states){
            nfa.functionList.add(state1.getMoveMap());
            if(state1.isStart()){
                nfa.startStates.add(state1);
            }
            if(state1.isEnd()){
                nfa.endStates.add(state1);
            }
        }

        return nfa;
    }

    public List<State> getAllStates() {
        return allStates;
    }

    public String[] getAlpha() {
        return alpha;
    }

    public List<Map<String, Integer[]>> getFunctionList() {
        return functionList;
    }

    public List<State> getStartStates() {
        return startStates;
    }

    public List<State> getEndStates() {
        return endStates;
    }

    /**
     * @param origin:
     * @param input:
     * @return State
     * @author ZhouXiang
     * @description 在nfa中，遇到输入input，应该返回的状态集合
     * @exception
     */
    public StateList move(State origin, String input){
        int originId = origin.getStateId();
        Map<String, Integer[]> map = this.functionList.get(originId - 1);
        Integer[] stateInteger = map.get(input);

        List<State> resultList = new ArrayList<>();
        for (Integer id: stateInteger){
            State s = Util.getStateById(id, allStates);
            resultList.add(s);
        }

        return new StateList(resultList);
    }

    /**
     * @param :fileName NFA表文件所在路径
     * @return NFA
     * @author ZhouXiang
     * @description 获取初始的NFA
     * @exception
     */
    public static NFA generateNFA(String fileName) throws FileNotFoundException {
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
                        endSymbol = line[i].split("/");
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

        return NFA.getNFAInstance(stateList);
    }


    /**
     * @param nfa:
     * @return List<State>
     * @author ZhouXiang
     * @description 将NFA确定化
     * @exception
     */
    public DFA determineNFA(){
        List<StateList> dfa1 = new ArrayList<>();

        StateList beginList = new StateList(startStates).moveWithBlank(this);
        dfa1.add(beginList);
        bfs(beginList, dfa1);

        //将StateList转为State
        List<State> dfa = new ArrayList<>();
        int cnt = 1;
        for(StateList stateList: dfa1){
            stateList.setStateListId(cnt);
            cnt++;
            dfa.add(stateList.turn2State());
        }

        return DFA.getDFAInstance(dfa);
    }

    //广度优先搜索整个nfa
    private void bfs(StateList stateList, List<StateList> result){
        List<StateList> tempStateList = new ArrayList<>(); //暂存一行中状态转移的StateList

        //求该stateList的所有a弧转换
        for(String input: alpha){
            tempStateList.add(stateList.moveWithInput(input, this));
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
                bfs(list, result);
            }
        }
    }
}
