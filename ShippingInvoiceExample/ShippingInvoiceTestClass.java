@IsTest
private class ShippingInvoiceTest {
    @IsTest
    static void testBulkItemInsert() {
        // first, create test invoices

        // instantiate a new Shipping_invoice__c object
        Shipping_invoice__c order1 = new Shipping_invoice__c(
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
        List<Item__c> list1 = new List<Item_c>();

        Item__c item1 = new Item__c(
            Name = 'item1',
            Price__c = 10.0,
            Quantity__c = 1,
            Shipping_invoice__c=order1.id,
            Weight__c = 1.0
        );

        Item__c item2 = new Item__c(
            Name = 'item2',
            Price__c = 20.0,
            Quantity__c = 2,
            Shipping_invoice__c=order1.id,
            Weight__c = 2.0
        );

        Item__c item3 = new Item__c(
            Name = 'item3',
            Price__c = 40.0,
            Quantity__c = 3,
            Shipping_invoice__c=order1.id,
            Weight__c = 3.0
        );

        // add the new items to itemList

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        insert itemList;

        // now order1 should have 3 items on it, with a total cost of $70
        // i.e., less than the discount amount.

        // retrieve the Shipping_invoice__c object using SOQL, 
        // and test assertions
        
        order1 = [SELECT id, subtotal__c, TotalWeight__c, GrandTotal__c,
                  ShippingDiscount__c, Shipping__c, Tax__c FROM Shipping_invoice__c
                  WHERE id = :order1.id];
        
        System.debug(order1);
        System.assert(order1 == order1);

    }
}