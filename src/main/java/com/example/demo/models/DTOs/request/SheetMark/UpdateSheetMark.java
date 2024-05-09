package com.example.demo.models.DTOs.request.SheetMark;

import com.example.demo.models.entity.enums.SheetMarkStatus;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSheetMark extends BaseSheetMarkRequest{
    @NotNull
    private Long id;

    @NotNull
    private Double assignmentScore;

    @NotNull
    private Double projectScore;

    @NotNull
    private Double midTermScore;

    @NotNull
    private Double finalExamScore;

    @NotNull
    private SheetMarkStatus sheetMarkStatus;
}
