package dk.medware.rehab;


import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.context.ContextEvent;
import org.universAAL.middleware.context.ContextEventPattern;
import org.universAAL.middleware.context.ContextSubscriber;
import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.ontology.rehabontology.ExerciseResults;

public class CSubscriber extends ContextSubscriber {

	protected CSubscriber(ModuleContext context,
			ContextEventPattern[] initialSubscriptions) {
		super(context, initialSubscriptions);
		// TODO Auto-generated constructor stub
	}

	protected CSubscriber(ModuleContext context) {
		super(context, getPermanentSubscriptions());
		// TODO Auto-generated constructor stub
	}

	private static ContextEventPattern[] getPermanentSubscriptions() {
		/*
		 * -Example- This subscribes for Context Events with: \n Subject: Any
		 * SwitchController \n Predicate: has Status Therefore it will receive its
		 * own published events
		 */
		ContextEventPattern[] ceps = new ContextEventPattern[1];

		ceps[0] = new ContextEventPattern();
		ceps[0].addRestriction(MergedRestriction.getAllValuesRestriction(
				ContextEvent.PROP_RDF_SUBJECT, ExerciseResults.MY_URI));
		ceps[0].addRestriction(MergedRestriction.getFixedValueRestriction(
				ContextEvent.PROP_RDF_PREDICATE, ExerciseResults.PROP_TIME));
		return ceps;
	}

	public void communicationChannelBroken() {
		// TODO Auto-generated method stub

	}

	public void handleContextEvent(ContextEvent event) {
		System.out.println("handleContextEvent("+event.getNamespace()+")");
		//Object ob = event.getRDFObject();
		Object subject = event.getRDFSubject();
		if (subject instanceof ExerciseResults) {
			ExerciseResults status = (ExerciseResults) subject;
			System.out.println("\n\nHere comes status: "+status.getTime());
			/*
			 * -Example- Do something with the received events. In this example
			 * nothing is done
			 */
		}
	}

}
