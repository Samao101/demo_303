package com.example.demo.models.DTOs;

public record AllSheetMarkOfCourse (
    SheetMarkDto sheetMarkForBeginner,
    SheetMarkDto sheetMarkForIntermediate,
    SheetMarkDto sheetMarkForAdvanced
){
}
