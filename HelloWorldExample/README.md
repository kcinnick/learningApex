# Apex Hello World Example

## Table of Contents

- [Overview](#overview)
- [Installation](#installation)
- [Usage](#usage)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)

## Overview

This is a simple Apex class that demonstrates the basic structure of a Salesforce Apex program. The program prints "Hello World" to the debug log.

## Installation

1. Log into your Salesforce account.
2. Navigate to the Developer Console.
3. Create a new Apex Class and name it `HelloWorld`.
4. Copy and paste the code from the [HelloWorld example](https://developer.salesforce.com/docs/atlas.en-us.apexcode.meta/apexcode/apex_qs_HelloWorld.htm).

## Usage

### HelloWorld Class

- **HelloWorld.cls**: The class contains a single method, `sayHello`, that writes "Hello World" to the debug log.
  - `sayHello()`: Use this method to print "Hello World" to the debug log.

#### Example

```apex
HelloWorld hello = new HelloWorld();
hello.sayHello();
```

In the Salesforce Developer Console, open the Debug -> Open Execute Anonymous Window, paste the code, and run it. Then, check the debug log for the "Hello World" message.

## Tests

There are no tests for this basic example as it's meant for educational purposes. However, always consider writing tests for more complex Apex Classes.

## Contributing

Feel free to fork this project, open issues, or create pull requests to improve the HelloWorld example or add new features.

## License

This project is open source and available under the [MIT License](LICENSE).
