package lex.entity;

import java.util.List;
import java.util.Map;

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
    private String endSymbol; //若为结束状态，则为其对应的标识，否则为NOT_END
    private Map<String, Integer> moveMap; //key为转移符号，value为转移到的状态集合编号

    public List<State> getStates() {
        return states;
    }

    public int getStateListId() {
        return stateListId;
    }

    public boolean isStart() {
        return start;
    }

    public String getEndSymbol() {
        return endSymbol;
    }

    public Map<String, Integer> getMoveMap() {
        return moveMap;
    }


}
