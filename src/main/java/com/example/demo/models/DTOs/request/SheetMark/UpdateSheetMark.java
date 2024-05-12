package com.example.demo.models.DTOs.request.SheetMark;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSheetMark{
    @NotNull
    private Long studentId;

    @NotNull
    private Long courseClassId;

    private Double assignmentScore;

    private Double projectScore;

    private Double midTermScore;

    private Double finalExamScore;
}
