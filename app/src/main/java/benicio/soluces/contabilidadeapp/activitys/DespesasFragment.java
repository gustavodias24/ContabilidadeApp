package benicio.soluces.contabilidadeapp.activitys;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.adapters.AdapterTransacao;
import benicio.soluces.contabilidadeapp.databinding.ActivityMainBinding;
import benicio.soluces.contabilidadeapp.databinding.FragmentDespesasBinding;
import benicio.soluces.contabilidadeapp.models.TransacaoModel;

public class DespesasFragment extends Fragment {

    private TextView msg;
    private RecyclerView recyclerView;
    private FragmentDespesasBinding vb;
    private ActivityMainBinding mainb;
    private List<TransacaoModel> lista = new ArrayList<>();
    private AdapterTransacao adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        vb = FragmentDespesasBinding.inflate(getLayoutInflater());
        msg = vb.despesasTextView;

        recyclerView = vb.recyclerDespesas;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);
        adapter = new AdapterTransacao(lista);
        recyclerView.setAdapter(adapter);

        if ( lista.size() == 0){
            msg.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            msg.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


        mainb = ActivityMainBinding.inflate(getLayoutInflater());
        mainb.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Toast.makeText(getActivity(), "page "+ position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        return vb.getRoot();
    }


}