package JoinQuery.Trial.controller;


import JoinQuery.Trial.config.IgniteConfig;
import JoinQuery.Trial.model.IgniteModel;
import JoinQuery.Trial.model.IgnitionModel;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.query.SqlFieldsQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class IgniteController {

    @Autowired
    IgniteConfig igniteConfig;

    @GetMapping("/getAll")
    public List<?> getAll()
    {
        IgniteCache<Integer,IgniteModel> cache= Ignition.ignite().cache("Trial_Schema");
        return cache.query(new SqlFieldsQuery("Select * from IgniteModel")).getAll();
    }
    @PostMapping("/insert")
    public String insert(@RequestBody IgniteModel igniteModel)
    {

        IgniteCache<Integer,IgniteModel> cache= Ignition.ignite().cache("Trial_Schema");
        cache.put(igniteModel.getCompany_code(),igniteModel);
        return "Saved Succefully";
    }


    @GetMapping("/get2All")
    public List<?> getAll_2()
    {
        IgniteCache<Integer,IgnitionModel> cache= Ignition.ignite().cache("Trial_Schema_Two");
        return cache.query(new SqlFieldsQuery("Select * from IgnitionModel")).getAll();
    }
    @PostMapping("/insert2")
    public String insert_2(@RequestBody IgnitionModel ignitionModel)
    {
        IgniteCache<Integer,IgnitionModel> cache= Ignition.ignite().cache("Trial_Schema_Two");
        cache.put(ignitionModel.getManufacturer_id(),ignitionModel);
        return "Saved Succefully";
    }

    @GetMapping("/join")   //Performs A bad join need to use qr.distributed=true to perform proper Join
    public List<?> join()
    {
        IgniteCache<Integer,IgniteModel> cache= Ignition.ignite().cache("Trial_Schema");
       String joinSql="Select IgnitionModel.* from IgniteModel,\""+"SCHEMA2"+"\".IgnitionModel where IgniteModel.comp_name=IgnitionModel.manufacturer_code";
        SqlFieldsQuery qry = new SqlFieldsQuery(joinSql);
        qry.setDistributedJoins(true);
        return cache.query(qry).getAll();
    }

    @GetMapping("/destroy")
    public String destroy(){
        Ignition.ignite().destroyCache("Trial_Schema");
        Ignition.ignite().destroyCache("Trial_Schema_Two");
        Ignition.ignite().destroyCache("Banker");
        return "Destroyed";
    }
}
