package com.regnosys.cdm.example.functions.impls;

import cdm.base.staticdata.identifier.AssignedIdentifier;
import cdm.base.staticdata.identifier.Identifier;
import cdm.base.staticdata.party.Party;
import cdm.base.staticdata.party.PartyRole;
import cdm.base.staticdata.party.PartyRoleEnum;
import cdm.base.staticdata.party.metafields.ReferenceWithMetaParty;
import cdm.event.common.Trade;
import cdm.event.common.TradeState;
import cdm.event.common.functions.Settle;
import cdm.event.workflow.EventTimestamp;
import cdm.event.workflow.EventTimestampQualificationEnum;
import cdm.event.workflow.WorkflowStep;
import cdm.event.workflow.WorkflowStep.WorkflowStepBuilder;
import cdm.product.common.settlement.SettlementInstructions;
import cdm.product.common.settlement.TransferSettlementEnum;
import com.google.common.collect.MoreCollectors;
import com.regnosys.rosetta.common.hashing.GlobalKeyProcessStep;
import com.regnosys.rosetta.common.hashing.NonNullHashCollector;
import com.regnosys.rosetta.common.hashing.ReKeyProcessStep;
import com.rosetta.model.lib.process.PostProcessStep;
import com.rosetta.model.lib.records.DateImpl;
import com.rosetta.model.metafields.FieldWithMetaString;
import org.isda.cdm.processor.EventEffectProcessStep;

import java.time.ZonedDateTime;
import java.util.*;

import static cdm.base.staticdata.party.PartyRoleEnum.CLIENT;
import static cdm.base.staticdata.party.PartyRoleEnum.COUNTERPARTY;
import static com.rosetta.util.CollectionUtils.emptyIfNull;

/**
 * Sample Settle implementation, should be used as a simple example only.
 */
public class SettleImpl extends Settle {

    private final List<PostProcessStep> postProcessors;

    public SettleImpl() {
        GlobalKeyProcessStep globalKeyProcessStep = new GlobalKeyProcessStep(NonNullHashCollector::new);
        this.postProcessors = Arrays.asList(globalKeyProcessStep,
                new ReKeyProcessStep(globalKeyProcessStep),
                new EventEffectProcessStep(globalKeyProcessStep));
    }

    @Override
    protected WorkflowStepBuilder doEvaluate(TradeState tradeState, WorkflowStep previousEvent, com.rosetta.model.lib.records.Date date) {
        WorkflowStepBuilder eventBuilder = WorkflowStep.builder();

        if (!isDeliveryVsPayment(tradeState.getTrade())) {
            throw new IllegalArgumentException("Only executions with transferSettlementType of DELIVERY_VERSUS_PAYMENT are supported");
        }

        List<Party> eventParties = new ArrayList<>();
        eventParties.add(getParty(tradeState.getTrade(), CLIENT));
        eventParties.add(getParty(tradeState.getTrade(), COUNTERPARTY));

        eventBuilder
                .addEventIdentifier(getIdentifier("settleEvent1", 1))
                .addParty(eventParties)
                .addTimestamp(getEventCreationTimestamp(ZonedDateTime.now()));

        eventBuilder.getOrCreateBusinessEvent().setEventDate(DateImpl.of(java.time.LocalDate.now()));

        // Update keys / references
        postProcessors.forEach(postProcessStep -> postProcessStep.runProcessStep(WorkflowStep.class, eventBuilder));

        return eventBuilder;
    }

    private Boolean isDeliveryVsPayment(Trade trade) {
		return emptyIfNull(trade.getTradableProduct().getSettlementInstructions()).stream()
                .map(SettlementInstructions::getSettlementTerms)
                .map(t -> t.getTransferSettlementType() == TransferSettlementEnum.DELIVERY_VERSUS_PAYMENT)
                .findFirst()
                .orElse(false);
    }

    private Party getParty(Trade trade, PartyRoleEnum partyRole) {
        Party party = trade.getPartyRole()
                .stream()
                .filter(_partyRole -> _partyRole.getRole() == partyRole)
                .map(PartyRole::getPartyReference)
                .map(ReferenceWithMetaParty::getValue)
                .collect(MoreCollectors.onlyElement());

        if (!isAccountSet(party)) {
            throw new IllegalArgumentException("No account found on party " + party);
        }

        return party;
    }

    private Boolean isAccountSet(Party party) {
        return Optional.ofNullable(party)
                .map(Party::getAccount).map(Objects::nonNull)
                .orElse(false);
    }

    private EventTimestamp getEventCreationTimestamp(ZonedDateTime eventDateTime) {
        return EventTimestamp.builder()
                .setDateTime(eventDateTime)
                .setQualification(EventTimestampQualificationEnum.EVENT_CREATION_DATE_TIME)
                .build();
    }

    private Identifier getIdentifier(String id, int version) {
        return Identifier.builder()
                .addAssignedIdentifier(AssignedIdentifier.builder()
                        .setIdentifier(FieldWithMetaString.builder().setValue(id).build())
                        .setVersion(version))
                .build();
    }
}
