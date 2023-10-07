

@IsTest
private class TestShippingInvoice{

    public static void assertDecimalEquals(Decimal expected, Decimal actual, String message) {
        System.assert(expected == actual, message + ' Expected: ' + expected + ', Actual: ' + actual);
        System.debug(message + ' Expected: ' + expected + ', Actual: ' + actual + ' (Test Passed)');
    }

    // Test for inserting three items at once
    public static testmethod void testBulkItemInsert(){
        // Create the shipping invoice. It's a best practice to either use defaults
        // or to explicitly set all values to zero so as to avoid having
        // extraneous data in your test.
        Shipping_Invoice__C order1 = new Shipping_Invoice__C(subtotal__c = 0, 
                          totalweight__c = 0, grandtotal__c = 0, 
                          ShippingDiscount__c = 0, Shipping__c = 0, tax__c = 0);

        // Insert the order and populate with items
        insert order1;
        List<Item__c> list1 = new List<Item__c>();
        Item__c item1 = new Item__C(Price__c = 10, weight__c = 1, quantity__c = 1, 
                                    Shipping_Invoice__C = order1.id);
        Item__c item2 = new Item__C(Price__c = 25, weight__c = 2, quantity__c = 1, 
                                    Shipping_Invoice__C = order1.id);
        Item__c item3 = new Item__C(Price__c = 40, weight__c = 3, quantity__c = 1, 
                                    Shipping_Invoice__C = order1.id);
        list1.add(item1);
        list1.add(item2);
        list1.add(item3);
        insert list1;
        
        // Retrieve the order, then do assertions
        order1 = [SELECT id, subtotal__c, tax__c, shipping__c, totalweight__c, 
                  grandtotal__c, shippingdiscount__c 
                  FROM Shipping_Invoice__C 
                  WHERE id = :order1.id];
        
        assertDecimalEquals(
                75.0, order1.subtotal__c, 'Order subtotal was '
        );
        assertDecimalEquals(
                6.9375, order1.tax__c, 'Tax was '
        );
        assertDecimalEquals(
                4.50, order1.shipping__c, 'Shipping was '
        );
        assertDecimalEquals(
                6.00, order1.totalweight__c, 'Total was '
        );
        assertDecimalEquals(
                86.4375, order1.grandtotal__c, 'Grand total was '
        );
        assertDecimalEquals(
                0, order1.shippingdiscount__c, 'Shipping discount was '
        );
    }

    public static testmethod void testBulkItemUpdate() {

        // Instantiate the Shipping_invoice__c object we'll use in the test
        Shipping_invoice__c  order1 = new Shipping_invoice__c(
                subtotal__c = 0, totalweight__c = 0,
                grandtotal__c = 0, shippingdiscount__c = 0
        );

        // insert the order
        insert order1;
        // instantiate a new list of Item__c objects
        List<Item__c> list1 = new List<Item__c>();
        // instantiate some new Item__c objects
        Item__c item1 = new Item__c(
                Price__c = 1, weight__c = 1, quantity__c = 1,
                Shipping_invoice__c = order1.id
        );
        Item__c item2 = new Item__c(
                Price__c = 2, weight__c = 2, quantity__c = 1,
                Shipping_invoice__c = order1.id
        );
        Item__c item3 = new Item__c(
                Price__c = 4, weight__c = 3, quantity__c = 1,
                Shipping_invoice__c = order1.id
        );
        // add the new items to the list we instantiated earlier
        list1.add(item1);
        list1.add(item2);
        list1.add(item3);
        // these objects only exist in-memory until the insert
        insert list1;

        // update the prices of the 3 items
        list1[0].price__c = 10;
        list1[1].price__c = 25;
        list1[2].price__c = 40;
        // these object prices have only been updated in memory
        update list1;
        // now they have been updated/'committed'

        // Access the order and assert the items updated.
        order1 = [SELECT id, subtotal__c, tax__c, shipping__c,
                  totalweight__c, grandtotal__c, shippingdiscount__c
                  FROM Shipping_invoice__c WHERE Id = :order1.Id];
        
        assertDecimalEquals(
                75.0, order1.subtotal__c, 'Subtotal was '
        );
        assertDecimalEquals(
                6.9375, order1.tax__c, 'Tax was '
        );
        assertDecimalEquals(
                4.50, order1.shipping__c, 'Shipping was '
        );
        assertDecimalEquals(
                6.0, order1.totalweight__c, 'Total weight was '
        );
        assertDecimalEquals(
                86.4375, order1.grandtotal__c, 'Grand total was '
        );
        assertDecimalEquals(
                0, order1.shippingdiscount__c, 'Shipping discount was '
        );
    }
}