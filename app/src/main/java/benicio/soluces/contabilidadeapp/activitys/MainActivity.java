package benicio.soluces.contabilidadeapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.databinding.ActivityMainBinding;
import benicio.soluces.contabilidadeapp.databinding.AdicionarTransicaoLayoutBinding;
import benicio.soluces.contabilidadeapp.databinding.CarregandoLayoutBinding;
import benicio.soluces.contabilidadeapp.models.TransacaoModel;
import benicio.soluces.contabilidadeapp.utils.Service;
import benicio.soluces.contabilidadeapp.utils.TransacaoStorageUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private Dialog dialog_carregando, dialog_escolha;

    private Retrofit retrofit;
    private Service service;
    private ActivityMainBinding vb;
    private Dialog dialogCriacao;

    private List<TransacaoModel> lista;

    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        vb = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(vb.getRoot());

        if ( TransacaoStorageUtil.loadTransacoes(getApplicationContext()) == null){
            lista = new ArrayList<>();
            Log.d("lista", "onCreate: " + lista.toString());
        }else{
            lista = TransacaoStorageUtil.loadTransacoes(getApplicationContext());
            Log.d("lista", "onCreate: " + lista.toString());
        }

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Receitas", ReceitasFragment.class)
                .add("Despesas", DespesasFragment.class)
                .add("Emprestimos", EmprestimoFragment.class)
                .create());

        ViewPager viewPager = vb.viewpager;
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        criarDialogCadastroTransicao();
        criarAlertDialogEscolha();

        vb.fabAdd.setOnClickListener( fabView -> {
            dialog_escolha.show();
        });

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                if ( TransacaoStorageUtil.loadTransacoes(getApplicationContext()) != null){
                    calcularResultado(TransacaoStorageUtil.loadTransacoes(getApplicationContext()));
                }

                handler.postDelayed(this, 1000); // Agendar a execução novamente após 3 segundos
            }
        };

        handler.postDelayed(runnable, 1000);

        criarRetrofit();
        criarDialogCarregando();

        vb.atualizarNuvemBTN.setOnClickListener( attView ->{
            puxarDados();
        });

        vb.enviarNuvemBTN.setOnClickListener( sendView ->{
            AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
            ab.setTitle("ATENÇÃO");
            ab.setMessage("TODOS OS DADOS DA NUVEM SERÃO SUBSTITUÍDOS PELOS DADOS ATUAIS DESSE DISPOSITIVO");
            ab.setPositiveButton("Prosseguir", (dialogInterface, i) -> enviarDados());
            ab.create().show();
        });
    }

    public void criarAlertDialogEscolha(){
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setMessage("Escolha uma opção.");
        b.setNegativeButton("Ver clientes", (dialogInterface, i) -> {
            startActivity(new Intent(getApplicationContext(), ClientesActivity.class));
            dialog_escolha.dismiss();
        });
        b.setPositiveButton("Criar transição", (dialogInterface, i) -> {
            dialogCriacao.show();
            dialog_escolha.dismiss();
        });
        dialog_escolha = b.create();
    }

    @SuppressLint("DefaultLocale")
    public void criarDialogCadastroTransicao(){
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        AdicionarTransicaoLayoutBinding atb = AdicionarTransicaoLayoutBinding.inflate(getLayoutInflater());

        atb.radioReceita.setOnClickListener( radioReceitaView -> {
            atb.layoutReceita.setVisibility(View.GONE);
        });
        atb.radioDespesa.setOnClickListener( radioDespesaView -> {
            atb.layoutReceita.setVisibility(View.GONE);
        });

        atb.radioEmprestimo.setOnClickListener( radioEmprestimoView ->{
            atb.layoutReceita.setVisibility(View.VISIBLE);
        });

        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1; 
        int dia = cal.get(Calendar.DAY_OF_MONTH);

        atb.calcularBtn.setOnClickListener( calcularView -> {
            String qtdParcelas = atb.parcelasEdtText.getText().toString();
            String jurozPorCento = atb.jurozEdtText.getText().toString();
            String valorString = atb.valorEdtText.getText().toString();

            if ( !qtdParcelas.isEmpty() && !jurozPorCento.isEmpty() && !valorString.isEmpty()){
                Double valorComJuros =  calcularValorComJuros(Double.parseDouble(valorString), Double.parseDouble(jurozPorCento));
                atb.resultadoCalcText.setText(
                        String.format(
                                "Valor final com juros.: R$ %.2f\nDividido de %s parcelas de R$ %.2f",
                                valorComJuros, qtdParcelas, valorComJuros/Double.parseDouble(qtdParcelas)
                        )
                );

            }else{
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            }
        });
        atb.confimarBtn.setOnClickListener( confirmarView -> {
            int tipo = 0;
            String descricao = atb.descriEdtText.getText().toString();
            String data = dia + "/" + mes + "/" + ano;
            
            String valorString = atb.valorEdtText.getText().toString();
            if ( !valorString.isEmpty() ){
                double valor = Double.parseDouble(valorString);

                if ( atb.radioDespesa.isChecked() )
                    tipo = 0;

                if ( atb.radioReceita.isChecked() )
                    tipo = 1;

                if ( atb.radioEmprestimo.isChecked() )
                    tipo = 2;

                if ( !descricao.isEmpty() ){
                    TransacaoModel transacaoModel = new TransacaoModel(
                            tipo,
                            descricao,
                            data,
                            valor
                    );

                    if ( tipo == 2){
                        String qtdParcelas = atb.parcelasEdtText.getText().toString();
                        String jurozPorCento = atb.jurozEdtText.getText().toString();

                        if ( !qtdParcelas.isEmpty() && !jurozPorCento.isEmpty()){
                            transacaoModel.setValorJuros(
                                    calcularValorComJuros(valor, Double.parseDouble(jurozPorCento))
                            );
                        }else{
                            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    lista.add(transacaoModel);
                    TransacaoStorageUtil.saveTransacoes(getApplicationContext(), lista);
                    Toast.makeText(this, "Transição adicionada!", Toast.LENGTH_SHORT).show();

                    atb.descriEdtText.setText("");
                    atb.jurozEdtText.setText("");
                    atb.valorEdtText.setText("");
                    atb.parcelasEdtText.setText("");

                    dialogCriacao.dismiss();

                }else{
                    Toast.makeText(this, "Descrição obrigatória.", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Valor obrigatório.", Toast.LENGTH_SHORT).show();
            }
        });

        b.setView(atb.getRoot());
        dialogCriacao = b.create();
    }

    public  double calcularValorComJuros(double valor, double juros) {
        double valorDoJuros = valor + ((valor * juros)/100);
        return valorDoJuros;
    }

    public void calcularResultado(List<TransacaoModel> listaResultado){
        Double dinheiroEmprestado = 0.0;
        Double receitas = 0.0;
        Double despesas = 0.0;
        Double total;
        Double faltaReceber;

        for ( TransacaoModel transacaoModel : listaResultado){
            switch ( transacaoModel.getTipo() ){
                case 0:
                    despesas += transacaoModel.getValor();
                    break;
                case 1:
                    receitas += transacaoModel.getValor();
                    break;
                case 2:
                    dinheiroEmprestado += transacaoModel.getValorJuros();
                    break;
            }
        }

        total = receitas - despesas;
        faltaReceber = dinheiroEmprestado - receitas;

        vb.faltaReceberTextView.setText(
               String.format("Falta receber: R$ %.2f", faltaReceber)
        );

        vb.receitasTextView.setText(
                String.format("Receitas: R$ %.2f", receitas)
        );

        vb.despesasTextView.setText(
                String.format("Despesas: R$ %.2f", despesas)
        );

        vb.totalTextView.setText(
                String.format("Total: R$ %.2f", total)
        );
    }

    public void criarRetrofit(){
        retrofit = new Retrofit.Builder().baseUrl("https://contabilidade-api-teal.vercel.app/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        service = retrofit.create(Service.class);
    }

    public void puxarDados(){
        dialog_carregando.show();
        service.puxarTransacoes().enqueue(new Callback<List<TransacaoModel>>() {
            @Override
            public void onResponse(Call<List<TransacaoModel>> call, Response<List<TransacaoModel>> response) {
                if (response.isSuccessful()){
                    TransacaoStorageUtil.saveTransacoes(getApplicationContext(), response.body());
                    Toast.makeText(MainActivity.this, "Transações recuperadas!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
                }
                dialog_carregando.dismiss();
            }

            @Override
            public void onFailure(Call<List<TransacaoModel>> call, Throwable t) {
                dialog_carregando.dismiss();
            }
        });
    }

    public void enviarDados(){
        dialog_carregando.show();
        service.enviarTransacoes(lista).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, response.body(), Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("bucetinha", "onResponse: " + response.message());
                    Toast.makeText(MainActivity.this, "Erro de conexão!", Toast.LENGTH_SHORT).show();
                }
                dialog_carregando.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog_carregando.dismiss();
            }
        });
    }

    public void criarDialogCarregando(){
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        b.setView(CarregandoLayoutBinding.inflate(getLayoutInflater()).getRoot());
        b.setCancelable(false);
        dialog_carregando = b.create();
    }
}