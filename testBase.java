import com.codeborne.selenide.Configuration;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.codeborne.selenide.Selenide.*;

public class TestBase {
    // Słownik
    protected static Map<String, String> dictionary = new HashMap<>();
    protected static String filePath = "C:\\path\\to\\your\\file.txt"; // Ścieżka do pliku z logami
    protected static String uploadFilePath = "C:\\path\\to\\upload\\file.pdf"; // Ścieżka do pliku do uploadu
    protected static String dictionaryFilePath = "C:\\path\\to\\dictionary.txt"; // Ścieżka do pliku ze słownikiem

    // Dane logowania
    protected static String username = "your-username";
    protected static String password = "your-password";

    @BeforeClass
    public void loadDictionary() {
        try (BufferedReader reader = new BufferedReader(new FileReader(dictionaryFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    dictionary.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @BeforeTest
    public void setUp() {
        // Ścieżka do WebDrivera (ChromeDriver, GeckoDriver, itp.)
        System.setProperty("webdriver.chrome.driver", "C:\\path\\to\\chromedriver.exe");

        // Konfiguracja Selenide
        Configuration.browser = "chrome";
        Configuration.startMaximized = true; // Zmaksymalizowana przeglądarka
        Configuration.browserCapabilities.setCapability("goog:chromeOptions", Map.of(
                "args", new String[]{
                        "--incognito", // Tryb incognito
                        "--disable-gpu",
                        "--disable-extensions"
                        // "--headless" // Tryb headless (zakomentowany)
                }
        ));
    }

    protected void logToFile(String url, String category, String number, String applicationNumber) {
        try (FileWriter writer = new FileWriter(new File(filePath), true)) {
            String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            writer.write(String.format("%s,%s,%s,%s,%s%n", url, timestamp, applicationNumber, category, number));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected String generateTestName() {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "Mariusz" + timestamp;
    }
}
