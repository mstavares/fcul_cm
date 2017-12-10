package pdb.cm.fc.ul.pt.pdb.models;

import java.io.Serializable;

/**
 * Created by nunonelas on 10/12/17.
 */

public class Medico implements Serializable {

    private String id;
    private String email;
    private String name;

    public Medico() {
    }

    public Medico(String id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
