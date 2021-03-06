/**
 * IssWsSupport.java
 * ===============================================================
 * See also AnswerAvailable
 * ===============================================================
 */
package antonio.franzese.iss2021_resumableb.supports;

import antonio.franzese.iss2021_resumableb.interaction.IssObserver;
import org.json.JSONObject;

import java.util.Vector;

/**
 IssObservableCommSupport.java
 ===============================================================

 ===============================================================
 */

public abstract class IssObservableCommSupport implements IssCommSupport {
    protected Vector<IssObserver> observers = new Vector<IssObserver>();

    protected void updateObservers(JSONObject jsonOnj) {
        //System.out.println("IssObservableCommSupport | updateObservers " + observers.size() );
        observers.forEach(v -> {
            //System.out.println("IssObservableCommSupport | updates " + v );
            v.handleInfo(jsonOnj);  //The observer should be a process ...
        });
    }

    //------------------------------ IssCommSupport ----------------------------------
    @Override
    public void registerObserver(IssObserver obs) {
        observers.add(obs);
    }

    @Override
    public void removeObserver(IssObserver obs) {
        observers.remove(obs);
    }

    @Override
    public abstract void close();
}
