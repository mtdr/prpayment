package com.edu.mtdr.prpayment.schema;

import javax.persistence.*;

@Table(name = "participant")
@Entity
public class ParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Basic
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Participant [id=%d]",
                id);
    }
}
