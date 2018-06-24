package sigame.com.br.sigame.model;

import java.io.Serializable;
import java.util.Date;
public class Localizacao implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Double latitude;

    private Double longitude;

    private Date data;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
