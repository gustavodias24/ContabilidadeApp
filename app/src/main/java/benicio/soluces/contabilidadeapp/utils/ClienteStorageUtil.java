package benicio.soluces.contabilidadeapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import benicio.soluces.contabilidadeapp.models.ClienteModel;

public class ClienteStorageUtil {
    private static final String PREF_NAO_QUITADO = "cliente_preferences_nao_quitado";
    private static final String KEY_TRANSACOES_NAO_QUITADO = "clientes_nao_quitado";

    private static final String PREF_QUITADO = "cliente_preferences_quitado";
    private static final String KEY_TRANSACOES_QUITADO = "clientes_quitado";

    public static void saveClientesNaoQuitado(Context context, List<ClienteModel> clientes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAO_QUITADO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(clientes);
        editor.putString(KEY_TRANSACOES_NAO_QUITADO, json);
        editor.apply();
    }

    public static List<ClienteModel> loadClientesNaoQuitado(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAO_QUITADO, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES_NAO_QUITADO, "");
            Type type = new TypeToken<List<ClienteModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public static void saveClientesQuitado(Context context, List<ClienteModel> clientes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_QUITADO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(clientes);
        editor.putString(KEY_TRANSACOES_QUITADO, json);
        editor.apply();
    }
    public static List<ClienteModel> loadClientesQuitado(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_QUITADO, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES_QUITADO, "");
            Type type = new TypeToken<List<ClienteModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
