package com.mogtechnologies.mongodbdriver;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        DatabaseTests.class,
        RestApiTests.class,
        IntegrationTests.class
})
public class AllTest {}