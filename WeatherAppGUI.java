// import javax.swing.*;
// import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.URI;
// import java.net.URL;
// import java.util.HashMap;
// import java.util.Map;

// public class WeatherAppGUI extends JFrame {
//     private JTextField cityInput;
//     private JButton fetchButton;
//     private JLabel tempLabel, feelsLabel, descLabel, humidityLabel, windLabel, pressureLabel, cityLabel, errorLabel;
//     private JPanel weatherPanel;
//     private final String apiKey = "fbe6431e3b3f525b57d33985e95f1e81";

//     public WeatherAppGUI() {
//         setTitle("Weather App - Enhanced");
//         setLayout(new BorderLayout());
//         setSize(450, 500);
//         setDefaultCloseOperation(EXIT_ON_CLOSE);
//         setLocationRelativeTo(null);

//         // Input Panel
//         JPanel inputPanel = new JPanel(new FlowLayout());
//         inputPanel.setBackground(new Color(70, 130, 180));
//         cityInput = new JTextField("London", 15);
//         cityInput.setFont(new Font("Arial", Font.PLAIN, 14));
//         fetchButton = new JButton("Get Weather");
//         fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
//         fetchButton.setBackground(new Color(30, 144, 255));
//         fetchButton.setForeground(Color.WHITE);
        
//         JLabel inputLabel = new JLabel("Enter City:");
//         inputLabel.setForeground(Color.WHITE);
//         inputLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
//         inputPanel.add(inputLabel);
//         inputPanel.add(cityInput);
//         inputPanel.add(fetchButton);
//         add(inputPanel, BorderLayout.NORTH);

//         // Weather Info Panel
//         weatherPanel = new JPanel();
//         weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
//         weatherPanel.setBackground(new Color(240, 248, 255));
//         weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

//         // Initialize labels with styling
//         cityLabel = createStyledLabel("City: ", Font.BOLD, 18, new Color(25, 25, 112));
//         tempLabel = createStyledLabel("Temperature: ", Font.BOLD, 16, new Color(220, 20, 60));
//         feelsLabel = createStyledLabel("Feels Like: ", Font.PLAIN, 14, new Color(70, 70, 70));
//         descLabel = createStyledLabel("Weather: ", Font.PLAIN, 14, new Color(34, 139, 34));
//         humidityLabel = createStyledLabel("Humidity: ", Font.PLAIN, 14, new Color(70, 70, 70));
//         windLabel = createStyledLabel("Wind Speed: ", Font.PLAIN, 14, new Color(70, 70, 70));
//         pressureLabel = createStyledLabel("Pressure: ", Font.PLAIN, 14, new Color(70, 70, 70));
//         errorLabel = createStyledLabel("", Font.ITALIC, 12, Color.RED);

//         weatherPanel.add(Box.createVerticalStrut(10));
//         weatherPanel.add(cityLabel);
//         weatherPanel.add(Box.createVerticalStrut(15));
//         weatherPanel.add(tempLabel);
//         weatherPanel.add(Box.createVerticalStrut(5));
//         weatherPanel.add(feelsLabel);
//         weatherPanel.add(Box.createVerticalStrut(10));
//         weatherPanel.add(descLabel);
//         weatherPanel.add(Box.createVerticalStrut(10));
//         weatherPanel.add(humidityLabel);
//         weatherPanel.add(Box.createVerticalStrut(5));
//         weatherPanel.add(windLabel);
//         weatherPanel.add(Box.createVerticalStrut(5));
//         weatherPanel.add(pressureLabel);
//         weatherPanel.add(Box.createVerticalStrut(20));
//         weatherPanel.add(errorLabel);

//         add(weatherPanel, BorderLayout.CENTER);

//         fetchButton.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 fetchWeather();
//             }
//         });

//         // Allow Enter key to trigger search
//         cityInput.addActionListener(new ActionListener() {
//             @Override
//             public void actionPerformed(ActionEvent e) {
//                 fetchWeather();
//             }
//         });

//         setVisible(true);
//     }

