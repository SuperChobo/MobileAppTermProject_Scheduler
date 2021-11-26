package com.example.scheduler;

import java.util.ArrayList;
import java.util.List;

public class EntityList {
    public static int CHECK_ENTITY = 1;
    public static int SCHEDULE_ENTITY = 2;
    public static int DOUBLE_ENTITY = 3;

    private int name;
    private List<Entity> list;
    private List<String> columns;

    public EntityList(int type){
        name = type;
        list = new ArrayList<Entity>();

        if(type == DOUBLE_ENTITY){
            columns = new ArrayList<String>();
        }
    }

    public EntityList(int type, List<String> columnList){
        name = type;
        list = new ArrayList<Entity>();
        columns = columnList;
    }

    public int getName() {
        return name;
    }

    public List<Entity> getList(){
        return list;
    }

    public void add(Entity data){
        list.add(data);
    }

    public int size(){
        return list.size();
    }

    public void remove(int index){
        list.remove(index);
    }

    public void get(int index){
        list.get(index);
    }

    public List<String> getColumns(){
        return columns;
    }
}
