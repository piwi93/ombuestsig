package Entities;
// Generated 16/05/2016 07:45:32 PM by Hibernate Tools 4.3.1


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * ZonaOmbu generated by hbm2java
 */
@Entity
@Table(name="zona_ombu"
    ,schema="public"
)
public class ZonaOmbu  implements java.io.Serializable {


     private int id;
     private Ombues ombues;
     private Serializable geom;

    public ZonaOmbu() {
    }

	
    public ZonaOmbu(Ombues ombues) {
        this.ombues = ombues;
    }
    public ZonaOmbu(Ombues ombues, Serializable geom) {
       this.ombues = ombues;
       this.geom = geom;
    }
   
     @GenericGenerator(name="generator", strategy="foreign", parameters=@Parameter(name="property", value="ombues"))@Id @GeneratedValue(generator="generator")

    
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

@OneToOne(fetch=FetchType.LAZY)@PrimaryKeyJoinColumn
    public Ombues getOmbues() {
        return this.ombues;
    }
    
    public void setOmbues(Ombues ombues) {
        this.ombues = ombues;
    }

    
    @Column(name="geom")
    public Serializable getGeom() {
        return this.geom;
    }
    
    public void setGeom(Serializable geom) {
        this.geom = geom;
    }




}


