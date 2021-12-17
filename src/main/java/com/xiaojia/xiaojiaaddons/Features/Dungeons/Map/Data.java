package com.xiaojia.xiaojiaaddons.Features.Dungeons.Map;

import java.util.ArrayList;

public class Data {
    public String name;
    public String type;
    public int secrets;
    public ArrayList<Integer> cores;

    public Data(){}

    public Data(String name, String type, int secrets, ArrayList<Integer> cores) {
        this.name = name;
        this.type = type;
        this.secrets = secrets;
        this.cores = cores;
    }
}
