/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Galvadion
 */
@Entity
@Table(name = "ombues")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ombues.findAll", query = "SELECT o FROM Ombues o"),
    @NamedQuery(name = "Ombues.findById", query = "SELECT o FROM Ombues o WHERE o.id = :id"),
    @NamedQuery(name = "Ombues.findByNombre", query = "SELECT o FROM Ombues o WHERE o.nombre = :nombre"),
    @NamedQuery(name = "Ombues.findByDescripcion", query = "SELECT o FROM Ombues o WHERE o.descripcion = :descripcion"),
    @NamedQuery(name = "Ombues.findByDireccion", query = "SELECT o FROM Ombues o WHERE o.direccion = :direccion"),
    @NamedQuery(name = "Ombues.findByUbicacion", query = "SELECT o FROM Ombues o WHERE o.ubicacion = :ubicacion"),
    @NamedQuery(name = "Ombues.findByExternalRef", query = "SELECT o FROM Ombues o WHERE o.externalRef = :externalRef")})
public class Ombues implements Serializable {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idOmbu")
    private Collection<Imagenes> imagenesCollection;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "direccion")
    private String direccion;
    @Column(name = "ubicacion")
    private String ubicacion;
    @Column(name = "external_ref")
    private String externalRef;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Categoria idCategoria;


    public Ombues() {
    }

    public Ombues(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getExternalRef() {
        return externalRef;
    }

    public void setExternalRef(String externalRef) {
        this.externalRef = externalRef;
    }

    public Categoria getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Categoria idCategoria) {
        this.idCategoria = idCategoria;
    }



    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ombues)) {
            return false;
        }
        Ombues other = (Ombues) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.Ombues[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<Imagenes> getImagenesCollection() {
        return imagenesCollection;
    }

    public void setImagenesCollection(Collection<Imagenes> imagenesCollection) {
        this.imagenesCollection = imagenesCollection;
    }
    
}
