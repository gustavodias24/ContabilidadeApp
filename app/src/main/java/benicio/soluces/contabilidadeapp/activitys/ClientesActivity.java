package benicio.soluces.contabilidadeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
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
import benicio.soluces.contabilidadeapp.databinding.CarregandoLayoutBinding;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.models.PagamentoModel;
import benicio.soluces.contabilidadeapp.utils.ClienteStorageUtil;
import benicio.soluces.contabilidadeapp.utils.RecyclerItemClickListener;
import benicio.soluces.contabilidadeapp.utils.Service;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding binding;
    private Dialog dialog_adicionar, dialog_editar, dialog_adicionar_pagamento, dialog_pergunta, dialog_carregando;

    private RecyclerView recyclerView;
    private AdapterCliente adapter;

    private List<ClienteModel> lista = new ArrayList<>();
    private Retrofit retrofit;
    private Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClientesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setTitle("Clientes");
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        criarDialogAdicionar();
        configurarRecycler();
        configurarAcoesCliente();
        configurarAvisoDeVazio();

        binding.clientFab.setOnClickListener( fabView -> {
            dialog_adicionar.show();
        });

        binding.pesquisarBtn.setOnClickListener( pesquisar -> {
            String query = binding.nomePesquisaEdt.getText().toString();
            limitarLista(query);
        });
    criarDialogCarregando();
    criarRetrofit();
    }

    public void limitarLista(String query) {
        if (query.isEmpty()) {
            configurarAvisoDeVazio();
        } else {
            List<ClienteModel> itemsToRemove = new ArrayList<>();

            for (ClienteModel item : lista) {
                if (!item.getNome().equals(query)) {
                    itemsToRemove.add(item);
                }
            }

            lista.removeAll(itemsToRemove);
            adapter.notifyDataSetChanged();
        }
    }

    public void criarDialogAdicionar(){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarClienteLayoutBinding bindingClienteLayout = AdicionarClienteLayoutBinding.inflate(getLayoutInflater());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        bindingClienteLayout.dataEdt.setText(currentDate);

        bindingClienteLayout.confirmarBtn.setOnClickListener( btnView -> {
            String nomeCliente = bindingClienteLayout.nomeClienteEdt.getText().toString();
            int qtd_parcelas = Integer.parseInt(
                    bindingClienteLayout.qtdParcelasEdt.getText().toString()
            );

            Double valorEmprestado = Double.parseDouble(bindingClienteLayout.dinheiroEmprestadoEdt.getText().toString());
            Double juros = Double.parseDouble(bindingClienteLayout.jurosEdt.getText().toString());
            Double dinheiroParaPagar = calcularValorComJuros(valorEmprestado, juros);
            String dataCliente = bindingClienteLayout.dataEdt.getText().toString();

            ClienteModel clienteModel = new ClienteModel();

            clienteModel.setValorParcela(dinheiroParaPagar / qtd_parcelas);
            clienteModel.setDinheiroEmpretado(valorEmprestado);
            clienteModel.setDinheeiroParaSerPago(dinheiroParaPagar);


            clienteModel.setNome(nomeCliente);
            clienteModel.setQtdParcelasFaltante(qtd_parcelas);
            clienteModel.setData(dataCliente);

            lista.add(clienteModel);
            binding.recyclerClientes.setVisibility(View.VISIBLE);
            atualizarListaSavlar();
            bindingClienteLayout.nomeClienteEdt.setText("");
            bindingClienteLayout.qtdParcelasEdt.setText("");
            bindingClienteLayout.jurosEdt.setText("");
            bindingClienteLayout.dinheiroEmprestadoEdt.setText("");
            dialog_adicionar.dismiss();
            Toast.makeText(this, "Cliente adicionado!", Toast.LENGTH_SHORT).show();
        });
        bindingClienteLayout.titleText.setText("Adicionar cliente");
        bindingClienteLayout.confirmarBtn.setText("ADICIONAR");
        b.setView(bindingClienteLayout.getRoot());
        dialog_adicionar = b.create();
    }

    public  double calcularValorComJuros(double valor, double juros) {
        double valorDoJuros = valor + ((valor * juros)/100);
        return valorDoJuros;
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

                        String histotyText =
                                lista.get(position).getVerPagamento() != null && lista.get(position).getVerPagamento() ? "Esconder pagamentos" : "Ver pagamentos";

                        b.setNeutralButton(histotyText, (dialogInterface, i) -> {
                            if (lista.get(position).getVerPagamento() && lista.get(position).getVerPagamento() != null){
                                lista.get(position).setVerPagamento(false);
                            }else{
                                lista.get(position).setVerPagamento(true);
                            }
                            adapter.notifyDataSetChanged();
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

    @SuppressLint("DefaultLocale")
    public void chamarAdicionarPagamento(ClienteModel clienteClicado){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarPagamentoLayoutBinding pagamentoLayoutBinding = AdicionarPagamentoLayoutBinding.inflate(getLayoutInflater());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());

        pagamentoLayoutBinding.descriEdt.setText("Valor diário");
        pagamentoLayoutBinding.valorEdt.setText(String.format("%.2f", clienteClicado.getValorParcela()));
        pagamentoLayoutBinding.dataEdt.setText(currentDate);
        pagamentoLayoutBinding.qtdPagamentoEdt.setText("1");

        pagamentoLayoutBinding.enviarBtn.setOnClickListener( enviarView -> {
            int qtdPagamento = Integer.parseInt(pagamentoLayoutBinding.qtdPagamentoEdt.getText().toString());
            String descri = pagamentoLayoutBinding.descriEdt.getText().toString();
            Double valor = Double.parseDouble(pagamentoLayoutBinding.valorEdt.getText().toString().replace(",", "."));
            String data = pagamentoLayoutBinding.dataEdt.getText().toString();
            PagamentoModel pagamento = new PagamentoModel();
            pagamento.setTitulo(descri);
            pagamento.setValor(valor);

            if (data.isEmpty()) {
                pagamento.setData(currentDate);
            } else {
                pagamento.setData(data);
            }
            if (qtdPagamento > 0){
                for ( int i = qtdPagamento ; i > 0 ; i--){
                    if ( clienteClicado.getListaPagamentos() == null){
                        List<PagamentoModel> listaNovaPagamento = new ArrayList<>();
                        listaNovaPagamento.add(pagamento);
                        clienteClicado.setListaPagamentos(listaNovaPagamento);
                    }else{
                        clienteClicado.getListaPagamentos().add(pagamento);
                    }
                }
            }

            pagamentoLayoutBinding.qtdPagamentoEdt.setText("1");
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.api_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        if ( item.getItemId() == R.id.enviarDados){
//            if ( ClienteStorageUtil.loadClientes(getApplicationContext()) != null){
//                dialog_carregando.show();
//                service.enviarClientes(ClienteStorageUtil.loadClientes(getApplicationContext())).enqueue(new Callback<String>() {
//                    @Override
//                    public void onResponse(Call<String> call, Response<String> response) {
//                        if (response.isSuccessful()){
//                        }else{
//                            Toast.makeText(ClientesActivity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
//                        }
//                        dialog_carregando.dismiss();
//                    }
//
//                    @Override
//                    public void onFailure(Call<String> call, Throwable t) {
//
//                    }
//                });
//            }
//        }

        if ( item.getItemId() == R.id.contabilidade){
          startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void criarDialogCarregando(){
        android.app.AlertDialog.Builder b = new android.app.AlertDialog.Builder(ClientesActivity.this);
        b.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        b.setCancelable(false);
        dialog_carregando = b.create();
    }
    public void criarRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl("https://contabilidade-api-teal.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(Service.class);
    }
}