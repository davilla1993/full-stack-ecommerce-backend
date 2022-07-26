package com.gbossoufolly.springbootecommerce.config;

import com.gbossoufolly.springbootecommerce.entities.Country;
import com.gbossoufolly.springbootecommerce.entities.Product;
import com.gbossoufolly.springbootecommerce.entities.ProductCategory;
import com.gbossoufolly.springbootecommerce.entities.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;

    @Autowired
    public MyDataRestConfig(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        // disable HTTP methods for Product: PUT, POST and DELETE
        disableHttpMethodes(Product.class, config, theUnsupportedActions);

        // disable HTTP methods for ProductCategory: PUT, POST and DELETE
        disableHttpMethodes(ProductCategory.class, config, theUnsupportedActions);

        // disable HTTP methods for Country: PUT, POST and DELETE
        disableHttpMethodes(Country.class, config, theUnsupportedActions);

        // disable HTTP methods for State: PUT, POST and DELETE
        disableHttpMethodes(State.class, config, theUnsupportedActions);


        // call an internal helper method
        exposeIds(config);
    }

    private void disableHttpMethodes(Class theClass, RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                        .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {

        // Expose entity ids

        // 1- get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // 2- create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();

        // 3- get the entity types for the entities
        for (EntityType tempEntityType : entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // 4- expose the entity ids for the array of entity/domain types
        Class[] domainTypes = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainTypes);
    }
}
