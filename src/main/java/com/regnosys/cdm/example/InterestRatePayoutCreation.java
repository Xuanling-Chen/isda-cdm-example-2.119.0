package com.regnosys.cdm.example;

import cdm.base.datetime.*;
import cdm.base.datetime.metafields.FieldWithMetaBusinessCenterEnum;
import cdm.base.datetime.metafields.ReferenceWithMetaBusinessCenters;
import cdm.base.math.NonNegativeQuantitySchedule;
import cdm.base.math.RateSchedule;
import cdm.base.math.metafields.ReferenceWithMetaQuantity;
import cdm.base.staticdata.asset.rates.FloatingRateIndexEnum;
import cdm.base.staticdata.party.PayerReceiver;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.observable.asset.FixedRateSpecification;
import cdm.observable.asset.FloatingRateOption;
import cdm.observable.asset.metafields.ReferenceWithMetaPrice;
import cdm.product.asset.DayCountFractionEnum;
import cdm.product.asset.FloatingRateSpecification;
import cdm.product.asset.InterestRatePayout;
import cdm.product.asset.RateSpecification;
import cdm.product.asset.metafields.FieldWithMetaDayCountFractionEnum;
import cdm.product.common.schedule.CalculationPeriodDates;
import cdm.product.common.schedule.PayRelativeToEnum;
import cdm.product.common.schedule.PaymentDates;
import cdm.product.common.settlement.ResolvablePayoutQuantity;
import com.rosetta.model.lib.meta.Reference;
import com.rosetta.model.lib.records.DateImpl;

/**
 * Examples of how to create ISDA CDM(TM) Java objects
 */
public class InterestRatePayoutCreation {

    public static final String CURRENCY_EUR = "EUR";

    public static InterestRatePayout getFloatingRatePayout() {
		return InterestRatePayout.builder()
				.setPayoutQuantity(ResolvablePayoutQuantity.builder()
						.setQuantitySchedule(NonNegativeQuantitySchedule.builder()
						.setInitialQuantity(ReferenceWithMetaQuantity.builder()
							.setReference(Reference.builder()
									.setScope("DOCUMENT")
									.setReference("quantity-1")))))
                .setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum.ACT_365_FIXED).build())
				.setCalculationPeriodDates(CalculationPeriodDates.builder()
						.setEffectiveDate(AdjustableOrRelativeDate.builder()
								.setAdjustableDate(AdjustableDate.builder()
										.setUnadjustedDate(DateImpl.of(2018, 1, 3))
										.setDateAdjustments(BusinessDayAdjustments.builder()
												.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
						.setTerminationDate(AdjustableOrRelativeDate.builder()
								.setAdjustableDate(AdjustableDate.builder()
										.setUnadjustedDate(DateImpl.of(2020, 1, 3))
										.setDateAdjustments(BusinessDayAdjustments.builder()
												.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
												.setBusinessCenters(BusinessCenters.builder()
														.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
																.setExternalReference("primaryBusinessCenters")
																.build()))))
						)
						.setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
								.setRollConvention(RollConventionEnum._3)
								.setPeriodMultiplier(6)
								.setPeriod(PeriodExtendedEnum.M))
						.setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
								.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
								.setBusinessCenters(BusinessCenters.builder()
										.setBusinessCentersReference(
												ReferenceWithMetaBusinessCenters.builder().setExternalReference("primaryBusinessCenters").build()))))

				.setPaymentDates(PaymentDates.builder()
						.setPaymentFrequency(Frequency.builder()
								.setPeriodMultiplier(3)
								.setPeriod(PeriodExtendedEnum.M)))

				.setRateSpecification(RateSpecification.builder()
						.setFloatingRate(FloatingRateSpecification.builder()
									.setRateOptionValue(FloatingRateOption.builder()
										.setFloatingRateIndexValue(FloatingRateIndexEnum.EUR_LIBOR_BBA)
											.setIndexTenor(Period.builder()
													.setPeriod(PeriodEnum.M)
													.setPeriodMultiplier(6)))))

				.setPayerReceiver(PayerReceiver.builder()
						.setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("giga-bank").build())
						.setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("mega-bank").build()))

				.build();
	}

	public static InterestRatePayout getFixedRatePayout() {
		return InterestRatePayout.builder()
				.setPayoutQuantity(ResolvablePayoutQuantity.builder()
						.setQuantitySchedule(NonNegativeQuantitySchedule.builder()
								.setInitialQuantity(ReferenceWithMetaQuantity.builder()
										.setReference(Reference.builder()
												.setScope("DOCUMENT")
												.setReference("quantity-2")))))
				.setDayCountFraction(FieldWithMetaDayCountFractionEnum.builder().setValue(DayCountFractionEnum._30E_360).build())
                .setCalculationPeriodDates(CalculationPeriodDates.builder()
						.setEffectiveDate(AdjustableOrRelativeDate.builder()
								.setAdjustableDate(AdjustableDate.builder()
										.setUnadjustedDate(DateImpl.of(2018, 1, 3))
										.setDateAdjustments(BusinessDayAdjustments.builder()
												.setBusinessDayConvention(BusinessDayConventionEnum.NONE))))
						.setTerminationDate(AdjustableOrRelativeDate.builder()
								.setAdjustableDate(AdjustableDate.builder()
										.setUnadjustedDate(DateImpl.of(2020, 1, 3))
										.setDateAdjustments(BusinessDayAdjustments.builder()
												.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
												.setBusinessCenters(BusinessCenters.builder()
														.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
																.setExternalReference("primaryBusinessCenters"))
														.addBusinessCenter(
																FieldWithMetaBusinessCenterEnum.builder().setValue(BusinessCenterEnum.EUTA).build())))))
						.setCalculationPeriodFrequency(CalculationPeriodFrequency.builder()
								.setRollConvention(RollConventionEnum._3)
								.setPeriodMultiplier(3)
								.setPeriod(PeriodExtendedEnum.M))
						.setCalculationPeriodDatesAdjustments(BusinessDayAdjustments.builder()
								.setBusinessDayConvention(BusinessDayConventionEnum.MODFOLLOWING)
								.setBusinessCenters(BusinessCenters.builder()
										.setBusinessCentersReference(ReferenceWithMetaBusinessCenters.builder()
												.setExternalReference("primaryBusinessCenters")))))
				.setPaymentDates(PaymentDates.builder()
						.setPayRelativeTo(PayRelativeToEnum.CALCULATION_PERIOD_END_DATE)
						.setPaymentFrequency(Frequency.builder()
								.setPeriodMultiplier(1)
								.setPeriod(PeriodExtendedEnum.Y)
								.build())
						.build())
				.setRateSpecification(RateSpecification.builder()
                        .setFixedRate(FixedRateSpecification.builder()
                                .setRateSchedule(RateSchedule.builder()
									.setInitialValue(ReferenceWithMetaPrice.builder()
										.setReference(Reference.builder()
												.setScope("DOCUMENT")
												.setReference("price-1"))))))

				.setPayerReceiver(PayerReceiver.builder()
						.setPayerPartyReference(ReferenceWithMetaParty.builder().setExternalReference("mega-bank").build())
						.setReceiverPartyReference(ReferenceWithMetaParty.builder().setExternalReference("giga-bank").build()))

				.build();
	}

	public static void main(String[] args) {
		System.out.println(getFixedRatePayout().toString());
		System.out.println(getFloatingRatePayout().toString());
	}
}
