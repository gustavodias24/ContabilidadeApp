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
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.adapters.AdapterQuitado;
import benicio.soluces.contabilidadeapp.databinding.ActivityQuitadosBinding;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.utils.ClienteStorageUtil;
import benicio.soluces.contabilidadeapp.utils.RecyclerItemClickListener;

public class QuitadosActivity extends AppCompatActivity {
    private Dialog dialog_pergunta;
    private RecyclerView recyclerView;
    private AdapterQuitado adapter;

    private List<ClienteModel> lista;
    private ActivityQuitadosBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuitadosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        lista = ClienteStorageUtil.loadClientesQuitado(getApplicationContext()) == null ? new ArrayList<>() : ClienteStorageUtil.loadClientesQuitado(getApplicationContext());
        configurarRecycler();
        configurarAcoesCliente();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Clientes quitados.");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if ( item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void configurarRecycler(){
        recyclerView = binding.quitadosRecycler;
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterQuitado(lista, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void configurarAcoesCliente(){
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        AlertDialog.Builder b = new AlertDialog.Builder(QuitadosActivity.this);
                        b.setMessage("Escolha uma opção.");
                        String histotyText =
                                lista.get(position).getVerPagamento() != null && lista.get(position).getVerPagamento() ? "Esconder pagamentos" : "Ver pagamentos";

                        b.setNeutralButton(histotyText, (dialogInterface, i) -> {
                            if (lista.get(position).getVerPagamento() && lista.get(position).getVerPagamento() != null){
                                lista.get(position).setVerPagamento(false);
                            }else{
                                lista.get(position).setVerPagamento(true);
                            }
                            adapter.notifyItemChanged(position);
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
}