package com.example.demo.helper;

public class ValidationMessage {
    public static class Course {
        public static final String COURSE_ID_NOT_NULL = "Course Id can not be null";
        public static final String COURSE_ID_NOT_EMPTY = "Course Id can not be empty";

    }

    public static class Student {
        public static final String STUDENT_NAME_NOT_NULL = "Student Name can not be null";
        public static final String STUDENT_NAME_NOT_EMPTY  = "Student Name can not be empty";
        
        public static final String STUDENT_AGE_NOT_NULL = "Student Age can not be null";
        public static final String STUDENT_AGE_NOT_NEGATIVE = "Student Age can not be negative";
        public static final String STUDENT_AGE_MIN = "Student Age can not be less than 16";

        public static final String STUDENT_Birthday_NULL = "Student birthday can not be null";

        public static final String STUDENT_PlaceOfBirth_NOT_NULL = "Student PlaceOfBirth can not be null";
        public static final String STUDENT_PlaceOfBirth_NOT_EMPTY  = "Student PlaceOfBirth can not be empty";
    
        public static final String STUDENT_CitizenIdentification_NOT_NULL = "Student Citizen Identification can not be null";
        public static final String STUDENT_CitizenIdentification_NOT_EMPTY  = "Student Citizen Identification can not be empty";

        public static final String STUDENT_Email_NOT_NULL = "Student Email can not be null";
        public static final String STUDENT_Email_NOT_EMPTY  = "Student Email can not be empty";
    }

    public static class Teacher {
        public static final String TEACHER_NAME_NOT_NULL = "Teacher Name can not be null";
        public static final String TEACHER_NAME_NOT_EMPTY  = "Teacher Name can not be empty";
        
        public static final String TEACHER_AGE_NOT_NULL = "Teacher Age can not be null";
        public static final String TEACHER_AGE_NOT_NEGATIVE = "Teacher Age can not be negative";
        public static final String TEACHER_AGE_MIN = "Teacher Age can not be less than 16";

        public static final String TEACHER_Birthday_NULL = "Teacher birthday can not be null";

        public static final String TEACHER_PlaceOfBirth_NOT_NULL = "Teacher PlaceOfBirth can not be null";
        public static final String TEACHER_PlaceOfBirth_NOT_EMPTY  = "Teacher PlaceOfBirth can not be empty";
    
        public static final String TEACHER_CitizenIdentification_NOT_NULL = "Teacher Citizen Identification can not be null";
        public static final String TEACHER_CitizenIdentification_NOT_EMPTY  = "Teacher Citizen Identification can not be empty";

        public static final String TEACHER_Email_NOT_NULL = "Teacher Email can not be null";
        public static final String TEACHER_Email_NOT_EMPTY  = "Teacher Email can not be empty";

        public static final String TEACHER_Main_Course_Id_NOT_NULL = "Teacher main course id can not be null";
        public static final String TEACHER_Main_Course_Id_NOT_EMPTY  = "Teacher main course id can not be empty";
    }
}
