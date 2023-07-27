package JoinQuery.Trial.config;


import JoinQuery.Trial.model.*;
import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.affinity.Affinity;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"JoinQuery.Trial"})
public class IgniteConfig {


    @Bean(name="igniteInstance")
    public Ignite igniteInstance(){

        IgniteConfiguration cfg=new IgniteConfiguration();
        cfg.setClientMode(true);


        CacheConfiguration<Long, Organization> ccfg1=new CacheConfiguration<>("Org_Cache");
        ccfg1.setCacheMode(CacheMode.PARTITIONED);
        ccfg1.setIndexedTypes(Long.class, Organization.class);

        CacheConfiguration<Long, Person> ccfg2=new CacheConfiguration<>("Col_Per_Cache");
        ccfg2.setCacheMode(CacheMode.PARTITIONED);
        ccfg2.setIndexedTypes(Affinity.class, Person.class);

        CacheConfiguration<Long,Person> ccfg3=new CacheConfiguration<>("Per_Cache");
        ccfg3.setCacheMode(CacheMode.PARTITIONED);
        ccfg3.setIndexedTypes(Long.class, Person.class);



        CacheConfiguration<Integer,IgniteModel> ccfg4=new CacheConfiguration("Trial_Schema");
        ccfg4.setCacheMode(CacheMode.PARTITIONED);
        ccfg4.setIndexedTypes(Integer.class, IgniteModel.class);
        ccfg4.setSqlSchema("Schema1");

        CacheConfiguration<Integer,IgnitionModel> ccfg5=new CacheConfiguration("Trial_Schema_Two");
        ccfg5.setCacheMode(CacheMode.PARTITIONED);
        ccfg5.setIndexedTypes(Integer.class, IgnitionModel.class);
        ccfg5.setSqlSchema("Schema2");

        CacheConfiguration<Integer, Banking> ccfg6=new CacheConfiguration("Banker");
        ccfg6.setCacheMode(CacheMode.PARTITIONED);
        ccfg6.setIndexedTypes(Integer.class, Banking.class);
        ccfg6.setSqlSchema("Schema3");

        Ignite ignite=Ignition.start(cfg);
        ignite.getOrCreateCache(ccfg1);
        ignite.getOrCreateCache(ccfg2);
        ignite.getOrCreateCache(ccfg3);
        ignite.getOrCreateCache(ccfg4);
        ignite.getOrCreateCache(ccfg5);
        ignite.getOrCreateCache(ccfg6);

        return ignite;
    }

}
