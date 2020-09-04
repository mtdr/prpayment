package com.edu.mtdr.prpayment.controller;

import com.edu.mtdr.prpayment.model.BaseResponseMessage;
import com.edu.mtdr.prpayment.model.SuccessResponseMessage;
import com.edu.mtdr.prpayment.repository.ParticipantRepository;
import com.edu.mtdr.prpayment.schema.ParticipantEntity;
import com.edu.mtdr.prpayment.service.IParticipantService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @PostMapping("/save")
    @ApiOperation("")
    public BaseResponseMessage<?> saveParticipant(@RequestBody ParticipantEntity participant) {
        return new SuccessResponseMessage<>();
    }
}
