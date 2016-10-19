package com.example.vito.wakemeup;

/**
 * Created by vito on 19/10/16.
 *
 * Création de la classe hobbies qui regroupe les 3 activités possibles pour une personne
 */

public class Hobbies {

    private int id;
    private String activity1;
    private String activity2;
    private String activity3;

    public Hobbies(){}

    public Hobbies(String activity1, String activity2, String activity3){
        this.activity1 = activity1;
        this.activity2 = activity2;
        this.activity3 = activity3;
    }

    //GET & SET ID
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


    //GET & SET activity1
    public String getActivity1() {
        return activity1;
    }
    public void setActivity1(String activity1) {
        this.activity1 = activity1;
    }

    //GET & SET activity2
    public String getActivity2() {
        return activity2;
    }
    public void setActivity2(String activity2) {
        this.activity2 = activity2;
    }

    //GET & SET activity3
    public String getActivity3() {
        return activity3;
    }
    public void setActivity3(String activity3) {
        this.activity3 = activity3;
    }

    public String toString(){
        return "ID : "+id+"\nActivité 1 : "+activity1+"\nActivité 2 : "+activity2+"\nActivité 3 : "+activity3;
    }
}
