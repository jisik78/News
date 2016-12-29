package com.example.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Administrator on 2016/12/22.
 */
@DatabaseTable(tableName = "db_user")
public class User {

    @DatabaseField(columnName = "_id",generatedId = true)
    private long id;

    @DatabaseField(columnName = "name")
    private String name;

    @DatabaseField(columnName = "age")
    private int age;

    public User(){
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public User(String name, int age){

        this.age = age;
        this.name = name;
    }





}
