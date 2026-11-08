package padroescomportamentais.iterator;

public class Transacao {

    private String descricao;
    private double valor;
    private boolean suspeita;

    public Transacao(String descricao, double valor, boolean suspeita) {
        this.descricao = descricao;
        this.valor = valor;
        this.suspeita = suspeita;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isSuspeita() {
        return suspeita;
    }

    public void setSuspeita(boolean suspeita) {
        this.suspeita = suspeita;
    }
}
