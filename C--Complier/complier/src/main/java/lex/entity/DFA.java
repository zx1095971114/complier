package lex.entity;

import javafx.util.Pair;
import utils.Util;

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

    /**
     * @param states:
     * @return DFA
     * @author ZhouXiang
     * @description 通过一个State列表获取一个DFA实例
     * @exception
     */
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

                //调试使用
//                if(value.length != 1){
//                    int a = 0;
//                }

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
     * @description 在状态origin，遇到输入input，该转移到的唯一状态，若输入input没有对应的转移状态，则返回null
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
     * @description 将原本的DFA最小化，正确答案初始版本，未真正实现最小化
     * @exception
     */
    public void minimizeDFADemo(){
        //建立新状态
        String[] newEndSymbol = new String[]{"OP"};
        Map<String, Integer[]> newMap = new HashMap<>();
        for (String letter: alpha){
            Integer[] integers = new Integer[1];
            if(alpha.equals("=")){
                integers[0] = 3;
            }else {
                integers[0] = 0;
            }
            newMap.put(letter, integers);
        }
        State newState = new State(7,false, newEndSymbol, newMap);

        //字母表改变,alpha
        List<String> stringList = Arrays.asList(alpha); //这个list不支持修改
        List<String> tempStrList = new ArrayList<>(stringList);
        tempStrList.remove("$");
        alpha = tempStrList.toArray(new String[0]);

        //删状态7, 9, 10
        State state7 = Util.getStateById(7, allStates);
        State state9 = Util.getStateById(9, allStates);
        State state10 = Util.getStateById(10, allStates);
        allStates.remove(state7);
        allStates.remove(state9);
        allStates.remove(state10);

        allStates.add(6, newState);

        //所有状态改变,allStates
        //改状态1
        State state1 = Util.getStateById(1, allStates);
        Map<String, Integer[]> map1 = state1.getMoveMap();
        map1.put("=", new Integer[]{7});
        map1.put(">", new Integer[]{7});
        map1.put("<", new Integer[]{7});
        //改状态11
        Util.getStateById(11, allStates).setStateId(9);


        //改endStates
        endStates.remove(state7);
        endStates.remove(state9);
        endStates.remove(state10);
        endStates.add(newState);


        //改functionList
        Map<String, Integer> dfaMap1 = functionList.get(0);
        dfaMap1.put("digit", 9);
        dfaMap1.put("=", 7);
        dfaMap1.put(">", 7);
        dfaMap1.put("<", 7);

        Map<String, Integer> dfaMap2 = functionList.get(10);
        dfaMap2.put("digit", 9);

        functionList.remove(8);
        functionList.remove(8); //删除一个以后，序号会改变

        Iterator<Map<String, Integer>> it = functionList.iterator();
        while (it.hasNext()){
            Map<String, Integer> map = it.next();
            map.remove("$");
        }


    }

    /**
     * @param :
     * @return DFA
     * @author ZhouXiang
     * @description 将所给的DFA最小化
     * @exception
     */
    public DFA minimizeDFA(){
        List<StateList> curList = new ArrayList<>();
        //根据endSymbol的不同，对State进行第一次划分
        //因为java对String[]的比较未重写，所以要将String[]转为String
        Map<State, String> map =new HashMap<>();
        for (State state: this.allStates){
            StringBuilder sb = new StringBuilder();
            for(String str: state.getEndSymbol()){
                sb.append(str + "_");
            }
            map.put(state, sb.toString());
        }

        Set<String> set = new HashSet<>(map.values());
        for(String str: set){
            List<State> list = new ArrayList<>();
            for(Map.Entry<State, String> entry: map.entrySet()){
                State key = entry.getKey();
                String value = entry.getValue();
                if(value.equals(str)){
                    list.add(key);
                }
            }
            curList.add(new StateList(list));
        }

        for(String input: alpha){
            if(input.equals("$")){
                continue;
            }

//            if(input.equals("=")){
//                int a = 0;
//            }

            curList = separate(curList, input);
        }

        List<StateList> result = check(curList);

        //将StateList转为State
        List<State> dfa = new ArrayList<>();
        int cnt = 1;
        for(StateList stateList: result){
            stateList.setStateListId(cnt);
            cnt++;
        }

        for (StateList stateList: result){

//            if(result.get(3).equals(stateList)){
//                int a = 3;
//            }

            dfa.add(stateList.turn2State(result, this));
        }

        return DFA.getDFAInstance(dfa);
    }

    //检查，直到不发生改变
    private List<StateList> check(List<StateList> curList){
        List<StateList> oldList = curList;
        for(String input: alpha){
            if(input.equals("$")){
                continue;
            }
            curList = separate(curList, input);
        }
        if(!oldList.equals(curList)){
            check(curList);
        }
        return oldList;
    }

    //使用某字母对目前的dfa(List<StateList>)进行一次划分,返回划分后的结果
    private List<StateList> separate(List<StateList> curList, String input){
        List<StateList> finalStateList = new ArrayList<>();

        Queue<StateList> queue = new LinkedList<>();
        for (StateList list: curList){
            queue.offer(list);
        }
        List<StateList> oldList = new ArrayList<>(curList);

        while (!queue.isEmpty()){
            StateList tempStateList = queue.poll();

            //调试用
//            if(tempStateList.getStates().size() == 4){
//                int a = 0;
//            }

            //根据a弧转换，对tempStateList中的状态进行分类
            //tempStateList中的每一个State，会对应到一个StateList
            //取StateList为key，若不用划分，应只有一个键值对，否则，就要根据value划分成不同的StateList
            Map<StateList, List<State>> map = new HashMap<>();
            for(State state: tempStateList.getStates()){
                StateList next = state.getMove(this, input);

                Pair<StateList, List<State>> pair = personalGetOrDefault(map, next, new ArrayList<>());
                List<State> list = pair.getValue();
                list.add(state);
                map.put(pair.getKey(), list);

            }

            if(map.size() == 1){//包含在oldList里面，说明是最终分组
                finalStateList.add(tempStateList);
            }else {//不包含在oldList里面，删除原来的分组，创建新分组，将新分组入队
                oldList.remove(tempStateList);
                for(List<State> stateList: map.values()){
                    StateList stateList1 = new StateList(stateList);
                    oldList.add(stateList1);
                    queue.add(stateList1);
                }
            }
        }

        return finalStateList;
    }

    //实现getOrDefault功能，用api的无法比较StateList是否相同
    private static Pair<StateList, List<State>> personalGetOrDefault(Map<StateList, List<State>> map, StateList testKey, List<State> defaultResult){
        for(Map.Entry<StateList, List<State>> entry: map.entrySet()){
            StateList key = entry.getKey();
            List<State> value = entry.getValue();

            if(testKey.equals(key)){
                return new Pair<>(key, value);
            }
        }

        return new Pair<>(testKey, defaultResult);
    }

}
