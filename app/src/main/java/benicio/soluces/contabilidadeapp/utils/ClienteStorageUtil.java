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
    private static final String PREF_NAME = "cliente_prefs";
    private static final String KEY_TRANSACOES = "clientes";

    public static void saveUsuario(Context context, List<ClienteModel> clientes) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(clientes);
        editor.putString(KEY_TRANSACOES, json);
        editor.apply();
    }

    public static List<ClienteModel> loadClientes(Context context) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = sharedPreferences.getString(KEY_TRANSACOES, "");
            Type type = new TypeToken<List<ClienteModel>>() {
            }.getType();
            return gson.fromJson(json, type);
        }catch (Exception e){
            return new ArrayList<>();
        }
    }
}
