package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.model.BaseResponseMessage;
import com.edu.mtdr.prpayment.model.FailureResponseMessage;
import com.edu.mtdr.prpayment.model.RequestMessage;
import com.edu.mtdr.prpayment.model.SuccessResponseMessage;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


/**
 * Participant controller
 */
@RestController
@RequestMapping("/api/participants")
@Api(tags = {"participants"}, value = "Participant controller")
public class ParticipantController {
    private final IParticipantService participantService;

    public ParticipantController(IParticipantService participantService, ParticipantRepository participantRepository) {
        this.participantService = participantService;
    }

    /**
     * @return {@link SuccessResponseMessage} with list of all {@link ParticipantEntity}
     */
    @GetMapping("/list")
    @ApiOperation("List participants")
    public BaseResponseMessage<List<ParticipantEntity>> allParticipants() {
        final List<ParticipantEntity> dbParticipants = new ArrayList<>(participantService.findAll());
        return new SuccessResponseMessage<>(dbParticipants);
    }

    /**
     * @param id {@link ParticipantEntity}'s id
     * @return {@link SuccessResponseMessage} with {@link ParticipantEntity}
     */
    @GetMapping("/get/{id}")
    @ApiOperation("Get participant by id")
    public BaseResponseMessage<ParticipantEntity> getParticipant(@PathVariable("id") Long id) {
        final ParticipantEntity dbParticipant = participantService.findById(id).orElse(null);
        return new SuccessResponseMessage<>(dbParticipant);
    }

    /**
     * @param participant participant to save
     * @return saved in same state at all shards participant
     */
    @PostMapping("/save")
    @ApiOperation("Create or update participant")
    public BaseResponseMessage<ParticipantEntity> saveParticipant(@RequestBody ParticipantEntity participant) {
        return new SuccessResponseMessage<>(participantService.save(participant));
    }

    /**
     * @param message {@link RequestMessage} with {@link ParticipantEntity#getId()}
     *                of {@link ParticipantEntity} to delete
     * @return {@link SuccessResponseMessage} empty message if all ok, {@link FailureResponseMessage} if exception
     */
    @PostMapping("/delete/id")
    @ApiOperation("Delete participant by id")
    public BaseResponseMessage<Void> deleteParticipantById(@RequestBody RequestMessage<Long> message) {
        participantService.deleteById(message.getData());
        return new SuccessResponseMessage<>();
    }

    /**
     * @param message {@link RequestMessage} with {@link ParticipantEntity#getName()}
     *                of {@link ParticipantEntity} to delete
     * @return {@link SuccessResponseMessage} empty message if all ok, {@link FailureResponseMessage} if exception
     */
    @PostMapping("/delete/name")
    @ApiOperation("Delete participant by name")
    public BaseResponseMessage<Void> deleteParticipantByName(@RequestBody RequestMessage<String> message) {
        ParticipantEntity participant = participantService.findFirstByName(message.getData()).orElse(null);
        if (participant != null) {
            participantService.deleteById(participant.getId());
            return new SuccessResponseMessage<>();
        } else {
            return new FailureResponseMessage<>("Participant not found");
        }
    }
}
