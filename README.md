# Simulink-Viewer (Team 22) 
The software tool will be useful for engineers, researchers, and students working with  Simulink models. It will provide a quick and easy way to visualize Simulink models without the need for the Simulink environment itself.




## [Project Run Video ](https://www.youtube.com/watch?v=A4lGpW046zU "Real-time trial ")


## Features

The MiniGPS Tracking System offers the following features:

- Locating the starting coordinates of the user.
- Storing the coordinates of multiple destinations and displaying them on an LCD screen for the user to choose from.
- Allowing the user to navigate through the destinations using the buttons on the Tiva C LaunchPad.
- Displaying the moved distance and remaining displacement to the selected destination on the LCD screen when the user starts moving towards it.
- Providing visual feedback to the user using built-in LEDs:
  - Stage 1: Turning on the red LED when the target destination is far away by more than 5 meters.
  - Stage 2: Turning on the yellow LED when the target destination is about to be reached (less than 5 meters away).
  - Stage 3: Turning on the green LED when the target destination is reached.
  - In case of the signal is lost, the white LED will be turned on

The trajectory of the distance traveled should satisfy the following criteria:

- The total distance between the start and end point should be greater than 100 meters.
- The path from the start point to the end point should form a non-straight line similar to the provided baseline path.
- The calculated distance should be compared with the distance obtained from Google Maps, and the error margin should be less than or equal to 5%.

## How to Use

##### To use the MiniGPS Tracking System, you have two ways:

##### 1. Using CCS: (RECOMMENDED)
- Download the zip file.
- Extract the file.
- Open the project using CCS.
- Make sure to configure the properties probebly in CCS.
- Uplode the project into your tiva c 123 GH6PM microcontroller.

##### 2. Using the source codes:
 -  Download the source files.
 -  Download the required libraries (the names located in CONFIG.h file).
 -  Add a startup file into your project file.
 - Uplode the project into your tiva c 123 GH6PM microcontroller.
 
## Tools Used
<div style="display: flex; align-items: center;">
  <img src="https://www.ti.com/diagrams/ccstudio_ccs_256.jpg" alt="CCS" title="CCS" width="70" height="70">
  <h3 style="margin-left: 10px; color: #2596be;">CCS</h3>
</div>

<div style="display: flex; align-items: center;">
  <img src="https://www.theiconadvantage.com/wp-content/uploads/2014/08/ti-logo.png" alt="Tiva C LaunchPad" title="Tiva C LaunchPad" width="70" height="70">
  
  <h3 style="margin-left: 10px; color: #2596be;">Tiva C LaunchPad</h3>
</div>
<div style="display: flex; align-items: center;">
  <img src="https://downloadly.ir/wp-content/uploads/2018/08/Keil.png" alt="Keil" title="Keil" width="70" height="70">
  <h3 style="margin-left: 10px; color: #2596be;">Keil</h3>
</div>

## Programming Languages 

<div style="display: flex; align-items: center;"> <img src="https://www.chetu.com/img/on-demand-developers/embedded-c/logo/embeded-c.png" alt="Embedded C" title="[Embedded C](poe://www.poe.com/_api/key_phrase?phrase=Embedded%20C&prompt=Tell%20me%20more%20about%20Embedded%20C.)" width="70" height="70"> <h3 style="margin-left: 10px; color: #00aef0;">Embedded C</h3> </div>

## Credits

This Team project is part of the requiremtents of "Introduction to Embedded Systems" course.


## Team members
- Abdelrahman Ahmed    2001722 (Team Leader)
- Eslam Mohamed           2000252
- Mohamed Ahmed         2001171
- Omar Muhammed        2001277
- Karim Ibrahim 2001118
- Mohamed Ayman 2001048
- Abdelrahman Elsayed 2002139
- Karim Mikhail 2000318


# Photos from a live Trial

<div align="center">
    <a href="https://ibb.co/FJrGGzQ"><img src="https://i.ibb.co/tcGyyD0/photo-2023-05-11-23-52-43.jpg" alt="Photo 5" height="400"></a>
    <br>
    <sup style="font-size: 24px;">This is the screen to choose your destination from</sup>
    <br>
    <br>
</div>


<div align="center">
    <a href="https://ibb.co/3B0hXmT"><img src="https://i.ibb.co/KF9yZx5/Screenshot-20230512-000221-Gallery.jpg" alt="Photo 2" height="400"></a>
    <br>
    <sup style="font-size: 50px;">The walking distance on Google Maps is 150m</sup>
    <br>
    <br>
</div>

<div align="center">
   <a href="https://ibb.co/ZcjBdYx"><img src="https://i.ibb.co/zPMXs7Z/image.png" alt="Photo 4" height="400"></a>
    <br>
    <sup style="font-size: 24px;">The screen displaying the total distance moved when reaching the destination </sup>
    <br>
    <br>
</div>


<div align="center">
    <a href="https://ibb.co/V9hPyTm"><img src="https://i.ibb.co/6wpfhFB/photo-2023-05-11-23-53-20.jpg" alt="Photo 3" height="400"></a>
    <br>
    <sup style="font-size: 24px;">The LED turns Green indicating that the destination is reached</sup>
    <br>
    <br>
</div>