//     private JLabel createStyledLabel(String text, int style, int size, Color color) {
//         JLabel label = new JLabel(text);
//         label.setFont(new Font("Arial", style, size));
//         label.setForeground(color);
//         label.setAlignmentX(Component.LEFT_ALIGNMENT);
//         return label;
//     }

//     private void fetchWeather() {
//         String city = cityInput.getText().trim();
//         if (city.isEmpty()) {
//             errorLabel.setText("Please enter a city name.");
//             return;
//         }

//         // Show loading state
//         fetchButton.setText("Loading...");
//         fetchButton.setEnabled(false);
//         errorLabel.setText("");

//         // Use SwingWorker for background processing
//         SwingWorker<Map<String, String>, Void> worker = new SwingWorker<Map<String, String>, Void>() {
//             @Override
//             protected Map<String, String> doInBackground() throws Exception {
//                 return getWeatherData(city);
//             }

//             @Override
//             protected void done() {
//                 try {
//                     Map<String, String> weatherData = get();
//                     if (weatherData.containsKey("error")) {
//                         errorLabel.setText(weatherData.get("error"));
//                         clearWeatherInfo();
//                     } else {
//                         displayWeatherData(weatherData);
//                     }
//                 } catch (Exception e) {
//                     errorLabel.setText("Error: Unable to fetch weather data");
//                     clearWeatherInfo();
//                 } finally {
//                     fetchButton.setText("Get Weather");
//                     fetchButton.setEnabled(true);
//                 }
//             }
//         };
//         worker.execute();
//     }

//     private Map<String, String> getWeatherData(String city) {
//         Map<String, String> result = new HashMap<>();
//         try {
//             String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" + 
//                              city.replace(" ", "%20") + "&appid=" + apiKey + "&units=metric";
            
//             URI uri = new URI(urlString);
//             URL url = uri.toURL();
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("GET");
//             conn.setConnectTimeout(5000);
//             conn.setReadTimeout(5000);

//             int responseCode = conn.getResponseCode();
//             if (responseCode != 200) {
//                 result.put("error", "City not found or API error");
//                 return result;
//             }

//             BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//             StringBuilder content = new StringBuilder();
//             String line;
//             while ((line = in.readLine()) != null) {
//                 content.append(line);
//             }
//             in.close();
//             conn.disconnect();

//             // Parse JSON manually
//             String jsonResponse = content.toString();
//             result = parseWeatherJson(jsonResponse);
            
//         } catch (Exception e) {
//             result.put("error", "Network error: " + e.getMessage());
//         }
//         return result;
//     }

//     private Map<String, String> parseWeatherJson(String json) {
//         Map<String, String> weatherData = new HashMap<>();
//         try {
//             // Extract city name
//             String cityName = extractValue(json, "\"name\":");
//             weatherData.put("city", cityName);

//             // Extract temperature data
//             String tempStr = extractValue(json, "\"temp\":");
//             String feelsLikeStr = extractValue(json, "\"feels_like\":");
//             String humidityStr = extractValue(json, "\"humidity\":");
//             String pressureStr = extractValue(json, "\"pressure\":");

//             weatherData.put("temperature", String.format("%.1f°C", Double.parseDouble(tempStr)));
//             weatherData.put("feels_like", String.format("%.1f°C", Double.parseDouble(feelsLikeStr)));
//             weatherData.put("humidity", humidityStr + "%");
//             weatherData.put("pressure", pressureStr + " hPa");

//             // Extract weather description
//             int weatherStart = json.indexOf("\"weather\":[");
//             if (weatherStart != -1) {
//                 String weatherSection = json.substring(weatherStart);
//                 String main = extractValue(weatherSection, "\"main\":");
//                 String description = extractValue(weatherSection, "\"description\":");
//                 weatherData.put("weather", main + " (" + capitalizeFirst(description) + ")");
//             }

//             // Extract wind data
//             String windSpeed = extractValue(json, "\"speed\":");
//             if (windSpeed != null && !windSpeed.isEmpty()) {
//                 weatherData.put("wind", String.format("%.1f m/s", Double.parseDouble(windSpeed)));
//             } else {
//                 weatherData.put("wind", "N/A");
//             }

