package lex.entity;

public enum State {
    S("S",false){
        @Override
        public State getNextState(char input) {
            if(input == ' '){
                return S;
            } else if (input == '\n') {
                return S;
            } else if (Character.isLetter(input)) {
                return M;
            } else if (input == '_') {
                return M;
            } else if (Character.isDigit(input)) {
                return N1;
            } else if (input == '\"') {
                return S1;
            } else if (input == '=') {
                return OP1;
            } else if (input == '>') {
                return O1;
            } else if (input == '<') {
                return O2;
            } else if (input == '!') {
                return O4;
            } else if (input == '.') {
                return OP13;
            } else if (input == '|') {
                return O5;
            } else if (input == '&') {
                return O6;
            } else if (input == '-') {
                return OP15;
            } else if (input == '(') {
                return SE1;
            } else if (input == ')') {
                return SE2;
            } else if (input == ',') {
                return SE3;
            } else {
                return ERROR;
            }
        }
    },

    N1("N1",false){
        @Override
        public State getNextState(char input) {
            if(Character.isDigit(input)){
                return N1;
            } else if (input == '.') {
                return  N2;
            } else if (!Character.isDigit(input)) {
                return INT;
            } else{
                return ERROR;
            }
        }
    },

    N2("N2",false){
        @Override
        public State getNextState(char input) {
            if(Character.isDigit(input)){
                return N2;
            } else if (!Character.isDigit(input)) {
                return FLOAT;
            } else {
                return ERROR;
            }
        }
    },

    FLOAT("FLOAT",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    M("M",false){
        @Override
        public State getNextState(char input) {
            if(Character.isDigit(input) || Character.isLetter(input) || input == '_'){
                return M;
            } else {
                return E;
            }
        }
    },

    INT("INT",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    S1("S1",false){
        @Override
        public State getNextState(char input) {
            if(input != '\"'){
                return S1;
            } else {
                return STR;
            }
        }
    },

    STR("STR",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O1("O1",false){
        @Override
        public State getNextState(char input) {
            if(input != '='){
                return OP2;
            } else {
                return OP4;
            }
        }
    },

    OP1("OP1",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O2("O2",false){
        @Override
        public State getNextState(char input) {
            if(input == '='){
                return O3;
            } else {
                return OP3;
            }
        }
    },

    OP2("OP2",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O3("O3",false){
        @Override
        public State getNextState(char input) {
            if(input == '>'){
                return OP7;
            } else {
                return OP5;
            }
        }
    },

    OP3("OP3",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O4("O4",false){
        @Override
        public State getNextState(char input) {
            if(input == '='){
                return OP6;
            } else {
                return OP14;
            }
        }
    },

    OP4("OP4",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O5("O5",false){
        @Override
        public State getNextState(char input) {
            if(input == '|'){
                return OP11;
            } else {
                return ERROR;
            }
        }
    },

    OP11("OP11",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    O6("O6",false){
        @Override
        public State getNextState(char input) {
            if(input == '&'){
                return OP9;
            } else {
                return ERROR;
            }
        }
    },

    OP9("OP9",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP5("OP5",false){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP6("OP6",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP14("OP14",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP7("OP7",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP13("OP13",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    OP15("OP15",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    E("E",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    },

    SE1("SE1",true){
        @Override
        public State getNextState(char input){
            return null;
        }
    },

    SE2("SE2",true){
        @Override
        public State getNextState(char input){
            return null;
        }
    },

    SE3("SE3",true){
        @Override
        public State getNextState(char input){
            return null;
        }
    },

    ERROR("ERROR",true){
        @Override
        public State getNextState(char input) {
            return null;
        }
    };

    private String name;
    private boolean end;
    private Token token;

    private State(String name, boolean end) {
        this.name = name;
        this.end = end;
        if(isEnd()){
            token = new Token();
        } else {
            token = null;
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public boolean isEnd() {
        return end;
    }

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        assert this.isEnd();
        this.token = token;
    }

    public abstract State getNextState(char input);


}


