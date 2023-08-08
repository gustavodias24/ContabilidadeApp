package benicio.soluces.contabilidadeapp.adapters;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.models.TransacaoModel;

public class AdapterTransacao extends RecyclerView.Adapter<AdapterTransacao.MyViewHolder> {

    List<TransacaoModel> lista;

    public AdapterTransacao(List<TransacaoModel> lista) {
        this.lista = lista;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.transicao_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        TransacaoModel transacaoModel = lista.get(position);

        if ( transacaoModel.getTipo() == 2){
            holder.valorComJuros.setVisibility(View.VISIBLE);
            holder.valorComJuros.setText(
                    String.format("valor com juros.: R$ %.2f", transacaoModel.getValorJuros())
            );
        }
        holder.descri.setText(
                "Descri.: " + transacaoModel.getDescricao()
        );
        holder.data.setText(
                "Data.: " + transacaoModel.getData()
        );

        holder.valor.setText(
                String.format("valor.: R$ %.2f", transacaoModel.getValor())
        );

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        TextView descri, data, valor, valorComJuros;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            descri = itemView.findViewById(R.id.descriTransicaoText);
            data = itemView.findViewById(R.id.dataTransicaoText);
            valor = itemView.findViewById(R.id.valorTransicaoText);
            valorComJuros = itemView.findViewById(R.id.valorJurosTransicaoText);
        }
    }
}
