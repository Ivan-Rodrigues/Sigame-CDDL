package sigame.com.br.sigame.model.enums;

public enum TipoUsuarioEnum {

    ADMINISTRADOR(1L),
    PEDESTRE(2L);

    private Long value;

    TipoUsuarioEnum(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
