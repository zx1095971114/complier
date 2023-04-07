package lex.implement;

import lex.entity.State;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * @projectName: complier
 * @package: lex.implement
 * @className: StateGenerate
 * @author: Zhou xiang
 * @description: 状态转换图的初始化
 * @date: 2023/4/7 17:30
 * @version: 1.0
 */
class StateGenerate {
    List<State> generateNFA() throws FileNotFoundException {
        List<State> stateList = new LinkedList<>();
        Scanner scanner = new Scanner(new FileReader("./src/main/resources/initialStateTable.csv"));
        scanner.nextLine();
        while (scanner.hasNextLine()){
            String[] line = scanner.nextLine().split(",");
            int stateId = Integer.parseInt(line[0]);
            boolean start = false;
            boolean end = false;
            if(line[1].equals("TRUE")){
                start = true;
            }
            if(line[2].equals("TRUE")){
                end = true;
            }
            int[] symbol = new int[24];
            for(int i = 0; i < 24; i++){
                symbol[i] = Integer.parseInt(line[i + 3]);
            }

            State state = new State(stateId, end, start){
                @Override
                public int getNextStateId(char input){
                    if(Character.isLetter(input)){
                        return symbol[0];
                    }
                    if(Character.isDigit(input)){
                        return symbol[1];
                    }
                    switch (input){
                        case '_':
                            return symbol[2];
                        case '+':
                            return symbol[3];
                        case '-':
                            return symbol[4];
                        case '*':
                            return symbol[5];
                        case '/':
                            return symbol[6];
                        case '%':
                            return symbol[7];
                        case '=':
                            return symbol[8];
                        case '$':
                            return symbol[9];
                        case '>':
                            return symbol[10];
                        case '<':
                            return symbol[11];
                        case '!':
                            return symbol[12];
                        case '&':
                            return symbol[13];
                        case '|':
                            return symbol[14];
                        case '(':
                            return symbol[15];
                        case ')':
                            return symbol[16];
                        case '{':
                            return symbol[17];
                        case '}':
                            return symbol[18];
                        case ',':
                            return symbol[19];
                        case ';':
                            return symbol[20];
                        case ' ':
                            return symbol[21];
                        case '\n':
                            return symbol[22];
                        default:
                            return symbol[23];
                    }
                }
            };
            stateList.add(state);
        }
        return stateList;
    }
}
