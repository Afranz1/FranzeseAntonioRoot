/**
 * IssCommSupport.java
 * ==========================================================================
 * <p>
 * ==========================================================================
 */
package antonio.franzese.iss2021_resumableb.supports;

import antonio.franzese.iss2021_resumableb.interaction.IssObserver;
import antonio.franzese.iss2021_resumableb.interaction.IssOperations;

public interface IssCommSupport extends IssOperations {
    void registerObserver(IssObserver obs);

    void removeObserver(IssObserver obs);

    void close();
}
