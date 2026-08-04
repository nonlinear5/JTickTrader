
# JTickTrader

JTickTrader is an open-source, Java-based platform for building fully automated trading systems which execute trading strategies without 
requiring manual user monitoring. The platform is designed to take human element/bias out of trading by operating entirely on 
pre-defined programmatic rules.


* **Market Depth Focus**: Operates using Level 1 data.

* **Zero Human Intervention**: Automates all stages of trading from data collection to order execution for fully automated trading.

* **Interactive Brokers Integration**: Designed to run via the Interactive Brokers API and Trader Workstation (TWS).

* **Asset Versatility**: Capable of trading any instrument (e.g., futures, options, stocks).

* **Integrated Framework**: Includes modules for historical data recording, backtesting, optimization, and forward testing.

## Prerequisites

- Java 21 or higher
- Maven 3.x
- Interactive Brokers brokerage account (if you plan to trade live, or paper-trade)


## Setup

1. **Clone the repository:**

   ```sh
   git clone https://github.com/nonlinear5/JTickTrader.git
   cd JTickTrader
   ```

2. **Install the TWS API JAR to your local Maven repository. This is needed because the TWS API JAR is not available in public Maven repositories.**

   ```sh
   mvn install:install-file -Dfile=lib/TwsApi-10.45.jar -DgroupId=com.ib -DartifactId=twsapi -Dversion=10.45 -Dpackaging=jar
   ```

## Build

To build the project and create a runnable JAR with all dependencies:

```sh
mvn clean package
```

The output JAR will be in the `target` directory, named like:

```
JTickTrader.jar
```

## Run

To start JTickTrader, run the following command:

```sh
java -jar target/JTickTrader.jar
```

If you run JTickTrader to optimize trading strategies, you may want to allocate more memory to the JVM.

```sh
java -Xms10g -Xms10g -jar target/JTickTrader.jar
```

When JTickTrader starts, it may print some warnings to conslole about using the custom look and feel. If that bothers you, you can suppress those warnings by adding the following JVM option:

```sh
java --enable-native-access=ALL-UNNAMED -jar target/JTickTrader.jar
```


