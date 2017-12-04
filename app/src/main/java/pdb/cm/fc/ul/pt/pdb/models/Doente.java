package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;


public class Doente implements Serializable {

    private String id;
    private String name;
    private String age;
    private String email;
    private String photo;
    private String medicoAssign;
    private String lastLogin;

    public Doente (){

    }

    public Doente(String id, String name, String age, String email, String photo, String medicoAssign, String lastLogin) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.photo = photo;
        this.medicoAssign = medicoAssign;
        this.lastLogin = lastLogin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getMedicoAssign() {
        return medicoAssign;
    }

    public void setMedicoAssign(String medicoAssign) {
        this.medicoAssign = medicoAssign;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }
}
