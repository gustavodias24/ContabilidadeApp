package benicio.soluces.contabilidadeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.utils.ClienteStorageUtil;

public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.MyViewHolder>{

    AdapterPagamento adapter;
    List<ClienteModel> lista;
    Context context;

    public AdapterCliente(List<ClienteModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cliente_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClienteModel clienteModel = lista.get(position);
        holder.nomeCliente.setText(clienteModel.getNome());

        holder.parcelas.setText("Parcelas: " + clienteModel.getQtdParcelasFaltante());

        if ( clienteModel.getListaPagamentos() != null ){
            holder.pagas.setText("Pagas: " + clienteModel.getListaPagamentos().size());
            holder.falta.setText("Falta: " + (clienteModel.getQtdParcelasFaltante() - clienteModel.getListaPagamentos().size()));
        }else{
            holder.pagas.setText("Pagas: 0");
            holder.falta.setText("Falta: 0");
        }

        if ( clienteModel.getListaPagamentos() != null){
            holder.recycleParcelas.setHasFixedSize(true);
            holder.recycleParcelas.setLayoutManager(new LinearLayoutManager(context));
            holder.recycleParcelas.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
            adapter = new AdapterPagamento(clienteModel.getListaPagamentos(), context);
            holder.recycleParcelas.setAdapter(adapter);
            ClienteStorageUtil.saveUsuario(context, lista);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nomeCliente, parcelas, pagas,falta;
        RecyclerView recycleParcelas;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCliente = itemView.findViewById(R.id.nome_cliente_text);
            parcelas = itemView.findViewById(R.id.qtd_parcelas_text);
            pagas = itemView.findViewById(R.id.parcelas_pagas_text);
            falta = itemView.findViewById(R.id.parcelas_falta_text);
            recycleParcelas = itemView.findViewById(R.id.recyclerParcelas);
        }
    }
}
