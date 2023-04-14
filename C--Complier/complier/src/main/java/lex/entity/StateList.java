package lex.entity;

import java.util.*;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: StateList
 * @author: Zhou xiang
 * @description: 状态的集合，主要用于nfa确定化和dfa最小化
 * @date: 2023/4/7 22:15
 * @version: 1.0
 */
public class StateList {
    private List<State> states; //集合中包含的状态
    private int stateListId; //状态集合编号0
    private boolean start; //标识有没有起始状态
    private String[] endSymbol; //若为结束状态，则为其对应的标识的数组，否则为null
    private Map<String, Integer[]> moveMap; //key为转移符号，value为转移到的状态集合编号

    public StateList(List<State> states, int stateListId) {
        this.states = new ArrayList<>();
        this.stateListId = stateListId;
        this.start = false;
        this.endSymbol = new String[0];
        this.moveMap = new HashMap<>();

        for(State state: this.states){
            this.addState(state);
        }
    }

    public StateList(List<State> states) {
        this.states = new ArrayList<>();
        this.stateListId = 0;
        this.start = false;
        this.endSymbol = new String[0];
        this.moveMap = new HashMap<>();

        for(State state: this.states){
            this.addState(state);
        }
    }

    public List<State> getStates() {
        return states;
    }

    public int getStateListId() {
        return stateListId;
    }

    public boolean isStart() {
        return start;
    }

    public String[] getEndSymbol() {
        return endSymbol;
    }

    public void setStateListId(int stateListId) {
        this.stateListId = stateListId;
    }

    public Map<String, Integer[]> getMoveMap() {
        return moveMap;
    }

    /**
     * @param stateList:
     * @return boolean
     * @author ZhouXiang
     * @description 判断一个状态集合是不是和另一个状态集合相同；
     * @exception 注意比较前要保证两者是针对同一个状态转换机的StateList
     */
    public boolean equals(StateList stateList){
        for(State state: states){
            if(!stateList.states.contains(state)){
                return false;
            }
        }
        return true;
    }

    /**
     * @param :
     * @return State
     * @author ZhouXiang
     * @description 将该状态集合转为对应的状态
     * @exception
     */
    public State turn2State(){
        return new State(stateListId, start, endSymbol, moveMap);
    }

    /**
     * @param :
     * @return void
     * @author ZhouXiang
     * @description 往该StateList中加入新状态
     * @exception
     */
    public void addState(State state){
        if(states.contains(state)){
            return;
        }

        this.states.add(state);
        this.endSymbol = Util.joint(endSymbol, state.getEndSymbol());
        if(state.isStart()){
            this.start = true;
        }
        for(Map.Entry<String, Integer[]> entry: state.getMoveMap().entrySet()){
            String key = entry.getKey();
            Integer[] value = entry.getValue();
            Integer[] newValue = moveMap.getOrDefault(key, new Integer[0]);
            newValue = Util.joint(newValue, value);
            moveMap.put(key, newValue);
        }
    }

    /**
     * @param stateList:
     * @return void
     * @author ZhouXiang
     * @description 向其中加入StateList
     * @exception
     */
    public void addStateList(StateList stateList){
        addStates(stateList.turn2States());
    }

    /**
     * @param states:
     * @return void
     * @author ZhouXiang
     * @description 向其中加入List<State>
     * @exception
     */
    public void addStates(List<State> states){
        for(State state: states){
            this.addState(state);
        }
    }

    /**
     * @param :
     * @return List<State>
     * @author ZhouXiang
     * @description 转为List<State>
     * @exception
     */
    public List<State> turn2States(){
        return this.states;
    }

    /**
     * @param input:
     * @return StateList
     * @author ZhouXiang
     * @description 面对某输出，在dfa中，除空弧转换以外的其他弧转换(注意结果中不能有重复的状态，且空弧的转换方式不同，空弧要找到没有新增状态为止)
     * @exception
     */
    public StateList moveWithInput(String input, DFA dfa){
        assert !input.equals("$");
        Set<State> stateSet = new HashSet<>();

        Integer[] stateIntegers = moveMap.get(input);
        for(Integer stateInteger: stateIntegers){
            stateSet.add(Util.getStateById(stateInteger, dfa.getAllStates()));
        }

        StateList list = new StateList(new ArrayList<>(stateSet));//没有加空弧转换的List

        list.addStateList(list.moveWithBlank(dfa));

        return list;
    }

    /**
     * @param input: 面对的输入
     * @param dfa: 所在的dfa
     * @return StateList
     * @author ZhouXiang
     * @description 面对某输出，在dfa中的空弧转换
     * @exception
     */
    public StateList moveWithBlank(DFA dfa){
        Set<State> stateSet = new HashSet<>();
        for (State state: states){
            Queue<State> queue = new LinkedList<>(); //用队列来消除递归
            queue.offer(state);

            while (!queue.isEmpty()){
                State stackState = queue.poll();
                State nextState = dfa.move(stackState, "$");
                if(!stateSet.contains(nextState)){
                    stateSet.add(nextState);
                    queue.offer(nextState);
                }
            }
        }

        return new StateList(new ArrayList<>(stateSet));
    }

    /**
     * @param input: 面对的输入
     * @param nfa: 所在的nfa
     * @return StateList
     * @author ZhouXiang
     * @description 面对某输出，在nfa中的空弧转换
     * @exception
     */
    public StateList moveWithBlank(NFA nfa){
        Set<State> stateSet = new HashSet<>();
        for (State state: states){
            Queue<State> queue = new LinkedList<>(); //用队列来消除递归
            queue.offer(state);

            while (!queue.isEmpty()){
                State stackState = queue.poll();
                StateList nextStates = nfa.move(stackState, "$");
                List<State> nexts = nextStates.getStates();
                for(State nextState: nexts){
                    if(!stateSet.contains(nextState)){
                        stateSet.add(nextState);
                        queue.offer(nextState);
                    }
                }
            }
        }

        return new StateList(new ArrayList<>(stateSet));
    }

    /**
     * @param input:
     * @return StateList
     * @author ZhouXiang
     * @description 面对某输出，在nfa中，除空弧转换以外的其他弧转换(注意结果中不能有重复的状态，且空弧的转换方式不同，空弧要找到没有新增状态为止)
     * @exception
     */
    public StateList moveWithInput(String input, NFA nfa){
        assert !input.equals("$");
        Set<State> stateSet = new HashSet<>();

        Integer[] stateIntegers = moveMap.get(input);
        for(Integer stateInteger: stateIntegers){
            stateSet.add(Util.getStateById(stateInteger, nfa.getAllStates()));
        }

        StateList list = new StateList(new ArrayList<>(stateSet));//没有加空弧转换的List

        list.addStateList(list.moveWithBlank(nfa));

        return list;
    }
}
