# Project Title

This project provides an interface to ImageMagick 'compare' utility to batch compare a group of images specified in csv file.

# Prerequisites
For each of the prerequisites, skip if it is already installed, otherwise go to the download website and follow instructions there to download and install the software according to your operating system.
Java (https://www.java.com/download)
Maven (https://maven.apache.org/download.cgi)
ImageMagick (https://imagemagick.org/script/download.php)


### Instructions

Step 1. 
Unzip the zip file to a folder, on windows open command line and navigate to the unzipped folder, run build_mvn.bat. On MacOS or other unix/linux machines, open command shell and navigate to the unzipped folder, run ./build_mvn.sh (may need to run change mode command: chmod +x build_mvn.sh). Once build finished with success, there will be a SimFinder.jar file under the unzipped folder.

Step 2.
Under the unzipped folder, edit 'compare_win.bat', change the path of ImageMagick compare executable to your ImageMagick installation directory, save the file. 
Under the unzipped folder, edit 'compare_mac.sh', change the path of ImageMagick compare executable to your ImageMagick installation directory, save the file. You may need to run 'chmod +x compare_mac.sh' to make it executable.

Step 3.
On Windows, open command line and navigate to the unzipped folder, run 'SimFinder test\image_name.csv test\images', output csv file will be created under test directory. 
On MacOS or other unix/linux system, open command shell and navigate to the unzipped folder, run './SimFinder.sh test\image_name.csv test\images', output csv file will be created under test directory. 

#### Implementation Steps

Step 1.
Go through the requirements many times, cross-platform made me think of java. Then I tried to find out a way to use ImageMagick compare utility without calling command line. The reason is if I can do compare at API level in java, then I don't need to install ImageMagick, therefore removing one dependancy. However, ImageMagick is not well documented on APIs. A lot of time was spent on finding API samples. One way is to download ImageMagick source code and reproduce the code for 'compare' in java. This could take >2 days, so I decided to backtrack and use the 'compare' utility. To support multiple platform, I had to create batch files and shell scripts. 

Step 2.
As a start of the project, I created a maven pom file. Then used 'mvn eclipse:eclipse' to generate a eclipse project. Then added package and one java file. In 'SimFinder.java', I have 'SimFinder' class and 'MagickOutput' class. 

Step X.
Not very satisfied with the current solution. A better solution would be using python and imagechop, then dockerize the application. I might give it a try next. 
  

  




