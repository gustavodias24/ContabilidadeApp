package benicio.soluces.contabilidadeapp.adapters;


import benicio.soluces.contabilidadeapp.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.contabilidadeapp.models.PagamentoModel;

public class AdapterPagamento extends RecyclerView.Adapter<AdapterPagamento.MyViewHolder>{

    List<PagamentoModel> lista;
    Context context;

    public AdapterPagamento(List<PagamentoModel> lista, Context context) {
        this.lista = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pagamento_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PagamentoModel pagamentoModel = lista.get(position);
        holder.descri.setText(pagamentoModel.getTitulo());
        holder.valor.setText(
                String.format("R$  %.2f", pagamentoModel.getValor())
        );
        holder.data.setText(pagamentoModel.getData());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView descri, valor, data;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descri = itemView.findViewById(R.id.descri_pgt_text);
            valor = itemView.findViewById(R.id.valor_pgt_text);
            data = itemView.findViewById(R.id.data_pgt_text);
        }
    }
}
