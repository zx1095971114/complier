package lex.entity;

import java.util.ArrayList;
import java.util.List;

public class State {
    private int stateId; //状态id
    private boolean start; //是否为起始状态
    private String endSymbol; //若为结束状态，则为其对应的标识，否则为NOT_END
    private String[] moveSymbol; //所有状态转移的符号

    public State(int stateId, boolean start, String endSymbol, String[] moveSymbol) {
        this.start = start;
        this.stateId = stateId;
        this.endSymbol = endSymbol;
        this.moveSymbol = moveSymbol;
    }

    public String getEndSymbol() {
        return endSymbol;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public String[] getMoveSymbol() {
        return moveSymbol;
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
        if(this.endSymbol.equals("NOT_END")){
            return false;
        }else {
            return true;
        }
    }

    /**
     * @param input:
     * @return State
     * @author ZhouXiang
     * @description 获取面对该输入的下一个状态
     * @exception
     */
    public State getNextState(char input, List<State> states){
        int id = getNextStateId(input);
        for(State state: states){
            if(id == state.getStateId()){
                return state;
            }
        }
        return null;
    }

    /**
     * @param input:
     * @return int
     * @author ZhouXiang
     * @description 每次创建实例时要重写该方法
     * @exception
     */
    public int getNextStateId(char input){
        return 0;
    }


    /**
     * @param :fa 状态转换机(nfa/dfa)
     * @return List<State>
     * @author ZhouXiang
     * @description 求其空转移的闭包
     * @exception
     */
    public List<State> getBlankMove(List<State> fa){
        List<State> blankMove = new ArrayList<>();
        if(this.getNextStateId('$') != 0){
            blankMove.add(this.getNextState('$', fa));
        }
    }
}


