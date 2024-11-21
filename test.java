import com.codeborne.selenide.SelenideElement;
import org.testng.annotations.Test;

import static com.codeborne.selenide.Selenide.*;

public class WebTest extends TestBase {

    @Test
    public void performTest() {
        try {
            // 1. Otwórz stronę
            open("/");

            // 2. Zaloguj się (okno systemowe)
            executeJavaScript("window.promptResponse = '" + username + ":" + password + "';");
            sleep(2000); // Czas na zalogowanie

            // 3. Wybierz profil
            $(By.xpath("//xpath-to-profile-dropdown")).selectOption("TENPROFIL");

            // 4. Kliknij "Złóż wniosek"
            $(By.xpath("//xpath-to-submit-button")).click();

            // 5. Wybierz produkt i scenariusz
            $(By.xpath("//xpath-to-product-dropdown")).selectOption("PARD");
            $(By.xpath("//xpath-to-scenario-dropdown")).selectOption("DRAP");

            // 6. Przejdź do zakładki "Kontrahent"
            $(By.xpath("//xpath-to-contractor-tab")).click();
            $(By.xpath("//xpath-to-select-contractor-button")).click();
            $(By.xpath("//xpath-to-specific-contractor")).click();

            // 7. Zakładka "Dokument"
            $(By.xpath("//xpath-to-document-tab")).click();
            String testName = generateTestName();
            $(By.xpath("//xpath-to-name-field")).setValue(testName);

            // Wybór kategorii ze słownika
            String category = "Kategoria1"; // Ustaw kolejną kategorię
            $(By.xpath("//xpath-to-category-dropdown")).selectOption(category);

            // Kliknij "Generuj"
            $(By.xpath("//xpath-to-generate-button")).click();

            // 8. Załącz plik
            $(By.xpath("//xpath-to-attachment-button")).click();
            SelenideElement uploadButton = $(By.xpath("//xpath-to-browse-button"));
            uploadButton.uploadFile(new File(uploadFilePath));
            $(By.xpath("//xpath-to-close-button")).click();

            // 9. Weryfikacja "Rodzaj"
            $(By.xpath("//xpath-to-kind-icon")).click();
            String retrievedNumber = $(By.xpath("//xpath-to-number-dropdown")).getSelectedText();

            // Sprawdzenie wartości w słowniku
            String expectedNumber = dictionary.get(category);
            if (expectedNumber != null && expectedNumber.equals(retrievedNumber)) {
                logToFile(Configuration.baseUrl, category, expectedNumber, "application-number");
            } else {
                throw new AssertionError("Wartości nie zgadzają się ze słownikiem.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
