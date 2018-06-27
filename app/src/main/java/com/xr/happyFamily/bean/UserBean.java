package com.xr.happyFamily.bean;

import java.io.Serializable;

public class UserBean implements Serializable {
    private static final long serialVersionUID = 1123123123L;



    @Override
    public String toString() {
        return "UserBean [username=" + username + ", password=" + password
                + ", sex=" + sex +  ", birthday="
                + birthday + "]";
    }

    String username;
    String password;
    char sex;
    String hobbit;
    String birthday;

    public UserBean() {
        super();
    }

    public UserBean(String username, String password, char sex, String hobbit,
                    String birthday) {
        super();
        this.username = username;
        this.password = password;
        this.sex = sex;
        this.hobbit = hobbit;
        this.birthday = birthday;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }


    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
