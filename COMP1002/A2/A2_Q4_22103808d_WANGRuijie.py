def LetterCount():

    '''
The function LetterCount() is used for counting the times of a character's occurrance in a sentence.
The character and the sentence will be input by the user.
One example of using LetterCount() is as below:
    Input text: Never Too late To Start. Please go ahead with us.
    Input a character to be searched: t
    The character t in the text occurred 4 times at [13, 20, 23, 44].
    '''

    print(LetterCount.__doc__)

    input_text = input("Input text: ")
    letter = input("Input a character to be searched: ")

    times = 0
    locations = []
    length = len(input_text)

    for i in range(0, length):
        if input_text[i] == letter:
            times += 1
            locations.append((i + 1))

    print(f"The character {letter} in the text occurred {times} times at {locations}.")
    
    return times, locations


LetterCount()
