package benicio.soluces.contabilidadeapp.models;

import java.io.Serializable;

public class TransacaoModel implements Serializable {
    int tipo; // 0 despesa 1 receita
    String descricao, data;
    double valor;

    public TransacaoModel(int tipo, String descricao, String data, double valor) {
        this.tipo = tipo;
        this.descricao = descricao;
        this.data = data;
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
