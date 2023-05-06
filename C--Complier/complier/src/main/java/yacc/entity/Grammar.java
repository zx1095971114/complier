package yacc.entity;

import utils.Util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @projectName: complier
 * @package: yacc.entity
 * @className: Grammar
 * @author: Zhou xiang
 * @description: 描述语法的类
 * @date: 2022/6/10 23:58
 * @version: 1.0
 */
public class Grammar {
    private List<String> nonEndSymbol; //非终结符集合
    private List<String> endSymbol; //终结符集合，此处将$也算在了终结符中
    private String startSymbol; //开始符号
    private List<Rool> rools; //一条一条单个的规则

    private Map<String,Set<String>> newRools; //一个非终结符对应的一组规则
    //单个符号的first集合
    private Map<String, List<String>> first;
    //非终结符的follow集合
    private Map<String, Set<String>> nonFollow;

    // 存储在查找FOLLOW集合时非终结符的状态,仅在本类中使用
    private Map<String, Integer> status;

    //预测分析表，第一个String是非终结符，第二个是终结符
    private Map<String,Map<String, Rool>> predictMap;

    private Grammar(List<String> nonEndSymbol, List<String> endSymbol, String startSymbol, List<Rool> rools) {
        this.nonEndSymbol = nonEndSymbol;
        this.endSymbol = endSymbol;
        this.startSymbol = startSymbol;
        this.rools = rools;
        setNewRoolsByRools();
//        if(this.endSymbol.contains("$")){
//            System.out.println("1");
//            System.exit(5);
//        }
        this.first = calculateFirst();
        calculateFOLLOW();
        calculateMap();
    }

    public List<String> getNonEndSymbol() {
        return nonEndSymbol;
    }

