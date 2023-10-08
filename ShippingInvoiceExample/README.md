# Apex Shipping Calculator

## Table of Contents

- [Overview](#overview)
- [Installation](#installation)
- [Usage](#usage)
  - [Shipping Calculator](#shipping-calculator)
  - [Shipping Invoice](#shipping-invoice)
- [Tests](#tests)
- [Contributing](#contributing)
- [License](#license)

## Overview

This Apex project contains classes and triggers that manage shipping costs and discounts in a Salesforce application. It dynamically calculates shipping costs based on various parameters like weight, tax, and discount conditions.

## Installation

1. Log into your Salesforce account.
2. Navigate to the Developer Console.
3. Create new Apex Classes and Triggers and copy the code from this repo into them.

## Usage

### Shipping Calculator

- **ShippingCalculator.cls**: The main class that handles the shipping cost calculations. 
  - `calculateShippingCost()`: Given the weight and other parameters, this method will return the shipping cost.
  - `calculateTax()`: Calculates the tax based on the subtotal.

### Shipping Invoice

- **ShippingInvoiceTrigger.trigger**: Trigger that fires whenever a new invoice is created or updated.
  - Automatically calculates the new shipping costs, tax, and grand total.
  - Applies discounts if conditions are met (e.g., free shipping for orders over $100).

#### Custom Fields

- `subtotal__c`: Subtotal amount of the invoice.
- `totalweight__c`: Total weight of items in the invoice.
- `grandtotal__c`: Total amount including tax and shipping.
- `ShippingDiscount__c`: Discounts applicable on the shipping.
- `Shipping__c`: Calculated shipping costs.
- `tax__c`: Calculated tax amount.

## Tests

- **ShippingInvoiceTest.cls**: Contains unit tests for shipping invoices.
  - `testFreeShipping()`: Validates that free shipping is applied for orders over $100.
  - `testTaxCalculation()`: Validates that tax is calculated correctly.

Run tests by:

1. Navigating to the test class in the Salesforce Developer Console.
2. Clicking 'Run Test'.

## Contributing

Feel free to open issues or create pull requests to improve the calculator or add new features. 

## License

This project is open source and available under the [MIT License](LICENSE).

---

Feel free to adjust this to better fit your project's specifics. Hope that helps! Anything else?
