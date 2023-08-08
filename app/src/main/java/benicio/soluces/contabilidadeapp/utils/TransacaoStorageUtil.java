package benicio.soluces.contabilidadeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.models.TransacaoModel;

public class TransacaoStorageUtil {

    private static final String PREF_NAME = "transacao_prefs";
    private static final String KEY_TRANSACOES = "transacoes";

    // Método para salvar a lista de transações
    public static void saveTransacoes(Context context, List<TransacaoModel> transacoes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(transacoes);
        editor.putString(KEY_TRANSACOES, json);
        editor.apply();
    }

    // Método para carregar a lista de transações
    public static List<TransacaoModel> loadTransacoes(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES, "");
            Type type = new TypeToken<List<TransacaoModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