    public List<String> getEndSymbol() {
        return endSymbol;
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public List<Rool> getRools() {
        return rools;
    }

    public Map<String, List<String>> getFirst() {
        return first;
    }

    public Map<String, Set<String>> getNonFollow() {
        return nonFollow;
    }

    public Map<String, Map<String, Rool>> getPredictMap() {
        return predictMap;
    }

    public void setEndSymbol(List<String> endSymbol) {
        this.endSymbol = endSymbol;
    }

    public void setStartSymbol(String startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void setRools(List<Rool> rools) {
        this.rools = rools;
    }

    public void setNonEndSymbol(List<String> nonEndSymbol) {
        this.nonEndSymbol = nonEndSymbol;
    }

    /**
     * @param :
     * @return void
     * @author ZhouXiang
     * @description 根据rools来确定newRools
     */
    private void setNewRoolsByRools() {
        Map<String,Set<String>> newRools = new HashMap<>();

        for(String nonSymbol : this.nonEndSymbol){
            Set<String> patterns = new HashSet<>();
            for(Rool rool : this.rools){
                if(rool.getLeft().equals(nonSymbol)){
                    patterns.add(rool.getRight());
                }
            }
            newRools.put(nonSymbol,patterns);
        }

        this.newRools = newRools;
    }

    /**
     * @param filePath: 文件路径
     * @param start: 起始符号
     * @return Grammar 获取的文法
     * @author ZhouXiang
     * @description 通过文件获取文法
     */
    public static Grammar getGrammarByFile(String filePath,String start) throws IOException {
        List<Rool> rools = new ArrayList<>();
        List<String> nonEndSymbols = new ArrayList<>();
        List<String> endSymbols = new ArrayList<>();
        String roolStr = "";

        InputStream in = ClassLoader.getSystemResourceAsStream(filePath);
        Scanner scanner = new Scanner(in);

        int cnt = 1; //用于记录规则号
        while (scanner.hasNextLine()){
            roolStr = scanner.nextLine();
            if(roolStr.equals("")){
                continue;
            }

            //获取规则
            Rool rool = new Rool();
            String roolNo = String.valueOf(cnt);
            cnt++;
            String content = roolStr;
//            if(roolNo.equals("20")){
//                System.out.println("here");
//
//            }
//            if(content.split("->",2).length == 1){
//                System.out.println(content);
//                System.out.println("规则错误");
//                System.exit(3);
//            }
            String left = content.split("->",2)[0].trim();
            String right = content.split("->",2)[1].trim();
            rool.setNo(roolNo);
            rool.setLeft(left);
            rool.setRight(right);
            rools.add(rool);

            //获取非终结符，出现在左边的全是非终结符，且非终结符只会在左边出现
            if(!nonEndSymbols.contains(left)){
                nonEndSymbols.add(left);
            }

            //获取终结符，先将右边的符号都放进去，后面进行对右边的符号分割，去除终结符的操作
            if(!endSymbols.contains(right)){
                endSymbols.add(right);
            }
        }

        //对终结符，进行分割，去除终结符的操作
        List<String> realEndSymbols = new ArrayList<>();
        Iterator<String> it = endSymbols.iterator();
        while (it.hasNext()){
            String endSymbol = it.next();

            if(endSymbol.equals("{ blockItem }")){
                int a = 3;
            }

            //如果它是非终结符，不用加入
            if(nonEndSymbols.contains(endSymbol)){;
                continue;
            }
            String[] endSymbolStr = endSymbol.split(" ");
            //进行分割，找终结符
            if(endSymbolStr.length != 1){
                for(int j = 0;j < endSymbolStr.length;j++){
                    if(!nonEndSymbols.contains(endSymbolStr[j]) && !realEndSymbols.contains(endSymbolStr[j])){
                        realEndSymbols.add(endSymbolStr[j]);
                    }
                }
                it.remove();
            }else {
                realEndSymbols.add(endSymbol);
            }
        }
        endSymbols = realEndSymbols;

        //对非终结符与终结符去重
        Set<String> set = new HashSet<>(nonEndSymbols);
        nonEndSymbols.clear();
        nonEndSymbols.addAll(set);

        set = new HashSet<>(endSymbols);
        endSymbols.clear();
        endSymbols.addAll(set);

        Grammar grammar = new Grammar(nonEndSymbols,endSymbols,start,rools);
        return grammar;
    }

    /**
     * @param :
     * @return Map<String,List<String>>
     * @author ZhouXiang
     * @description 计算本语法终结符与非终结符的First集
     */
    private Map<String,List<String>> calculateFirst(){
        Map<String,List<String>> result = new HashMap<>();

        //终结符，first集合是他本身
        for(String endSymbol : this.endSymbol){
            List<String> first = new ArrayList<>();
            first.add(endSymbol);
            result.put(endSymbol,first);
        }

        //非终结符，first集合要递归地计算
        for(String nonSymbol : this.nonEndSymbol){
//            if(nonSymbol.equals("querySpecification unionStatements")){
//                System.out.println("error");
//            }
            result.put(nonSymbol,getFirstBySingle(nonSymbol));
        }

        return result;
    }

    /**
     * @param symbol:
     * @return List<String>
     * @author ZhouXiang
     * @description 计算某符号(包括一系列式子的情况)的First集合的递归算法
     */
    private List<String> getFirstBySingle(String symbol){
        Set<String> result = new HashSet<>();

        if(this.endSymbol.contains(symbol)){ //单个终结符，first集合是自身
            result.add(symbol);

        } else if(this.nonEndSymbol.contains(symbol)){ //单个非终结符，其First集合是其所在的，所有产生该非终结符的规则的右部，的first集合之和
            Set<String> rights = this.newRools.get(symbol);
            for(String right : rights){
                result.addAll(this.getFirstBySingle(right));
            }

        }else {//处理有几个符号的情况
            String[] patterns = symbol.split(" ");
            if(this.endSymbol.contains(patterns[0])){
                //第一个符号是终结符，其first集合就是这个终结符
                result.add(patterns[0]);
            } else {
                for(int i = 0;i < patterns.length;i++){
                    List<String> patternsFirst = this.getFirstBySingle(patterns[i]);
                    if(!patternsFirst.contains("$")){
                        result.addAll(patternsFirst);
                        break;
                    } else {
                        if(i == patterns.length - 1){
                            result.addAll(patternsFirst);
                        }else {
                            patternsFirst.remove("$");
                            result.addAll(patternsFirst);
                        }
                    }
                }
            }
        }

        List<String> realResult = new ArrayList<>(result);
        return realResult;
    }

//    /**
//     * @param :
//     * @return Map<String,Set<String>>
//     * @author ZhouXiang
//     * @description 计算非终结符的FOLLOW集合
//     */
//    private void calculateFollow(){
//        this.nonFollow = new HashMap<>();
//        //用来存储某字符串的查找状态
//        Map<String,Boolean> isSearch = new HashMap<>();
//
//        for(String nonEndSymbol : this.nonEndSymbol){
//            this.nonFollow.put(nonEndSymbol,null);
//            isSearch.put(nonEndSymbol,true);
//        }
//
//
//        //对开始符号，要加入"#"进他的Follow集合
//        Set<String> startSet = new HashSet<>();
//        startSet.add("#");
//        this.addStrToFollow(this.startSymbol,startSet);
//
//        for(String nonEnd : this.nonEndSymbol){
//            int i = 0;
//            if(!isSearch.get(nonEnd)){
//                continue;
//            }
//            this.getFollowBySingle(nonEnd,isSearch);
//        }
//
//    }
//
//    /**
//     * @param sym:
//     * @param syms:
//     * @return void
//     * @author ZhouXiang
//     * @description 将某符号集加入Follow集合中
//     */
//    private void addStrToFollow(String sym,Set<String> syms){
//        Set<String> now = this.nonFollow.get(sym);
//        if(now != null){
//            now.addAll(syms);
//        }else {
//            now = new HashSet<>();
//            now.addAll(syms);
//            this.nonFollow.put(sym,now);
//        }
//    }
//
//    /**
//     * @param symbol:
//     * @return Set<String>
//     * @author ZhouXiang
//     * @description 计算某符号的First集合的递归算法
//     */
//    private Set<String> getFollowBySingle(String symbol,Map<String,Boolean> isSearch){
//
//        LoopRool:for(Rool rool : rools){
//            String[] rights = rool.getRight().split(" ");
//            for(int i = 0; i < rights.length; i++){
//                if(rights[i].equals(symbol)){
//                    //匹配结果在最右边
//                    if(i == rights.length - 1){
//                        //看左边是不是正在找
//                        if(isSearch.get(rool.getLeft())){
//                            Set<String> set = this.nonFollow.get(rool.getLeft());
//                            if(set.size() != 0){
//                                addStrToFollow(symbol,set);
//                            }
//                            continue LoopRool;
//                        }else {
//                            Set<String> set = getFollowBySingle(rool.getLeft(),isSearch);
//                            addStrToFollow(symbol,set);
//                        }
//                    }else {
//                        //匹配结果不在最右边
//                        //求右边表达式的First
//                        String later = rights[i + 1];
//                        if(i < rights.length - 2){
//                            for(int k = i + 1; k < rights.length;k++){
//                                later = later + " " + rights[k];
//                            }
//                        }
//                        List<String> laterFirst = this.getFirstBySingle(later);
//
//                        //看右边表达式是否有"$"
//                        if(laterFirst.contains("$")){
//                            laterFirst.remove("$");
//                            //判断左边是不是正在找
//                            if(isSearch.get(rool.getLeft())){
//                                Set<String> set = new HashSet<>(laterFirst);
//                                this.addStrToFollow(symbol,set);
//
//                                Set<String> set1 = this.nonFollow.get(rool.getLeft());
//                                if(set1 != null){
//                                    addStrToFollow(symbol,set1);
//                                }
//                                continue LoopRool;
//                            }else {
//                                Set<String> set = new HashSet<>(laterFirst);
//                                this.addStrToFollow(symbol,set);
//                                this.addStrToFollow(symbol,this.getFollowBySingle(rool.getLeft(), isSearch));
//                            }
//                        }else {
//                            Set<String> set = new HashSet<>(laterFirst);
//                            this.addStrToFollow(symbol,set);
//                        }
//                    }
//                }
//            }
//        }
//
////        System.exit(100);
//        isSearch.remove(symbol);
//        isSearch.put(symbol,false);
//        return this.nonFollow.get(symbol);
//    }

    // 将某个符号加入到某个非终结符的FOLLOW集合中
    private void addCharToFOLLOW(String character, String nonEndChar){
        Set<String> nonEndCharFollow = this.nonFollow.get(nonEndChar);
        // 加入前先判断有没有在映射中了
        if (nonEndCharFollow != null){
            nonEndCharFollow.add(character);
        } else{
            nonEndCharFollow = new HashSet<>();
            nonEndCharFollow.add(character);
            this.nonFollow.put(nonEndChar, nonEndCharFollow);
        }
    }

    // 将某个符号集加入到某个非终结符的FOLLOW集合中
    private void addCharsToFOLLOW(Set<String> characterSet, String nonEndChar){
        Set<String> nonEndCharFollow = nonFollow.get(nonEndChar);
        // 加入前先判断有没有元素在映射中了
        if (nonEndCharFollow != null){
            nonEndCharFollow.addAll(characterSet);
        } else{
            nonEndCharFollow = new HashSet<>();
            nonEndCharFollow.addAll(characterSet);
            nonFollow.put(nonEndChar, nonEndCharFollow);
        }
    }

    /**
     * @param :
     * @return void
     * @author ZhouXiang
     * @description 计算Fowllow集合
     */
    private void calculateFOLLOW(){

        status = new HashMap<>();

        nonFollow = new HashMap<>();

        for (String character : this.nonEndSymbol) {

            // 初始化非终结符的状态
            // 状态1表示还没找过
            // 状态2表示正在查找
            // 状态3表示已经结束查找
            status.put(character, 1);
            nonFollow.put(character, new HashSet<>());
        }

        // 首先在开始符号的FOLLOW集中加入界符
        Set<String> startFollow = new HashSet<>();
        startFollow.add("#");
        this.nonFollow.put(this.startSymbol, startFollow);

        for (String character : this.nonEndSymbol){
            // 如果已经查找完，就跳过
            if (status.get(character) == 3){
                continue;
            }

            FOLLOWx(character);
        }
    }

    // 将字符串中的符号拆开
    public List<String> disassemble(String value){
        String[] cStr = value.split(" ");

        List<String> cList = new ArrayList<>();

        for (String str : cStr){
            cList.add(str);
        }
        return cList;
    }

    // 递归地计算非终结符的FOLLOW集合
    private Set<String> FOLLOWx(String x){
        // 首先置当前查找的非终结符的状态为正在查找
        status.put(x, 2);

        // 在产生式中搜索所有非终结符出现的位置
        for (String character : this.nonEndSymbol){
            Set<String> rightSet = this.newRools.get(character);
            String[] rightItems = rightSet.toArray(new String[rightSet.size()]);

            RightItemLoop:for (String item : rightItems){
                // 获取符号列表
                List<String> cList = this.disassemble(item);

                // 接下来，搜索当前查找的非终结符的位置
                for (int i=0; i<cList.size(); i++){
                    String nonEndChar = cList.get(i);
                    if (nonEndChar.equals(x)){
                        // 判断是否处于最右的位置
                        if (i < cList.size() - 1){
                            // 如果没在最右边的位置
                            // 下面循环判断后一个符号是否是非终结符
                            for (int j=i+1; j<cList.size(); j++){
                                String nextChar = cList.get(j);
                                if (this.nonEndSymbol.contains(nextChar)){
                                    // 如果是非终结符，查看其FIRST集是否包含空串
                                    Set<String> nextFirst = this.first.get(nextChar).stream().collect(Collectors.toSet());
                                    // 如果包含空串，并且此时这个符号是最后一个符号
                                    // 就要将其FIRST除去空串的集合加入FOLLOW集，且左部的FOLLOW集加入FOLLOW集
                                    if (nextFirst.contains("$")){
                                        // 判断是否是最后一个符号
                                        if (j == cList.size() - 1){
                                            // 这里首先判断一下要递归查找的非终结符的状态
                                            // 如果为正在查找，就会陷入死循环
                                            // 所以要略过这一条产生式
                                            // 在略过产生式之前，因为直接略过会遗漏掉之前正在查找的非终结符的FOLLOW集中的元素，所以要加上
                                            if (status.get(character) == 2){
                                                Set<String> follow = this.nonFollow.get(character);
                                                if (follow.size() != 0){
                                                    addCharsToFOLLOW(follow, x);
                                                }
                                                continue RightItemLoop;
                                            }
                                            Set<String> leftFOLLOW = FOLLOWx(character);
                                            Set<String> nextFirstExceptNULL = new HashSet<>(nextFirst);
                                            nextFirstExceptNULL.remove("$");
                                            addCharsToFOLLOW(leftFOLLOW, x);
                                            addCharsToFOLLOW(nextFirstExceptNULL, x);
                                        } else{
                                            // 如果不是最后一个符号，将FIRST集合加入
                                            Set<String> nextFirstExceptNULL = new HashSet<>(nextFirst);
                                            nextFirstExceptNULL.remove("$");
                                            addCharsToFOLLOW(nextFirstExceptNULL, x);
                                        }
                                    } else{
                                        // 如果不包含空串加入FIRST之后跳出循环
                                        addCharsToFOLLOW(nextFirst, x);
                                        break;
                                    }
                                } else{
                                    // 如果不是非终结符，把此符号加入到当前查找的非终结符的FOLLOW集中
                                    addCharToFOLLOW(nextChar, nonEndChar);
                                    break;
                                }
                            }
                        }
                        // 如果在最右边，将FOLLOW（左部）加入到当前非终结符的FOLLOW集合
                        else{
                            // 这里首先判断一下要递归查找的非终结符的状态
                            // 如果为正在查找，就会陷入死循环
                            // 所以要略过这一条产生式
                            // 在略过产生式之前，因为直接略过会遗漏掉之前正在查找的非终结符的FOLLOW集中的元素，所以要加上
                            if (status.get(character) == 2){
                                Set<String> follow = this.nonFollow.get(character);
                                if (follow.size() != 0){
                                    addCharsToFOLLOW(follow, x);
                                }
                                continue RightItemLoop;
                            }
                            Set<String> leftFOLLOW = FOLLOWx(character);
                            addCharsToFOLLOW(leftFOLLOW, x);
                        }
                    }
                }
            }
        }

        // 如果return，说明已经查找完
        status.put(x, 3);
        return this.nonFollow.get(x);
    }

    /**
     * @param :
     * @return void
     * @author ZhouXiang
     * @description 计算预测分析表
     */
    private void calculateMap(){
        this.predictMap = new HashMap<>();

        Rool error = new Rool("error","error","error");
        //初始化，置为error
        for (String nonEnd : this.nonEndSymbol){
            Map<String,Rool> map = new HashMap<>();
            for(String end : this.endSymbol){
                if(end.equals("$")){
                    continue;
                }
                map.put(end,error);
            }
            this.predictMap.put(nonEnd,map);
        }


        //遍历rool来看预测分析表
        for (Rool rool : this.rools){

            List<String> first = this.getFirstBySingle(rool.getRight()); //右边的first集合
            Set<String> follow = this.nonFollow.get(rool.getLeft()); //左边的follow集合，左边必定是非终结符
            String non = rool.getLeft();

            //在终结符中加#,此时$还包含在endSymbol中
            List<String> newSym = new ArrayList<>(this.endSymbol);
            newSym.add("#");


            if(!first.contains("$")){
                for(String end : newSym){
                    //去掉endSymbol中的$的影响
                    if(end.equals("$")){
                        continue;
                    }
                    if(first.contains(end)){
                        editPredictMap(non,end,rool);
                    }
                }
            }else {
                for(String end : newSym){
                    if(end.equals("$")){
                        continue;
                    }
                    if(first.contains(end)){
                        editPredictMap(non,end,rool);
                    }
                }

                for(String endB : newSym){
                    if (endB.equals("$")){
                        continue;
                    }

                    if(follow.contains(endB)){
                        editPredictMap(non,endB,rool);
                    }
                }
            }
        }

    }

    /**
     * @param non:
     * @param end:
     * @param rool:
     * @return void
     * @author ZhouXiang
     * @description 修改预测分析表的某一项
     */
    private void editPredictMap(String non,String end,Rool rool){
        Map<String,Rool> map = this.predictMap.get(non);

        map.remove(end);
        map.put(end,rool);

    }

    /**
     * @param filePath:
     * @return void
     * @author ZhouXiang
     * @description 打印该文法的预测分析表
     * @exception
     */
    public void printPredictTable(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        Map<String,Map<String, Rool>> map = this.predictMap;
        content.append(",");
        List<String> nonSym = new ArrayList<>(map.keySet());
        List<Map<String, Rool>> list = new ArrayList<>(map.values());
        List<String> endSym = new ArrayList<>(list.get(0).keySet());

        for (String str: endSym){
//            System.out.println(str + " ");

            str = str.replaceAll(",", "dot");
            content.append(str + ",");
        }
        content.append("\n");

        for (String non: nonSym){
            content.append(non + ",");
            Map<String, Rool> map1 = map.get(non);
            for (String end: endSym){
                Rool rool = map1.get(end);
                String left = rool.getLeft();
                String right = rool.getRight().replaceAll(",", "dot");

                content.append(left + " -> " + right + ",");
            }
            content.append("\n");
        }

        Util.writeFIle(filePath, content.toString());
    }
}
