package JoinQuery.Trial.controller;


import JoinQuery.Trial.config.IgniteConfig;
import JoinQuery.Trial.model.Banking;
import com.monitorjbl.xlsx.StreamingReader;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@RestController
public class DataStreamer {

    @Autowired
    IgniteConfig igniteConfig;



    //Function to Load a large Dataset form XLS to Ignite

    @GetMapping("/load_bulk")
    public List<?> readXLSFile(String file) throws IOException, InvalidFormatException {
        InputStream inputStream=new FileInputStream("C:\\Users\\prate\\Downloads\\Banking\\banking_trial_1.xlsx");//Absolute Path
        Workbook workbook= StreamingReader.builder().rowCacheSize(5).bufferSize(4096).open(inputStream);
       Sheet sheet=workbook.getSheet("Sheet1");
        Iterator<Row> rowIterator = sheet.iterator();

        Ignite ignite=igniteConfig.igniteInstance();
        Banking bank=new Banking();
        IgniteCache<Integer, Banking> cache = ignite.getOrCreateCache("Banker");
        System.out.println("****");
        System.out.println("Load Start-"+System.currentTimeMillis());
        System.out.println("****");

        try(IgniteDataStreamer<Integer, Banking> stmr = ignite.dataStreamer("Banker")){
            while(rowIterator.hasNext())
            {
                Row row= rowIterator.next();
                bank.setDate(String.valueOf((int)row.getCell(0).getNumericCellValue()));
                bank.setDomain(row.getCell(1).getStringCellValue());
                bank.setLocation(row.getCell(2).getStringCellValue());
                bank.setValue((int) row.getCell(3).getNumericCellValue());
                bank.setTransaction_count((int) row.getCell(4).getNumericCellValue());
                bank.setId((int) row.getCell(5).getNumericCellValue());
                stmr.addData(bank.getId(),bank);
            }
        }
        catch(Exception e)
        {
            System.out.println("Streamer Error"+e);
        }
        System.out.println("****");
        System.out.println("Load End, Select Start-"+System.currentTimeMillis());
        System.out.println("****");
        List<?> data= cache.query(new SqlFieldsQuery("Select * from Banking")).getAll();
        System.out.println("****");
        System.out.println("Select End-"+System.currentTimeMillis());
        System.out.println("****");
        return data;

        //Total Time Taken Loading 1 million rows = 30 secs Getting 1 million rows=5 secs

    }
}
