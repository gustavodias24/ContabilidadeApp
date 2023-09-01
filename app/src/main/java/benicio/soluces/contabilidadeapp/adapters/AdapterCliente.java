package benicio.soluces.contabilidadeapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import benicio.soluces.contabilidadeapp.R;
import benicio.soluces.contabilidadeapp.models.ClienteModel;
import benicio.soluces.contabilidadeapp.models.PagamentoModel;
import benicio.soluces.contabilidadeapp.models.TransacaoModel;
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

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ClienteModel clienteModel = lista.get(position);

        double jaPagou = 0;

        if ( clienteModel.getListaPagamentos() != null){
            for (PagamentoModel pagamento : clienteModel.getListaPagamentos()){
                jaPagou += pagamento.getValor();
            }
        }

        Double faltaPagar = clienteModel.getDinheeiroParaSerPago() - jaPagou;
        holder.infoExtra.setText(
                String.format(
                        "Pegou: R$%.2f"+ "\n"+
                        "Valor com juros: R$%.2f"+ "\n" +
                        "Já pagou: R$%.2f"+"\n"+
                        "Falta: R$%.2f"+"\n"+
                        "valor Parcela: R$%.2f",
                        clienteModel.getDinheiroEmpretado(),
                        clienteModel.getDinheeiroParaSerPago(),
                        jaPagou,
                        faltaPagar,
                        clienteModel.getValorParcela()
                        )

        );
        if ( clienteModel.getVerPagamento() != null && clienteModel.getVerPagamento()){
            holder.recycleParcelas.setVisibility(View.VISIBLE);
        }else{
            holder.recycleParcelas.setVisibility(View.GONE);
        }

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

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

            String formattedDate = dateFormat.format(calendar.getTime());
            for (PagamentoModel pagamento : clienteModel.getListaPagamentos()){
                if ( pagamento.getData().equals(formattedDate) ){
                    holder.pagoCheck.setChecked(true);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CheckBox pagoCheck;
        TextView nomeCliente, parcelas, pagas,falta, infoExtra;
        RecyclerView recycleParcelas;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeCliente = itemView.findViewById(R.id.nome_cliente_text);
            parcelas = itemView.findViewById(R.id.qtd_parcelas_text);
            pagas = itemView.findViewById(R.id.parcelas_pagas_text);
            falta = itemView.findViewById(R.id.parcelas_falta_text);
            recycleParcelas = itemView.findViewById(R.id.recyclerParcelas);
            pagoCheck = itemView.findViewById(R.id.pago_check);
            infoExtra = itemView.findViewById(R.id.info_extra_text);
        }
    }
}
