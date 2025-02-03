
///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS de.siegmar:fastcsv:3.4.0
//DEPS com.alibaba.fastjson2:fastjson2:2.0.54

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSON;

import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.reader.CsvRecord;


/**
 * Extracting Domains from CSV file and write a JSON file.
 *
 * dependency: - FastCsv by Siegmar - FastJSON by Alibaba
 *
 * source:
 * <a href="https://knowledge.hubspot.com/forms/what-domains-are-blocked-when-using-the-forms-email-domains-to-block-feature">
 * HubSpot </a>
 */
public class ExtractCsv {

    public static void main(String... args) throws IOException {
        Path file = Paths.get(System.getProperty("user.dir"), "free-domain.csv");

        try (CsvReader<CsvRecord> csv = CsvReader.builder().ofCsvRecord(file)) {

            var list = csv.stream().map(rec -> rec.getFields().get(0)).toList();

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("email_domains", list);

            String jsonString = JSON.toJSONString(jsonMap);

            Path jsonFile = Paths.get("domains.json");
            try {
                if (Files.exists(jsonFile)) {
                    // create file and write if not exist.
                    Files.write(
                            jsonFile,
                            jsonString.getBytes(StandardCharsets.UTF_8),
                            StandardOpenOption.CREATE,
                            StandardOpenOption.WRITE);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
