# Cardio Data Simulator

The Cardio Data Simulator is a Java-based application designed to simulate real-time cardiovascular data for multiple patients. This tool is particularly useful for educational purposes, enabling students to interact with real-time data streams of ECG, blood pressure, blood saturation, and other cardiovascular signals.

## Features

- Simulate real-time ECG, blood pressure, blood saturation, and blood levels data.
- Supports multiple output strategies:
  - Console output for direct observation.
  - File output for data persistence.
  - WebSocket and TCP output for networked data streaming.
- Configurable patient count and data generation rate.
- Randomized patient ID assignment for simulated data diversity.

## Getting Started

### Prerequisites

- Java JDK 11 or newer.
- Maven for managing dependencies and compiling the application.

### Installation

1. Clone the repository:

   ```sh
   git clone https://github.com/tpepels/signal_project.git
   ```

2. Navigate to the project directory:

   ```sh
   cd signal_project
   ```

3. Compile and package the application using Maven:
   ```sh
   mvn clean package
   ```
   This step compiles the source code and packages the application into an executable JAR file located in the `target/` directory.

### Running the Simulator

After packaging, you can run the simulator directly from the executable JAR:

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar
```

To run with specific options (e.g., to set the patient count and choose an output strategy):

```sh
java -jar target/cardio_generator-1.0-SNAPSHOT.jar --patient-count 100 --output file:./output
```

### Supported Output Options

- `console`: Directly prints the simulated data to the console.
- `file:<directory>`: Saves the simulated data to files within the specified directory.
- `websocket:<port>`: Streams the simulated data to WebSocket clients connected to the specified port.
- `tcp:<port>`: Streams the simulated data to TCP clients connected to the specified port.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Project Members
- Student ID: 6424784


## Architecture

| Module | Responsibility |
|---|---|
| `cardio_generator` | Generates simulated patient data and streams it (console, file, TCP, WebSocket) |
| `dataAccess` | Receives incoming data from files, TCP or WebSocket and parses it |
| `data_management` | Stores patient records, controls staff access and logs data requests |
| `patientIdentification` | Matches incoming data points to hospital patients and handles mismatches |
| `alerts` | Evaluates patient records and generates alerts |

## My contributions, by project phase

This repository was developed in five phases as part of the Software Engineering course at Maastricht University.

### Part 1 – Setup and code quality
- Fixed the build errors, added the JUnit dependency and implemented `getRecords` in `Patient` (filtering records by time range), so `mvn clean package` runs without errors
- Fixed code style violations following the Google Java Style Guide
- Documented the core classes with Javadoc

### Part 2 – System design
- Designed four subsystems: Alert Generation, Data Storage, Patient Identification and Data Access Layer
- Created UML class diagrams and written descriptions for each (see `uml_models/`)

### Part 3 – Implementation and testing
- Implemented data reading from the simulator's output files into `DataStorage`
- Implemented the alert logic in `AlertGenerator`:
  - **Blood pressure:** critical thresholds (systolic above 180 or below 90 mmHg, diastolic above 120 or below 60 mmHg) and trends of three consecutive readings changing by more than 10 mmHg each
  - **Blood oxygen saturation:** below 92%, or a drop of more than 5% within 10 minutes
  - **Hypotensive hypoxemia:** systolic pressure below 90 mmHg combined with saturation below 92%
  - **ECG:** peaks far above the moving average, using a one-hour sliding window
  - **Triggered alerts:** emitted when a nurse or patient presses the alert button
- Refactored all modules following the SOLID principles
- Wrote unit tests for every module, covering normal and edge cases (JaCoCo coverage report in `TestCoverageReport.pdf`)

### Part 4 – Design patterns
- **Factory Method:** a separate alert factory for each alert type
- **Strategy:** a separate strategy for each alert condition
- **Decorator:** priority and repeated alerts
- **Singleton:** `DataStorage` and `HealthDataSimulator`
- Added unit, module and integration tests

### Part 5 – Real-time data
- Adapted the `DataReader` interface for a continuous data stream and implemented a `WebsocketClient` that connects to the simulator
- Incoming messages are parsed, matched to patients and stored in `DataStorage`, then evaluated for alerts immediately
- Corrupted messages and connection errors or interruptions are caught and logged without stopping the client
- `DataStorage` uses concurrent maps, so simultaneous updates are handled safely
- Refactored the Patient Identification module and added tests for the WebSocket client and data parsing
## Testing

Run all tests with:

```sh
mvn test
```

The coverage report, screenshots and a video of all tests passing are in `testDocuments/`.
