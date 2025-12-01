Known ways to run the code

1. Install IntelliJ-Idea
- The student version can be found here https://www.jetbrains.com/academy/student-pack/
2. Download the JavaFX sdk version from https://gluonhq.com/products/javafx/ (currently version 25) and extract it
3. Clone https://gitlab.maastrichtuniversity.nl/ken_group24_2025/ken_group24_2025.git and open the project folder using intellij
4. In intellij, click the four lines on the top left and inside of the File tab, open Project Structure
5. Inside of Project Structure go to Libraries, at the top of the second column there should be a + sign, click it
6. Navigate to where you extracted the JavaFX SDK and open it, inside there should be a folder called lib, select it and click OK
7. Apply changes and press OK in Project Structure, on the top right of the screen there should be a play button
8. Click on the Current File button to the left of the play button and click on Edit Configuration
9. Add a new configuration by clicking the + sign on the first column on the top left, choose Application
10. Now click on Modify options to the right of Build and Run, out of all the options click on Add VM options
11. A new text bar should appear that says VM options, paste the following line
--module-path "<PATHTOYOURLIB>" --add-modules javafx.controls,javafx.fxml
12. replace <PATHTOYOURLIB> with the file path to the JavaFX lib folder
13. Choose GradeDashboardApp as your Main Class, then click Apply and OK
14. Now click on the play button to run the code, you should see the GUI appear shortly