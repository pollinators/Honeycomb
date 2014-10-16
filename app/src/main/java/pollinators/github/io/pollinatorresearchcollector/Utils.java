package pollinators.github.io.pollinatorresearchcollector;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

import timber.log.Timber;

public class Utils {
	
	public static final int NONE = -1;

    public static List<String> countries = getCountrysList();

    public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line);
	    }
	    return ( (sb.length() == 0) ? " " : sb.toString() );
	}


	public static InputStream getInputStream(String file, Context myContext) {
		AssetManager assetManager = myContext.getResources().getAssets();
		InputStream stream = null;
		try {
			stream = assetManager.open(file);
		} catch (IOException ioEx) {
            Timber.e("Error opening file [%s]", file);

			try {
				stream = assetManager.open("empty.csv");
			} catch (IOException e) {
                Timber.e("Error opening empty.csv");
			}
		}
		return stream;
	}

	public static String[] concatAll(String[] first, String[]... rest) {
		int totalLength = first.length;
		for (String[] array : rest) {
			totalLength += array.length;
		}
		// T[] result = Arrays.copyOf(first, totalLength);
		int offset = first.length;
		String[] result = new String[totalLength];

        //Why not just do: result = first.clone();  ?
		for (int i = 0; i < offset; i++) {
			result[i] = first[i];
		}
		for (String[] array : rest) {
			System.arraycopy(array, 0, result, offset, array.length);
			offset += array.length;
		}
		return result;
	}

    public static String listToCSVString(List<String> list) {
		ListIterator<String> it = list.listIterator();
		while (it.hasNext()) {
			if (it.next() == null) {
				it.remove();
			}
		}

		return list.toString().replace("[", "").replace("]", "");
	}

	/**
	 * Gets an array of countries from "java.util" list of locales
	 * <p>
	 * <a href="http://stackoverflow.com/a/9760438/1967630">Stack Overflow</a>
	 */
	private static List<String> getCountrysList() {
        Locale[] locales = Locale.getAvailableLocales();
        List<String> countries = new ArrayList<String>();

        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length()>0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        countries.add("Blend");

        if (!countries.contains("Rwanda")) {
            countries.add("Rwanda");
        }

        Collections.sort(countries); //Consider removing. Do people expect this?

        return countries;
	}

    /**
     * @return List of countries provided by getCountriesList
     */
    public static List<String> getCountries()
    {
        return countries;
    }


    /**
     * Returns the int value of the screen size (e.g. small, medium, etc.)
     */
    public static int getSize(Context context) {
        int screenLayout = context.getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        return screenLayout;
    }

    /**
     * Helper method to find screen size<br>
     * <a href="http://stackoverflow.com/a/19256468/1967630>Stack Overflow</a>
     */
    public static String getSizeName(Context context) {
        switch (getSize(context)) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return "small";
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return "normal";
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return "large";
            case 4: // Configuration.SCREENLAYOUT_SIZE_XLARGE is API >= 9
                return "xlarge";
            default:
                return "undefined";
        }
    }

    /**
     * A toaster to make all of our toast.
     */
    public static class Toaster {
        final Context context;

        public Toaster(Context context) {
            this.context = context;
        }

        /**
         * @param bread text to show as a pop-up Toast
         */
        public void toast(String bread) {
            if (context != null) {
                Toast.makeText(context, bread, Toast.LENGTH_LONG).show();
            }
            else {
                Timber.w("The toaster has no power (context is null), so the bread can't toast");
            }
        }

        /**
         * @param context for some reason we need this...
         * @param bread text to show as a pop-up Toast
         */
        public static void toast(Context context, String bread) {
            Toast.makeText(context, bread, Toast.LENGTH_LONG).show();
        }
    }
}
