/*
 * Copyright (c) 2016 Aberger Software GmbH. All Rights Reserved.
 *               http://www.aberger.at
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

package embedded.example.jerseyguice;

import java.awt.Desktop;
import java.net.URI;
import java.util.EnumSet;
import java.util.List;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.slf4j.bridge.SLF4JBridgeHandler;

import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;

import embedded.example.JaxRsPatientProvider;

public class GuiceJersey2ServletContextListener extends GuiceServletContextListener {

	@Override
	protected Injector getInjector() {

		final List<Module> modules = Lists.newArrayList();

	
		modules.add(new GuiceHk2Helper() {
			@Override
			protected void configureServlets() {
//				bind(JaxRsPatientProvider.class).in(Scopes.SINGLETON);
				rest("/*").packages(JaxRsPatientProvider.class);
			}
		});
		return Guice.createInjector(Stage.PRODUCTION, modules);
	}

	public static void main(final String[] args) throws Exception {

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		final Server server = new Server(8080);

		final ServletContextHandler sch = new ServletContextHandler(server, "/");
		sch.addEventListener(new GuiceJersey2ServletContextListener());
		sch.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
		server.start();

		Desktop.getDesktop().browse(new URI("http://localhost:8080/Patient"));
	}
}