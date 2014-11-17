package io.github.pollinators.honeycomb.data.api;

/**
 * Created by ted on 11/2/14.
 */
public interface Saveable {

    public Object getKey();

    /**
     * Synchronously retrieves the data
     */
    public void get();

    /**
     * Synchronously commits the data
     */
    public void save();

    /**
     * API to Asynchronously control data
     */
    public static interface Async extends Saveable {

        /**
         * @returns null if successful, a DataError otherwise.
         */
        public DataError load();

        /**
         * @returns null if successful, a DataError otherwise.
         */
        public DataError store();
    }

    public class DataError extends Exception {
        public final CharSequence msg = "Ah, eff";
    }
}
