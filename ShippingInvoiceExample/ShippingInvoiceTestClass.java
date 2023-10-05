@IsTest
private class ShippingInvoiceTest {
    @IsTest
    static void testBulkItemInsert() {
        // first, create test invoices

        // instantiate a new Shipping_Invoice__C object
        Shipping_Invoice__C order1 = new Shipping_Invoice__C(
            subtotal__c = 0,
            TotalWeight__c = 0,
            GrandTotal__c = 0,
            ShippingDiscount__c = 0,
            Shipping__c = 0,
            Tax__c = 0
        );

        // insert the order
        insert order1;

        // create a new list of items
        List<Item__c> itemList;

        Item__c item1 = new Item__c(
            Name = 'item1',
            Price__c = 10.0,
            Quantity__c = 1,
            Shipping_Invoice__c=order1,
            Weight__c = 1.0
        );

        Item__c item2 = new Item__c(
            Name = 'item2',
            Price__c = 20.0,
            Quantity__c = 2,
            Shipping_Invoice__c=order1,
            Weight__c = 2.0
        );

        Item__c item3 = new Item__c(
            Name = 'item3',
            Price__c = 40.0,
            Quantity__c = 3,
            Shipping_Invoice__c=order1,
            Weight__c = 3.0
        );

        // add the new items to itemList

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        insert itemList;

        // now order1 should have 3 items on it, with a total cost of $70
        // i.e., less than the discount amount.

        // retrieve the Shipping_Invoice__c object using SOQL, 
        // and test assertions
        
        order1 = [SELECT id, subtotal__c, TotalWeight__c, GrandTotal__c,
                  ShippingDiscount__c, Shipping__c, Tax__c FROM Shipping_Invoice__C
                  WHERE id = :order1.id];
        
        print(order1)

    }
}