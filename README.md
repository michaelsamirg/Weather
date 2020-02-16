# Weather Project
## Run the application

- Run command `mvn spring-boot:run`
- open http://localhost:8080

## End point URLs:
- /weather: fetch current weahter temprature using current location, and saves results in in-memory DB
- /weather/history/all: retreive all weather history saved in DB
- /weather/historbyIP?ip=88.135.141.121: retreive all weather history saved in DB by IP address
- /weather/historbyIP?ip=88.135.141.121: retreive all weather history saved in DB by IP address
-- parameters: ip -> ip address
- weather/history/byDateRange?fromDate=16/02/2020&toDate=16/02/2020: retreive all weather history saved in DB by date range
-- parameters: fromDate -> start date, toDate -> end date

## Test the application
- Run command `mvn test`
