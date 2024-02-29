package com.codefromscratch.http;

public enum HttpMethod {
    GET,HEAD;
    public static final int MAX_LENGHT;

    static {
        int tempMaxLenght =-1;
        for(HttpMethod method : values()){
            if(method.name().length() > tempMaxLenght){
                tempMaxLenght = method.name().length();
            }
        }
        MAX_LENGHT = tempMaxLenght;
    }
}
