package benicio.soluces.contabilidadeapp.utils;

import java.util.List;

import benicio.soluces.contabilidadeapp.models.TransacaoModel;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface Service {

    @GET("puxar")
    Call<List<TransacaoModel>>puxarTransacoes();

    @POST("enviar")
    Call<String>enviarTransacoes(@Body List<TransacaoModel> lista);
}
