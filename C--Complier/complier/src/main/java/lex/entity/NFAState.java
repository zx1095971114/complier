package lex.entity;

import java.util.List;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: NFAState
 * @author: Zhou xiang
 * @description: nfa的状态
 * @date: 2023/4/7 23:45
 * @version: 1.0
 */
public class NFAState extends State{
    public NFAState(int stateId, boolean start, String endSymbol, String[] moveSymbol) {
        super(stateId, start, endSymbol, moveSymbol);
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
