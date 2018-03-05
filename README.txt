Author: Musabbir Ahmed Baki

To run the program, copy each line below and paste it in the terminal. Note
that each line is a separate command. Alternatively the project can also be
imported in to Intellij and executed from there, in that case these commands
won't be neccessary.

----------------------------Setup Commands---------------------------------
git clone https://github.com/musabbirbaki/CSCI2020U_Assignments.git
cd CSCI2020U_Assignments/Assignment1/
gradle copyTask build
java -jar build/libs/Assignment1.jar
----------------------------------------------------------------------------

--------------------Description of the commands-----------------------------
-Line 1: clones the repository from github
-Line 2: navigates to the Assignment 1 folder
-Line 3: firstly it copies the "sample.fxml" file into the classes file
    and then compiles the java files. The copy of the FXML file is
    neccessary for the program to run.
-Line 4: runs the built jar file.
---------------------------------------------------------------------------

---------------------------How to Use the program-------------------------
-Firstly choose the directory "data" which has the "test" and "train" folders.
-The program will train and test, and a GUI with tree view will be displaced
-Note that in the terminal the on going process will be described as the
program runs. It will include additional stats after running the test.
--------------------------------------------------------------------------
