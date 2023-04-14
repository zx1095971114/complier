package lex.entity;

import java.util.*;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: DFA
 * @author: Zhou xiang
 * @description: DFA实体
 * @date: 2023/4/8 16:01
 * @version: 1.0
 */
public class DFA {
    private List<State> allStates; //所有状态的集合
    private String[] alpha; //字母表
    private List<Map<String, Integer>> functionList; //状态转化表，因为是DFA，所以一个输入字符只会对应一个输出,List的序号对应状态序号 - 1
    private State startState; //唯一初态
    private List<State> endStates; //终止状态集合

    private DFA(){
        allStates = new ArrayList<>();
        alpha = new String[0];
        functionList = new ArrayList<>();
        startState = null;
        endStates = new ArrayList<>();
    }

    public static DFA getDFAInstance(List<State> states){
        DFA dfa = new DFA();
        dfa.allStates = states;

        assert states != null;
        State state = states.get(0);
        dfa.alpha = state.getMoveMap().keySet().toArray(new String[0]);

        int cnt = 0; //记录初始状态数目，超过一个会断言失败
        for(State state1: states){
            Map<String, Integer> map = new HashMap<>();
            for(Map.Entry<String, Integer[]> entry: state1.getMoveMap().entrySet()){
                String key = entry.getKey();
                Integer[] value = entry.getValue();
                assert value.length == 1;
                map.put(key, value[0]);
            }
            dfa.functionList.add(map);

            if(state1.isStart()){
                dfa.startState = state1;
                cnt++;
                assert cnt <= 1;
            }

            if(state1.isEnd()){
                dfa.endStates.add(state1);
            }
        }

        return dfa;
    }

    public List<State> getAllStates() {
        return allStates;
    }

    public String[] getAlpha() {
        return alpha;
    }

    public List<Map<String, Integer>> getFunctionList() {
        return functionList;
    }

    public State getStartState() {
        return startState;
    }

    public List<State> getEndStates() {
        return endStates;
    }

    /**
     * @param : id
     * @return State
     * @author ZhouXiang
     * @description 根据序号，返回dfa中对应的状态
     * @exception
     */
    public State getStateById(int id){
        return Util.getStateById(id, allStates);
    }

    /**
     * @param input: 遇到的输入
     * @param origin 初始状态
     * @return State
     * @author ZhouXiang
     * @description 在状态origin，遇到输入input，该转移到的唯一状态
     * @exception
     */
    public State move(State origin, String input){
        int originId = origin.getStateId();
        Map<String, Integer> map = functionList.get(originId - 1);
        return getStateById(map.get(input));
    }

    /**
     * @param :
     * @return DFA
     * @author ZhouXiang
     * @description 将DFA最小化
     * @exception
     */
    public DFA minimizeDFA(){
        //产生新字母表
        List<String> stringList = Arrays.asList(alpha);
        stringList.remove("$");
        String[] newAlpha = stringList.toArray(new String[0]);

        List<StateList> dfaList = new ArrayList<>();

        StateList temp = new StateList(endStates);
        StateList firstX = temp.moveWithBlank(this); //第一次划分时，产生的等价于终止状态的集合

        List<State> temp1 = new ArrayList<>(allStates);
        temp1.removeAll(firstX.getStates());
        StateList firstY = new StateList(temp1); //第一次划分时，产生的除了终止状态以外的集合

        List<StateList> curDfa = new ArrayList<>();
        curDfa.add(firstX);
        curDfa.add(firstY);

        for(String letter: newAlpha){

        }

        return null;
    }

    //使用某字母对目前的dfa(List<StateList>)进行一次划分,返回划分后的结果
    private List<StateList> separate(List<StateList> curList, String input){
        Queue<StateList> queue = new LinkedList<>();
        for (StateList list: curList){
            queue.offer(list);
        }
        List<StateList> oldList = new ArrayList<>(curList);

        while (!queue.isEmpty()){
            StateList tempStateList = queue.poll();

            StateList temp = tempStateList.moveWithInput(input, this);
            //看a弧转换后的结果在不在oldList里面
            boolean contain = false;
            for (StateList list: oldList){
                if(temp.equals(list)){
                    contain = true;
                    break;
                }
            }

            if(contain){

            }
        }

        return null;
    }

}
