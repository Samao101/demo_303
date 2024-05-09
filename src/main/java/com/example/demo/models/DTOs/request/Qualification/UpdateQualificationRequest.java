package com.example.demo.models.DTOs.request.Qualification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateQualificationRequest extends BaseQualificationRequest{
    @NotNull
    private Long id;
}