//         } catch (Exception e) {
//             weatherData.put("error", "Failed to parse weather data");
//         }
//         return weatherData;
//     }

//     private String extractValue(String json, String key) {
//         try {
//             int keyIndex = json.indexOf(key);
//             if (keyIndex == -1) return "";
            
//             int valueStart = keyIndex + key.length();
//             char firstChar = json.charAt(valueStart);
            
//             // Skip whitespace and find actual value start
//             while (firstChar == ' ' || firstChar == '\t') {
//                 valueStart++;
//                 firstChar = json.charAt(valueStart);
//             }
            
//             int valueEnd;
//             if (firstChar == '"') {
//                 // String value
//                 valueStart++; // Skip opening quote
//                 valueEnd = json.indexOf('"', valueStart);
//                 return json.substring(valueStart, valueEnd);
//             } else {
//                 // Numeric value
//                 valueEnd = valueStart;
//                 while (valueEnd < json.length()) {
//                     char c = json.charAt(valueEnd);
//                     if (c == ',' || c == '}' || c == ']' || c == ' ') break;
//                     valueEnd++;
//                 }
//                 return json.substring(valueStart, valueEnd);
//             }
//         } catch (Exception e) {
//             return "";
//         }
//     }

//     private String capitalizeFirst(String str) {
//         if (str == null || str.isEmpty()) return str;
//         return str.substring(0, 1).toUpperCase() + str.substring(1);
//     }

//     private void displayWeatherData(Map<String, String> data) {
//         cityLabel.setText("📍 " + data.get("city"));
//         tempLabel.setText("🌡️ Temperature: " + data.get("temperature"));
//         feelsLabel.setText("🤏 Feels Like: " + data.get("feels_like"));
//         descLabel.setText("☁️ Weather: " + data.get("weather"));
//         humidityLabel.setText("💧 Humidity: " + data.get("humidity"));
//         windLabel.setText("💨 Wind Speed: " + data.get("wind"));
//         pressureLabel.setText("🔽 Pressure: " + data.get("pressure"));
//         errorLabel.setText("");
//     }

