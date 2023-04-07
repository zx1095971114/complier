package lex.entity;

import java.util.List;

public class State {
    private int stateId;
    private boolean start;
    private boolean end;

    public State(int stateId, boolean end, boolean start) {
        this.start = start;
        this.stateId = stateId;
        this.end = end;
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

    public void setEnd(boolean end) {
        this.end = end;
    }

    public int getStateId() {
        return stateId;
    }

    public boolean isEnd() {
        return end;
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


}


