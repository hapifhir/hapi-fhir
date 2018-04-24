package embedded.example.jerseyguice;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spi.Container;
import org.glassfish.jersey.server.spi.ContainerLifecycleListener;
import org.glassfish.jersey.servlet.ServletContainer;
import org.jvnet.hk2.guice.bridge.api.GuiceBridge;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

public abstract class GuiceHk2Helper extends ServletModule {
	private static final Logger log = LoggerFactory.getLogger(GuiceHk2Helper.class);

	@Override
	abstract protected void configureServlets();

	public interface RestKeyBindingBuilder {
		void packages(String... packages);

		void packages(Package... packages);

		void packages(Class<?>... clazz);
	}

	protected RestKeyBindingBuilder rest(final String... urlPatterns) {
		return new RestKeyBindingBuilderImpl(Arrays.asList(urlPatterns));
	}

	private class RestKeyBindingBuilderImpl implements RestKeyBindingBuilder {
		List<String> paths;

		public RestKeyBindingBuilderImpl(final List<String> paths) {
			this.paths = paths;
		}

		private boolean checkIfPackageExistsAndLog(final String packge) {
			boolean exists = false;
			final String resourcePath = packge.replace(".", "/");
			final URL resource = getClass().getClassLoader().getResource(resourcePath);
			if (resource != null) {
				exists = true;
				log.info("rest(" + paths + ").packages(" + packge + ")");
			} else {
				log.info("No Beans in '" + packge + "' found. Requests " + paths + " will fail.");
			}
			return exists;
		}

		@Override
		public void packages(final String... packages) {
			final StringBuilder sb = new StringBuilder();

			for (final String pkg : packages) {
				if (sb.length() > 0) {
					sb.append(',');
				}
				checkIfPackageExistsAndLog(pkg);
				sb.append(pkg);
			}
			final Map<String, String> params = new HashMap<>();
			params.put("javax.ws.rs.Application", GuiceResourceConfig.class.getCanonicalName());
			if (sb.length() > 0) {
				params.put("jersey.config.server.provider.packages", sb.toString());
			}
			bind(ServletContainer.class).in(Scopes.SINGLETON);
			for (final String path : paths) {
				serve(path).with(ServletContainer.class, params);
			}
		}

		@Override
		public void packages(final Package... packages) {
			packages(FluentIterable.from(packages).transform(new Function<Package, String>() {

				@Override
				public String apply(final Package arg0) {
					return arg0.getName();
				}
			}).toArray(String.class));
		}

		@Override
		public void packages(final Class<?>... clazz) {
			packages(FluentIterable.from(clazz).transform(new Function<Class<?>, String>() {

				@Override
				public String apply(final Class<?> arg0) {
					return arg0.getPackage().getName();
				}
			}).toArray(String.class));
		}
	}
}

class GuiceResourceConfig extends ResourceConfig {
	public GuiceResourceConfig() {
		register(new ContainerLifecycleListener() {
			@Override
			public void onStartup(final Container container) {
				final ServletContainer servletContainer = (ServletContainer) container;
				final ServiceLocator serviceLocator = container.getApplicationHandler().getServiceLocator();
				GuiceBridge.getGuiceBridge().initializeGuiceBridge(serviceLocator);
				final GuiceIntoHK2Bridge guiceBridge = serviceLocator.getService(GuiceIntoHK2Bridge.class);
				final Injector injector = (Injector) servletContainer.getServletContext()
						.getAttribute(Injector.class.getName());
				guiceBridge.bridgeGuiceInjector(injector);
			}

			@Override
			public void onReload(final Container container) {
			}

			@Override
			public void onShutdown(final Container container) {
			}
		});
	}
}