package sigame.com.br.sigame.model;

public enum Teste {

    ADMINISTRADOR(1L),
    PEDESTRE(2L);

    private Long value;

    Teste(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

}
