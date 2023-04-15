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

    //注意，这里的状态转移与a弧转换不同，只是简单的将所有状态的状态转移加和，没有算加和的空弧转换
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

        for(State state: states){
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


    /**
     * @param stateList:
     * @return boolean
     * @author ZhouXiang
     * @description 判断一个状态集合是不是和另一个状态集合相同；
     * @exception 注意比较前要保证两者是针对同一个状态转换机的StateList
     */
    public boolean equals(StateList stateList){
        return stateList.getStates().equals(states);
    }

    /**
     * @param :
     * @return State
     * @author ZhouXiang
     * @description 在要变为State的StateList和StateList来源的NFA中，将该状态集合转为对应的状态
     * @exception 要求raw中的StateList已经编好号了
     */
    public State turn2State(List<StateList> raw, NFA nfa){
        Map<String, Integer[]> stateMap = new HashMap<>();
        Set<String> alphas = moveMap.keySet();
        for(String alpha: alphas){
            StateList stateList = null;
            if(alpha.equals("$")){
                //注意，空弧转换会包括自身，但是在StateList转为State时，经"$"不能包括自身
                stateList = this.moveWithBlankWithoutSelf(nfa);

            } else {
                stateList = this.moveWithInput(alpha, nfa);
            }

            Integer[] integers = new Integer[1];
            integers[0] = Util.getStateListId(stateList, raw);

            stateMap.put(alpha, integers);
        }

        return new State(stateListId, start, endSymbol, stateMap);
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
        Set<String> set = new HashSet<>();
        Collections.addAll(set, endSymbol);
        if(set.size() >= 2){
            Iterator<String> it = set.iterator();
            while (it.hasNext()){
                String str = it.next();
                if(str.equals("NOT_END")){
                    it.remove();
                }
            }
        }
        this.endSymbol = set.toArray(new String[0]);

        if(state.isStart()){
            this.start = true;
        }

        //要记得加上它状态转以后状态的空弧转换
         for(Map.Entry<String, Integer[]> entry: state.getMoveMap().entrySet()){
            String key = entry.getKey();
            Integer[] value = entry.getValue();
            Integer[] newValue = moveMap.getOrDefault(key, new Integer[0]);

            //加状态转移后的状态
            newValue = Util.joint(newValue, value);
            //newValue进行去重复和多余的0
            Set<Integer> set1 = new HashSet<>(Arrays.asList(newValue));
            if(set1.size() != 1){
                set1.remove(0);
            }
            newValue = set1.toArray(new Integer[0]);

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
            State state = Util.getStateById(stateInteger, dfa.getAllStates());
            if(state != null){
                stateSet.add(Util.getStateById(stateInteger, dfa.getAllStates()));
            }
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
     * @param
     * @param nfa: 所在的nfa
     * @return StateList
     * @author ZhouXiang
     * @description 在nfa中的空弧转换
     * @exception
     */
    public StateList moveWithBlank(NFA nfa){
        Set<State> stateSet = new HashSet<>();
        for (State state: states){
            Queue<State> queue = new LinkedList<>(); //用队列来消除递归
            queue.offer(state);
            stateSet.add(state); //空弧转换要包括自身

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
     * @exception 注意，如果a弧转换结果为空会返回null
     */
    public StateList moveWithInput(String input, NFA nfa){
        assert !input.equals("$");
        Set<State> stateSet = new HashSet<>();

        Integer[] stateIntegers = moveMap.get(input);
        for(Integer stateInteger: stateIntegers){
            State state = Util.getStateById(stateInteger, nfa.getAllStates());
            if(state != null){
                stateSet.add(Util.getStateById(stateInteger, nfa.getAllStates()));
            }
        }

        StateList list = new StateList(new ArrayList<>(stateSet));//没有加空弧转换的List

        list.addStateList(list.moveWithBlank(nfa));

        return list;
    }


    /**
     * @param
     * @param nfa: 所在的nfa
     * @return StateList
     * @author ZhouXiang
     * @description 在nfa中除去自身的空弧转换
     * @exception
     */
    public StateList moveWithBlankWithoutSelf(NFA nfa){
        Set<State> stateSet = new HashSet<>();
        for (State state: states){
            Queue<State> queue = new LinkedList<>(); //用队列来消除递归
            queue.offer(state);
//            stateSet.add(state); //空弧转换要包括自身

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
}
