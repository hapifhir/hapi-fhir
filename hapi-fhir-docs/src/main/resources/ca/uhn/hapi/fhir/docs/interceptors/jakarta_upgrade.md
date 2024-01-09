# 7.0.0 Interceptor Upgrade Guide

As of HAPI-FHIR 7.0.0, dependency on the `javax.*` packages has now changed to instead use the `jakarta.*` packages. This is a breaking change for any users who have written their own interceptors, as the package names of the interfaces have changed.

In order to upgrade your interceptors, you will need to change, at a minimum, the imports in your affected interceptor implementations. For example, if you have an interceptor that uses imports such as `javax.servlet.http.HttpServletRequest`, you will need to change these to `jakarta.servlet.http.HttpServletRequest`. The following is an example of a migration of an interceptor.

## Example

### Old Server Interceptor

```java
package com.example;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.function.Supplier;

public class SampleInteceptor{

    private static final Logger ourLog = LoggerFactory.getLogger(SampleInteceptor.class);
    
	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED)
	public boolean serverIncomingRequestPreProcessed(HttpServletRequest theHttpServletRequest, HttpServletResponse theHttpServletResponse) {
		ourLog.info("I'm an interceptor!");
		return true;
	}
}
```

## New Server Interceptor

```java
package com.example;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Iterator;
import java.util.function.Supplier;

public class SampleInteceptor{

    private static final Logger ourLog = LoggerFactory.getLogger(SampleInteceptor.class);
    
	@Hook(Pointcut.SERVER_INCOMING_REQUEST_PRE_PROCESSED)
	public boolean serverIncomingRequestPreProcessed(HttpServletRequest theHttpServletRequest, HttpServletResponse theHttpServletResponse) {
		ourLog.info("I'm an interceptor!");
		return true;
	}
}
```

You'll note that there is only one very subtle difference between these two versions, and that is the change from: 

```java
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
```

to:

```java
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
```

