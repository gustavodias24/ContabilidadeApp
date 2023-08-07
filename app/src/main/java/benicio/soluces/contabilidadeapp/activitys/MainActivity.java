package benicio.soluces.contabilidadeapp.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
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
import benicio.soluces.contabilidadeapp.models.TransacaoModel;
import benicio.soluces.contabilidadeapp.utils.TransacaoStorageUtil;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding vb;
    private Dialog dialogCriacao;

    private List<TransacaoModel> lista;

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
                .create());

        ViewPager viewPager = vb.viewpager;
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        viewPagerTab.setViewPager(viewPager);

        criarDialogCadastroTransicao();

        vb.fabAdd.setOnClickListener( fabView -> {
            dialogCriacao.show();
        });
    }

    public void criarDialogCadastroTransicao(){
        AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
        AdicionarTransicaoLayoutBinding atb = AdicionarTransicaoLayoutBinding.inflate(getLayoutInflater());
        atb.radioReceita.setOnClickListener( radioReceitaView -> {
            atb.layoutReceita.setVisibility(View.VISIBLE);
        });
        atb.radioDespesa.setOnClickListener( radioDespesaView -> {
            atb.layoutReceita.setVisibility(View.GONE);
        });

        Calendar cal = Calendar.getInstance();
        int ano = cal.get(Calendar.YEAR);
        int mes = cal.get(Calendar.MONTH) + 1; 
        int dia = cal.get(Calendar.DAY_OF_MONTH);

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

                if ( !descricao.isEmpty() ){
                    
                    TransacaoModel transacaoModel = new TransacaoModel(
                            tipo,
                            descricao,
                            data,
                            valor
                    );

                    lista.add(transacaoModel);
                    TransacaoStorageUtil.saveTransacoes(getApplicationContext(), lista);
                    Toast.makeText(this, "Transição adicionada!", Toast.LENGTH_SHORT).show();
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

}