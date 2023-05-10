package com.epam.esm.authorization.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
public class PersonDto extends RepresentationModel<PersonDto> {

    @Email(message = "email not valid")
    private String email;
    @NotNull(message = "enabled must not be null")
    private Boolean enabled;
    @NotNull(message = "authorities must not be null")
    private Set<String> authorities;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        PersonDto personDto = (PersonDto) o;
        return email.equals(personDto.email) && enabled.equals(personDto.enabled) && authorities.equals(
            personDto.authorities);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, enabled, authorities);
    }
}
