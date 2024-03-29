package edu.puj.toolmaster.users.exceptions;

import edu.puj.toolmaster.users.entities.DomainEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EntityAlreadyExistsException extends RuntimeException {
    DomainEntity resource;
}
