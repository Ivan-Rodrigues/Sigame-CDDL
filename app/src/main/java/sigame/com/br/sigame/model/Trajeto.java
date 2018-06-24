package sigame.com.br.sigame.model;

import java.io.Serializable;
import java.util.Date;

public class Trajeto implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;

    private Double latitudeInicial;

    private Double longitudeInicial;

    private Double latitudeFinal;

    private Double longitudeFinal;

    private Date dataTrajeto;

    private Usuario usuario;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitudeInicial() {
        return latitudeInicial;
    }

    public void setLatitudeInicial(Double latitudeInicial) {
        this.latitudeInicial = latitudeInicial;
    }

    public Double getLongitudeInicial() {
        return longitudeInicial;
    }

    public void setLongitudeInicial(Double longitudeInicial) {
        this.longitudeInicial = longitudeInicial;
    }

    public Double getLatitudeFinal() {
        return latitudeFinal;
    }

    public void setLatitudeFinal(Double latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    public Double getLongitudeFinal() {
        return longitudeFinal;
    }

    public void setLongitudeFinal(Double longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    public Date getDataTrajeto() {
        return dataTrajeto;
    }

    public void setDataTrajeto(Date dataTrajeto) {
        this.dataTrajeto = dataTrajeto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
