package benicio.soluces.contabilidadeapp.models;

import java.io.Serializable;
import java.util.List;

public class ClienteModel implements Serializable {
    Boolean verPagamento = false;

    public Double getDinheiroEmpretado() {
        return dinheiroEmpretado;
    }

    public void setDinheiroEmpretado(Double dinheiroEmpretado) {
        this.dinheiroEmpretado = dinheiroEmpretado;
    }

    public Double getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(Double valorParcela) {
        this.valorParcela = valorParcela;
    }

    public Double getDinheeiroParaSerPago() {
        return dinheeiroParaSerPago;
    }

    public void setDinheeiroParaSerPago(Double dinheeiroParaSerPago) {
        this.dinheeiroParaSerPago = dinheeiroParaSerPago;
    }

    Double dinheiroEmpretado;
    Double dinheeiroParaSerPago;
    Double valorParcela;

    public Boolean getVerPagamento() {
        return verPagamento;
    }

    public void setVerPagamento(Boolean verPagamento) {
        this.verPagamento = verPagamento;
    }

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
