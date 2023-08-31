package benicio.soluces.contabilidadeapp.models;

import java.io.Serializable;
import java.util.List;

public class ClienteModel implements Serializable {
    List<PagamentoModel> listaPagamentos;



    String nome;
    int qtdParcelasFaltante;

    public List<PagamentoModel> getListaPagamentos() {
        return listaPagamentos;
    }

    public void setListaPagamentos(List<PagamentoModel> listaPagamentos) {
        this.listaPagamentos = listaPagamentos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtdParcelasFaltante() {
        return qtdParcelasFaltante;
    }

    public void setQtdParcelasFaltante(int qtdParcelasFaltante) {
        this.qtdParcelasFaltante = qtdParcelasFaltante;
    }
}
