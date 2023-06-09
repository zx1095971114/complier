package lex.entity;

import utils.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class State {
    private int stateId; //状态id
    private boolean start; //是否为起始状态
    private String[] endSymbol; //若为结束状态，则为其对应的标识的数组，否则为空数组
    private Map<String, Integer[]> moveMap; //状态转换表

    public State(int stateId, boolean start, String[] endSymbol, Map<String, Integer[]> moveMap) {
        this.start = start;
        this.stateId = stateId;
        this.endSymbol = endSymbol;
        this.moveMap = moveMap;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String[] getEndSymbol() {
        return endSymbol;
    }

    public boolean isStart() {
        return start;
    }

    public Map<String, Integer[]> getMoveMap() {
        return moveMap;
    }

    public int getStateId() {
        return stateId;
    }

    /**
     * @param :
     * @return boolean
     * @author ZhouXiang
     * @description 判断是不是终止状态
     * @exception
     */
    public boolean isEnd(){
        if(this.endSymbol[0].equals("NOT_END")){
            return false;
        }else {
            return true;
        }
    }

    /**
     * @param fa 状态转换机
     * @param input 面对的转移
     * @return StateList
     * @author ZhouXiang
     * @description 求其在DFA中转移的闭包
     * @exception
     */
    public StateList getMove(DFA dfa, String input){
        List<State> fa = dfa.getAllStates();

        Integer[] bagInteger = moveMap.get(input);
        List<State> bag = new ArrayList<>();
        for(Integer cnt: bagInteger){
            State state = Util.getStateById(cnt, fa);
            if(state != null){
                bag.add(Util.getStateById(cnt, fa));
            }
        }

        return new StateList(bag);

    }

}


