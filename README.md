# My personal assistant (PTDMA project)

This project is a Personal Assistant, the idea is to follow some specs for the MVP and if there is time, try to implement some extra functionalities. In this repository you wil find the project developed during [Programming of Cell Phones and Mobile Autonomous Devices](https://www.fib.upc.edu/en/studies/masters/master-informatics-engineering/curriculum/syllabus/PTDMA-MEI) subject of the Masters Degree in Informatics Engineering of the [Universitat Politècnica de Catalunya - Facultad d'informàtica de Barcelona](https://www.fib.upc.edu/en) during Q2 2019/2020.

## Project information

- Min SDK = 25
- Target SDK = 29
- Gradle plugin version = 3.6.2
- Gradle version = 5.6.4
- Compile SDK version = 29

*Testing was been maded with android version 9*

*To test the application you need to add a Key for the WeatherAPI, the one used is [OpenWeather](https://openweathermap.org/api)*

## Functionalities

This is the list of functionalities asked in the specification:

#### Ask for the current weather


#### Ask for tomorrow’s weather forecast


#### Ask for the weather for the following days


#### Ask for the weather in a certain location


#### Ask for the location


#### Changing the default location to a certain city 
It correcly changes the preferences file but 

#### Check the default location
It correcly says where you are (it check it at the preferences file) but there is no shown map


## Possible questions for the assistant

### Regarding weather:

#### Regarding weather in the default location

- what's the weather today
- what's the current weather
- what's the weather

#### Regarding tomorrow's weather

- what will be the weather tomorrow
- weather for tomorrow

#### Regarding the weather in the default location on the following days

- what's the weather forecast
- what's the forecast for the following days
- weather forecast

#### Regarding the weather in a specific place

- what's the weather in *(place name)*

### Regarding localization

#### To know where you are

- where am I
- what's my current location

#### To know your default location

- what's my default location

#### To change the default location

- change my default location to *(place name)*

### Regarding calendar
*This functionality is not finished correctly*

- what are my events for today
- what are my events for tomorrow
- create a new event
