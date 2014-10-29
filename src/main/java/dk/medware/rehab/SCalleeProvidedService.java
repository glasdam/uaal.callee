package dk.medware.rehab;


import org.universAAL.middleware.owl.MergedRestriction;
import org.universAAL.middleware.owl.OntologyManagement;
import org.universAAL.middleware.owl.SimpleOntology;
import org.universAAL.middleware.rdf.Resource;
import org.universAAL.middleware.rdf.ResourceFactory;
import org.universAAL.middleware.rdf.TypeMapper;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.phThing.DeviceService;
import org.universAAL.ontology.rehabontology.ExerciseAnalyser;
import org.universAAL.ontology.rehabontology.ExerciseResults;
import org.universAAL.ontology.rehabontology.RehabontologyOntology;
import org.universAAL.ontology.rehabontology.SuggestionResult;
import org.universAAL.ontology.device.SwitchController;

/* -Example- This service example provides Device Services*/
public class SCalleeProvidedService extends DeviceService {

	/*
	 * -Example- this namespace can be reused in many parts of the code, but not
	 * all of them
	 */
	protected static final String SERVICE_OWN_NAMESPACE = "http://your.ontology.URL.com/YourServerDomainOntology.owl#";
	// TODO: Change Namespace
	public static final String MY_URI = SERVICE_OWN_NAMESPACE
			+ "ExerciseAnalyse";
	protected static final String SERVICE_GET_EXERCISE_SUGGESTION_URI = SERVICE_OWN_NAMESPACE
			+ "getSuggestion";
	protected static final String OUTPUT_SUGGESTION = SERVICE_OWN_NAMESPACE
			+ "outputSuggestion";
	protected static final String INPUT_RESULTS = SERVICE_OWN_NAMESPACE
			+ "inputResults";
	/*	protected static final String SERVICE_SET_STATUS_URI = SERVICE_OWN_NAMESPACE
			+ "setStatus";
	protected static final String OUTPUT_STATUS = SERVICE_OWN_NAMESPACE
			+ "outputStatus";
	protected static final String INPUT_STATUS = SERVICE_OWN_NAMESPACE
			+ "inputStatus";
	protected static final String INPUT_DEVICE = SERVICE_OWN_NAMESPACE
			+ "inputDevice";
	 */
	/* INPUT
	 *  [ [result (double)], [Timestamp (double)] ]
	 * OUTPUT
	 * [ suggestion (integer), slope (float), baseline (integer) ]
	 */
	/* -Example- This registers three profiles */
	public static ServiceProfile[] profiles = new ServiceProfile[1];

	static {
		/*
		 * -Example- This piece of code tells ontology management that these
		 * provided services extend the DeviceService ontology, without having
		 * to code a full Ontology class
		 */
		OntologyManagement.getInstance().register(
				Activator.context,
				new SimpleOntology(MY_URI, ExerciseAnalyser.MY_URI,
						new ResourceFactory() {
					public Resource createInstance(String classURI,
							String instanceURI, int factoryIndex) {
						return new SCalleeProvidedService(instanceURI);
					}
				}));

		// Declaration of first profile. In: SwitchController; Out: StatusValue
		SCalleeProvidedService getExerciseSuggestion = new SCalleeProvidedService(
				SERVICE_GET_EXERCISE_SUGGESTION_URI);
		getExerciseSuggestion.addInputWithAddEffect(INPUT_RESULTS, ExerciseResults.MY_URI, 1, -1, new String []{ ExerciseAnalyser.PROP_EXERCISE_RESULTS });
		getExerciseSuggestion.addOutput(OUTPUT_SUGGESTION, SuggestionResult.MY_URI, 1, 1, new String []{ ExerciseAnalyser.PROP_SUGGESTION_RESULT });
		/*		getExerciseSuggestion.addOutput(OUTPUT_STATUS,
				TypeMapper.getDatatypeURI(Boolean.class), 1, 1, new String[] {
						DeviceService.PROP_CONTROLS,
						SwitchController.PROP_HAS_VALUE });
		 */
		//		profiles[0].getTheService().addInstanceLevelRestriction(r, new String[] { DeviceService.PROP_CONTROLS });
		profiles[0] = getExerciseSuggestion.getProfile();
	}

	protected SCalleeProvidedService(String uri) {
		super(uri);
	}

	public String getClassURI() {
		return MY_URI;
	}

}
