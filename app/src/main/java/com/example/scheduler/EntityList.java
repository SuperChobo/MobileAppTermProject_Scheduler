package com.example.scheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EntityList {
    public static final int NORMAL_ENTITY = 1;
    public static final int SCHEDULE_ENTITY = 2;
    public static final int DOUBLE_ENTITY = 3;

    private String name;
    private int type;
    private List<Entity> list;
    private List<String> columns;

    public EntityList(int type){
        this("", type);
    }
    public EntityList(int type, List<String> columnList){
        this("", type, columnList);
    }

    public EntityList(String name, int type){
        this.name = name;
        this.type = type;
        list = new ArrayList<Entity>();
        if(type == DOUBLE_ENTITY){
            columns = new ArrayList<String>();
        }
    }

    public EntityList(String name, int type, List<String> columnList){
        this.name = name;
        this.type = type;
        list = new ArrayList<Entity>();
        columns = columnList;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getType() {
        return type;
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

    public void remove(Entity target){
        for(int i = 0; i < size(); i++){
            if(list.get(i).equals(target)){
                list.remove(i);
                return;
            }
        }
    }

    public void sort(Comparator<Entity> cmp){
        Collections.sort(list, cmp);
    }

    public Entity get(int index){
        return list.get(index);
    }

    public List<String> getColumns(){
        return columns;
    }
}
