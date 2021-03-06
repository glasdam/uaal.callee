package dk.medware.rehab;


import java.util.Arrays;
import java.util.List;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.service.CallStatus;
import org.universAAL.middleware.service.ServiceCall;
import org.universAAL.middleware.service.ServiceCallee;
import org.universAAL.middleware.service.ServiceResponse;
import org.universAAL.middleware.service.owls.process.ProcessOutput;
import org.universAAL.middleware.service.owls.profile.ServiceProfile;
import org.universAAL.ontology.rehabontology.ExerciseResults;
import org.universAAL.ontology.rehabontology.SuggestionResult;

public class SCallee extends ServiceCallee {
	//private SwitchController theDevice;
	/* -Example- An error response for common use */
	private static final ServiceResponse failure = new ServiceResponse(
			CallStatus.serviceSpecificFailure);

	protected SCallee(ModuleContext context, ServiceProfile[] realizedServices) {
		super(context, realizedServices);
		/* -Example- Instantiate a virtual SwitchController */
//		theDevice = new SwitchController(CPublisher.DEVICE_OWN_URI);
//		theDevice.setValue(StatusValue.NoCondition);
	}

	protected SCallee(ModuleContext context) {
		/* -Example- Instantiate a virtual SwitchController */
		super(context, SCalleeProvidedService.profiles);
//		theDevice = new SwitchController(CPublisher.DEVICE_OWN_URI);
//		theDevice.setValue(StatusValue.NoCondition);
	}

	public void communicationChannelBroken() {
		/* -Example- Remove the virtual SwitchController */
		//theDevice = null;
	}

	public ServiceResponse handleCall(ServiceCall call) {
		System.out.println("Hurra got called "+call);
		if (call == null) {
			failure.addOutput(new ProcessOutput(
					ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR, "Null call!?!"));
			return failure;
		}

		String operation = call.getProcessURI();
		if (operation == null) {
			failure.addOutput(new ProcessOutput(
					ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
					"Null operation!?!"));
			return failure;
		}

		/*
		 * -Example- This returns the status of the requested OnOffActuator. it
		 * doesn�t need to check the input because the Service restriction of
		 * this server specifies that it only handles its single virtual
		 * SwitchController. If the request got here, it means it addressed this
		 * virtual SwitchController
		 */
		if (operation.startsWith(SCalleeProvidedService.SERVICE_GET_EXERCISE_SUGGESTION_URI)) {
			//ArrayList<ExerciseResults> results = (ArrayList<ExerciseResults>)call.getInputValue(SCalleeProvidedService.INPUT_RESULTS);
			ExerciseResults results = (ExerciseResults)call.getInputValue(SCalleeProvidedService.INPUT_RESULTS);
			System.out.println("Input: "+results);
			
			ServiceResponse response = new ServiceResponse(CallStatus.succeeded);
			List<Double> suggestion = ProgressionCalculator.calculate_simple(Arrays.asList(results.getTime()), Arrays.asList(results.getResults()));
			SuggestionResult suggestion_result = new SuggestionResult();
			suggestion_result.setEstimate(suggestion.get(2));
			suggestion_result.setSlope(suggestion.get(0));
			suggestion_result.setRecommendation(suggestion.get(3));
			System.out.println("Returning: "+suggestion_result);
			response.addOutput(SCalleeProvidedService.OUTPUT_SUGGESTION, suggestion_result);
//			response.addOutput(ExerciseAnalyser.PROP_SUGGESTION_RESULT, suggestion_result);
//			response.addOutput(new ProcessOutput(ExerciseAnalyser.PROP_SUGGESTION_RESULT, suggestion_result));
//			response.addOutput(new ProcessOutput(SCalleeProvidedService.OUTPUT_SUGGESTION, suggestion_result));
/*				response.addOutput(new ProcessOutput(
						SCalleeProvidedService.OUTPUT_STATUS, theDevice
								.getValue()));
								*/
			return response;
		}

		/*
		 * -Example- This changes the status of the requested OnOffActuator,
		 * according to the additional input. it doesn�t need to check the input
		 * because the Service restriction of this server specifies that it only
		 * handles its single virtual SwitchController. If the request got here,
		 * it means it addressed this virtual SwitchController
		 */
/*		if (operation.startsWith(SCalleeProvidedService.SERVICE_SET_STATUS_URI)) {
			StatusValue status = (StatusValue) call
					.getInputValue(SCalleeProvidedService.INPUT_STATUS);
			if (theDevice == null) {
				failure.addOutput(new ProcessOutput(
						ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
						"Device not ready!"));
				return failure;
			} else if (status != null) {
				theDevice.setValue(status);
				Activator.cpublisher.publishStatusEvent(status);
				return new ServiceResponse(CallStatus.succeeded);
			} else {
				failure.addOutput(new ProcessOutput(
						ServiceResponse.PROP_SERVICE_SPECIFIC_ERROR,
						"Device not ready or wrong input!"));
				return failure;
			}
		}
		*/
		return failure;
	}
}
