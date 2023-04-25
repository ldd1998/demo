package org.example.entity;

import lombok.Data;

/**
 * @author ldd
 */
@Data
public class User {
//    @TableId(value = "id",type = IdType.AUTO)
    private String id;
    private String name;
    private int age;
    private String createTime;
    private String updateTime;

    public User() {
        initUser("0000507d42e042bca735943016fa2750","ldd",20,"2023-02-16 19:15:25","2023-02-16 19:15:25");
    }
    public User(String id,String name, int age, String createTime, String updateTime){
        initUser(id,name,age,createTime,updateTime);
    }

    public void initUser(String id,String name, int age, String createTime, String updateTime) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
