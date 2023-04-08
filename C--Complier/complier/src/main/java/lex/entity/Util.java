package lex.entity;

import java.util.Arrays;
import java.util.List;

/**
 * @projectName: complier
 * @package: lex.entity
 * @className: Util
 * @author: Zhou xiang
 * @description: 用于写常用的静态方法
 * @date: 2023/4/8 9:14
 * @version: 1.0
 */
public class Util {
    /**
     * @param id:
     * @param states:
     * @return State
     * @author ZhouXiang
     * @description 根据状态id在状态列表(一个dfa或nfa)中找到对应的状态
     * @exception
     */
    public static State getStateById(int id, List<State> states){
        for(State state: states){
            if(state.getStateId() == id){
                return state;
            }
        }

        return null;
    }

    /**
     * @param nums1:
     * @param nums2:
     * @return String
     * @author ZhouXiang
     * @description 拼接两字符数组
     * @exception
     */
    public static String[] joint(String[] nums1, String[] nums2){
        String[] str = new String[nums1.length + nums2.length];
        for(int i = 0; i < str.length; i++){
            if(i < nums1.length){
                str[i] = nums1[i];
            }else {
                str[i] = nums2[i - nums1.length];
            }
        }
        return str;
    }

    /**
     * @param nums1:
     * @param nums2:
     * @return Integer
     * @author ZhouXiang
     * @description 拼接两整数数组
     * @exception
     */
    public static Integer[] joint(Integer[] nums1, Integer[] nums2){
        Integer[] str = new Integer[nums1.length + nums2.length];
        for(int i = 0; i < str.length; i++){
            if(i < nums1.length){
                str[i] = nums1[i];
            }else {
                str[i] = nums2[i - nums1.length];
            }
        }
        return str;
    }
}
