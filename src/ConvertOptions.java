import com.google.gson.Gson;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import io.github.cdimascio.dotenv.Dotenv;
import static java.lang.String.format;

public class ConvertOptions {
    private String from;
    private String to;
    private double amount;
    private double converted;
    private final String endpoint;

    public ConvertOptions(String optionString) {
        parseOptionsFromOptionString(optionString);
        Dotenv env = Dotenv.load();

        String API_KEY = env.get("EXCHANGE_API_KEY");
        endpoint = format("https://v6.exchangerate-api.com/v6/%s/pair/", API_KEY);
    }

    public String getFrom(){ return from; }
    public String getTo(){ return to; }
    public double getAmount() { return amount; }
    public double getConverted() { return converted; }

    public void parseOptionsFromOptionString(@org.jetbrains.annotations.NotNull String optionString) {
        // USD >>> CLP
        String[] parts = optionString.split(" >>> ");
        parts[0]=parts[0].split(" ")[1];
        from = parts[0].trim();
        to = parts[1].trim();
    }

    public double convert(double amountToConvert) {
        amount = amountToConvert;
        HttpURLConnection request = getHttpURLConnection();

        // Convert to JSON
        Gson gson = new Gson();
        try {
            JsonElement json = gson.fromJson(new InputStreamReader(request.getInputStream()), JsonElement.class);
            // Accessing object
            String req_result = json.getAsJsonObject().get("result").getAsString();

            if (req_result.equals("success")) {
                converted = json.getAsJsonObject().get("conversion_rate").getAsDouble() * amount;
                return converted;
            }

            throw new Error(json.getAsJsonObject().get("error-type").getAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String convertToCurrency(double amountToConvert){
        double converted_currency = convert(amountToConvert);

        return getFormattedAmount(to, converted_currency);
    }

    public String getFormattedAmount(String symbol, double amountToFormat){
        Locale loc = getLocaleFromSymbol(symbol);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(loc);

        return formatter.format(amountToFormat);
    }

    private Locale getLocaleFromSymbol(String symbol){
        if(symbol == null || symbol.isEmpty() || symbol.isBlank()) return Locale.getDefault();

        Locale loc;
        if(symbol.equals("USD")){
            loc = new Locale("en", "US");
        }else{
            loc = new Locale("es", symbol.substring(0,2));
        }

        return loc;
    }

    private HttpURLConnection getHttpURLConnection() {
        String url_str = endpoint + "/" + from + "/" + to + "/" + amount;

        URL url;
        try {
            url = new URL(url_str);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        HttpURLConnection request;
        try {
            request = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            request.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return request;
    }

}
