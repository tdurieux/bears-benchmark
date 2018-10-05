package csse.policies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Damsith Karunaratna(dammakaru@gmail.com) on 10/5/2018.
 */
@Component
public class PolicyFilterActivator implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(PolicyFilterActivator.class);
    private Environment environment;

    /**
     * This overridden method in EnvironmentAware allows accessing environment resources
     * @param environment allows accessing environment resources
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * This overridden method allows us to modify the BeanDefinitionRegistry after Spring has auto configured
     * the bean classes. It has been used here to remove unwanted Interceptor filters from the filter chain
     * and keep only those specified in the properties file
     * @param beanDefinitionRegistry provides a reference to the spring bean definition registry
     * @throws BeansException Abstract superclass for all exceptions thrown in the beans package and subpackages.
     */
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {

        Set activePolicies = getActivePolicies();
        String filterPrefix = environment.getProperty("csse.filterprefix");

        for (String beanName : beanDefinitionRegistry.getBeanDefinitionNames()) {
            if(beanName.startsWith(filterPrefix)) {
                logger.info("csse policy bean : " + beanName);
                if(!activePolicies.contains(beanName)) {
                    logger.info("this bean will be unregistered : " + beanName);
                    beanDefinitionRegistry.removeBeanDefinition(beanName);
                }
            }
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
    }

    /**
     * This method creates a set of filter names that need to be applied to the filter chain
     * The filter names are stored in the property "csse.activeFilters" as a comma separated list
     * @return a set of filter names to make comparisons more efficient.
     */
    private Set getActivePolicies() {

        String str = environment.getProperty("csse.activeFilters").replace("\n", "").replace("\r", "");
        Set set = new HashSet<>(Arrays.asList(str.split("\\s*,\\s*")));
        logger.info(set.toString());
        return set;

    }
}
