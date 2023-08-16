package benicio.soluces.contabilidadeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.models.ClienteModel;

public class AdapterCliente extends RecyclerView.Adapter<AdapterCliente.MyViewHolder>{

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
            holder.falta.setText("Falta: " + (clienteModel.getListaPagamentos().size() - clienteModel.getQtdParcelasFaltante()));
        }else{
            holder.pagas.setText("Pagas: 0");
            holder.falta.setText("Falta: 0");
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
