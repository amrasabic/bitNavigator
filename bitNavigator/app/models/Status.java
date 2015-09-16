package models;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * Created by Amra on 9/16/2015.
 */
public class Status {

    public static final Integer DENIED = 0;
    public static final Integer ACCEPTED = 1;
    public static final Integer WAITING = 2;

    @Id
    public Integer id;
    @Column(unique = true)
    public Integer status;

    public Status() {

    }
}
