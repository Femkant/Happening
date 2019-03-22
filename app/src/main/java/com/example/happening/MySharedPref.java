package com.example.happening;

class MySharedPref {
    private static final MySharedPref ourInstance = new MySharedPref();

    private SharedPref sharedPref;

    static MySharedPref getInstance() {
        return ourInstance;
    }

    private MySharedPref() {
    }

    public SharedPref getSharedPref() {
        return sharedPref;
    }

    public void setSharedPref(SharedPref sharedPref) {
        this.sharedPref = sharedPref;
    }
}
