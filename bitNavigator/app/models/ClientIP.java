package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by hajrudin.sehic on 15/10/15.
 */
@Entity
public class ClientIP {
    @Id
    public Integer id;
    @ManyToOne
    public Place place;
    @Column
    public String ipAddress;

    public ClientIP(){

    }
}
