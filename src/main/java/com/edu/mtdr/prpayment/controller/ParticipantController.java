package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.model.BaseResponseMessage;
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


@RestController
@RequestMapping("/api/participants")
@Api(tags = {"participants"}, value = "Participant controller")
public class ParticipantController {
    private final IParticipantService participantService;
    private final ParticipantRepository repository;

    @Autowired
    public ParticipantController(IParticipantService participantService, ParticipantRepository repository) {
        this.participantService = participantService;
        this.repository = repository;
    }

    @GetMapping("/list")
    @ApiOperation("List participants")
    public BaseResponseMessage<?> listParticipants() {
        final List<ParticipantEntity> dbParticipants = new ArrayList<>(repository.findAll());
        return new SuccessResponseMessage<>(dbParticipants);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("Get participant by id")
    public BaseResponseMessage<ParticipantEntity> getParticipant(@PathVariable("id") long id) {
        final ParticipantEntity dbParticipant = repository.findById(id).orElse(null);
        return new SuccessResponseMessage<>(dbParticipant);
    }

    @PostMapping("/save")
    @ApiOperation("Create or update participant")
    public BaseResponseMessage<?> saveParticipant(@RequestBody ParticipantEntity participant) {
        final ParticipantEntity savedParticipant = repository.save(participant);
        return new SuccessResponseMessage<>(savedParticipant);
    }

    @PostMapping("/delete")
    @ApiOperation("Delete participant")
    public BaseResponseMessage<?> deleteParticipant(@RequestBody ParticipantEntity participant) {
        repository.deleteById(participant.getId());
        return new SuccessResponseMessage<>();
    }
}
