package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import Utils.JsonRequest;
import Utils.JsonUtil;
import Utils.Security;
import br.com.dticampossales.appsischamados.R;
import okhttp3.FormBody;
import okhttp3.RequestBody;

public class AtendimentoController extends BaseController {
    private final ArrayList<JSONObject> historico;
    private final Integer chamadoId;

    public AtendimentoController(Context context, Integer chamadoId) {
        super(context, setDataSet(context, chamadoId));

        this.historico = buildObjectList(getContext().getString(R.string.api_historico_key));
        this.chamadoId = chamadoId;
    }

    public Integer getChamadoId() {
        return this.chamadoId;
    }

    public ArrayList<JSONObject> getHistorico() {
        return this.historico;
    }

    public void sendReport(String statusId, String message) {
        String hashLogin = Security.getHashLogin(getContext());

        if (!hashLogin.equals("")) {
            try {
                String messageStatus = "(" + JsonUtil.getJsonVal(getStatus(), statusId) + ") ";
                String parsedMessage = messageStatus + message;

                RequestBody requestBody = new FormBody.Builder()
                        .add(getContext().getString(R.string.api_atendimento_up_status_key), statusId)
                        .add(getContext().getString(R.string.api_atendimento_up_msg_key), parsedMessage)
                        .build();

                String chamadosUrl = String.format(getContext().getString(R.string.api_atendimento_up), hashLogin, chamadoId);
                JSONObject response = JsonRequest.postRequest(chamadosUrl, requestBody);

                String messageInfo = JsonUtil.getJsonVal(
                        response.getJSONObject(getContext().getString(R.string.api_up_message_key)), "info");

                Toast.makeText(getContext(), messageInfo , Toast.LENGTH_SHORT).show();

            } catch (ExecutionException | InterruptedException |  JSONException e) {
                Toast.makeText(getContext(), R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    public JSONObject getStatusObjectByProfile() {
        final int PERFIL_TECNICO = 3;
        JSONObject properStatus = getStatus();
        int profile = Security.getSessionUserProfile(getContext());
        if (profile == PERFIL_TECNICO) {
            properStatus.remove("1");
            properStatus.remove("5");
        }
        return properStatus;
    }

    public JSONObject getDetalhes() {
        return buildPropObject(PropType.DETALHES);
    }

    private static JSONObject setDataSet(Context context, Integer chamadoId) {
        JSONObject fullDataSet = new JSONObject();

        String hashLogin = Security.getHashLogin(context);

        if (!hashLogin.equals("")) {
            try {
                String chamadosUrl = String.format(context.getString(R.string.api_atendimento), hashLogin, chamadoId);
                fullDataSet = JsonRequest.request(chamadosUrl);

            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(context, R.string.app_fail, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

        return fullDataSet;
    }
}
