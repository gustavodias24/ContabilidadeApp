package benicio.soluces.contabilidadeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.adapters.AdapterCliente;
import benicio.soluces.contabilidadeapp.databinding.ActivityClientesBinding;
import benicio.soluces.contabilidadeapp.databinding.AdicionarClienteLayoutBinding;
import benicio.soluces.contabilidadeapp.databinding.AdicionarPagamentoLayoutBinding;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.models.PagamentoModel;
import benicio.soluces.contabilidadeapp.utils.ClienteStorageUtil;
import benicio.soluces.contabilidadeapp.utils.RecyclerItemClickListener;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding binding;
    private Dialog dialog_adicionar, dialog_editar, dialog_adicionar_pagamento, dialog_pergunta;

    private RecyclerView recyclerView;
    private AdapterCliente adapter;

    private List<ClienteModel> lista = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Clientes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        criarDialogAdicionar();
        configurarRecycler();
        configurarAcoesCliente();
        configurarAvisoDeVazio();

        binding.clientFab.setOnClickListener( fabView -> {
            dialog_adicionar.show();
        });

    }

    public void criarDialogAdicionar(){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarClienteLayoutBinding bindingClienteLayout = AdicionarClienteLayoutBinding.inflate(getLayoutInflater());
        bindingClienteLayout.confirmarBtn.setOnClickListener( btnView -> {
            String nomeCliente = bindingClienteLayout.nomeClienteEdt.getText().toString();
            int qtd_parcelas = Integer.parseInt(
                    bindingClienteLayout.qtdParcelasEdt.getText().toString()
            );
            ClienteModel clienteModel = new ClienteModel();
            clienteModel.setNome(nomeCliente);
            clienteModel.setQtdParcelasFaltante(qtd_parcelas);
            lista.add(clienteModel);
            atualizarListaSavlar();
            bindingClienteLayout.nomeClienteEdt.setText("");
            bindingClienteLayout.qtdParcelasEdt.setText("");
            dialog_adicionar.dismiss();
            Toast.makeText(this, "Cliente adicionado!", Toast.LENGTH_SHORT).show();
        });
        bindingClienteLayout.titleText.setText("Adicionar cliente");
        bindingClienteLayout.confirmarBtn.setText("ADICIONAR");
        b.setView(bindingClienteLayout.getRoot());
        dialog_adicionar = b.create();
    }
    public void atualizarListaSavlar(){
        ClienteStorageUtil.saveUsuario(getApplicationContext(), lista);
        configurarAvisoDeVazio();
        adapter.notifyDataSetChanged();
    }
    public void configurarRecycler(){
        recyclerView = binding.recyclerClientes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterCliente(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    public void configurarAcoesCliente(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
                        b.setMessage("Escolha uma opção.");
                        b.setPositiveButton("Editar", (dialogInterface, i) -> {
                                chamarEdicaoCliente(lista.get(position));
                                dialog_pergunta.dismiss();
                        });

                        b.setNegativeButton("Adicionar Pagamento", (dialogInterface, i) -> {
                            chamarAdicionarPagamento(lista.get(position));
                            dialog_pergunta.dismiss();
                        });

                        dialog_pergunta = b.create();
                        dialog_pergunta.show();
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    }
                }
        ));
    }
    public void chamarEdicaoCliente(ClienteModel clienteClicado){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarClienteLayoutBinding bindingClienteLayout = AdicionarClienteLayoutBinding.inflate(getLayoutInflater());
        bindingClienteLayout.nomeClienteEdt.setText(clienteClicado.getNome());
        bindingClienteLayout.qtdParcelasEdt.setText(clienteClicado.getQtdParcelasFaltante() + "");
        bindingClienteLayout.titleText.setText("Editar cliente");
        bindingClienteLayout.confirmarBtn.setText("Editar");
        bindingClienteLayout.confirmarBtn.setOnClickListener( editarView -> {
            String novoNome = bindingClienteLayout.nomeClienteEdt.getText().toString();
            int novaQtd = Integer.parseInt(bindingClienteLayout.qtdParcelasEdt.getText().toString());
            clienteClicado.setNome(novoNome);
            clienteClicado.setQtdParcelasFaltante(novaQtd);
            atualizarListaSavlar();
            dialog_editar.dismiss();
        });
        b.setView(bindingClienteLayout.getRoot());
        dialog_editar = b.create();
        dialog_editar.show();
    }

    public void chamarAdicionarPagamento(ClienteModel clienteClicado){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarPagamentoLayoutBinding pagamentoLayoutBinding = AdicionarPagamentoLayoutBinding.inflate(getLayoutInflater());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        pagamentoLayoutBinding.enviarBtn.setOnClickListener( enviarView -> {
            String descri = pagamentoLayoutBinding.descriEdt.getText().toString();
            Double valor = Double.parseDouble(pagamentoLayoutBinding.valorEdt.getText().toString());
            String data = pagamentoLayoutBinding.dataEdt.getText().toString();
            PagamentoModel pagamento = new PagamentoModel();
            pagamento.setTitulo(descri);
            pagamento.setValor(valor);

            if (data.isEmpty()) {
                pagamento.setData(currentDate);
            } else {
                pagamento.setData(data);
            }

            if ( clienteClicado.getListaPagamentos() == null){
                List<PagamentoModel> listaNovaPagamento = new ArrayList<>();
                listaNovaPagamento.add(pagamento);
                clienteClicado.setListaPagamentos(listaNovaPagamento);
            }else{
                clienteClicado.getListaPagamentos().add(pagamento);
            }

            pagamentoLayoutBinding.dataEdt.setText("");
            pagamentoLayoutBinding.descriEdt.setText("");
            pagamentoLayoutBinding.valorEdt.setText("");
            atualizarListaSavlar();
            dialog_adicionar_pagamento.dismiss();
        });

        b.setView(pagamentoLayoutBinding.getRoot());
        dialog_adicionar_pagamento = b.create();
        dialog_adicionar_pagamento.show();
    }
    public void configurarAvisoDeVazio(){
        lista.clear();
        if (ClienteStorageUtil.loadClientes(getApplicationContext()) != null){
            lista.addAll(ClienteStorageUtil.loadClientes(getApplicationContext()));
            binding.avisoClienteText.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }else{
            binding.recyclerClientes.setVisibility(View.GONE);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}