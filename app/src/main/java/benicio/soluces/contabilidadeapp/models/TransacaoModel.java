package benicio.soluces.contabilidadeapp.models;

import java.io.Serializable;

public class TransacaoModel implements Serializable {
    int tipo; // 0 despesa 1 receita
    String descricao, titulo;
    double valor;

    public TransacaoModel(int tipo, String descricao, String titulo, double valor) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.titulo = titulo;
        this.valor = valor;
    }

    public TransacaoModel() {
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
