package benicio.soluces.contabilidadeapp.activitys;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.adapters.AdapterTransacao;
import benicio.soluces.contabilidadeapp.databinding.FragmentEmprestimoBinding;
import benicio.soluces.contabilidadeapp.models.TransacaoModel;
import benicio.soluces.contabilidadeapp.utils.TransacaoStorageUtil;


public class EmprestimoFragment extends Fragment {
    private AdapterTransacao adapter;
    private List<TransacaoModel> lista;
    private RecyclerView recyclerView;
    private TextView msg;
    private Handler handler;
    private Runnable runnable;

    FragmentEmprestimoBinding evb;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        evb = FragmentEmprestimoBinding.inflate(getLayoutInflater());
        msg = evb.msg;

        recyclerView = evb.recyclerEmprestimos;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        if ( TransacaoStorageUtil.loadTransacoes(getActivity()) == null){
            lista = new ArrayList<>();
        }else{
            lista = TransacaoStorageUtil.loadTransacoes(getActivity());
        }

        adapter = new AdapterTransacao(lista);
        recyclerView.setAdapter(adapter);

        handler = new Handler(Looper.getMainLooper());

        runnable = new Runnable() {
            @Override
            public void run() {
                updateRecycler();
                handler.postDelayed(this, 1000); // Agendar a execução novamente após 3 segundos
            }
        };

        handler.postDelayed(runnable, 1000);

        return evb.getRoot();
    }

    public  void updateRecycler(){
        lista.clear();
        if ( TransacaoStorageUtil.loadTransacoes(getActivity()) != null){
            for ( TransacaoModel transacao : TransacaoStorageUtil.loadTransacoes(getActivity())){
                if (transacao.getTipo() == 2)
                    lista.add(transacao);
            }
        }

        if ( lista.size() == 0){
            msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            msg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}