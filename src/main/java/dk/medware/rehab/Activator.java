package dk.medware.rehab;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

public class Activator implements BundleActivator {
	public static BundleContext osgiContext = null;
	public static ModuleContext context = null;

	public static SCallee scallee = null;
	public static CSubscriber csubscriber = null;

	public void start(BundleContext bcontext) throws Exception {
		Activator.osgiContext = bcontext;
		Activator.context = uAALBundleContainer.THE_CONTAINER
				.registerModule(new Object[] { bcontext });
		System.out.println("Callee registering");
		scallee = new SCallee(context);
		csubscriber = new CSubscriber(context);
		System.out.println("Callee ready");
	}

	public void stop(BundleContext arg0) throws Exception {
		scallee.close();
		csubscriber.close();
	}

}
