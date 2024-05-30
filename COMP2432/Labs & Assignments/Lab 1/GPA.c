
#include <stdio.h>

int myStrcmp(char string1[], char string2[])
{
    int i = 0;
    while (string1[i] == string2[i] && string1[i] != '\0') {
        i++;
    }
    return string1[i] - string2[i];
}

int main(int argc, char *argv[])
{
	 int num_subj = argc-1, num_subj_other = argc-1, invalid = 0;
	 float in_gp, sum_gp = 0.0;
	 int in_gp_other, sum_gp_other = 0;
	 char in_grade;
	 int i;
	 printf("PolyU System:\n");
	 for (i = 1; i <= num_subj; i++) {
		 if (myStrcmp(argv[i], "D-") == 0) {
			printf("Grade for subject %d is %s, invalid\n",i,argv[i]);
			invalid++;
		 	continue;
		 }
		 in_grade = argv[i][0];
		 switch (in_grade) {
			 case 'A': in_gp = 4.0; break;
			 case 'B': in_gp = 3.0; break;
			 case 'C': in_gp = 2.0; break;
			 case 'D': in_gp = 1.0; break;
			 case 'F': in_gp = 0.0; break;
			 default: printf("Wrong grade %s\n",argv[i]);
		 }
		 if (argv[i][1] == '+') in_gp = in_gp + 0.3;
		 if (argv[i][1] == '-') in_gp = in_gp - 0.3;
		 sum_gp = sum_gp + in_gp;
		 printf("Grade for subject %d is %s, GP %5.2f\n",i,argv[i],in_gp);
	 }
	 printf("Your GPA for %d valid subjects is %5.2f\n",num_subj - invalid,sum_gp/(num_subj - invalid));

	 printf("Other System:\n");
	 for (i = 1; i <= num_subj_other; i++) {
		 in_grade = argv[i][0];
		 switch (in_grade) {
			 case 'A': in_gp_other = 11; break;
			 case 'B': in_gp_other = 8; break;
			 case 'C': in_gp_other = 5; break;
			 case 'D': in_gp_other = 2; break;
			 case 'F': in_gp_other = 0; break;
			 default: printf("Wrong grade %s\n",argv[i]);
		 }
		 if (argv[i][1] == '+') in_gp_other = in_gp_other + 1;
		 if (argv[i][1] == '-') in_gp_other = in_gp_other - 1;
		 sum_gp_other = sum_gp_other + in_gp_other;
		 printf("Grade for subject %d is %s, GP %d\n",i,argv[i],in_gp_other);
	 }
	 printf("Your GPA for %d valid subjects is %.2f\n",num_subj,(float)sum_gp_other/num_subj_other);
}