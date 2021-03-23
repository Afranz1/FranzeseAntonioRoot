package antonio.franzese.iss2021_resumableb.interaction;

import org.json.JSONObject;

public interface IssObserver {
    void handleInfo(String info);

    void handleInfo(JSONObject info);
}
