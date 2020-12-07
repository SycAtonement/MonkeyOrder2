package com.example.monkeyorder;


class FoodType {
    private String typename;
    private int typepic;


    public FoodType(String typename, int typepic) {
        this.typename = typename;
        this.typepic = typepic;
    }


    public String getTypename() {
        return typename;
    }

    public int getTypepic() {
        return typepic;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }

    public void setTypepic(int typepic) {
        this.typepic = typepic;
    }
}
