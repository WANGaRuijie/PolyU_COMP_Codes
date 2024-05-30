#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>

typedef struct {
    int id;
    char name[100];
} Student;

typedef struct {
    int id;
    char subject[20];
    int year;
    int sem;
    char gradeLetter[3];
    float grade;
    bool isAvailable;
} Grade;

int main(int argc, char *argv[]) {

    int i = 0, j = 0, k = 0, p = 0, numberOfFiles = 0, numberOfStudentIDs = 0, numberOfAllStudents = 0, numberOfAllGrades = 0;

    // Process input arguments

    for (i = 1; i < argc; i++) {
       if (strcmp(argv[i], "student") == 0) {
            numberOfFiles = i-1;
            break;
        }
    }
    
    char *files[numberOfFiles];
    numberOfStudentIDs = argc - numberOfFiles - 2;
    
    for (i = 1; i <= numberOfFiles; i++) {
        files[i - 1] = argv[i];
    }

    char *studentIDs[numberOfStudentIDs];
    
    for (i = numberOfFiles + 2; i <= argc; i++) {
        studentIDs[k++] = argv[i];
    }

    // Read student.dat file

    FILE * file = fopen("student.dat", "r");
    Student students[100];

    if (file == NULL) {
        printf("Error: File not found\n");
        return 1;
    }  

    i = 0;
    while (fscanf(file, "%d %s", &students[i].id, &students[i].name) == 2) {
        i++;
    }
    numberOfAllStudents = i;

    fclose(file);

    // Read all grade files

    char subjectTag[10], gradeLetter[3];
    Grade grades[1000];
    j = 0;

    for (i = 0; i < numberOfFiles; i++) {

        file = fopen(files[i], "r");

        if (file == NULL) {
            printf("Error: File not found\n");
            return 1;
        }  
        
        // Read subject data once at the beginning of each file
        
        char subject[20];
        int year, sem;
        fscanf(file, "%s %s %d %d", subjectTag, &subject, &year, &sem);

        // Read student grades till the end of the file.
        char line[100];

        while (fgets(line, sizeof(line), file)) {

            if (strchr(line, ' ')) {  // Check if there is a space in the line  
                sscanf(line, "%d %s", &grades[j].id, gradeLetter);
                grades[j].isAvailable = true;
            } else {
                sscanf(line, "%d", &grades[j].id);
                strcpy(gradeLetter, " ");
                grades[j].grade = 0.0;
                grades[j].isAvailable = false;  // Apply default grade if only id is provided
            }

            strcpy(grades[j].subject, subject);
            grades[j].year = year;
            grades[j].sem = sem;
            strcpy(grades[j].gradeLetter, gradeLetter);

            if (strcmp(gradeLetter, "A+") == 0) grades[j].grade = 4.3;
            else if (strcmp(gradeLetter, "A") == 0) grades[j].grade = 4.0;
            else if (strcmp(gradeLetter, "A-") == 0) grades[j].grade = 3.7;
            else if (strcmp(gradeLetter, "B+") == 0) grades[j].grade = 3.3;
            else if (strcmp(gradeLetter, "B") == 0) grades[j].grade = 3.0;
            else if (strcmp(gradeLetter, "B-") == 0) grades[j].grade = 2.7;
            else if (strcmp(gradeLetter, "C+") == 0) grades[j].grade = 2.3;
            else if (strcmp(gradeLetter, "C") == 0) grades[j].grade = 2.0;
            else if (strcmp(gradeLetter, "C-") == 0) grades[j].grade = 1.7;
            else if (strcmp(gradeLetter, "D+") == 0) grades[j].grade = 1.3;
            else if (strcmp(gradeLetter, "D") == 0) grades[j].grade = 1.0;
            else if (strcmp(gradeLetter, "F") == 0) grades[j].grade = 0.0;

            j++;

        }
        fclose(file);
    }
    numberOfAllGrades = j;

    // Find grades and calculate GPA for each student

    for (i = 0; i < numberOfStudentIDs; i++) {

        int subjectCount = 0;
        float totalPoints = 0;

        for (j = 0; j < numberOfAllStudents; j++) {

            if (students[j].id == atoi(studentIDs[i])) {
                printf("Transcript for %d %s\n", students[j].id, students[j].name);

                for (k = 0; k < numberOfAllGrades; k++) {

                    if (grades[k].id == students[j].id) {

                        int yearTemp = grades[k].year;
                        int semTemp = grades[k].sem;
                        float gradeTemp = grades[k].grade;
                        
                        for (p = k + 1; p < numberOfAllGrades; p++) {
                            if (grades[p].id == students[j].id && strcmp(grades[k].subject, grades[p].subject) == 0) {
                                if (grades[p].year > yearTemp || (grades[p].year == yearTemp && grades[p].sem > semTemp)) {
                                    yearTemp = grades[p].year;
                                    semTemp = grades[p].sem;
                                    gradeTemp = grades[p].grade;
                                    grades[k].isAvailable = false;
                                }
                            }
                        }

                        printf("%s %d Sem %d %s\n", grades[k].subject, grades[k].year, grades[k].sem, grades[k].gradeLetter);

                        if (grades[k].isAvailable) {
                            totalPoints += gradeTemp;
                            subjectCount++;
                        }

                    }
                }
            }
        }

        if (subjectCount == 0) {
            printf("blank\n");
        } else {
            printf("GPA for %d subjects %.2f\n", subjectCount, totalPoints / subjectCount);
        }
        printf("\n");
    }

    return 0;

}


