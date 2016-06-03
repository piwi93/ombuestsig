/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entities;

import java.io.Serializable;
import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Galvadion
 */
@Entity
@Table(name = "referencia_ombu")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ReferenciaOmbu.findAll", query = "SELECT r FROM ReferenciaOmbu r"),
    @NamedQuery(name = "ReferenciaOmbu.findById", query = "SELECT r FROM ReferenciaOmbu r WHERE r.id = :id")})
public class ReferenciaOmbu implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "categoria_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private CategoriaReferencias categoriaId;
    @JoinColumn(name = "id", referencedColumnName = "id", insertable = false, updatable = false)
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    private Ombues ombues;

    public ReferenciaOmbu() {
    }

    public ReferenciaOmbu(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CategoriaReferencias getCategoriaReferenciasId() {
        return categoriaId;
    }

    public void setCategoriaReferenciasId(CategoriaReferencias categoriaId) {
        this.categoriaId = categoriaId;
    }

    public Ombues getOmbues() {
        return ombues;
    }

    public void setOmbues(Ombues ombues) {
        this.ombues = ombues;
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
        if (!(object instanceof ReferenciaOmbu)) {
            return false;
        }
        ReferenciaOmbu other = (ReferenciaOmbu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entities.ReferenciaOmbu[ id=" + id + " ]";
    }
    
}
