package com.regnosys.cdm.example;

import cdm.event.common.functions.Settle;
import com.regnosys.cdm.example.functions.impls.SettleImpl;
import com.rosetta.model.lib.validation.ModelObjectValidator;

public class CdmDemoTestsModule extends DemoCdmRuntimeModule {

	@Override
	protected void configure() {
		super.configure();
	}

	@Override
	protected Class<? extends ModelObjectValidator> bindModelObjectValidator() {
		return NoOpValidator.class;
	}

	// Functions

}
