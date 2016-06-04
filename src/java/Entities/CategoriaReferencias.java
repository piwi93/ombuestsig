/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "categoria_referencias")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CategoriaReferencias.findAll", query = "SELECT c FROM CategoriaReferencias c"),
    @NamedQuery(name = "CategoriaReferencias.findById", query = "SELECT c FROM CategoriaReferencias c WHERE c.id = :id"),
    @NamedQuery(name = "CategoriaReferencias.findByDetalle", query = "SELECT c FROM CategoriaReferencias c WHERE c.detalle = :detalle")})
public class CategoriaReferencias implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "detalle")
    private String detalle;
    @OneToMany(mappedBy = "categoriaId", fetch = FetchType.EAGER)
    private List<ReferenciaOmbu> referenciaOmbuList;

    public CategoriaReferencias() {
    }

    public CategoriaReferencias(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    @XmlTransient
    public List<ReferenciaOmbu> getReferenciaOmbuList() {
        return referenciaOmbuList;
    }

    public void setReferenciaOmbuList(List<ReferenciaOmbu> referenciaOmbuList) {
        this.referenciaOmbuList = referenciaOmbuList;
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
        if (!(object instanceof CategoriaReferencias)) {
            return false;
        }
        CategoriaReferencias other = (CategoriaReferencias) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.CategoriaReferencias[ id=" + id + " ]";
    }
    
}
