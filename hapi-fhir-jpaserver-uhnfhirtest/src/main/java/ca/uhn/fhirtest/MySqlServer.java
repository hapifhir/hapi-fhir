package ca.uhn.fhirtest;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * Created by mochaholic on 18/02/2015.
 */
public class MySqlServer implements InitializingBean, DisposableBean {
    private static final org.slf4j.Logger ourLog = org.slf4j.LoggerFactory.getLogger(MySqlServer.class);

    @Override
    public void destroy() throws Exception {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

}