//     private void clearWeatherInfo() {
//         cityLabel.setText("City: ");
//         tempLabel.setText("Temperature: ");
//         feelsLabel.setText("Feels Like: ");
//         descLabel.setText("Weather: ");
//         humidityLabel.setText("Humidity: ");
//         windLabel.setText("Wind Speed: ");
//         pressureLabel.setText("Pressure: ");
//     }

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(new Runnable() {
//             @Override
//             public void run() {
//                 // Removed the problematic UIManager code - app will use default look and feel
//                 new WeatherAppGUI();
//             }
//         });
//     }
// }


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class WeatherAppGUI extends JFrame {
    private JTextField cityInput;
    private JButton fetchButton;
    private JLabel tempLabel, feelsLabel, descLabel, humidityLabel, windLabel, pressureLabel, cityLabel, errorLabel, timeLabel;
    private final String apiKey = "fbe6431e3b3f525b57d33985e95f1e81";

    public WeatherAppGUI() {
        setTitle("Weather App - Enhanced");
        setLayout(new BorderLayout());
        setSize(450, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout());
        inputPanel.setBackground(new Color(70, 130, 180));
        cityInput = new JTextField("London", 15);
        cityInput.setFont(new Font("Arial", Font.PLAIN, 14));
        fetchButton = new JButton("Get Weather");
        fetchButton.setFont(new Font("Arial", Font.BOLD, 14));
        fetchButton.setBackground(new Color(30, 144, 255));
        fetchButton.setForeground(Color.WHITE);

        JLabel inputLabel = new JLabel("Enter City:");
        inputLabel.setForeground(Color.WHITE);
        inputLabel.setFont(new Font("Arial", Font.BOLD, 14));

        inputPanel.add(inputLabel);
        inputPanel.add(cityInput);
        inputPanel.add(fetchButton);
        add(inputPanel, BorderLayout.NORTH);

        // Weather Info Panel
        JPanel weatherPanel = new JPanel();
        weatherPanel.setLayout(new BoxLayout(weatherPanel, BoxLayout.Y_AXIS));
        weatherPanel.setBackground(new Color(240, 248, 255));
        weatherPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        cityLabel = createStyledLabel("City: ", Font.BOLD, 18, new Color(25, 25, 112));
        timeLabel = createStyledLabel("Local Time: ", Font.PLAIN, 14, new Color(70, 70, 70));
        tempLabel = createStyledLabel("Temperature: ", Font.BOLD, 16, new Color(220, 20, 60));
        feelsLabel = createStyledLabel("Feels Like: ", Font.PLAIN, 14, new Color(70, 70, 70));
        descLabel = createStyledLabel("Weather: ", Font.PLAIN, 14, new Color(34, 139, 34));
        humidityLabel = createStyledLabel("Humidity: ", Font.PLAIN, 14, new Color(70, 70, 70));
        windLabel = createStyledLabel("Wind Speed: ", Font.PLAIN, 14, new Color(70, 70, 70));
        pressureLabel = createStyledLabel("Pressure: ", Font.PLAIN, 14, new Color(70, 70, 70));
        errorLabel = createStyledLabel("", Font.ITALIC, 12, Color.RED);

        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(cityLabel);
        weatherPanel.add(timeLabel);
        weatherPanel.add(Box.createVerticalStrut(15));
        weatherPanel.add(tempLabel);
        weatherPanel.add(Box.createVerticalStrut(5));
        weatherPanel.add(feelsLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(descLabel);
        weatherPanel.add(Box.createVerticalStrut(10));
        weatherPanel.add(humidityLabel);
        weatherPanel.add(Box.createVerticalStrut(5));
        weatherPanel.add(windLabel);
        weatherPanel.add(Box.createVerticalStrut(5));
        weatherPanel.add(pressureLabel);
        weatherPanel.add(Box.createVerticalStrut(20));
        weatherPanel.add(errorLabel);
        add(weatherPanel, BorderLayout.CENTER);

        fetchButton.addActionListener(e -> fetchWeather());
        cityInput.addActionListener(e -> fetchWeather());

        setVisible(true);
    }

    private JLabel createStyledLabel(String text, int style, int size, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", style, size));
        label.setForeground(color);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void fetchWeather() {
        String city = cityInput.getText().trim();
        if (city.isEmpty()) {
            errorLabel.setText("Please enter a city name.");
            return;
        }
        fetchButton.setText("Loading...");
        fetchButton.setEnabled(false);
        errorLabel.setText("");

        new SwingWorker<Map<String, String>, Void>() {
            @Override
            protected Map<String, String> doInBackground() {
                return getWeatherData(city);
            }

            @Override
            protected void done() {
                try {
                    Map<String, String> data = get();
                    if (data.containsKey("error")) {
                        errorLabel.setText(data.get("error"));
                        clearWeatherInfo();
                    } else {
                        displayWeatherData(data);
                    }
                } catch (Exception e) {
                    errorLabel.setText("Error: Unable to fetch weather data");
                    clearWeatherInfo();
                }
                fetchButton.setText("Get Weather");
                fetchButton.setEnabled(true);
            }
        }.execute();
    }

    private Map<String, String> getWeatherData(String city) {
        Map<String, String> result = new HashMap<>();
        try {
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q=" +
                    city.replace(" ", "%20") + "&appid=" + apiKey + "&units=metric";

            URI uri = new URI(urlString);
            URL url = uri.toURL();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                result.put("error", "City not found or API error");
                return result;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
            in.close();
            conn.disconnect();

            result = parseWeatherJson(content.toString());

        } catch (Exception e) {
            result.put("error", "Network error: " + e.getMessage());
        }
        return result;
    }

    private Map<String, String> parseWeatherJson(String json) {
        Map<String, String> weatherData = new HashMap<>();
        try {
            String cityName = extractValue(json, "\"name\":");
            weatherData.put("city", cityName);

            String tempStr = extractValue(json, "\"temp\":");
            String feelsLikeStr = extractValue(json, "\"feels_like\":");
            String humidityStr = extractValue(json, "\"humidity\":");
            String pressureStr = extractValue(json, "\"pressure\":");

            weatherData.put("temperature", String.format("%.1f°C", Double.parseDouble(tempStr)));
            weatherData.put("feels_like", String.format("%.1f°C", Double.parseDouble(feelsLikeStr)));
            weatherData.put("humidity", humidityStr + "%");
            weatherData.put("pressure", pressureStr + " hPa");

            int weatherStart = json.indexOf("\"weather\":[");
            if (weatherStart != -1) {
                String weatherSection = json.substring(weatherStart);
                String main = extractValue(weatherSection, "\"main\":");
                String description = extractValue(weatherSection, "\"description\":");
                weatherData.put("weather", main + " (" + capitalizeFirst(description) + ")");
            }

            String windSpeed = extractValue(json, "\"speed\":");
            weatherData.put("wind", windSpeed.isEmpty()? "N/A" : String.format("%.1f m/s", Double.parseDouble(windSpeed)));

            // New: Extract timezone offset in seconds and compute local time
            String timezoneOffsetStr = extractValue(json, "\"timezone\":");
            if (!timezoneOffsetStr.isEmpty()) {
                long timezoneOffsetSeconds = Long.parseLong(timezoneOffsetStr);
                String localTime = calculateLocalTime(timezoneOffsetSeconds);
                weatherData.put("local_time", localTime);
            } else {
                weatherData.put("local_time", "N/A");
            }

        } catch (Exception e) {
            weatherData.put("error", "Failed to parse weather data");
        }
        return weatherData;
    }

    private String extractValue(String json, String key) {
        try {
            int keyIndex = json.indexOf(key);
            if (keyIndex == -1) return "";
            int valueStart = keyIndex + key.length();
            char firstChar = json.charAt(valueStart);

            while (firstChar == ' ' || firstChar == '\t') {
                valueStart++;
                firstChar = json.charAt(valueStart);
            }

            int valueEnd;
            if (firstChar == '"') {
                valueStart++;
                valueEnd = json.indexOf('"', valueStart);
                return json.substring(valueStart, valueEnd);
            } else {
                valueEnd = valueStart;
                while (valueEnd < json.length()) {
                    char c = json.charAt(valueEnd);
                    if (c == ',' || c == '}' || c == ']' || c == ' ') break;
                    valueEnd++;
                }
                return json.substring(valueStart, valueEnd);
            }
        } catch (Exception e) {
            return "";
        }
    }

    private String capitalizeFirst(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0,1).toUpperCase() + str.substring(1);
    }

    private String calculateLocalTime(long timezoneOffsetSeconds) {
        long utcMillis = System.currentTimeMillis();
        long localMillis = utcMillis + (timezoneOffsetSeconds * 1000L);
        Date localDate = new Date(localMillis);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(localDate);
    }

    private void displayWeatherData(Map<String, String> data) {
        cityLabel.setText("📍 " + data.get("city"));
        timeLabel.setText("🕒 Local Time: " + data.get("local_time"));
        tempLabel.setText("🌡️ Temperature: " + data.get("temperature"));
        feelsLabel.setText("🤏 Feels Like: " + data.get("feels_like"));
        descLabel.setText("☁️ Weather: " + data.get("weather"));
        humidityLabel.setText("💧 Humidity: " + data.get("humidity"));
        windLabel.setText("💨 Wind Speed: " + data.get("wind"));
        pressureLabel.setText("🔽 Pressure: " + data.get("pressure"));
        errorLabel.setText("");
    }

    private void clearWeatherInfo() {
        cityLabel.setText("City: ");
        timeLabel.setText("Local Time: ");
        tempLabel.setText("Temperature: ");
        feelsLabel.setText("Feels Like: ");
        descLabel.setText("Weather: ");
        humidityLabel.setText("Humidity: ");
        windLabel.setText("Wind Speed: ");
        pressureLabel.setText("Pressure: ");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WeatherAppGUI());
    }
}
