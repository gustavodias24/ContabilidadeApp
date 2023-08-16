package benicio.soluces.contabilidadeapp.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.adapters.AdapterCliente;
import benicio.soluces.contabilidadeapp.databinding.ActivityClientesBinding;
import benicio.soluces.contabilidadeapp.databinding.AdicionarClienteLayoutBinding;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.utils.ClienteStorageUtil;

public class ClientesActivity extends AppCompatActivity {

    private ActivityClientesBinding binding;
    private Dialog dialog_adicionar;

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
        configurarAvisoDeVazio();

        binding.clientFab.setOnClickListener( fabView -> {
            dialog_adicionar.show();
        });

    }

    public void criarDialogAdicionar(){
        AlertDialog.Builder b = new AlertDialog.Builder(ClientesActivity.this);
        AdicionarClienteLayoutBinding bindingClienteLayout = AdicionarClienteLayoutBinding.inflate(getLayoutInflater());
        bindingClienteLayout.titleText.setText("Adicionar cliente");
        bindingClienteLayout.confirmarBtn.setText("ADICIONAR");
        b.setView(bindingClienteLayout.getRoot());
        dialog_adicionar = b.create();
    }
    public void configurarRecycler(){
        recyclerView = binding.recyclerClientes;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterCliente(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    public void configurarAvisoDeVazio(){
        if (ClienteStorageUtil.loadClientes(getApplicationContext()) != null){
            lista.addAll(ClienteStorageUtil.loadClientes(getApplicationContext()));
            binding.avisoClienteText.setVisibility(View.GONE);
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