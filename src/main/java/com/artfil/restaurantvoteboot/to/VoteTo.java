package com.artfil.restaurantvoteboot.to;

import lombok.EqualsAndHashCode;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.beans.ConstructorProperties;
import java.time.LocalDate;

@Value
@EqualsAndHashCode(callSuper = true)
public class VoteTo extends BaseTo {

    @NotBlank
    LocalDate date;

    @NotBlank
    int restId;

    @NotBlank
    @Size(min = 2, max = 100) String restName;

    @ConstructorProperties({"id", "date", "restId", "restName"})
    public VoteTo(Integer id, LocalDate date, int restId, String restName) {
        super(id);
        this.date = date;
        this.restId = restId;
        this.restName = restName;
    }
}
