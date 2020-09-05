package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.config.datasource.DbContextHolder;
import com.edu.mtdr.prpayment.config.datasource.DbTypeEnum;
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
import java.util.UUID;


/**
 * Participant controller
 */
@RestController
@RequestMapping("/api/participants")
@Api(tags = {"participants"}, value = "Participant controller")
public class ParticipantController {
    private final IParticipantService participantService;
    private final ParticipantRepository participantRepository;

    @Autowired
    public ParticipantController(IParticipantService participantService, ParticipantRepository participantRepository) {
        this.participantService = participantService;
        this.participantRepository = participantRepository;
    }

    @GetMapping("/list")
    @ApiOperation("List participants")
    public BaseResponseMessage<?> listParticipants() {
        final List<ParticipantEntity> dbParticipants = new ArrayList<>(participantRepository.findAll());
        return new SuccessResponseMessage<>(dbParticipants);
    }

    @GetMapping("/get/{id}")
    @ApiOperation("Get participant by id")
    public BaseResponseMessage<ParticipantEntity> getParticipant(@PathVariable("id") UUID id) {
        final ParticipantEntity dbParticipant = participantRepository.findById(id).orElse(null);
        return new SuccessResponseMessage<>(dbParticipant);
    }

    @PostMapping("/save")
    @ApiOperation("Create or update participant")
    public BaseResponseMessage<?> saveParticipant(@RequestBody ParticipantEntity participant) {
        final ParticipantEntity savedParticipant = participantRepository.save(participant);
        return new SuccessResponseMessage<>(savedParticipant);
    }

    @PostMapping("/save2")
    @ApiOperation("Create or update participant")
    public BaseResponseMessage<?> saveParticipant2(@RequestBody ParticipantEntity participant) {
        DbContextHolder.setCurrentDb(DbTypeEnum.SHARD2);
        return new SuccessResponseMessage<>(participantService.save(participant));
    }

    @PostMapping("/delete")
    @ApiOperation("Delete participant")
    public BaseResponseMessage<?> deleteParticipant(@RequestBody ParticipantEntity participant) {
        participantRepository.deleteById(participant.getId());
        return new SuccessResponseMessage<>();
    }
}
