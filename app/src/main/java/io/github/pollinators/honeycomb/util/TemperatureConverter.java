package io.github.pollinators.honeycomb.util;

/**
 * Created by ted on 12/9/14.
 */
public class TemperatureConverter {

    public static final int UNIT_FAHRENHEIT = 0;
    public static final int UNIT_CELCIUS = 1;
    public static final int UNIT_KELVIN = 2;

    /**
     * Convert an value from one unit to the other
     */
    public static double convert(int fromUnit, int toUnit, double value) {
        switch (fromUnit) {

            case UNIT_FAHRENHEIT:
                switch (toUnit) {
                    case UNIT_CELCIUS:
                        return fahrenheitToCelsius(value);
                    case UNIT_KELVIN:
                        return fahrenheitToKelvin(value);
                }
                break;

            case UNIT_CELCIUS:
                switch (toUnit) {
                    case UNIT_FAHRENHEIT:
                        return celsiusToFahrenheit(value);
                    case UNIT_KELVIN:
                        return celsiusToKelvin(value);
                }
                break;

            case UNIT_KELVIN:
                switch (toUnit) {
                    case UNIT_FAHRENHEIT:
                        return kelvinToFahrenheit(value);
                    case UNIT_CELCIUS:
                        return kelvinToCelsius(value);
                }
                break;
        }

        return value;
    }

    public static double fahrenheitToCelsius(double fahrenheit) {
        return (fahrenheit - 32) * 5 / 9.0;
    }

    public static double celsiusToFahrenheit(double celcius) {
        return 9 / 5.0 * celcius + 32;
    }

    public static double celsiusToKelvin(double celcius) {
        return celcius + 273.15;
    }

    public static double kelvinToCelsius(double kelvin) {
        return kelvin - 273.15;
    }

    public static double fahrenheitToKelvin(double fahrenheit) {
        return celsiusToKelvin(fahrenheitToCelsius(fahrenheit));
    }

    public static double kelvinToFahrenheit(double kelvin) {
        return celsiusToFahrenheit(kelvinToCelsius(kelvin));
    }
}
