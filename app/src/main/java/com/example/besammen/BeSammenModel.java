package com.example.besammen;public class BeSammenModel {

    private String name, image, gender, diagnose, uid, status;
    private int age;

    public BeSammenModel() {
    }

    public BeSammenModel(String name, String image, String gender, String diagnose, String uid, String status, int age) {
        this.name = name;
        this.image = image;
        this.gender = gender;
        this.diagnose = diagnose;
        this.uid = uid;
        this.status = status;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
