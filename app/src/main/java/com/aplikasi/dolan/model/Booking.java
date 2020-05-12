
package com.aplikasi.dolan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Booking {

    @SerializedName("user_hotel")
    @Expose
    private List<UserDolan> userDolans = new ArrayList<>();
    //todo aaa
    public List<UserDolan> getUserDolans() {
        return userDolans;
    }

    public void setUserDolans(List<UserDolan> userDolans) {
        this.userDolans = userDolans;
    }

}
