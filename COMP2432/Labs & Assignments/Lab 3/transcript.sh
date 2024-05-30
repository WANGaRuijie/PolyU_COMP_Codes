#!/bin/bash

divider="student"
isFile=true
files=()
ids=()
names=()

# A function to calculate grades with given id and name

calculate_grades() {

    local parameter=$1
    local name=$2

    echo -e "Transcript for $parameter $name"

    declare -A grades
    declare -A years
    declare -A sems
    declare -A status
    
    for file in "${files[@]}"; do
        while IFS=" " read -r field1 field2 field3 field4 || [ -n "$field1" ]; do

            if [ "$field1" == "Subject" ]; then

                subject=$field2
                currentYear=$field3
                currentSem=$field4
                    
            elif [ "$field1" == "$parameter" ]; then

                grades["$subject"]=0
                years["$subject"]=$currentYear
                sems["$subject"]=$currentSem

                echo -e  "$subject $currentYear Sem $currentSem $field2"

                # Only the later grade of a same subject will be recorded
                # If the grade is not the newest one, its status will be set to "na"

                if [ $currentYear -ge ${years["$subject"]} ]; then
                    if [ $currentSem -ge ${sems["$subject"]} ]; then
                        status["$subject"]="a"
                        case $field2 in
                            "A+") grades["$subject"]=4.3;;
                            "A") grades["$subject"]=4;;
                            "A-") grades["$subject"]=3.7;;
                            "B+") grades["$subject"]=3.3;;
                            "B") grades["$subject"]=3;;
                            "B-") grades["$subject"]=2.7;;
                            "C+") grades["$subject"]=2.3;;
                            "C") grades["$subject"]=2;;
                            "C-") grades["$subject"]=1.7;;
                            "D+") grades["$subject"]=1.3;;
                            "D") grades["$subject"]=1;;
                            "F") grades["$subject"]=0;;
                            "") status["$subject"]="na";;
                        esac
                    fi
                fi
            fi
        done < "$file"
    done

    local count=0
    local total=0

    # Only grade with status "a" will be counted

    for subject in "${!status[@]}"; do
        if [ "${status[$subject]}" == "a" ]; then
            count=$((count+1))
        fi
    done

    for grade in "${!grades[@]}"; do
        if [ "${status[$grade]}" == "a" ]; then
            total=$(echo "$total+${grades[$grade]}" | bc -l)
        fi
    done
    
    if [ $count == 0 ]; then
        echo -e "blank"
    else
        average=$(echo "scale=2; $total/$count" | bc -l)
        echo -e "GPA for $count subjects is $average\n"
    fi
}

while IFS=" " read -r id name; do
    ids+=("$id")
    names+=("$name")
done < student.dat

for parameter in "$@"; do

    if [ "$parameter" == "$divider" ]; then
        isFile=false
        continue
    fi

    if $isFile; then
        if [ "$parameter" == "COMP*" ]; then
            for file in COMP*; do
                files+=("$file")
            done
        else
            files+=("$parameter")
        fi

    else

        for ((i=0; i<${#ids[@]}; i++)); do
            if [ "$parameter" == "${ids[$i]}" ]; then
                name="${names[$i]}"
                calculate_grades "$parameter" "$name"
                break
            fi
        done

        # If there are students with the same name, print their id and calculate their grades

        for ((j=0; j<${#names[@]}; j++)); do
            if [ "$name" == "${names[$j]}" -a "$parameter" != "${ids[$j]}" ]; then
                idSameName="${ids[$j]}"
                echo -e "Also found a student whose name is the same as $parameter $name: $idSameName $name"
                calculate_grades "$idSameName" "$name"
            fi
        done

    fi

done